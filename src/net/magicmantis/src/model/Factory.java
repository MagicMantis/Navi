package net.magicmantis.src.model;

import net.magicmantis.src.server.dataStructures.EntityData;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

/**
 * Factory class: this structure spawns drones at a periodic rate.
 */
public class Factory extends Spawner {

    public Factory(double x, double y, int team, Level level) {
        super(x, y, 60, 60, team, 800, 300, level);
        activate();
    }

    @Override
    public void update() {
        super.update();
        int droneCount = 0;
        for (Entity e : level.getEntityList()) {
            if (e instanceof Drone && ((Drone) e).getTeam() == getTeam()) {
                droneCount++;
            }
        }
        if (droneCount >= (int) level.getOptions().get("droneMax")) {
            deactivate();
        }
        else {
            activate();
        }
    }

    @Override
    public void spawnEntity() {
        new Drone(getX(), getY(), getTeam(), level);
    }

    @Override
    public void fromData(EntityData entityData) {
        super.fromData(entityData);
    }

    @Override
    public void draw(int xView, int yView) {
        super.draw(xView, yView);

        //get the color from the entity
        double[] col = getColor();
        glColor3d(col[0], col[1], col[2]);

        glBegin(GL_LINE_LOOP);
        glVertex2d(getX()-xView-getWidth()/2, getY()-yView-getHeight()/2);
        glVertex2d(getX()-xView+getWidth()/2, getY()-yView-getHeight()/2);
        glVertex2d(getX()-xView+getWidth()/2, getY()-yView+getHeight()/2);
        glVertex2d(getX()-xView-getWidth()/2, getY()-yView+getHeight()/2);
        glEnd();

        //draw respawn box
        if (isActive()) {
            glColor3d(1d, 1d, 1d);
            double ratio = (double) getSpawnCooldown() / (double) getSpawnRate();
            glBegin(GL_LINE_LOOP);
            glVertex2d(getX()-xView-getWidth()/2*ratio, getY()-yView-getHeight()/2*ratio);
            glVertex2d(getX()-xView+getWidth()/2*ratio, getY()-yView-getHeight()/2*ratio);
            glVertex2d(getX()-xView+getWidth()/2*ratio, getY()-yView+getHeight()/2*ratio);
            glVertex2d(getX()-xView-getWidth()/2*ratio, getY()-yView+getHeight()/2*ratio);
            glEnd();
        }
    }
}
