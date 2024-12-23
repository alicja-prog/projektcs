package com.example.internal;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;

public class WorldMapArea {

    private static final String DATA_FILE = "countryInfoMap.ser";
    private JComponent ui = null;
    JLabel output = new JLabel();
    JLabel infoLabel = new JLabel("Hover over a country");
    public static final int SIZE = 500; // Reduced size to fit the window
    BufferedImage image;
    Area area;
    ArrayList<Shape> shapeList;
    Map<Shape, String[]> countryInfoMap;

    public WorldMapArea() {
        try {
            loadCountryInfoMap(); // Load existing data if available
            initUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Add save call when exiting or updating
    public void saveOnExit() {
        saveCountryInfoMap();
    }

    public final void initUI() throws Exception {
        if (ui != null) {
            return;
        }//https://tse2.mm.bing.net/th?id=OIP.GCR7D-XhkHLwqxaOJHtV6gHaFv&pid=Api
        URL url = new URL("https://tse2.mm.bing.net/th?id=OIP.GCR7D-XhkHLwqxaOJHtV6gHaFv&pid=Api");
        image = ImageIO.read(url);
        image = resizeImage(image, SIZE, SIZE); // Resize image to fit within SIZE
        long then = System.currentTimeMillis();
        System.out.println("" + then);
        area = getOutline(Color.WHITE, image, 12);
        long now = System.currentTimeMillis();
        System.out.println("Time in mins: " + (now - then) / 60000d);
        shapeList = separateShapeIntoRegions(area);
        filterOceans(); // Filter out ocean shapes
        calibrateShapes();

        countryInfoMap = assignCountryInfo(shapeList);

        ui = new JPanel(new BorderLayout(4, 4));
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        output.addMouseMotionListener(new MousePositionListener());

        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ui.add(infoLabel, BorderLayout.NORTH);
        ui.add(output, BorderLayout.CENTER);
        refresh();
    }

    public Area getOutline(Color target, BufferedImage bi, int tolerance) {
        GeneralPath gp = new GeneralPath();

        boolean cont = false;
        for (int xx = 0; xx < bi.getWidth(); xx++) {
            for (int yy = 0; yy < bi.getHeight(); yy++) {
                if (isIncluded(new Color(bi.getRGB(xx, yy)), target, tolerance)) {
                    if (cont) {
                        gp.lineTo(xx, yy);
                        gp.lineTo(xx, yy + 1);
                        gp.lineTo(xx + 1, yy + 1);
                        gp.lineTo(xx + 1, yy);
                        gp.lineTo(xx, yy);
                    } else {
                        gp.moveTo(xx, yy);
                    }
                    cont = true;
                } else {
                    cont = false;
                }
            }
            cont = false;
        }
        gp.closePath();

        return new Area(gp);
    }

    public static ArrayList<Shape> separateShapeIntoRegions(Shape shape) {
        ArrayList<Shape> regions = new ArrayList<>();

        PathIterator pi = shape.getPathIterator(null);
        GeneralPath gp = new GeneralPath();
        while (!pi.isDone()) {
            double[] coords = new double[6];
            int pathSegmentType = pi.currentSegment(coords);
            int windingRule = pi.getWindingRule();
            gp.setWindingRule(windingRule);
            if (pathSegmentType == PathIterator.SEG_MOVETO) {
                gp = new GeneralPath();
                gp.setWindingRule(windingRule);
                gp.moveTo(coords[0], coords[1]);
            } else if (pathSegmentType == PathIterator.SEG_LINETO) {
                gp.lineTo(coords[0], coords[1]);
            } else if (pathSegmentType == PathIterator.SEG_QUADTO) {
                gp.quadTo(coords[0], coords[1], coords[2], coords[3]);
            } else if (pathSegmentType == PathIterator.SEG_CUBICTO) {
                gp.curveTo(
                        coords[0], coords[1],
                        coords[2], coords[3],
                        coords[4], coords[5]);
            } else if (pathSegmentType == PathIterator.SEG_CLOSE) {
                gp.closePath();
                regions.add(new Area(gp));
            } else {
                System.err.println("Unexpected value! " + pathSegmentType);
            }

            pi.next();
        }

        return regions;
    }

    private void filterOceans() {
        shapeList.removeIf(shape -> {
            Rectangle bounds = shape.getBounds();
            double area = bounds.getWidth() * bounds.getHeight();
            // Exclude shapes that are disproportionately large (likely oceans)
            return area > (SIZE * SIZE * 0.1); // Adjust the threshold as needed
        });
    }

    private void calibrateShapes() {
        for (int i = 0; i < shapeList.size(); i++) {
            Shape shape = shapeList.get(i);
            Rectangle bounds = shape.getBounds();
            AffineTransform transform = new AffineTransform();
            transform.translate(-bounds.getX(), -bounds.getY());
            transform.translate(bounds.getX(), bounds.getY());
            shapeList.set(i, transform.createTransformedShape(shape));
        }
    }

    private Map<Shape, String[]> assignCountryInfo(ArrayList<Shape> shapes) {
        Map<Shape, String[]> map = new HashMap<>();
        // Example country information (this should be replaced with actual data)
        for (int i = 0; i < shapes.size(); i++) {
            map.put(shapes.get(i), new String[]{"Country " + (i + 1), "Description for Country " + (i + 1)});
        }
        return map;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = outputImage.createGraphics();
        g.drawImage(resultingImage, 0, 0, null);
        g.dispose();
        return outputImage;
    }

    class MousePositionListener implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            for (Map.Entry<Shape, String[]> entry : countryInfoMap.entrySet()) {
                if (entry.getKey().contains(p)) {
                    infoLabel.setText(entry.getValue()[0] + ": " + entry.getValue()[1]);
                    refresh();
                    return;
                }
            }
            infoLabel.setText("Hover over a country");
            refresh();
        }
    }

    public static boolean isIncluded(Color target, Color pixel, int tolerance) {
        int rT = target.getRed();
        int gT = target.getGreen();
        int bT = target.getBlue();
        int rP = pixel.getRed();
        int gP = pixel.getGreen();
        int bP = pixel.getBlue();
        return ((rP - tolerance <= rT) && (rT <= rP + tolerance)
                && (gP - tolerance <= gT) && (gT <= gP + tolerance)
                && (bP - tolerance <= bT) && (bT <= bP + tolerance));
    }

    private void refresh() {
        output.setIcon(new ImageIcon(getImage()));
    }

    private BufferedImage getImage() {
        BufferedImage bi = new BufferedImage(
                SIZE, SIZE, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = bi.createGraphics();
        g.drawImage(image, 0, 0, SIZE, SIZE, null);
        g.setColor(Color.ORANGE.darker());
        g.fill(area);
        g.setColor(Color.RED);
        g.draw(area);
        try {
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point p1 = output.getLocationOnScreen();
            int x = p.x - p1.x;
            int y = p.y - p1.y;
            Point pointOnImage = new Point(x, y);
            for (Shape shape : shapeList) {
                if (shape.contains(pointOnImage)) {
                    g.setColor(Color.GREEN.darker());
                    g.fill(shape);
                    break;
                }
            }
        } catch (Exception doNothing) {
        }

        g.dispose();

        return bi;
    }

    public JComponent getUI() {
        return ui;
    }

    public static void main(String[] args) {
        Runnable r = () -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            WorldMapArea o = new WorldMapArea();

            JFrame f = new JFrame(o.getClass().getSimpleName());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);

            f.setContentPane(o.getUI());
            f.setResizable(false);
            f.pack();

            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    o.saveOnExit(); // Save data when the window closes
                    super.windowClosing(e);
                }
            });

            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);
    }

    // Serialization method
    public void saveCountryInfoMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(countryInfoMap);
            System.out.println("countryInfoMap serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialization method
    @SuppressWarnings("unchecked")
    public Map<Shape, String[]> loadCountryInfoMap() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            countryInfoMap = (Map<Shape, String[]>) ois.readObject();
            System.out.println("countryInfoMap deserialized successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found. Initializing a new countryInfoMap.");
            countryInfoMap = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return countryInfoMap;
    }

}
