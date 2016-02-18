package net.magicmantis.src.server.dataStructures;

import net.magicmantis.src.server.OnlineLevel;
import net.magicmantis.src.server.dataStructures.EntityData;

import java.util.ArrayList;

/**
 * LevelData: Class containing serializable data about an online level allowing it to be
 * sent from the server to connected clients.
 */
public class LevelData {

    private int width, height; //width and height of the level (not viewport)
    private ArrayList<EntityData> entities; //list of stored entities on this level
    private int playerID; //the player id for the user to whom this is sent

    /**
     * Store the information about a level for sending to clients.
     *
     * @param level - which level instance to serialize.
     * @param sessionID - the sessionID of the user to send the data to.
     */
    public LevelData(OnlineLevel level, int sessionID) {
        this.width = level.getWidth();
        this.height = level.getHeight();
        entities = new ArrayList<EntityData>();
        synchronized (level.getEntityList()) {
            for (int i = 0; i < level.getEntityList().size(); i++) {
                entities.add(new EntityData(level.getEntityList().get(i)));
            }
        }
        playerID = sessionID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPlayerID() { return playerID; }

    public ArrayList<EntityData> getEntities() {
        return entities;
    }
}
