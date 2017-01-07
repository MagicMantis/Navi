package net.magicmantis.src.view.GUI;

import net.magicmantis.src.services.TextEngine;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Label class: GUIElement that simply displays text
 */
public class Label extends GUIElement {

    private String text;
    private Color color;

    public Label(int x, int y, int width, int height, String text, Color color) {
        super(x, y, width, height);
        this.text = text;
        this.color = color;
    }

    @Override
    public void update() { }

    @Override
    public void mouseEvent(int button) { }

    @Override
    public void draw() {
        TextEngine.drawText(text, x-3*width/8, y-height/4, 3*width/4, height/2, 0,0,false, color, 1);
    }
}
