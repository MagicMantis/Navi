package net.magicmantis.src.model;

import net.magicmantis.src.server.dataStructures.EntityData;

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
}
