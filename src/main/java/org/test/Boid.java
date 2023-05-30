package org.test;

import java.util.List;

public class Boid {
    public static final double turnfactor = 0.2;
    public static final double visualRange = 100;
    public static final double protectedRange = 25;
    public static final double runRange = 66;
    public static final double centeringfactor = 0.0005;
    public static final double avoidfactor = 0.05;
    public static final double matchingfactor = 0.05;
    public static final double escapefactor = 0.01;
    public static final double maxspeed = 3;
    public static final double minspeed = 2;
    public static final double topmargin = 150;
    public static final double bottommargin = 150;
    public static final double leftmargin = 150;
    public static final double rightmargin = 150;

    public float x;
    public float y;
    public float vx;
    public float vy;
    public boolean enemy;

    public Boid(float x, float y, float vx, float vy, boolean enemy){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.enemy = enemy;
    }

    public void update(List<Boid> boids, List<Boid> predators){
        int neighborCounter = 0;
        float avgVx = 0;
        float avgVy = 0;
        float avgX = 0;
        float avgY = 0;

        for(Boid boid : boids){
            double distance = Math.sqrt(Math.pow(boid.x-x, 2) + Math.pow(boid.y-y, 2));
            if(distance <= protectedRange) {
                separate(boid);
            }else if(distance <= visualRange && !enemy){
                neighborCounter++;
                avgVx += boid.vx;
                avgVy += boid.vy;

                avgX += boid.x;
                avgY += boid.y;
            }
        }
        if(neighborCounter != 0 && !enemy){
            align(avgVx, avgVy, neighborCounter);
            group(avgX, avgY, neighborCounter);
        }
        stayInBounds();
        limitSpeed();
        if(!enemy)
            runFromEnemy(predators);

        x += vx;
        y += vy;
    }

    public void separate(Boid boid){
        vx += (x - boid.x) * avoidfactor;
        vy += (y - boid.y) * avoidfactor;
    }

    public void align(float avgVx, float avgVy, int neighborCounter){
        vx += (avgVx/neighborCounter - vx) * matchingfactor;
        vy += (avgVy/neighborCounter - vy) * matchingfactor;
    }

    public void group(float avgX, float avgY, int neighborCounter){
        vx += (avgX/neighborCounter - x) * centeringfactor;
        vy += (avgY/neighborCounter - y) * centeringfactor;
    }

    public void stayInBounds(){
        if(x < leftmargin) vx += turnfactor;
        if(x > 1000-rightmargin) vx -= turnfactor;
        if(y < bottommargin) vy += turnfactor;
        if(y > 700-topmargin) vy -= turnfactor;
    }

    public void limitSpeed(){
        double speed = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
        if(speed < minspeed){
            vx = (float) (vx/speed * minspeed);
            vy = (float) (vy/speed * minspeed);
        }
        if (speed > maxspeed) {
            vx = (float) (vx/speed * maxspeed);
            vy = (float) (vy/speed * maxspeed);
        }
    }

    public void runFromEnemy(List<Boid> predators){
        for(Boid predator : predators){
            double distance = Math.sqrt(Math.pow(predator.x-x, 2) + Math.pow(predator.y-y, 2));
            if(distance <= visualRange){
                vx -= (predator.x-x)*escapefactor;
                vy -= (predator.y-y)*escapefactor;
            }
        }
    }

    public float getX() {return x;}
    public float getY() {return y;}
    public float getVx() {return vx;}
    public float getVy() {return vy;}

}
