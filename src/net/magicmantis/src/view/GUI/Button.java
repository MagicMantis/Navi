package net.magicmantis.src.view.GUI;

import net.magicmantis.src.view.Game;
import net.magicmantis.src.services.TextEngine;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.Callable;

/**
 * Button class: this class accepts a Callable 'Action' and performs that action when clicked in game.
 */

public class Button extends GUIElement {

    private Callable<Void> action;
    private boolean active;
    private String text;
    private int textWidth;

    public Button(int x, int y, int width, int height, String text, Callable<Void> action) {
        super(x, y, width, height);
        this.text = text;
        this.action = action;
        this.active = false;
    }

    public void update() {
        active = checkCollision();
    }

    public void mouseEvent(int button) {
        if (button == 0 && checkCollision()) try {
            action.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw() {
        if (active) GL11.glColor3f(0f, 1f, 1f);
        else GL11.glColor3f(0f, 0f, 1f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - width / 2, y - height / 2);
        GL11.glVertex2f(x - width / 2, y + height / 2);
        GL11.glVertex2f(x+width/2, y+height/2);
        GL11.glVertex2f(x+width/2, y-height/2);
        GL11.glEnd();

        TextEngine.drawText(text, x-3*width/8, y-height/4, 3*width/4, height/2, false);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setText(String text) { this.text = text; }

}
