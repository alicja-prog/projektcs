package com.example.internal.src.applications;

import com.example.internal.src.model.Country;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ContourCalculator {

    public static Map<Shape, Integer> processImage(BufferedImage image, double width, double height, Color targetColor, int tolerance) {
        // Step 1: Get the outline of the target color in the image
        Area outline = ContourCalculator.getOutline(targetColor, image, tolerance);

        // Step 2: Separate the outline into individual regions
        ArrayList<Shape> shapeList = ContourCalculator.separateShapeIntoRegions(outline);

        // Step 3: Filter out large ocean regions
        ContourCalculator.filterOceans(shapeList, width, height);

        // Step 4: Calibrate shapes
        ContourCalculator.calibrateShapes(shapeList);

        // Step 5: Assign country information to each shape
        return ContourCalculator.assignCountryInfo(shapeList);
    }

    public static Area getOutline(Color target, BufferedImage image, int tolerance) {
        GeneralPath gp = new GeneralPath();

        boolean cont = false;
        for (int xx = 0; xx < image.getWidth(); xx++) {
            for (int yy = 0; yy < image.getHeight(); yy++) {
                if (isIncluded(new Color(image.getRGB(xx, yy)), target, tolerance)) {
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

    public static void filterOceans(ArrayList<Shape> shapeList, double width, double height) {
        shapeList.removeIf(shape -> {
            Rectangle bounds = shape.getBounds();
            double area = bounds.getWidth() * bounds.getHeight();
            // Exclude shapes that are disproportionately large (likely oceans)
            return area > (width * height * 0.1); // Adjust the threshold as needed
        });
    }

    public static void calibrateShapes(ArrayList<Shape> shapeList) {
        for (int i = 0; i < shapeList.size(); i++) {
            Shape shape = shapeList.get(i);
            Rectangle bounds = shape.getBounds();
            AffineTransform transform = new AffineTransform();
            transform.translate(-bounds.getX(), -bounds.getY());
            transform.translate(bounds.getX(), bounds.getY());
            shapeList.set(i, transform.createTransformedShape(shape));
        }
    }

    public static Map<Shape, Integer> assignCountryInfo(ArrayList<Shape> shapes) {
        shapes.sort(new Comparator<Shape>() {
            @Override
            public int compare(Shape o1, Shape o2) {
                if (o1.getBounds().getHeight()-o2.getBounds().getHeight() <0) return -1;
                if (o1.getBounds().getHeight()-o2.getBounds().getHeight() >0) return 1;
                return 0;
            }
        });
        Map<Integer, Country.Continent> continentMap = new HashMap<>();
        continentMap.put(120, Country.Continent.EUROPE);
        continentMap.put(104, Country.Continent.EUROPE);
        continentMap.put(118, Country.Continent.ASIA);
        continentMap.put(122, Country.Continent.ASIA);
        continentMap.put(121, Country.Continent.ASIA);
        continentMap.put(105, Country.Continent.ASIA);
        continentMap.put(110, Country.Continent.ASIA);
        continentMap.put(115, Country.Continent.ASIA);
        continentMap.put(114, Country.Continent.ASIA);
        continentMap.put(4, Country.Continent.ASIA);
        continentMap.put(127, Country.Continent.AFRICA);
        continentMap.put(116, Country.Continent.AFRICA);
        continentMap.put(124, Country.Continent.NORTH_AMERICA);
        continentMap.put(106, Country.Continent.NORTH_AMERICA);
        continentMap.put(111, Country.Continent.NORTH_AMERICA);
        continentMap.put(117, Country.Continent.NORTH_AMERICA);
        continentMap.put(94, Country.Continent.NORTH_AMERICA);
        continentMap.put(126, Country.Continent.SOUTH_AMERICA);
        continentMap.put(119, Country.Continent.OCEANIA);
        continentMap.put(123, Country.Continent.OCEANIA);
        continentMap.put(107, Country.Continent.OCEANIA);
        continentMap.put(112, Country.Continent.OCEANIA);
        Map<Shape, Integer> map = new HashMap<>();
        for (int i = 0; i < shapes.size(); i++) {
            map.put(shapes.get(i),i);
        }
        return map;
    }
}
