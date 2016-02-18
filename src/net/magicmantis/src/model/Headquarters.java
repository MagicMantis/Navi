package net.magicmantis.src.model;

import net.magicmantis.src.model.ships.Player;
import net.magicmantis.src.server.dataStructures.EntityData;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

/**
 * Created by Joseph on 12/14/2015.
 */
public class Headquarters extends Spawner {

    private ArrayList<Player> respawnQueue;

    public Headquarters(int x, int y, int team, Level level) {
        super(x, y, 90, 60, team, 1500, 300, level);
        respawnQueue = new ArrayList<Player>();
    }

    @Override
    public void update() {
        super.update();
        if (!respawnQueue.isEmpty()) activate();
        else deactivate();
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

    public void queueSpawn(Player player) {
        respawnQueue.add(player);
    }

    @Override
    public void fromData(EntityData entityData) {
        super.fromData(entityData);
    }

    @Override
    public void spawnEntity() {
        Player spawnPlayer = respawnQueue.get(0);
        spawnPlayer.reset();
        level.addEntity(spawnPlayer);
        respawnQueue.remove(0);
    }
}
