package net.magicmantis.src.view.GUI;

import net.magicmantis.src.view.Game;

/**
 * GUIElement class: parent class for all interfaces such as buttons, switches, and sliders.
 */
public abstract class GUIElement {

    protected int x, y, width, height;

    public GUIElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected boolean checkCollision() {
        if (Game.mouseX < x + width / 2 && Game.mouseX > x - width / 2 && Game.mouseY < y + height / 2 && Game.mouseY > y - height / 2)
            return true;
        else
            return false;
    }

    public abstract void update();

    public abstract void mouseEvent(int button);

    public abstract void draw();

}
