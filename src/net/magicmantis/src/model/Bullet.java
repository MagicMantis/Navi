package net.magicmantis.src.model;

import net.magicmantis.src.server.dataStructures.EntityData;
import org.lwjgl.opengl.GL11;

/**
 * Bullet class: implementation of the projectile abstract class. This is a basic projectile used by most basic
 * weapons.
 */
public class Bullet extends Projectile {

    /**
     * Create a new projectile instance.
     *
     * @param setX - initial x location of the bullet
     * @param setY - initial y location of the bullet
     * @param setTeam - which team fired the bullet (used to determine collision
     * @param setSpeed - speed at which the bullet travels per tick
     * @param setDirection - direction in degrees
     * @param setLevel - reference to the level in which the bullet exists
     */
    public Bullet(double setX, double setY, int setTeam, double setSpeed, double setDirection, Target owner, Level setLevel)
	{
		super(setX, setY, 3, 3, setTeam, setSpeed, setDirection, 100, owner, setLevel);
	}

    /**
     * Update this bullet instance.
     */
	public void update()
	{
		super.update();
	}

    /**
     * Draw this bullet instance.
     *
     * @param xView - offset from camera view position
     * @param yView - offset from camera view position
     */
    @Override
	public void draw(int xView, int yView)
	{
        super.draw(xView, yView);

        //color
        double col[] = getColor();
        GL11.glColor3d(col[0],col[1],col[2]);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(getX() - xView, getY() - yView);
        GL11.glVertex2d(getX()-xView+getWidth(), getY()-yView);
        GL11.glVertex2d(getX()-xView+getWidth(), getY()-yView+getHeight());
        GL11.glVertex2d(getX()-xView, getY()-yView+getHeight());
        GL11.glEnd();
	}

    /**
     * Called upon impacting with a valid entity.
     *
     * @param e - entity collided with.
     */
	public void onImpact(Target e)
	{
		if (e.damage(10)) level.results.addKill(getOwner());
		level.results.addDamage(getOwner(), 10);
		destroy();
	}

    /**
     * Create a bullet instance from data supplied by an EntityData object.
     *
     * @param entityData - instance containing data.
     */
    @Override
    public void fromData(EntityData entityData) {
        super.fromData(entityData);
    }

    /**
     * Get the color of the object dependant on team.
     *
     * @return array of three doubles, representing red, green, and blue values of the color to be made.
     */
    private double[] getColor() {
        switch (getTeam()) {
            case 1: return Target.BLUE_TEAM_COLOR;
            case 2: return Target.GREEN_TEAM_COLOR;
            case 3: return Target.RED_TEAM_COLOR;
            case 4: return Target.YELLOW_TEAM_COLOR;
            case 5: return Target.PURPLE_TEAM_COLOR;
            case 6: return Target.ORANGE_TEAM_COLOR;
            case 7: return Target.LIME_TEAM_COLOR;
            case 8: return Target.PINK_TEAM_COLOR;
            default: return Target.BLUE_TEAM_COLOR;
        }
    }

}
