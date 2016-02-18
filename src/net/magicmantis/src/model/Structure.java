package net.magicmantis.src.model;

/**
 * Created by Joseph on 12/14/2015.
 */
public abstract class Structure extends Target {

    public Structure(double x, double y, int width, int height, int team, int life, Level level) {
        super(x, y, width, height, team, life, level);
    }

    public boolean isShip() { return false; }

    public boolean isTarget() { return true; }

}
