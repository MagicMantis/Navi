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
	}

    /**
     * Shows the main menu of the game
     */
	private void mainMenu() {
	    //title
        guiElements.add(new Label(game.getWidth()/2, game.getHeight()-100, 400, 100, "Project Navi", Color.white));
        //new game button
        guiElements.add(new Button(game.getWidth()/2, game.getHeight()/2+120,
                120, 60, "New Game", () -> {
            int size = Game.rand.nextInt(2)*1000;
            game.newLevel(2000+size, 2000+size);
            game.paused = false;
            return null;
        }));
        //continue button
        guiElements.add(new Button(game.getWidth()/2, game.getHeight()/2+40,
                120, 60, "Continue", () -> {
            if (game.level != null)
                game.paused = false;
            return null;
        }));
        //multiplayer button
        guiElements.add(new Button(game.getWidth()/2, game.getHeight()/2-40,
                120, 60, "Multiplayer", () -> {
            try {
                game.setServerProxy(new ServerControllerProxy(game));
                game.getServerProxy().matchMake();
                game.setOnlineGame(game.getServerProxy().getGameInfo());
                game.getServerProxy().unlock();
            } catch (ConnectException e) {
                System.err.println("Could not connect to server.");
                return null;
            } catch (IOException | GameNotFoundException e) {
                e.printStackTrace();
            }
            game.showMenu(1);
            return null;
        }));
        //quit button
        guiElements.add(new Button(game.getWidth()/2, game.getHeight()/2-120,
                120, 60, "Quit", () -> {
            System.exit(0);
            return null;
        }));
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
        //cancel button
        guiElements.add(new Button(game.getWidth()-300, 100,
                120, 60, "Cancel", () -> {
            serverActionQueue.add(() -> {
                try {
                    game.getServerProxy().disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                game.showMenu(0);
                return null;
            });
            return null;
        }));
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
            case 1: s = "Blue";
            case 2: s = "Green";
            case 3: s = "Red";
            case 4: s = "Yellow";
            case 5: s = "Purple";
            case 6: s = "Orange";
            case 7: s = "Lime";
            case 8: s = "Pink";
            default: s = "Blue";
        }
        guiElements.add(new Label(game.getWidth()/2, game.getHeight()-30, 200, 100, s+" Team Wins", c));

        //score report
        labelRow("Unit\tKills\tDeaths\tDamage\tAccuracy\tScore",70,game.getHeight()-80,game.getWidth(), 20,Color.white);
        ArrayList<String> report = game.results.getScoreReport();
        System.out.println("Score 1");
        System.out.println(report);
        System.out.println(report.get(0));
        for (int i = 0; i < Math.min(report.size(), 20); i++) {
            s = report.get(i);
            colorVals = Target.getColor(Integer.valueOf(s.split("\t")[0]));
            c = new Color((float)colorVals[0],(float)colorVals[1],(float)colorVals[2]);
            labelRow(s.substring(s.indexOf("\t")+1),70,game.getHeight()-100-(i*20),game.getWidth(),20,c);
        }

        //history
        guiElements.add(new History(10,10, game.getWidth()-200, 200, game.results.getHistory()));

        //continue
        guiElements.add(new Button(game.getWidth()-150, 100,
                120, 60, "Continue", () -> {
            game.showMenu(0);
            return null;
        }));
    }

    private void labelRow(String s, int x, int y, int width, int height, Color c) {
        String items[] = s.split("\t");
        int w1 = width/items.length;
        int w2 = width/items.length-20;
        for (int i = 0; i < items.length; i++) {
            guiElements.add(new Label(x+(w1*i)+10,y,w2,height, items[i], c));
        }
    }

    /**
     * Draw the current menu
     */
	public void draw()
	{
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

	public void mouseEvent(int button) {
        if (Game.mousePressed) {
            for (GUIElement e : guiElements) {
                e.mouseEvent(button);
            }
        }
    }

}
