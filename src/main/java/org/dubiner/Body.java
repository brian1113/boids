package org.dubiner;

// represents a celestial body
public class Body {
    private double m, x, y, vX, vY, aX, aY;
    private final double pX, pY, pvX, pvY, paX, paY;

    public Body(double m, double x, double y, double vX, double vY, double aX, double aY) {
        this.m = m;
        this.x = x;
        this.y = y;
        this.vX = vX;
        this.vY = vY;
        this.aX = aX;
        this.aY = aY;

        this.pX = x;
        this.pY = y;
        this.pvX = vX;
        this.pvY = vY;
        this.paX = aX;
        this.paY = aY;
    }

    public void update(double fgx, double fgy) {
        aX = fgx / m;
        aY = fgy / m;
        vX += aX;
        vY += aY;
        x += vX;
        y += vY;
    }

    public void reset() {
        x = pX;
        y = pY;
        vX = pvX;
        vY = pvY;
        aX = paX;
        aY = paY;
    }

    public double getM() {
        return m;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return
                String.format("m=%010f", m) +
                String.format(", x=%010f", x) +
                String.format(", y=%010f", y) +
                String.format(", vX=%010f", vX) +
                String.format(", vY=%010f", vY) +
                String.format(", aX=%010f", aX) +
                String.format(", aY=%010f", aY);
    }
}
