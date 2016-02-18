package net.magicmantis.src.view;

import net.magicmantis.src.exceptions.AccessDeniedException;
import net.magicmantis.src.exceptions.FailedStartGameException;
import net.magicmantis.src.exceptions.GameNotFoundException;
import net.magicmantis.src.exceptions.UnknownOptionException;
import net.magicmantis.src.services.ServerControllerProxy;
import net.magicmantis.src.view.GUI.Button;
import net.magicmantis.src.view.GUI.GUIElement;
import net.magicmantis.src.view.GUI.Switch;
import net.magicmantis.src.view.GUI.TeamSelector;

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

        if (menu == 0) {
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
            //help button
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
        //multiplayer menu
        else if (menu == 1) {
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
