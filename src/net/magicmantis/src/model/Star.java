package net.magicmantis.src.model;

import net.magicmantis.src.view.Game;
import net.magicmantis.src.view.Screen;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Star class: particle representing an background star. These are generated at the start of
 * a level.
 */
public class Star {

    //location and size data (type 1 = big 2x2 star)
    private int x, y, type;

    public static ArrayList<Star> starList = new ArrayList<Star>();

    /**
     * Create a new star instance.
     *
     * @param setX - x location
     * @param setY - y location
     */
    public Star(int setX, int setY) {
        x = setX;
        y = setY;
        //10% change to be a big star
        if (Game.rand.nextInt(10) == 0)
            type = 1;
        else
            type = 0;
        //add this entity to the starList
        starList.add(this);
    }

    /**
     * Draw this star.
     *
     * @param xView - offset due to camera location
     * @param yView - offset due to camera location
     */
    public void draw(int xView, int yView) {
        int size = 1+type;
        if (x-xView+size < 0 || x-xView > Screen.WIDTH || y-yView+size < 0 || y-yView > Screen.HEIGHT) return;
        GL11.glColor3f(1f,1f,1f);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex2f(x - xView+size, y - yView);
        GL11.glVertex2f(x - xView, y - yView-size);
        GL11.glVertex2f(x - xView-size, y - yView);
        GL11.glVertex2f(x - xView, y - yView+size);
        GL11.glEnd();
    }

}
