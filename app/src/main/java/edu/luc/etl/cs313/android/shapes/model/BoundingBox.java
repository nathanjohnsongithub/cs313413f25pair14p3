package edu.luc.etl.cs313.android.shapes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

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
        for(Shape shape : g.getShapes()) {
            shape.accept(this);
        }
    return null;
    }

    @Override
    public Location onLocation(final Location l) {
        //get the shape from l, find its bounding box, then get the rectangle from that bounding box
        return new Location(l.getX(), l.getY(), l.getShape().accept(this).getShape());
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
