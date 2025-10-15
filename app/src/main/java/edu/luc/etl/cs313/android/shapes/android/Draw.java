package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(0); //Idk what a good default color is
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(Style.STROKE);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape shape : g.getShapes()) {
            shape.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.translate(-l.getX(), -l.getY());
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(Style.STROKE);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        final var points = s.getPoints();
        final int size = points.size();
        // Polygons with only one point or no points are not drawn
        if (size < 2) {
            return null;
        }
        // Create an array of line segments, each defined by 4 floats (x1, y1, x2, y2)
        // There are as many segments as there are points (the last point connects to the first
        final float[] segments = new float[size * 4];
        int index = 0;
        for (int i = 0; i < size; i++) {
            final Point start = points.get(i);
            final Point end = points.get((i + 1) % size);
            segments[index++] = start.getX();
            segments[index++] = start.getY();
            segments[index++] = end.getX();
            segments[index++] = end.getY();
        }
        // Draw all the line segments
        canvas.drawLines(segments, paint);
        return null;
    }
}
