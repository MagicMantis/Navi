package net.magicmantis.src.model.ships;

import net.magicmantis.src.model.Bullet;
import net.magicmantis.src.model.Level;
import net.magicmantis.src.model.Target;
import net.magicmantis.src.server.dataStructures.EntityData;
import net.magicmantis.src.view.Game;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;

/**
 * Ship class: base class for all ships, including AI, player, and player controller ships.
 * Contains data relevant to all ships, such as direction, speed, weapons, and health.
 */
public class Ship extends Target {
	
	private double direction; //direction of momentum
	private double facingDirection; //direction faced (gun direction)
	private double xSpeed, ySpeed; //current horizontal and vertical speed
	private double speed, maxSpeed; //total speed per tick, and the maximum speed allowed
	private double acceleration; //multiplier for acceleration
	private int weapon, refire, accuracy; //which weapon this ship has equipped, weapon cooldown, and weapon accuracy
	
	public Random rand = Game.rand; //reference to random

	/**
	 * Create a new ship instance.
	 *
	 * @param x - initial x location.
	 * @param y - initial y location.
	 * @param width - width of this ship.
	 * @param height - height of this ship.
     * @param team - the team that this ship belongs to.
     * @param maxLife - how much life the ship as upon spawn.
	 * @param maxSpeed - maximum speed of this ship.
	 * @param weapon - which weapon this ship has equipped.
	 * @param level - reference to the level in which this ship exists.
	 */
	public Ship(double x, double y, int width, int height, int team, int maxLife, double maxSpeed, int weapon, Level level)
	{
		super(x, y, width, height, team, maxLife, level);
        this.maxSpeed = maxSpeed;
        this.weapon = weapon;
        acceleration = 0.1;
        accuracy = 0;
        reset();
	}

    /**
     * Called upon spawning
     */
    public void reset() {
        //reset life
        setLife(getMaxLife());

        //movement stats
        direction = 0;
        facingDirection = 0;
        xSpeed = 0;
        ySpeed = 0;
        speed = 0;

        //weapon stats
        refire = 0;
    }
	
	public double getXSpeed()
	{
		return xSpeed;
	}
	
	public double getYSpeed()
	{
		return ySpeed;
	}

    public double getSpeed() { return speed; }

    public double getMaxSpeed() { return maxSpeed; }
	
	public double getFacingDirection()
	{
		return facingDirection;
	}
	
	public void setFacingDirection(double setFacingDirection)
	{
		facingDirection = setFacingDirection;
	}
	
	public double getDirection()
	{
		return direction;
	}
	
	public void setDirection(double setDirection)
	{
		direction = setDirection;
	}

	public int getRefire()
	{
		return refire;
	}
	
	public int getAccuracy()
	{
		return accuracy;
	} 
	
	public void setAccuracy(int setAccuracy)
	{
		accuracy = setAccuracy;
	}

    public double getAcceleration() { return acceleration; }

    public int getWeapon() { return weapon; }

	/**
	 * Update this ship.
	 */
	@Override
	public void update()
	{
        super.update();

		//move xSpeed and ySpeed
		move(xSpeed, ySpeed);

		//reduce weapon cooldown
		if (refire > 0)
			refire -= 1;

		//wrap screen
		if (getX() >= level.getWidth()+getWidth()/2 || getX() <= 0-getWidth()/2)
			setX(level.getWidth()-getX());
		if (getY() >= level.getHeight()+getHeight()/2 || getY() <= 0-getHeight()/2)
			setY(level.getHeight()-getY());
	}

	/**
	 * Rotate the ship by the specified degrees.
	 *
	 * @param addDirection - degrees to rotate the ship.
	 */
	public void rotate(double addDirection)
	{
		facingDirection += addDirection;
		while (facingDirection < 0)
			facingDirection += 360;
		while (facingDirection >= 360)
			facingDirection -= 360;
	}

	/**
	 * Add speed to the ship in the direction currently being faced.
	 * @param magnitude
	 */
	public void accelerate(double magnitude)
	{
		//calculate new x and y speeds
		double xSpeedChange = Math.cos(facingDirection*Math.PI/180)*acceleration*magnitude;
		double ySpeedChange = Math.sin(facingDirection*Math.PI/180)*acceleration*magnitude;
		xSpeed += xSpeedChange;
		ySpeed += ySpeedChange;
		direction = Math.atan2(ySpeed, xSpeed);

		//scale speed to not exceed maxSpeed
		speed = Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));
		double ratio;
		if (speed > maxSpeed)
		{
			ratio = xSpeed/speed;
			xSpeed = ratio*maxSpeed;
			ratio = ySpeed/speed;
			ySpeed = ratio*maxSpeed;
		}
	}

	/**
	 * Reduce the magnitude of current speed.
	 *
	 * @param magnitude - amount of speed to reduce.
	 */
	public void decelerate(double magnitude)
	{
		magnitude = 1-magnitude;
		if (magnitude >= 0)
		{
			xSpeed = xSpeed*magnitude;
			ySpeed = ySpeed*magnitude;
		}
        if (Math.abs(xSpeed) < .1 && Math.abs(ySpeed) < .1) {
            xSpeed = ySpeed = 0;
        }
	}

	/**
	 * Fires the currently equipped weapon if it is not on cooldown.
	 */
	public void fireWeapon()
	{
		//if weapon is on cooldown, return
		if (refire > 0)
			return;
		//if weapon is standard gun
		if (weapon == 1)
		{
			//fire a single bullet and set cooldown to 50 ticks
			fireBullet(rand.nextInt(accuracy+1)-(accuracy+1)/2);
			refire = 50;
		}
		//if weapon is shotgun variant
		if (weapon == 2)
		{
			//fire 5 bullets and set cooldown to 50 ticks
			fireBullet(rand.nextInt(3));
			fireBullet(rand.nextInt(3));
			fireBullet(-rand.nextInt(3));
			fireBullet(rand.nextInt(3));
			fireBullet(rand.nextInt(3));
			refire = 50;
		}
	}

	/**
	 * Creates a bullet instance at current location with a direction specified.
	 *
	 * @param offset - amount of direction offset
	 */
	public void fireBullet(int offset)
	{
		new Bullet(getX(), getY(), getTeam(), 4+rand.nextInt(4)/2, getFacingDirection()+offset, level);
	}


	/**
	 * Draw this ship
	 *
	 * @param xView - offset due to viewport
	 * @param yView - offset due to viewport
	 */
	@Override
	public void draw(int xView, int yView)
	{
        super.draw(xView, yView);

		//get the color from the entity
        double[] col = getColor();
        GL11.glColor3d(col[0], col[1], col[2]);

        glBegin(GL_LINE_LOOP);
        for (int i = 0; i < 360; i++) {
            float theta = 2.0f * 3.1415926f * (float) i / 360f;
            float x = getWidth() * (float) Math.cos(theta);
            float y = getHeight() * (float) Math.sin(theta);
            glVertex2f(x/2+ (float) getX() - xView, y/2+ (float) getY() - yView);
        }
        glEnd();
        double xDir = Math.cos(facingDirection*Math.PI/180);
        double yDir = Math.sin(facingDirection*Math.PI/180);
        glBegin(GL_LINES);
        glVertex2d(getX()-xView,getY()-yView);
        glVertex2d((getX()+xDir*8)-xView, (getY()+yDir*8)-yView);
        glEnd();
	}

    public boolean isShip() {
        return true;
    }

    public boolean isTarget() { return true; }

	/**
	 * Initializes this ship to the data provided by the passed EntityData instance.
	 *
	 * @param entityData - EntityData instance containing information about the entity to be created.
	 */
    @Override
    public void fromData(EntityData entityData) {
        super.fromData(entityData);
        direction = entityData.getDirection();
        facingDirection = entityData.getFacingDirection();
        xSpeed = entityData.getxSpeed();
        ySpeed = entityData.getySpeed();
        speed = entityData.getSpeed();
        maxSpeed = entityData.getMaxSpeed();
        acceleration = entityData.getAcceleration();
        weapon = entityData.getWeapon();
        refire = entityData.getRefire();
        accuracy = entityData.getAccuracy();
    }
}
