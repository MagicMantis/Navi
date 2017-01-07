package net.magicmantis.src.server;

import net.magicmantis.src.model.*;
import net.magicmantis.src.server.dataStructures.EntityData;
import net.magicmantis.src.server.dataStructures.UserData;
import net.magicmantis.src.view.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * OnlineLevel class: subclass of Level suited for online play. Clients will handle rendering and input, but
 * the OnlineLevel class handles all logic processing for an online game.
 */
public class OnlineLevel extends Level {

    //private int width, height;
    private OnlineGame onlineGame; //reference to the OnlineGame that this level belongs to

    private HashMap<Integer, Player> playerShips; //references to which ships belong to which users
    private ArrayList<EntityData> entityQueue;

    /**
     * Create a new online level.
     *
     * @param game - pass reference to the online game that this instance is associated with.
     * @param width - width of the level.
     * @param height - height of the level.
     */
    public OnlineLevel(OnlineGame game, int width, int height, Map<String, Object> options) {
        super(null, width, height);
        this.onlineGame = game;

        playerShips = new HashMap<Integer, Player>();
        entityQueue = new ArrayList<EntityData>();
        generateSpace();
        generateUnits();
    }

    /**
     * Generate environment, synced across all clients.
     */
    @Override
    protected void generateSpace()
    {
        for(int i = 0; i < getWidth(); i++)
        {
            for(int j = 0; j < getHeight(); j++)
            {
                if (Game.rand.nextInt(2000) == 0)
                    new Star(i, j);
            }
        }
    }

    /**
     * Create player ships for each player connected to the lobby when the game was started.
     */
    public void generateUnits() {
        for (int i = 0; i < onlineGame.getPlayers().size(); i++) {
            //retrieve data
            User u = onlineGame.getPlayers().get(i);
            UserData ud = onlineGame.getUserData().get(u.getID());

            //generate suitable spawn location
            int xSpawn = Game.rand.nextInt(getWidth());
            while (xSpawn < 400 || xSpawn > getWidth()-400) xSpawn = Game.rand.nextInt(getWidth());
            int ySpawn = Game.rand.nextInt(getHeight());
            while (ySpawn < 400 || ySpawn > getHeight()-400) ySpawn = Game.rand.nextInt(getHeight());

            //create the player base
            new Headquarters(xSpawn, ySpawn, ud.getTeam(), this);
            if ((boolean) onlineGame.getOptions().get("spawnFactories")) new Factory(xSpawn+120, ySpawn, ud.getTeam(), this);
            playerShips.put(u.getID(), new Player(xSpawn, ySpawn, u.getID(), ud.getTeam(), this));
        }
    }

    /**
     * Update all the entities in the level. No rendering or input handled.
     */
    @Override
    public void update() {

        /** queue up entities and create them from data??? */
        synchronized (getEntityList()) {
            while (entityQueue.size() > 0) {

            }
            for (int i = 0; i < getEntityList().size(); i++) {
                Entity e = getEntityList().get(i);
                e.update();
            }
        }
    }

    public void queueCreate(EntityData entityData) {
        this.entityQueue.add(entityData);
    }

    public OnlineGame getGame() {
        return onlineGame;
    }

    public void setGame(OnlineGame game) {
        this.onlineGame = game;
    }

    public Player getPlayer(int sessionID) {
        return playerShips.get(sessionID);
    }
}
