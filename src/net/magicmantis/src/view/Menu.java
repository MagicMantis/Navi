package net.magicmantis.src.view;

import net.magicmantis.src.exceptions.AccessDeniedException;
import net.magicmantis.src.exceptions.FailedStartGameException;
import net.magicmantis.src.exceptions.GameNotFoundException;
import net.magicmantis.src.exceptions.UnknownOptionException;
import net.magicmantis.src.model.Ship;
import net.magicmantis.src.model.Target;
import net.magicmantis.src.server.dataStructures.UserData;
import net.magicmantis.src.services.ServerControllerProxy;
import net.magicmantis.src.services.TextEngine;
import net.magicmantis.src.view.GUI.*;
import net.magicmantis.src.view.GUI.Button;
import net.magicmantis.src.view.GUI.Label;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Menu class: this class is responsible for populating and displaying menus. Upon creation, a menu instance
 * is passed a number corresponding to its type (ie. 0 = Main Menu). It then creates new buttons and places references
 * to them in the buttons array. It propagates all user input to the appropriate button/switch.
 */
public class Menu {
	
	private Game game;
	
	private int menu;
    private List<GUIElement> guiElements;

    //multiplayer lobby GUIElements
    private Switch allowTeamsSwitch;
    private Switch spawnFactories;

    private List<Callable<Void>> serverActionQueue;

	public Menu(int menuScreen, Game setGame)
	{
		game = setGame;
		menu = menuScreen;
        guiElements = new ArrayList<>();
        serverActionQueue = new ArrayList<>();

        //main menu
        if (menu == 0) {
            mainMenu();
        }
        //multiplayer menu
        else if (menu == 1) {
            multiplayerMenu();
        }
        //results screen
        else if (menu == 2) {
            resultsScreen();
        }
        //pause menu
        else if (menu == 3) {
            pauseMenu();
        }
	}

    /**
     * Shows the main menu of the game
     */
	private void mainMenu() {
	    game.reset();
	    //title
        guiElements.add(new Label(game.getWidth()/2, game.getHeight()-100, 400, 100, "Project Navi", Color.white));
        //new game button
        addNewButton(game.getWidth()/2, game.getHeight()/2+40);
        addMultiplayerButton(game.getWidth()/2, game.getHeight()/2-40);
        addQuitButton(game.getWidth()/2,game.getHeight()/2-120);
    }

    /**
     * Menu shown while in a multiplayer lobby before the game starts
     */
    public void multiplayerMenu() {
        //start game button
        guiElements.add(new Button(game.getWidth()-150, 100,
                120, 60, "Start", () -> {
            serverActionQueue.add(() -> {
                try {
                    game.getServerProxy().startGame();
                } catch (FailedStartGameException | IOException e) {
                    e.printStackTrace();
                }
                return null;
            });
            return null;
        }));
        addMainMenuButton(game.getWidth()-300, 100, "Leave Game");

        //allow team switch
        allowTeamsSwitch = new Switch(game.getWidth() - 400, game.getHeight() - 100, 100, 40, "Teams", false,() -> {
            serverActionQueue.add(() -> {
                try {
                    game.getServerProxy().changeOption("allowTeams", true);
                } catch (UnknownOptionException | AccessDeniedException e) {
                    e.printStackTrace();
                }
                return null;
            });
            return null;
        }, () -> {
            serverActionQueue.add(() -> {
                try {
                    game.getServerProxy().changeOption("allowTeams", false);
                } catch (UnknownOptionException | AccessDeniedException e) {
                    e.printStackTrace();
                }
                return null;
            });
            return null;
        });
        guiElements.add(allowTeamsSwitch);

        //spawn factory switch
        spawnFactories = new Switch(game.getWidth() - 400, game.getHeight() - 200, 100, 40, "Factories", false,() -> {
            serverActionQueue.add(() -> {
                try {
                    game.getServerProxy().changeOption("spawnFactories", true);
                } catch (UnknownOptionException | AccessDeniedException e) {
                    e.printStackTrace();
                }
                return null;
            });
            return null;
        }, () -> {
            serverActionQueue.add(() -> {
                try {
                    game.getServerProxy().changeOption("spawnFactories", false);
                } catch (UnknownOptionException | AccessDeniedException e) {
                    e.printStackTrace();
                }
                return null;
            });
            return null;
        });
        guiElements.add(spawnFactories);

        //generate team selectors
        for (int i = 0; i < game.getOnlineGame().getMaxPlayers(); i++) {
            guiElements.add(new TeamSelector(50, game.getHeight() - 100 - (i * 80), 50, 50, i, game, serverActionQueue));
        }
    }

    /**
     * Screen shown after completion of a game
     */
    public void resultsScreen() {
        //victory
        String s;
        double[] colorVals = Target.getColor(game.results.getWinner());
        Color c = new Color((float)colorVals[0],(float)colorVals[1],(float)colorVals[2]);
        switch (game.results.getWinner()) {
            case 1: s = "Blue"; break;
            case 2: s = "Green"; break;
            case 3: s = "Red"; break;
            case 4: s = "Yellow"; break;
            case 5: s = "Purple"; break;
            case 6: s = "Orange"; break;
            case 7: s = "Lime"; break;
            case 8: s = "Pink"; break;
            default: s = "Blue";
        }
        guiElements.add(new Label(game.getWidth()/2, game.getHeight()-30, 200, 100, s+" Team Wins", c));

        //score report
        labelRow("Unit\tKills\tDeaths\tDamage\tAccuracy\tScore",70,game.getHeight()-80,game.getWidth(), 20,Color.white);
        ArrayList<String> report = game.results.getScoreReport();
        for (int i = 0; i < Math.min(report.size(), 20); i++) {
            s = report.get(i);
            colorVals = Target.getColor(Integer.valueOf(s.split("\t")[0]));
            c = new Color((float)colorVals[0],(float)colorVals[1],(float)colorVals[2]);
            labelRow(s.substring(s.indexOf("\t")+1),70,game.getHeight()-100-(i*20),game.getWidth(),20,c);
        }

        //history
        guiElements.add(new History(10,10, game.getWidth()-200, 200, game.results.getHistory()));

        //continue
        addMainMenuButton(game.getWidth()-150, 100, "Continue");
        guiElements.add(new Button(game.getWidth()-150, 100,
                120, 60, "Continue", () -> {
            game.showMenu(0);
            return null;
        }));
    }

    /**
     * Creates a row of labels from a string delimited by tabs
     *
     * @param s - string delimted by tabs containing content for labels
     * @param x - bottom left corner x
     * @param y - bottom left corner y
     * @param width - width of space occupied by all labels
     * @param height - height of space occupied by all labels
     * @param c - color to make the labels
     */
    private void labelRow(String s, int x, int y, int width, int height, Color c) {
        String items[] = s.split("\t");
        int w1 = width/items.length;
        int w2 = width/items.length-20;
        for (int i = 0; i < items.length; i++) {
            guiElements.add(new Label(x+(w1*i)+10,y,w2,height, items[i], c));
        }
    }

    private void pauseMenu() {
        //title
        guiElements.add(new Label(game.getWidth()/2, game.getHeight()-100, 400, 100, "Project Navi", Color.white));
        addContinueButton(game.getWidth()/2, game.getHeight()/2+40);
        addMultiplayerButton(game.getWidth()/2,game.getHeight()/2-40);
        addMainMenuButton(game.getWidth()/2, game.getHeight()/2-120, "End Game");
    }

    /**
     * Draw the current menu
     */
	public void draw()
	{
	    if (menu == 3) {
            game.level.draw();
        }
        //draw buttons (created upon showing the menu
        for (GUIElement e : guiElements) {
            e.draw();
        }
	}
	
	public void update()
	{
        for (GUIElement e : guiElements) {
            e.update();
        }
        if (menu == 1 && game.getOnlineGame() != null && !game.getServerProxy().isLocked()) {
            try {
                game.setOnlineGame(game.getServerProxy().getGameInfo());
                if (game.getOnlineGame().isStarted()) {
                    game.getServerProxy().getLevel();
                    game.paused = false;
                }
                allowTeamsSwitch.setOn((boolean) game.getOnlineGame().getOptions().get("allowTeams"));
                spawnFactories.setOn((boolean) game.getOnlineGame().getOptions().get("spawnFactories"));
                if (!serverActionQueue.isEmpty()) {
                    serverActionQueue.forEach(e -> {
                        try {
                            e.call();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    });
                }
                serverActionQueue.clear();
            } catch (GameNotFoundException | IOException e) {
                System.out.println(e.getMessage());
            }

        }
	}

	private void addNewButton(int x, int y) {
        guiElements.add(new Button(x, y,
                120, 60, "New Game", () -> {
            int size = Game.rand.nextInt(2)*1000;
            game.newLevel(2000+size, 2000+size);
            game.paused = false;
            return null;
        }));
    }

	private void addContinueButton(int x, int y) {
        //continue button
        guiElements.add(new Button(x, y,
                120, 60, "Continue", () -> {
            if (game.level != null)
                game.paused = false;
            return null;
        }));
    }

	private void addMultiplayerButton(int x, int y) {
        //multiplayer button
        guiElements.add(new Button(x, y,
                120, 60, "Multiplayer", () -> {
            game.reset();
            try {
                game.setServerProxy(new ServerControllerProxy(game));
                game.getServerProxy().matchMake();
                game.setOnlineGame(game.getServerProxy().getGameInfo());
                game.getServerProxy().unlock();
            } catch (ConnectException e) {
                System.err.println("Could not connect to server.");
                game.showMenu(0);
                return null;
            } catch (IOException | GameNotFoundException e) {
                e.printStackTrace();
            }
            game.showMenu(1);
            return null;
        }));
    }

    //add button that returns to the main menu
    private void addMainMenuButton(int x, int y, String text) {
        guiElements.add(new Button(x, y,
                120, 60, text, () -> {
            game.showMenu(0);
            return null;
        }));
    }

    private void addQuitButton(int x, int y) {
        //quit button
        guiElements.add(new Button(x, game.getHeight()/2-120,
                120, 60, "Quit", () -> {
            System.exit(0);
            return null;
        }));
    }

	public void mouseEvent(int button) {
        if (Game.mousePressed) {
            for (GUIElement e : guiElements) {
                e.mouseEvent(button);
            }
        }
    }

}
