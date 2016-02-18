package net.magicmantis.src.server.dataStructures;

import net.magicmantis.src.model.*;

/**
 * Created by Joseph on 5/10/2015.
 *
 * EntityData class: This class is used to transfer game object data to and from the server to sync games across
 * all clients. It can hold data of multiple types of classes, as well as the class that the data was taken from.
 */
public class EntityData {

    //general
    private Class tClass;
    private int width, height;
    private double x, y;
    private int imageOffsetX,imageOffsetY;

    //target
    private int team;
    private int life, maxLife;
    private boolean drawHealthBar;
    private int drawHealthBarTimer;

    //ship
    private double direction;
    private double facingDirection;
    private double xSpeed, ySpeed;
    private double speed, maxSpeed;
    private double acceleration;
    private int weapon, refire, accuracy;

    //projectile
    private int timer;

    //player
    private int playerID;

    //spawner
    private int spawnCooldown;
    private int spawnRate;
    private boolean active;

    /**
     * Create an EntityData instance with data taken from entity. How it treats the data is dependant on the class
     * given to the constructor
     *
     * @param entity - instance from which to store data
     */
    public EntityData(Entity entity) {
        tClass = entity.getClass();
        height = entity.getHeight();
        width = entity.getWidth();
        x = entity.getX();
        y = entity.getY();
        imageOffsetX = entity.getImageOffsetX();
        imageOffsetY = entity.getImageOffsetY();

        if (Target.class.isAssignableFrom(tClass)) getTargetData((Target) entity);
        if (Ship.class.isAssignableFrom(tClass)) getShipData((Ship) entity);
        if (Projectile.class.isAssignableFrom(tClass)) getProjectileData((Projectile) entity);
        if (Player.class.isAssignableFrom(tClass)) getPlayerData((Player) entity);
        if (Spawner.class.isAssignableFrom(tClass)) getSpawnerData((Spawner) entity);
    }

    private void getTargetData(Target target) {
        team = target.getTeam();
        life = target.getLife();
        maxLife = target.getMaxLife();
        drawHealthBar = target.getDrawHealthBar();
        drawHealthBarTimer = target.getDrawHealthBarTimer();
    }

    /**
     * This is called by the constructor if the entity passed is a ship. It will query the instance for
     * information specific to ship classes and subclasses.
     *
     * @param ship - ship to query data from (casted reference of entity)
     */
    private void getShipData(Ship ship) {
        direction = ship.getDirection();
        facingDirection = ship.getFacingDirection();
        xSpeed = ship.getXSpeed();
        ySpeed = ship.getYSpeed();
        speed = ship.getSpeed();
        maxSpeed = ship.getMaxSpeed();
        acceleration = ship.getAcceleration();
        weapon = ship.getWeapon();
        refire = ship.getRefire();
        accuracy = ship.getAccuracy();
    }

    /**
     * This is called by the constructor if the entity passed is a ship. It will query the instance for
     * information specific to ship classes and subclasses.
     *
     * @param p - projectile to take data from (casted reference to entity)
     */
    private void getProjectileData(Projectile p) {
        team = p.getTeam();
        speed = p.getSpeed();
        direction = p.getDirection();
        timer = p.getTimer();
    }

    /**
     * Further specializes the ship data if the ship corresponds to a player ship
     *
     * @param p - reference to entity casted as the player class
     */
    private void getPlayerData(Player p) {
        playerID = p.getPlayerID();
    }

    private void getSpawnerData(Spawner s) {
        spawnCooldown = s.getSpawnCooldown();
        spawnRate = s.getSpawnRate();
        active = s.isActive();
    }

    public Class getStoredClass() {
        return tClass;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getImageOffsetX() {
        return imageOffsetX;
    }

    public int getImageOffsetY() {
        return imageOffsetY;
    }

    public double getDirection() {
        return direction;
    }

    public double getFacingDirection() {
        return facingDirection;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public double getSpeed() {
        return speed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public int getTeam() {
        return team;
    }

    public int getWeapon() {
        return weapon;
    }

    public int getRefire() {
        return refire;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getLife() {
        return life;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public int getTimer() {
        return timer;
    }

    public int getPlayerID() { return playerID; }

    public int getSpawnCooldown() {
        return spawnCooldown;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public boolean isActive() {
        return active;
    }

    public boolean getDrawHealthBar() {
        return drawHealthBar;
    }

    public int getDrawHealthBarTimer() {
        return drawHealthBarTimer;
    }
}
