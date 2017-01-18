package net.magicmantis.src.model;

import net.magicmantis.src.server.dataStructures.EntityData;
import net.magicmantis.src.view.Screen;

import java.awt.*;

/**
 * Entity class: super class for all in-game objects. Holds information about location, size, and references
 * to the level. As much data as possible is kept private from subclasses.
 */
public abstract class Entity {

    public Level level; //reference to the level in which this entity exists
    private double x, y;  //location data
    private int width, height; //size data
    private int imageOffsetX, imageOffsetY; //information about where to draw this objects image

    private boolean remove;

    /**
     * Create a new Entity instance.
     *
     * @param setX      - initial x location.
     * @param setY      - initial y location.
     * @param setWidth  - width of this entity.
     * @param setHeight - height of this entity.
     * @param setLevel  - reference to the level in which this entity exists.
     */
    public Entity(double setX, double setY, int setWidth, int setHeight, Level setLevel) {
        level = setLevel;
        synchronized (level.getEntityList()) {
            level.addEntity(this);
        }
        x = setX;
        y = setY;
        width = setWidth;
        height = setHeight;
        imageOffsetX = -width / 2;
        imageOffsetY = -height / 2;
        remove = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getImageOffsetX() {
        return imageOffsetX;
    }

    public int getImageOffsetY() {
        return imageOffsetY;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x + imageOffsetX, (int) y + imageOffsetY, width, height);
    }

    public void setX(double setX) {
        x = setX;
    }

    public void setY(double setY) {
        y = setY;
    }

    public void move(double moveX, double moveY) {
        setX(getX() + moveX);
        setY(getY() + moveY);
    }

    public void setWidth(int setWidth) {
        width = setWidth;
    }

    public void setHeight(int setHeight) {
        height = setHeight;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public abstract void update();

    public void draw(int xView, int yView) {
        if (x - xView + width < 0 || x - xView > Screen.WIDTH || y - yView + height < 0 || y - yView > Screen.HEIGHT)
            return;
    }

    public abstract boolean isShip();

    public abstract boolean isTarget();

    /**
     * Create an entity from the data stored in the entityData parameter.
     *
     * @param entityData - EntityData instance containing information about the entity to be created.
     */
    public void fromData(EntityData entityData) {
        this.x = entityData.getX();
        this.y = entityData.getY();
        this.width = entityData.getWidth();
        this.height = entityData.getHeight();
        this.imageOffsetX = entityData.getImageOffsetX();
        this.imageOffsetY = entityData.getImageOffsetY();
    }

    public void destroy() {
        level.removeEntity(this);
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean getRemove() {
        return remove;
    }

}
