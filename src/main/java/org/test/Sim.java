package org.test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Sim extends JPanel implements ActionListener {

    List<Boid> boids;
    List<Boid> predators;
    private final int DELAY = 25;
    private final Timer timer;

    private final JLabel data;

    public Sim() {
        data = new JLabel();

        add(data);

        timer = new Timer(DELAY, this);
        timer.start();


        InputMap im = getInputMap(WHEN_FOCUSED);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "onQ");

        // reset position and velocities of bodies when q is pressed
        am.put("onQ", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Point test = MouseInfo.getPointerInfo().getLocation();
                System.out.println("x:" + test.x);
                System.out.println("y:" + test.y);
            }
        });

        boids = generateRandomBoids(100, false);
        predators = generateRandomBoids(3, true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 700);
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

        for(Boid boid : boids) {
            boid.update(boids, predators);

            g.setColor(Color.black);
            g.fill(new Ellipse2D.Double(boid.getX() - 6, boid.getY() - 6, 12, 12));
        }
        for(Boid predator : predators){
            predator.update(predators, predators);
            g.setColor(Color.red);
            g.fill(new Ellipse2D.Double(predator.getX() - 6, predator.getY() - 6, 12, 12));
        }


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
        data.setText("test");
        repaint();
    }

    public List<Boid> generateRandomBoids(int number, boolean enemy){
        Random random = new Random();
        List<Boid> boids = new ArrayList<>();
        for(int i = 0; i < number; i++){
            float x = (float) (Math.random()*800 + 100);
            float y = (float) (Math.random()*500 + 100);
            float vx;
            float vy;
            if(random.nextBoolean())
                vx = (float) (Math.random()*0.7071+1.4142);
            else{
                vx = (float) (Math.random()*0.7071-1.4142);
            }
            if(random.nextBoolean())
                vy = (float) (Math.random()*0.7071+1.4142);
            else{
                vy = (float) (Math.random()*0.7071-1.4142);
            }

            System.out.println("x = " + x);
            System.out.println("y = " + y);
            boids.add(new Boid(x, y, vx, vy, enemy));
        }
        return boids;
    }

    public void addBoid(Point location){
        Random random = new Random();
        float vx;
        float vy;
        if(random.nextBoolean())
            vx = (float) (Math.random()*0.7071+1.4142);
        else{
            vx = (float) (Math.random()*0.7071-1.4142);
        }
        if(random.nextBoolean())
            vy = (float) (Math.random()*0.7071+1.4142);
        else{
            vy = (float) (Math.random()*0.7071-1.4142);
        }

        System.out.println("x = " + location.x);
        System.out.println("y = " + location.y);
        boids.add(new Boid(location.x, location.y, vx, vy, false));
    }
}
