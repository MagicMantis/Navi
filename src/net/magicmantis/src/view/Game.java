package net.magicmantis.src.view;

import net.magicmantis.src.exceptions.GameNotFoundException;
import net.magicmantis.src.model.Level;
import net.magicmantis.src.model.Results;
import net.magicmantis.src.model.Target;
import net.magicmantis.src.server.OnlineGame;
import net.magicmantis.src.services.ServerController;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Game class: this class handles all game logic, including keeping track of input.
 * This class contains the main game loop.
 */
public class Game implements Runnable {

	public static boolean running = false;
	
	public int WIDTH, HEIGHT; //width and height of the screen, used primarily for HUD placement.
    public static int mouseX, mouseY; //variables to store the mouseX and mouseY locations for easy access across all classes
    public static boolean mousePressed; //if the mouse is pressed, can be easily accessed by all classes

    public Screen screen; //reference to the screen instance that acts as a display for this game
	public Level level; //level contains the information about the currently executing level and all entities
	public HUD hud; //hud gives information to the player
	public Menu menu; //reference to a menu instance
    public Results results;

    private ServerController serverProxy; //controller for interacting with a server
    private OnlineGame onlineGame; //reference to the OnlineGame instance the player is currently connected to
    private int gameID; //id of current online game
    private int sessionID; //id of current server session
	
	public static Random rand = new Random(); //random class instance,  accessible from all classes

    //key boolean store information about keys pressed without constantly polling, can be accessed from all classes
	public static boolean leftkey, rightkey, downkey, upkey, spacekey;
	public static boolean dkey, skey;
	
	public boolean paused;
	private int ticker;

    /**
     * Initialize a game.
     *
     * @param screen - screen to display to
     * @param setWidth - width of the specified screen
     * @param setHeight - height of the specified screen
     */
	public Game(Screen screen, int setWidth, int setHeight) {

		WIDTH = setWidth;
		HEIGHT = setHeight;

        this.screen = screen;

        //show startup menu
		showMenu(0);

        //zero all keys
		leftkey = false;
		rightkey = false;
		downkey = false;
		upkey = false;
		spacekey = false;
		dkey = false;
		skey = false;
		ticker = 0;
	}

    //

    /**
     * Pause game and create menu of specified screen
     *
     * @param menuScreen - which menu screen to display, info found in Menu class
     */
	public void showMenu(int menuScreen)
	{
		paused = true;
		menu = new Menu(menuScreen, this);
	}

    //create a new level of size width * height

    /**
     * Create a new level instance and set to currently active level
     *
     * @param width - width of the ingame level (not screen width)
     * @param height - height of the ingame level (not screen height)
     */
	public void newLevel(int width, int height)
	{
        //create hud too if there is not current level
		if (level == null) {
            level = new Level(this, width, height);
            level.generate();
            hud = new HUD(this, level);
        }
		else
		{
			level = new Level(this, width, height);
            level.generate();
		}

        //update the hud to new level
		if (hud != null)
			hud.update();
	}

    /**
     * Start the main game loop
     */
    @Override
	public void run()
	{
        running = true;
		while (running)
		{
			tick();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

    /**
     * Update the active component, either a menu or a level and hud.
     * Primary logic function.
     */
	private void tick() 
	{
	    if (!paused) {
            if (onlineGame != null) {
                try {
                    onlineGame = serverProxy.getGameInfo();
                    if (onlineGame.isEnded()) {
                        results = onlineGame.getResults();
                        level = null;
                        showMenu(2);
                    }
                    serverProxy.getLevel();
                    level.updateOnline();
                    level.updateEntityList();
                    serverProxy.updateInput();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                level.update();
                level.updateEntityList();

                //periodic checks
                ticker = (ticker + 1) % 1000;
                if (ticker % 60 == 0) {
                    level.results.store();
                }
                if (ticker % 20 == 0) {
                    checkVictory();
                }
            }
        }
		else
		{
			menu.update();
		}
	}

    private void checkVictory() {
	    int teamsRemaining = 0;
        for (int i : Target.getTeamCount()) {
            if (i > 0) teamsRemaining++;
        }
        if (teamsRemaining <= 1 || Target.getTeamCount()[level.player.getTeam()-1] == 0) {
            level.results.store();
            results = level.results;
            level = null;
            showMenu(2);
        }
    }

    /**
     * Draw the active component, either a menu or level and hud.
     * Primary interface between the game logic and the screen.
     */
	public void draw() throws InterruptedException {
		//graphics
		if (!paused)
		{
			level.draw();
			hud.draw();
		}
		else
		{
			menu.draw();
		}
	}

    /**
     * Update the static key boolean to pressed for easy access by other classes without
     * interacting directly with the input controller.
     *
     * @param key - key code for the key to be set to pressed. (GLFW_KEY)
     */
	public void keyPressed(int key) {
        if (key == GLFW.GLFW_KEY_LEFT)
            Game.leftkey = true;
        if (key == GLFW.GLFW_KEY_RIGHT)
            Game.rightkey = true;
        if (key == GLFW.GLFW_KEY_DOWN)
            Game.downkey = true;
        if (key == GLFW.GLFW_KEY_UP)
            Game.upkey = true;
        if (key == GLFW.GLFW_KEY_SPACE)
            Game.spacekey = true;
        if (key == GLFW.GLFW_KEY_D)
            Game.dkey = true;
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            if (!paused) showMenu(0);
            else if (level != null) paused = false;
        }
	}

    /**
     * Update the static key boolean to released for easy access by other classes without
     * interacting directly with the input controller.
     *
     * @param key - key code for the key to be released. (GLFW_KEY)
     */
	public void keyReleased(int key) {
        if (key == GLFW.GLFW_KEY_LEFT)
            Game.leftkey = false;
        if (key == GLFW.GLFW_KEY_RIGHT)
            Game.rightkey = false;
        if (key == GLFW.GLFW_KEY_DOWN)
            Game.downkey = false;
        if (key == GLFW.GLFW_KEY_UP)
            Game.upkey = false;
        if (key == GLFW.GLFW_KEY_SPACE)
            Game.spacekey = false;
        if (key == GLFW.GLFW_KEY_D)
            Game.dkey = false;
	}

    /**
     * Pass the mouse event to the currently active
     *
     * @param button - which button was pressed
     * @param action - pressed/released (GLFW_PRESS/GLFW_RELEASED)
     */
    public void mouseEvent(int button, int action) {
        if (action == GLFW_PRESS) mousePressed = true;
        if (action == GLFW_RELEASE) mousePressed = false;
        if (paused) menu.mouseEvent(button);
        else if (!hud.mouseEvent(button)) level.mouseEvent(button);
    }
	
	public void stop() 
	{
		running = false;
	}

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public ServerController getServerProxy() {
        return serverProxy;
    }

    public void setServerProxy(ServerController serverProxy) {
        this.serverProxy = serverProxy;
    }

    public void setOnlineGame(OnlineGame game) {
        this.onlineGame = game;
    }

    public OnlineGame getOnlineGame() {
        return onlineGame;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

}
