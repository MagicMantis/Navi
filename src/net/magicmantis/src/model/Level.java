package net.magicmantis.src.model;

import net.magicmantis.src.server.dataStructures.EntityData;
import net.magicmantis.src.server.dataStructures.LevelData;
import net.magicmantis.src.view.Game;
import net.magicmantis.src.services.TextEngine;

import java.util.*;

/**
 * Level class: this class holds all information about entities and game objects.
 */
public class Level {
	
	private int width, height; //width and height of the level, affects gameplay but not rendering
	private List<Entity> entityList; //list of references to all entities
	private List<Entity> addList = new ArrayList<Entity>(); //list of entities to add next cycle
	private List<Entity> removeList = new ArrayList<Entity>(); //list of entities to add next cycle
    private Game game; //reference to the game object
	public Player player; //reference to the player belonging to this client
    public Results results;
    public int xView, yView, xViewSize, yViewSize; //current camera position and size
    private boolean lockedCamera; //camera is following player
    private double minimapWidth, minimapHeight; //minimap size information

    private Map<String, Object> options; //stores options that affect the execution of this level (such as friendly fire)

	/**
	 * Create a new instance of level of the specified size.
	 *
	 * @param game - reference to the game object that this level belongs to.
	 * @param width - new width of the level.
	 * @param height - new height of the level.
	 */
    public Level(Game game, int width, int height, Map<String, Object> options)
	{
        //set width and height
        this.width = width;
        this.height = height;

        this.entityList = Collections.synchronizedList(new ArrayList<Entity>());
        this.results = new Results();

        //only do the rest for offline games
        if (game == null) return;

        //determine view screen size
        if (width > game.getWidth()) xViewSize = game.getWidth();
        else xViewSize = width;
        if (height > game.getHeight()) yViewSize = game.getHeight();
        else yViewSize = height;

        this.game = game;
        minimapWidth = minimapHeight = 0;
        lockedCamera = true;

        this.options = options;
    }

    public Level(Game game, int width, int height) {
        this(game, width, height, null);

        //define options
        options = new HashMap<String, Object>();
        options.put("droneMax", 50);
        options.put("friendlyFire", false);
        options.put("allowTeams", true);
        options.put("spawnFactories", true);
        options.put("spawnHeadquarters", true);
    }

	/**
	 * Generate the level, call private methods that generate the individual parts
	 */
    public void generate() {
        results.resetTeamCount();
        generateSpace();
        generateUnits();
    }

	/**
	 * Generate the stars and environment
	 */
	protected void generateSpace()
	{
		Star.starList.clear();
        for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				if (Game.rand.nextInt(2000) == 0)
					new Star(i, j);
			}
		}
	}

	/**
	 * Generate units and structures.
	 */
	private void generateUnits()
	{
		//set units per team
		int blueDrones = Game.rand.nextInt(10);
		int greenDrones = Game.rand.nextInt(10);
		int redDrones = Game.rand.nextInt(10);
		int yellowDrones = Game.rand.nextInt(10);

		yellowDrones = 0;
		redDrones = 0;
		greenDrones = 12;

        options.put("spawnFactories", false);
        options.put("spawnHeadquarters", false);
		
		//determine amount of teams
        Headquarters blueHQ, greenHQ, redHQ, yellowHQ;
		int teams = 0;
        if ((boolean) options.get("spawnHeadquarters"))
            blueHQ = new Headquarters(width/8, height/8, 1, this);
		if (blueDrones > 0) {
            teams++;
            if ((boolean) options.get("spawnFactories")) {
                new Factory(width / 8, height / 4, 1, this);
                new Factory(width / 4, height / 8, 1, this);
            }
        }
		if (greenDrones > 0) {
            teams++;
            if ((boolean) options.get("spawnHeadquarters"))
                greenHQ = new Headquarters(7*width/8, height/8, 2, this);
            if ((boolean) options.get("spawnFactories")) {
                new Factory(7 * width / 8, height / 4, 2, this);
                new Factory(3 * width / 4, height / 8, 2, this);
            }
        }
		if (redDrones > 0) {
            teams++;
            if ((boolean) options.get("spawnHeadquarters"))
                redHQ = new Headquarters(width/8, 7*height/8, 3, this);
            if ((boolean) options.get("spawnFactories")) {
                new Factory(width / 8, 3 * height / 4, 3, this);
                new Factory(width / 4, 7 * height / 8, 3, this);
            }
        }
		if (yellowDrones > 0) {
            teams++;
            if ((boolean) options.get("spawnHeadquarters"))
                yellowHQ = new Headquarters(7*width/8, 7*height/8, 4, this);
            if ((boolean) options.get("spawnFactories")) {
                new Factory(7 * width / 8, 3 * height / 4, 4, this);
                new Factory(3 * width / 4, 7 * height / 8, 4, this);
            }
        }

		//depending on teams, spawn ships in various locations
		if (teams > 2)
		{
			player = new Player(width/8, height/8, 0, 1, this);
			for (int i = 0; i < blueDrones; i++)
			{
				new Drone(Game.rand.nextInt(width/4), Game.rand.nextInt(height/4), 1, this);
			}
			for (int i = 0; i < greenDrones; i++)
			{
				new Drone(Game.rand.nextInt(width/4)+3*width/4, Game.rand.nextInt(height/4), 2, this);
			}
			for (int i = 0; i < redDrones; i++)
			{
				new Drone(Game.rand.nextInt(width/4), Game.rand.nextInt(height/4)+3*height/4, 3, this);
			}
			for (int i = 0; i < yellowDrones; i++)
			{
				new Drone(Game.rand.nextInt(width/4)+3*width/4, Game.rand.nextInt(height/4)+3*height/4, 4, this);
			}
		}
		else if (teams <= 2)
		{
			int team1 = 0;
			int team2 = 0;
			int limit1 = 0;
			int limit2 = 0;
			boolean blue, green, red, yellow;
			blue = green = red = yellow = false;

            blue = true;
			if (greenDrones > 0)
				green = true;
			if (redDrones > 0)
				red = true;
			if (yellowDrones > 0)
				yellow = true;
			
			if (blue)
			{
				team1 = 1;
				limit1 = blueDrones;
				player = new Player(width/8, height/2, 0, 1, this);
				blue = false;
			}
			else if (green)
			{
				team1 = 2;
				limit1 = greenDrones;
				green = false;
			}
			else if (red)
			{
				team1 = 3;
				limit1 = redDrones;
				red = false;
			}
			else if (yellow)
			{
				team1 = 4;
				limit1 = yellowDrones;
				yellow = false;
			}
			for (int i = 0; i < limit1; i++)
			{
				new Drone(Game.rand.nextInt(width/4), Game.rand.nextInt(height), team1, this);
			}
			
			if (blue)
			{
				team2 = 1;
				limit2 = blueDrones;
				player = new Player(width/8, height/2, 0, 1, this);
				blue = false;
			}
			else if (green)
			{
				team2 = 2;
				limit2 = greenDrones;
				green = false;
			}
			else if (red)
			{
				team2 = 3;
				limit2 = redDrones;
				red = false;
			}
			else if (yellow)
			{
				team2 = 4;
				limit2 = yellowDrones;
				yellow = false;
			}
			for (int i = 0; i < limit2; i++)
			{
				new Drone(Game.rand.nextInt(width/4)+3*width/4, Game.rand.nextInt(height), team2, this);
			}
			
		}
        results.addScore(player, "Player-1");
		results.store();
	}

	/**
	 * Update the level and all entities in the entities array for this level
	 */
	public void update()
    {
        //game logic
        if (player != null) {
            //control player
            updateMovement();

            //control screen position
            updateCamera();
        }

		//update all entities
        synchronized (entityList) {
            for (int i = 0; i < entityList.size(); i++) {
                Entity e = entityList.get(i);
                e.update();
            }
        }
	}

	/**
	 * Alternate update method for online play. Only calls sends handles information about controls
	 * and camera. The rest is left up to the server.
	 */
    public void updateOnline() {
        //game logic
        if (player != null) {
            //control player
			/* moved to server controller proxy for separation of concerns */

            //control screen position
            updateCamera();
        }
    }

	/**
	 * Read the information about your keys pressed and control the player as needed
	 */
    private void updateMovement() {
        if (Game.leftkey)
			player.rotate(2);
        if (Game.rightkey)
            player.rotate(-2);
        if (Game.upkey)
            player.accelerate(.5);
        if (Game.downkey)
            player.accelerate(-0.5);
        if (Game.spacekey)
            player.decelerate(.03);
        if (Game.dkey)
            player.fireWeapon();
    }

	/**
	 * Updates the camera location, either following player or jump to location clicked on minimap
	 */
    private void updateCamera() {
		//if the mouse is outside the minimap, set the camera to locked (follow player)
        if (!(Game.mouseX < minimapWidth && Game.mouseX > 0 && Game.mouseY > game.getHeight()-minimapHeight && Game.mouseY < getHeight()))
            lockedCamera = true;
		//if the camera is locked, move the camera to the player's location
        if (lockedCamera) {
            xView = (int) player.getX() - xViewSize / 2;
            yView = (int) player.getY() - yViewSize / 2;
        }
		//otherwise move the camera to the location on clicked on the minimap
        else {
            xView = (int) ((Game.mouseX / minimapWidth) * getWidth()) - xViewSize/2;
            yView = (int) (((minimapHeight-(game.getHeight()-Game.mouseY)) / minimapHeight) * getHeight()) - yViewSize/2;
        }
		//keep the camera in the bounds of the viewport
        if (xView < 0)
        {
            xView = 0;
        }
        if (yView < 0)
        {
            yView = 0;
        }
        if (xView > getWidth()-xViewSize)
        {
            xView = getWidth()-xViewSize;
        }
        if (yView > getHeight()-yViewSize)
        {
            yView = getHeight()-yViewSize;
        }
    }

	/**
	 * Draw the entities and environment.
	 */
	public void draw()
	{
		//draw the environment first so it will be deeper than entities.
		for (int i = 0; i < Star.starList.size(); i++)
		{
			Star s = Star.starList.get(i);
			s.draw(xView, yView);
		}
		//draw entities
        synchronized (entityList) {
            for (int i = 0; i < entityList.size(); i++) {
                Entity e = entityList.get(i);
                e.draw(xView, yView);

                //if online, draw usernames
                if (e instanceof Player && game.getOnlineGame() != null) {
                    Player p = (Player) e;
                    String username = game.getOnlineGame().getUserData().get(p.getPlayerID()).getUsername();
                    TextEngine.drawText(username, p.getX()-(3*username.length()), p.getY()+14, 6*username.length(), 8, xView, yView, true);
                }
            }
        }
	}

	/**
	 * Initialize this level to have the data specified in level data
	 *
	 * @param data
	 */
    public void fromLevelData(LevelData data) {
        width = data.getWidth();
        height = data.getHeight();
        synchronized (entityList) {
            entityList = Collections.synchronizedList(new ArrayList<Entity>());
        }

        //determine view screen size
        if (width > game.getWidth()) xViewSize = game.getWidth();
        else xViewSize = width;
        if (height > game.getHeight()) yViewSize = game.getHeight();
        else yViewSize = height;

        //per class initiations
        for (int i = 0; i < data.getEntities().size(); i++) {
            EntityData e = data.getEntities().get(i);
            if (e.getStoredClass() == Player.class) {
                Player p = new Player(0, 0, 0, 0, this);
                p.fromData(e);
                if (p.getPlayerID() == data.getPlayerID()) this.player = p;
            }
            if (e.getStoredClass() == Drone.class) {
                Drone d = new Drone(0, 0, 0, this);
                d.fromData(e);
            }
            if (e.getStoredClass() == Bullet.class) {
                Bullet b = new Bullet(0, 0, 0, 0, 0, null, this);
                b.fromData(e);
            }
            if (e.getStoredClass() == Headquarters.class) {
                Headquarters hq = new Headquarters(0, 0, 0, this);
                hq.fromData(e);
            }
            if (e.getStoredClass() == Factory.class) {
                Factory f = new Factory(0, 0, 0, this);
                f.fromData(e);
            }
        }

        /* cover all entities method (broken)
        for (EntityData e : data.getEntities()) {
            try {
                Entity ne = (Entity) e.getStoredClass().newInstance();
                ne.fromData(e);
                ne.setLevel(this);
                addEntity(ne);
                if (e.getStoredClass() == Player.class)
                    player = (Player) ne;
            } catch (InstantiationException e1) {
                System.err.println("Error: "+e1.getMessage());
            } catch (IllegalAccessException e1) {
                System.err.println("Error: " + e1.getMessage());
            }
        }*/
    }

	public synchronized List<Entity> getEntityList()
	{
		return entityList;
	}
	
	public void addEntity(Entity e)
	{
		addList.add(e);
	}
	
	public void removeEntity(Entity e)
	{
		removeList.add(e);
	}

	public void updateEntityList() {
		if (!addList.isEmpty()) {
            synchronized (entityList) {
                for (Entity e : addList) {
                    entityList.add(e);
                }
            }
		}
		addList = new ArrayList<Entity>();
		if (!removeList.isEmpty()) {
            synchronized (entityList) {
                for (Entity e : removeList) {
                    entityList.remove(e);
                }
            }
		}
		removeList = new ArrayList<Entity>();
	}
	
	public void mouseEvent(int button)
	{
		
	}

    public void setLockedCamera(boolean lockedCamera) {
        this.lockedCamera = lockedCamera;
    }

    public boolean getLockedCamera() {
        return lockedCamera;
    }

    public void setMinimapDimensions(double w, double h) {
        minimapHeight = h;
        minimapWidth = w;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Map getOptions() {
        return options;
    }
}
