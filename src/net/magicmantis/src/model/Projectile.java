package net.magicmantis.src.model;

import net.magicmantis.src.server.dataStructures.EntityData;

import java.awt.Rectangle;

/**
 * Projectile class: serves as a base class for projectile objects fired by
 * various weapons.
 */
public abstract class Projectile extends Entity {
	
	private int team; //which team the projectile belongs to
	private double speed, direction; //the speed and direction the projectile is traveling
	private int timer; //how many ticks the projectile will last
	private Target owner;

	/**
	 * Create a new projectile.
	 *
	 * @param setX - initial x location.
	 * @param setY - initial y location.
	 * @param setWidth - width of the projectile.
	 * @param setHeight - height of the projectile.
	 * @param setTeam - which team the projectile belongs to.
	 * @param setSpeed - initial speed of the projectile.
	 * @param setDirection - initial direction in degrees of the projectiles motion.
	 * @param setTimer - how long (in ticks) the projectile will last.
	 * @param setLevel - reference to the level in which the projectile exists.
	 */
	public Projectile(double setX, double setY, int setWidth, int setHeight, int setTeam, double setSpeed, double setDirection, int setTimer, Target owner, Level setLevel)
	{
		super(setX, setY, setWidth, setHeight, setLevel);
		team = setTeam;
		speed = setSpeed;
		direction = setDirection;
		timer = setTimer;
		this.owner = owner;
	}
	
	public int getTeam()
	{
		return team;
	}
	
	public double getSpeed()
	{
		return speed;
	}
	
	public double getDirection()
	{
		return direction;
	}

    public int getTimer() { return timer; }

    /**
     * Update this projectile
     */
	public void update()
	{
		//move
		double xSpeed = Math.cos(direction*Math.PI/180)*speed;
		double ySpeed = Math.sin(direction*Math.PI/180)*speed;
		move(xSpeed, ySpeed);
		
		//check collision
		checkCollision();
		
		//check timer
		timer -= 1;
		if (timer <= 0)
			destroy();
	}

    /**
     * Check if this projectile has collided with a valid target.
     */
	public void checkCollision()
	{
        //get bounds of entity
		Rectangle r1 = getBounds();
        //loop through all entities in the level
		for (int i = 0; i < level.getEntityList().size(); i++)
        {
			Entity e = level.getEntityList().get(i);
            //ships are valid targets
			if (e.isTarget()) {
                Target t = (Target) e;
                //skip ships on the projectiles team
                if (t.getTeam() == getTeam())
                    continue;
                //get enemy ship bounds
                Rectangle r2 = e.getBounds();
                //check if projectile collides with enemy ship
                if (r2.intersects(r1)) {
                    //if there is a collision, call onImpact()
                    onImpact(t);
                    return;
                }
            }
		}
	}

    /**
     * Behavior of the projectile when it impacts with a valid target.
     * This function should be implemented by subclasses.
     *
     * @param e - the ship with which the projectile collided
     */
	public void onImpact(Target e) {
		level.results.addHit(owner);
	}

    public boolean isShip() {
        return false;
    }

    public boolean isTarget() {
        return false;
    }

	@Override
	public void fromData(EntityData entityData) {
		super.fromData(entityData);
		team = entityData.getTeam();
		speed = entityData.getSpeed();
		direction = entityData.getDirection();
		timer = entityData.getTimer();
	}

	public Target getOwner() {
		return owner;
	}
}