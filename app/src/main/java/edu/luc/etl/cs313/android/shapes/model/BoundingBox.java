package edu.luc.etl.cs313.android.shapes.model;

import java.util.List;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        final List<? extends Shape> shapes = g.getShapes();
        if (shapes.isEmpty()) {
            return new Location(0, 0, new Rectangle(0, 0));
        }

        Integer minX = null, minY = null, maxX = null, maxY = null;

        for (final Shape s : shapes) {
            final Location childBox = s.accept(this);
            final int cx = childBox.getX();
            final int cy = childBox.getY();
            final Rectangle cr = (Rectangle) childBox.getShape();
            final int cmaxX = cx + cr.getWidth();
            final int cmaxY = cy + cr.getHeight();

            minX = (minX == null) ? cx : Math.min(minX, cx);
            minY = (minY == null) ? cy : Math.min(minY, cy);
            maxX = (maxX == null) ? cmaxX : Math.max(maxX, cmaxX);
            maxY = (maxY == null) ? cmaxY : Math.max(maxY, cmaxY);
        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }

    @Override
    public Location onLocation(final Location l) {
        final Shape inner = l.getShape();
        if (inner == null) {
            return new Location(l.getX(), l.getY(), new Rectangle(0, 0));
        }
        final Location child = inner.accept(this);
        final Shape childShape = child.getShape();
        if (!(childShape instanceof Rectangle)) {
            return new Location(l.getX(), l.getY(), new Rectangle(0, 0));
        }
        final Rectangle rect = (Rectangle) childShape;
        return new Location(
            l.getX() + child.getX(),
            l.getY() + child.getY(),
            new Rectangle(rect.getWidth(), rect.getHeight())
        );
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, r);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        final List<? extends Point> pts = s.getPoints();
        if (pts.isEmpty()) {
            return new Location(0, 0, new Rectangle(0, 0));
        }

        int minX = pts.get(0).getX();
        int minY = pts.get(0).getY();
        int maxX = minX;
        int maxY = minY;

        for (final Point pt : pts) {
            minX = Math.min(minX, pt.getX());
            minY = Math.min(minY, pt.getY());
            maxX = Math.max(maxX, pt.getX());
            maxY = Math.max(maxY, pt.getY());
        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }
}
