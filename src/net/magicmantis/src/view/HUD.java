package net.magicmantis.src.view;

import net.magicmantis.src.model.Entity;
import net.magicmantis.src.model.Level;
import net.magicmantis.src.model.Target;
import org.lwjgl.opengl.GL11;

/**
 * HUD class: this class tracks information and displays with the level while the game is being executed.
 * It's draw() method displays this information in an overlay on the screen.
 */
public class HUD {

    public Game game; //reference to the game instance
    public Level level; //reference to the level for information about game objects
    public double screenWidth, screenHeight, levelWidth, levelHeight; //width and height information for rendering logic

    //variables for minimap rendering logic
    public double minimapWidth, minimapHeight;
    public double minimapXScale, minimapYScale;

    /**
     * Create a HUD object and set its references.
     *
     * @param setGame  - reference to game instance.
     * @param setLevel - reference to the active level instance.
     */
    public HUD(Game setGame, Level setLevel) {
        game = setGame;
        level = setLevel;
        screenWidth = game.getWidth();
        screenHeight = game.getHeight();
        update();
    }

    /**
     * Draws a minimap in the top screen displaying information about unit locations relative to the map size.
     */
    public void drawMinimap() throws InterruptedException {
        //draw box
        GL11.glColor3f(0f, 0f, 0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(0, level.yViewSize);
        GL11.glVertex2d(minimapWidth, level.yViewSize);
        GL11.glVertex2d(minimapWidth, level.yViewSize - minimapHeight);
        GL11.glVertex2d(0, level.yViewSize - minimapHeight);
        GL11.glEnd();
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2d(0, level.yViewSize);
        GL11.glVertex2d(minimapWidth, level.yViewSize);
        GL11.glVertex2d(minimapWidth, level.yViewSize - minimapHeight);
        GL11.glVertex2d(0, level.yViewSize - minimapHeight);
        GL11.glEnd();

        //draw ship dots
        Target t;
        synchronized (level.getEntityList()) {
            for (Entity e : level.getEntityList()) {
                if (!e.isTarget())
                    continue;
                t = (Target) e;
                double[] col = t.getColor();
                GL11.glColor3d(col[0], col[1], col[2]);
                if (e.isShip()) {
                    GL11.glBegin(GL11.GL_QUADS);
                    GL11.glVertex2d(Math.floor(e.getX() / 16) * minimapXScale, level.yViewSize - minimapHeight + Math.floor(e.getY() / 16) * minimapYScale);
                    GL11.glVertex2d(Math.floor(e.getX() / 16) * minimapXScale + 1, level.yViewSize - minimapHeight + Math.floor(e.getY() / 16) * minimapYScale);
                    GL11.glVertex2d(Math.floor(e.getX() / 16) * minimapXScale + 1, level.yViewSize - minimapHeight + Math.floor(e.getY() / 16) * minimapYScale + 1);
                    GL11.glVertex2d(Math.floor(e.getX() / 16) * minimapXScale, level.yViewSize - minimapHeight + Math.floor(e.getY() / 16) * minimapYScale + 1);
                    GL11.glEnd();
                } else {
                    GL11.glBegin(GL11.GL_LINE_LOOP);
                    GL11.glVertex2d(Math.floor(e.getX() / 16) * minimapXScale, level.yViewSize - minimapHeight + Math.floor(e.getY() / 16) * minimapYScale);
                    GL11.glVertex2d(Math.floor(e.getX() / 16) * minimapXScale + 2, level.yViewSize - minimapHeight + Math.floor(e.getY() / 16) * minimapYScale);
                    GL11.glVertex2d(Math.floor(e.getX() / 16) * minimapXScale + 2, level.yViewSize - minimapHeight + Math.floor(e.getY() / 16) * minimapYScale + 2);
                    GL11.glVertex2d(Math.floor(e.getX() / 16) * minimapXScale, level.yViewSize - minimapHeight + Math.floor(e.getY() / 16) * minimapYScale + 2);
                    GL11.glEnd();
                }
            }
        }
    }

    /**
     * Draw information about the player: health
     */
    public void drawStatus() {
        //draw health bar
        if (level.player != null) {
            //query life values from the player
            int life = level.player.getLife();
            int maxLife = level.player.getMaxLife();

            //calculate color based on percent life
            double red = 1.0 - (double) life / maxLife;
            double green = (double) life / maxLife;
            double width = (double) life / maxLife * 100;

            //draw the health bar
            GL11.glColor3d(red, green, 0f);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(game.WIDTH - 150, game.getHeight() - 40);
            GL11.glVertex2d(game.WIDTH - 150 + width, game.getHeight() - 40);
            GL11.glVertex2d(game.WIDTH - 150 + width, game.getHeight() - 40 + 10);
            GL11.glVertex2d(game.WIDTH - 150, game.getHeight() - 40 + 10);
            GL11.glEnd();
            GL11.glColor3d(1, 1, 1);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2d(game.WIDTH - 150, game.getHeight() - 40);
            GL11.glVertex2d(game.WIDTH - 50, game.getHeight() - 40);
            GL11.glVertex2d(game.WIDTH - 50, game.getHeight() - 40 + 10);
            GL11.glVertex2d(game.WIDTH - 150, game.getHeight() - 40 + 10);
            GL11.glEnd();
        }
    }

    /**
     * Handle a mouse event.
     *
     * @param button - which button was pressed
     * @return returns true if HUD registered a mouse event, and false if the event falls outside the bounds of
     * elements
     */
    public boolean mouseEvent(int button) {
        int x = Game.mouseX, y = Game.mouseY;
        //if the left button is pressed
        if (button == 0 && Game.mousePressed) {
            //if the minimap is left clicked, unlock the camera view to follow the mouse instead of the player
            if (x < minimapWidth && x > 0 && y > game.getHeight() - minimapHeight && y < levelHeight) {
                level.setLockedCamera(false);
            }
        }
        //lock the camera whenever the left mouse isn't pressed
        else if (button == 0 && !Game.mousePressed) {
            level.setLockedCamera(true);
        }
        return false;
    }

    /**
     * Called whenever the level is changed.
     */
    public void update() {
        level = game.level;
        levelWidth = level.getWidth();
        levelHeight = level.getHeight();

        minimapXScale = 2000 / levelWidth;
        minimapYScale = 2000 / levelHeight;
        minimapWidth = (int) (levelWidth / 16 * minimapXScale);
        minimapHeight = (int) (levelHeight / 16 * minimapYScale);

        level.setMinimapDimensions(minimapWidth, minimapHeight);
    }

    /**
     * Draw elements of the HUD
     */
    public void draw() throws InterruptedException {
        drawMinimap();
        drawStatus();
    }

}
