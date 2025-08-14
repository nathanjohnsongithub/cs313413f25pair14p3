package edu.luc.etl.cs313.android.shapes.model;

/**
 * A graphical shape.
 */
public sealed interface Shape permits Circle, Rectangle, Group, StrokeColor, Fill, Location, Outline {
    <Result> Result accept(Visitor<Result> v);
}
