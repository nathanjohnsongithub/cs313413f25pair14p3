package edu.luc.etl.cs313.android.shapes.model;

/**
 * A visitor to compute the number of basic shapes in a (possibly complex)
 * shape.
 */
public class Count implements Visitor<Integer> {

    @Override
    public Integer onPolygon(final Polygon p) {
        return 1;
    }

    @Override
    public Integer onCircle(final Circle c) {
        return 1;
    }

    @Override
    public Integer onRectangle(final Rectangle q) {
        return 1;
    }

    @Override
    public Integer onGroup(final Group g) {
        // Loop over all the shapes in the group and count
        int count = 0;
        for (Shape shape : g.getShapes()) {
            count += shape.accept(this);
        }
        return count;
    }

    @Override
    public Integer onOutline(final Outline o) {
        // Count the inner shape
        return o.getShape().accept(this);
    }

    @Override
    public Integer onFill(final Fill c) {
        // Count the inner shape
        return c.getShape().accept(this);
    }

    @Override
    public Integer onLocation(final Location l) {
        // Count the inner shape
        return l.getShape().accept(this);
    }

    @Override
    public Integer onStrokeColor(final StrokeColor c) {
        // Count the inner shape
        return c.getShape().accept(this);
    }
}
