package edu.luc.etl.cs313.android.shapes.model;

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
        // Initialize the bounding box coordinates
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        // Calculate the bounding box for each shape in the group and combine them
        // to form the bounding box for the group
        for (final Shape shape : g.getShapes()) {
            final Location box = shape.accept(this);
            final Rectangle rect = (Rectangle) box.getShape();
            final int left = box.getX();
            final int top = box.getY();
            final int right = left + rect.getWidth();
            final int bottom = top + rect.getHeight();
            if (left < minX) {
                minX = left;
            }
            if (top < minY) {
                minY = top;
            }
            if (right > maxX) {
                maxX = right;
            }
            if (bottom > maxY) {
                maxY = bottom;
            }
        }

        if (minX == Integer.MAX_VALUE) {
            return new Location(0, 0, new Rectangle(0, 0));
        }

        // Return the bounding box as a location and rectangle for the entire group
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }

    @Override
    public Location onLocation(final Location l) {
        final Location inner = l.getShape().accept(this);
        return new Location(
            l.getX() + inner.getX(),
            l.getY() + inner.getY(),
            inner.getShape()
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
        int minx = Integer.MAX_VALUE;
        int maxx = -1;
        int miny = Integer.MAX_VALUE;
        int maxy = -1;
        for(Point point : s.getPoints()) {
            if(point.getX() > maxx) {
                maxx = point.getX();
            }
            if(point.getX() < minx) {
                minx = point.getX();
            }
            if(point.getY() > maxy) {
                maxy = point.getY();
            }
            if(point.getY() < miny) {
                miny = point.getY();
            }
        }
        return new Location(minx, miny, new Rectangle(maxx - minx, maxy - miny));
    }
}
