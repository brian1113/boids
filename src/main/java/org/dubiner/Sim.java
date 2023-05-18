package org.dubiner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;

public class Sim extends JPanel implements ActionListener {

    private final int DELAY = 1;
    private final Timer timer;

    private final Body body1, body2;

    private double fg, ftheta, fgx, fgy, d;

    private static final double G = 65;

    private final int bodyDisplaySize = 20;

    private final JLabel data;

    public Sim() {
        data = new JLabel();

        add(data);

        timer = new Timer(DELAY, this);
        timer.start();

        body1 = new Body(100, 100, 100, 3, 0, 0, 0);
        body2 = new Body(100, -100, -100, -3, 0, 0, 0);


        InputMap im = getInputMap(WHEN_FOCUSED);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "onQ");

        // reset position and velocities of bodies when q is pressed
        am.put("onQ", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                body1.reset();
                body2.reset();
            }
        });

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    // Create two velocity variables VX and VY
    // Along with two position variables xx and xy And two acceleration variables ax and ay
    // Calculate the acceleration given the mass of the object and the position of the object relative to the other object.
    // Adjust the velocity on every frame by the acceleration. Adjust the position on every frame by the velocity.
    // The force is pointing towards the other object so if you know the relative angle of the two objects you can use
    // that angle to split the force into X and Y components
    public void draw(Graphics gd) {
        Graphics2D g = (Graphics2D) gd;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(rh);

        // calculate distance between bodies with distance formula
        d = Math.sqrt(Math.pow(body2.getX() - body1.getX(), 2) + Math.pow(body2.getY() - body1.getY(), 2));

        // calculate gravitational force magnitude
        fg = (G * body1.getM() * body2.getM()) / Math.pow(d, 2);

        // calculate relative angle between the two bodies
        // atan2 returns radians
        ftheta = Math.atan2(body2.getY() - body1.getY(),
                body2.getX() - body1.getX());

        // calculate x and y components of fg
        fgx = Math.cos(ftheta) * fg;
        fgy = Math.sin(ftheta) * fg;

        body1.update(fgx, fgy);
        // negative because its other direction lol
        body2.update(-fgx, -fgy);

        // convert cartesian coordinates into pixel coordinates
        // http://edspi31415.blogspot.com/2012/09/cartesian-coordinates-to-pixel-screen.html
        // xp = (x - Xmin) * A / (Xmax - Xmin)
        // yp = (y - Ymax) * -B / (Ymax - Ymin)
        double xp1 = (body1.getX() + getWidth()) * getWidth() / (getWidth() + getWidth());
        double yp1 = (body1.getY() + getHeight()) * getHeight() / (getHeight() + getHeight());

        double xp2 = (body2.getX() + getWidth()) * getWidth() / (getWidth() + getWidth());
        double yp2 = (body2.getY() + getHeight()) * getHeight() / (getHeight() + getHeight());

        // draw body1 and body2
        g.setColor(Color.RED);
        g.fill(new Ellipse2D.Double(xp1 - bodyDisplaySize / 2., yp1 - bodyDisplaySize / 2., 20, 20));
        g.setColor(Color.GREEN);
        g.fill(new Ellipse2D.Double(xp2 - bodyDisplaySize / 2., yp2 - bodyDisplaySize / 2., 20, 20));

        // draw center point
        g.setColor(Color.BLACK);
        g.fill(new Ellipse2D.Double(getWidth() / 2. - 5 / 2., getHeight() / 2. - 5 / 2., 5, 5));
    }

    @Override
    protected void paintComponent(Graphics gd) {
        super.paintComponent(gd);
        draw(gd);
    }

    public Timer getTimer() {
        return timer;
    }

    // will be called every DELAY by the timer
    @Override
    public void actionPerformed(ActionEvent e) {
        data.setText("<html>Body 1: " + body1 + "<br>Body 2: " + body2 + "</html>");
        repaint();
    }
}

