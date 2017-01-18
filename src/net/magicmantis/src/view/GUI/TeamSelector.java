package net.magicmantis.src.view.GUI;

import net.magicmantis.src.exceptions.AccessDeniedException;
import net.magicmantis.src.model.Ship;
import net.magicmantis.src.server.dataStructures.UserData;
import net.magicmantis.src.services.TextEngine;
import net.magicmantis.src.view.Game;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * TeamSelector class: this GUI Element is a colored square that indicates a players team in the multiplayer game
 * lobby during setup. If allowed, the player can click their team selector to change their team from amoung the 8
 * available teams.
 */
public class TeamSelector extends GUIElement {

    private int order;
    private Game game;
    private UserData userData;
    private List<Callable<Void>> actionQueue;

    public TeamSelector(int x, int y, int width, int height, int order, Game game, List<Callable<Void>> actionQueue) {
        super(x, y, width, height);
        this.actionQueue = actionQueue;
        this.order = order;
        this.game = game;
        this.userData = null;
    }

    @Override
    public void update() {
        boolean found = false;
        if (game.getOnlineGame() != null && game.getOnlineGame().getUserData() != null) {
            for (UserData ud : game.getOnlineGame().getUserData().values()) {
                if (ud.getPlayerNumber() == order) {
                    userData = ud;
                    found = true;
                }
            }
        }
        if (!found) userData = null;
    }

    @Override
    public void mouseEvent(int button) {
        if (userData != null && checkCollision()) {
            if (button == 0) {
                actionQueue.add(() -> {
                    try {
                        game.getServerProxy().changeTeam(userData.getPlayerID(), true);
                    } catch (AccessDeniedException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            }
            if (button == 1) {
                actionQueue.add(() -> {
                    try {
                        game.getServerProxy().changeTeam(userData.getPlayerID(), false);
                    } catch (AccessDeniedException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            }
        }
    }

    @Override
    public void draw() {
        if (userData != null) {
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glColor3d(Ship.teamColors[userData.getTeam() - 1][0], Ship.teamColors[userData.getTeam() - 1][1],
                    Ship.teamColors[userData.getTeam() - 1][2]);
            GL11.glVertex2f(x - width / 2, y - height / 2);
            GL11.glVertex2f(x - width / 2, y + height / 2);
            GL11.glVertex2f(x + width / 2, y + height / 2);
            GL11.glVertex2f(x + width / 2, y - height / 2);
            GL11.glEnd();

            String username = userData.getUsername();
            TextEngine.drawText(username, x + width, y - 5, 10 * (username.length()), 20, false);
        }
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x - width / 2, y - height / 2);
        GL11.glVertex2f(x - width / 2, y + height / 2);
        GL11.glVertex2f(x + width / 2, y + height / 2);
        GL11.glVertex2f(x + width / 2, y - height / 2);
        GL11.glEnd();
    }
}
