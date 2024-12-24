package com.example.internal.src.applications;

import com.example.internal.src.model.Country;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;

import static com.example.internal.src.applications.ContourCalculator.processImage;

public class WorldMapApp {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 450;
    private static final int TOLERANCE = 12;
    private static final String IMAGE_PATH = "src/static_files/map3.jpg";
    private App app;
    private JPanel worldMapPanel = null;
    private JLabel output = new JLabel();
    private JLabel infoLabel = new JLabel("Hover over a country");
    private BufferedImage mapImage;
    private Map<Shape, Country.Continent> countryInfoMap;



    public WorldMapApp(App app) {
        try {
            this.app=app;
            initUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public JPanel initUI() throws Exception {
        if (worldMapPanel != null) {

        }//https://tse2.mm.bing.net/th?id=OIP.GCR7D-XhkHLwqxaOJHtV6gHaFv&pid=Api
        mapImage = ImageIO.read(new File(IMAGE_PATH));
        mapImage = resizeImage(mapImage, WIDTH, HEIGHT); // Resize image to fit within SIZE
        this.countryInfoMap = processImage(mapImage, WIDTH, HEIGHT, Color.WHITE, TOLERANCE);
        worldMapPanel = new JPanel(new BorderLayout(4, 4));
        worldMapPanel.setBorder(new EmptyBorder(4, 4, 4, 4));

        output.addMouseMotionListener(new MousePositionListener());
        output.addMouseListener(new MousePositionListener());

        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        worldMapPanel.add(infoLabel, BorderLayout.NORTH);
        worldMapPanel.add(output, BorderLayout.CENTER);
        refresh();
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> app.switchPanel("MainAppPanel"));
        worldMapPanel.add(backButton, BorderLayout.SOUTH);
        return worldMapPanel;  //?????

    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = outputImage.createGraphics();
        g.drawImage(resultingImage, 0, 0, null);
        g.dispose();
        return outputImage;
    }

    class MousePositionListener implements MouseMotionListener, MouseListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            // Do nothing
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            for (Map.Entry<Shape, Country.Continent> entry : countryInfoMap.entrySet()) {
                if (entry.getKey().contains(p)) {
                    infoLabel.setText(String.valueOf(entry.getValue()));
                    refresh();
                    return;
                }
            }
            infoLabel.setText("Hover over a country");
            refresh();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            for (Map.Entry<Shape, Country.Continent> entry : countryInfoMap.entrySet()) {
                if (entry.getKey().contains(p)) {
                    app.selectEuropeOnCountryListAppPanel();
                    app.switchPanel("CountryListPanel");
                    return;
                }
            }
            infoLabel.setText("Hover over a country");
            refresh();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Do nothing
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // Do nothing
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // Do nothing
        }
    }


    private void refresh() {
        output.setIcon(new ImageIcon(getMapImage()));
    }

    private BufferedImage getMapImage() {
        BufferedImage bi = new BufferedImage(
                WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = bi.createGraphics();
        g.drawImage(mapImage, 0, 0, WIDTH, HEIGHT, null);
//        g.setColor(Color.ORANGE.darker());
//        g.fill(area);
//        g.setColor(Color.RED);
//        g.draw(area);
        try {
            Point p = MouseInfo.getPointerInfo().getLocation();
            Point p1 = output.getLocationOnScreen();
            int x = p.x - p1.x;
            int y = p.y - p1.y;
            Point pointOnImage = new Point(x, y);
            for (Shape shape : this.countryInfoMap.keySet()) {
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
        return worldMapPanel;
    }

    public static void main(String[] args) {
        Runnable r = () -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            WorldMapApp o = new WorldMapApp(new App());

            JFrame f = new JFrame(o.getClass().getSimpleName());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);

            f.setContentPane(o.getUI());
            f.setResizable(false);
            f.pack();

            f.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    //o.saveOnExit(); // Save data when the window closes
                    super.windowClosing(e);
                }
            });

            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);
    }



}
