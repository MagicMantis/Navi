package net.magicmantis.src.view.GUI;

import net.magicmantis.src.model.Target;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Class History: shows the number of units for each team over the course of the game.
 */
public class History extends GUIElement {

    private ArrayList<int[]> history;

    public History(int x, int y, int width, int height, ArrayList<int[]> history) {
        super(x,y,width,height);
        this.history = history;
    }

    @Override
    public void update() { }

    @Override
    public void mouseEvent(int button) { }

    @Override
    public void draw() {
        for (int team = 0; team < 8; team++) {
            double[] col = Target.getColor(team+1);
            GL11.glColor3d(col[0], col[1], col[2]);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            double interval = (double)width/(double)history.size();
            int i;
            for (i = 0; i < history.size(); i++) {
                GL11.glVertex2d(x+(i*interval), y+history.get(i)[team]*5);
            }
            while (i > 0) {
                i--;
                GL11.glVertex2d(x+(i*interval), y+history.get(i)[team]*5);
            }
            GL11.glEnd();
        }
    }
}
