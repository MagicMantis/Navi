package net.magicmantis.src.model.ships;

import net.magicmantis.src.model.Entity;
import net.magicmantis.src.model.Headquarters;
import net.magicmantis.src.model.Level;
import net.magicmantis.src.model.ships.Ship;
import net.magicmantis.src.server.dataStructures.EntityData;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Player class: this class is a subclas of Ship that is controlled by a players client.
 * It is controllable by the game that owns it though user input.
 */
public class Player extends Ship {

    private int playerID; //id of this player instance for online identification

    public static double[] PLAYER_COLOR = {0d, 1d, 1d}; //color that the player will appear ingame

    /**
     * Create a new player with default parameters. Used in combination with fromData().
     */
    public Player() {
        super(0,0,16,16,1,100,4,2,null);
        playerID = -1;
    }

    /**
     * Create a new Player instance.
     *
     * @param x - initial x location.
     * @param y - initial y location.
     * @param pid - player id (for online play).
     * @param team - which team the player belongs to.
     * @param level - reference to the level in which this player exists.
     */
	public Player(int x, int y, int pid, int team, Level level)
	{
		super(x, y, 16, 16, team, 100, 4, 2, level);
        playerID = pid;
	}

    /**
     * Create a player instance from data stored in a EntityData object.
     *
     * @param entityData - instance to take data from.
     */
    @Override
    public void fromData(EntityData entityData) {
        super.fromData(entityData);
        playerID = entityData.getPlayerID();
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public double[] getColor() {
        double[] pCol = Ship.teamColors[getTeam()-1].clone();
        int max = 0;
        for (int i = 1; i < 3; i++) {
            if (pCol[i] > pCol[max]) max = i;
            if (max == 0) pCol[2] += .3;
            if (max == 1) pCol[0] += .3;
            if (max == 2) pCol[1] += .3;
        }
        return pCol;
    }

    @Override
    public void destroy() {
        level.removeEntity(this);
        for (Entity e : level.getEntityList()) {
            if (e instanceof Headquarters) {
                Headquarters hq = (Headquarters) e;
                if (hq.getTeam() == this.getTeam()){
                    hq.queueSpawn(this);
                    setX(hq.getX());
                    setY(hq.getY());
                }
            }
        }
    }

    /*// Player sprite?
    @Override
    public void draw(int xView, int yView)
    {
        super.draw(xView, yView);

        //get the color from the entity
        double[] col = getColor();
        GL11.glColor3d(col[0], col[1], col[2]);

        double facingDir = getFacingDirection();

        //create angles
        double xDir = Math.cos(facingDir*Math.PI/180);
        double yDir = Math.sin(facingDir*Math.PI/180);
        double xDir2 = Math.cos((facingDir+120)%360*Math.PI/180);
        double yDir2 = Math.sin((facingDir+120)%360*Math.PI/180);
        double xDir3 = Math.cos((facingDir-120)%360*Math.PI/180);
        double yDir3 = Math.sin((facingDir-120)%360*Math.PI/180);
        double xDir4 = Math.cos((facingDir+150)%360*Math.PI/180);
        double yDir4 = Math.sin((facingDir+150)%360*Math.PI/180);
        double xDir5 = Math.cos((facingDir-150)%360*Math.PI/180);
        double yDir5 = Math.sin((facingDir-150)%360*Math.PI/180);
        double xDir6 = Math.cos((facingDir+30)%360*Math.PI/180);
        double yDir6 = Math.sin((facingDir+30)%360*Math.PI/180);
        double xDir7 = Math.cos((facingDir-30)%360*Math.PI/180);
        double yDir7 = Math.sin((facingDir-30)%360*Math.PI/180);

        glBegin(GL_LINE_LOOP);
        glVertex2d((getX()+xDir*-getWidth()/4)-xView, (getY()+yDir*-getHeight()/4)-yView);
        glVertex2d((getX()+xDir*3*getWidth()/4)-xView, (getY()+yDir*3*getHeight()/4)-yView);
        glVertex2d((getX()+xDir6*getWidth()/2)-xView, (getY()+yDir6*getHeight()/2)-yView);
        glVertex2d((getX()+xDir2*3*getWidth()/8)-xView, (getY()+yDir2*3*getWidth()/8)-yView);
        glVertex2d((getX()+xDir4*getWidth()/2)-xView, (getY()+yDir4*getWidth()/2)-yView);
        glVertex2d((getX()+xDir5*getWidth()/2)-xView, (getY()+yDir5*getWidth()/2)-yView);
        glVertex2d((getX()+xDir3*3*getWidth()/8)-xView, (getY()+yDir3*3*getWidth()/8)-yView);
        glVertex2d((getX()+xDir7*getWidth()/2)-xView, (getY()+yDir7*getWidth()/2)-yView);
        glVertex2d((getX()+xDir*3*getWidth()/4)-xView, (getY()+yDir*3*getHeight()/4)-yView);
        glEnd();
    }*/
}
