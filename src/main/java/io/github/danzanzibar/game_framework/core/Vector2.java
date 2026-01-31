//******************************************************************************
//  Zan Owsley
//  COMP 452
//
//  This class represents a 2D vector. It has methods for returning new Vector2s
//  from operations as well as methods for mutating in place. An alternative
//  design decision could have kep these immutable, but it's very convenient
//  to mutate them in place for many kinematic operations.
//******************************************************************************
package io.github.danzanzibar.game_framework.core;


import java.awt.*;

public class Vector2 {
    private double x;
    private double y;

    //--------------------------------------------------------------------------
    //  A default constructor giving an origin position.
    //--------------------------------------------------------------------------
    public Vector2() {
        this.x = 0.0;
        this.y = 0.0;
    }

    //--------------------------------------------------------------------------
    //  A parametrized constructor for the class.
    //--------------------------------------------------------------------------
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //--------------------------------------------------------------------------
    //  A 'toString' method for the class.
    //--------------------------------------------------------------------------
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    //--------------------------------------------------------------------------
    //  A static constructor that takes an angle (in radians) and returns a
    //  vector of length 1.
    //--------------------------------------------------------------------------
    public static Vector2 fromAngle(double angle) {
        return new Vector2(Math.cos(angle), Math.sin(angle));
    }

    //--------------------------------------------------------------------------
    //  A getter for 'x'.
    //--------------------------------------------------------------------------
    public double getX() {
        return x;
    }

    //--------------------------------------------------------------------------
    //  A getter for 'y'.
    //--------------------------------------------------------------------------
    public double getY() {
        return y;
    }

    //--------------------------------------------------------------------------
    //  Returns the magnitude of the vector.
    //--------------------------------------------------------------------------
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    //--------------------------------------------------------------------------
    //  Returns a new vector that is the addition of this vector with another.
    //--------------------------------------------------------------------------
    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    //--------------------------------------------------------------------------
    //  Returns a new vector that is the difference of this vector with another.
    //--------------------------------------------------------------------------
    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    //--------------------------------------------------------------------------
    //  Returns a new vector that is this scaled by a double valued argument.
    //--------------------------------------------------------------------------
    public Vector2 scale(double scale) {
        return new Vector2(x * scale, y * scale);
    }

    //--------------------------------------------------------------------------
    //  Returns the dot product of this vector and another.
    //--------------------------------------------------------------------------
    public double dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    //--------------------------------------------------------------------------
    //  Returns the distance between this vector and another.
    //--------------------------------------------------------------------------
    public double distance(Vector2 other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    //--------------------------------------------------------------------------
    //  Returns a new vector that is the midpoint of this vector and another.
    //--------------------------------------------------------------------------
    public Vector2 midpoint(Vector2 other) {
        return add(other).scale(0.5);
    }

    //--------------------------------------------------------------------------
    //  Returns the angle of this vector relative to the positive x axis.
    //--------------------------------------------------------------------------
    public double toAngle() {
        return Math.atan2(y, x);
    }

    //--------------------------------------------------------------------------
    //  All in place mutating method start here.
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    //  Scales the vector to match a given value.
    //--------------------------------------------------------------------------
    public void setMagnitude(double value) {
        double scale = value/magnitude();
        this.x *= scale;
        this.y *= scale;
    }

    //--------------------------------------------------------------------------
    //  Scales the vector to have a magnitude of 1.
    //--------------------------------------------------------------------------
    public void normalize() {
        double mag = magnitude();
        x /= mag;
        y /= mag;
    }

    //--------------------------------------------------------------------------
    //  Translates the vector by a given other vector. Mutates the original.
    //--------------------------------------------------------------------------
    public void translate(Vector2 translation) {
        this.x += translation.x;
        this.y += translation.y;
    }

    //--------------------------------------------------------------------------
    //  Performs a modulo operation on the x value of the vector.
    //--------------------------------------------------------------------------
    public void moduloX(int modulus) {
        x %= modulus;
        if (x < 0)
            x += modulus;
    }

    //--------------------------------------------------------------------------
    //  Performs a modulo operation on the y value of the vector.
    //--------------------------------------------------------------------------
    public void moduloY(int modulus) {
        y %= modulus;
        if (y < 0)
            y += modulus;
    }

    //--------------------------------------------------------------------------
    //  Performs modulo operations on both the x and y value by dims.width and
    //  dims.height, respectively.
    //--------------------------------------------------------------------------
    public void modulo(Dimension dims) {
        moduloX(dims.width);
        moduloY(dims.height);
    }
}
