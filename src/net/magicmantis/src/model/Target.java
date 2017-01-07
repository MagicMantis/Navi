package net.magicmantis.src.model;

import net.magicmantis.src.server.dataStructures.EntityData;
import net.magicmantis.src.view.Game;
import org.lwjgl.opengl.GL11;

/**
 * Target class: this instance acts as a parent for all entities that have life and can take damage.
 * It also has a team variable to avoid damage from friendly projectiles.
 */
public abstract class Target extends Entity {

    //color data by team
    public static double[] BLUE_TEAM_COLOR = {0d, 0d, 1d};
    public static double[] GREEN_TEAM_COLOR = {0d, 1d, 0d};
    public static double[] RED_TEAM_COLOR = {1d, 0d, 0d};
    public static double[] YELLOW_TEAM_COLOR = {1d, 1d, 0d};
    public static double[] PURPLE_TEAM_COLOR = {.3d, .1d, .55d};
    public static double[] ORANGE_TEAM_COLOR = {1d, .65d, 0d};
    public static double[] LIME_TEAM_COLOR = {.5d, 1d, 0d};
    public static double[] PINK_TEAM_COLOR = {1d, 0d, 1d};

    public static double[][] teamColors = {BLUE_TEAM_COLOR, GREEN_TEAM_COLOR, RED_TEAM_COLOR, YELLOW_TEAM_COLOR,
            PURPLE_TEAM_COLOR, ORANGE_TEAM_COLOR, LIME_TEAM_COLOR, PINK_TEAM_COLOR};

    private static int teamCount[] = {0,0,0,0,0,0,0,0};

    private int team; //team that this target belongs to (cannot take damage from projectiles of the same team.
    private int life, maxLife; //life and maxLife for this target. target spawns with life = maxLife and is destroyed when life = 0
    private boolean drawHealthBar; //should the health bar be drawn for this target.
    private int drawHealthBarTimer; //how many ticks should the health bar be drawn for.

    public Target(double x, double y, int width, int height, int team, int life, Level level) {
        super(x, y, width, height, level);

        this.team = team;
        addTeamCount(team);
        this.maxLife = life;
        this.life = life;
        this.drawHealthBar = false;
        this.drawHealthBarTimer = 0;
    }

    public void update() {

        //check still alive
        if (life <= 0) {
            life = 0;
            destroy();
        }

        //update health bar timer
        if (drawHealthBarTimer > 0) drawHealthBarTimer--;
        if (drawHealthBarTimer == 0) drawHealthBar = false;
    }

    public int getTeam() {
        return team;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public int getLife() {
        return life;
    }

    public boolean getDrawHealthBar() {
        return drawHealthBar;
    }

    public int getDrawHealthBarTimer() {
        return drawHealthBarTimer;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public static int[] getTeamCount() { return teamCount; }

    public static void resetTeamCount() {
        for (int i = 0; i < teamCount.length; i++) {
            teamCount[i] = 0;
        }
    }

    public static void addTeamCount(int team) {
        if (team == 0) return;
        teamCount[team-1]++;
    }

    /**
     * Inflict specified damage on this target.
     *
     * @param damage - amount of life to subtract from this target.
     */
    public boolean damage(int damage)
    {
        life -= damage;
        drawHealthBar = true;
        drawHealthBarTimer = 60;
        if (life <= 0) return true;
        return false;
    }

    @Override
    public void destroy() {
        teamCount[team-1] -= 1;
        level.results.addDeath(this);
        super.destroy();
    }

    /**
     * Draw the health bar above a target when it takes damage.
     */
    public void drawHealthBar(int xView, int yView) {
        //calculate color based on percent life
        double red = 1.0 - (double) life / maxLife;
        double green = (double) life / maxLife;
        double width = (double) life / maxLife * (getWidth()*4/3);

        //draw the health bar
        GL11.glColor3d(red, green, 0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(getX()-getWidth()*2/3-xView, getY()+getHeight()*2/3-yView);
        GL11.glVertex2d(getX()-getWidth()*2/3+width-xView, getY()+getHeight()*2/3-yView);
        GL11.glVertex2d(getX()-getWidth()*2/3+width-xView, getY()+getHeight()*2/3+2-yView);
        GL11.glVertex2d(getX()-getWidth()*2/3-xView, getY()+getHeight()*2/3+2-yView);
        GL11.glEnd();
    }

    /**
     * Draw the target
     *
     * @param xView - The x offset due to camera position.
     * @param yView - The y offset due to camera position.
     */
    public void draw(int xView, int yView) {
        if (drawHealthBar) drawHealthBar(xView, yView);
    }

    /**
     * Get the color of this target for drawing.
     *
     * @return Array of doubles representing the red, green, and blue values to make a color.
     */
    public double[] getColor() {
        switch (getTeam()) {
            case 1: return BLUE_TEAM_COLOR;
            case 2: return GREEN_TEAM_COLOR;
            case 3: return RED_TEAM_COLOR;
            case 4: return YELLOW_TEAM_COLOR;
            case 5: return PURPLE_TEAM_COLOR;
            case 6: return ORANGE_TEAM_COLOR;
            case 7: return LIME_TEAM_COLOR;
            case 8: return PINK_TEAM_COLOR;
            default: return BLUE_TEAM_COLOR;
        }
    }

    /**
     * Static version for other classes to use to get team colors
     *
     * @param team - which team's color to return
     * @return Array of doubles representing the red, green, and blue values to make a color.
     */
    public static double[] getColor(int team) {
        switch (team) {
            case 1: return BLUE_TEAM_COLOR;
            case 2: return GREEN_TEAM_COLOR;
            case 3: return RED_TEAM_COLOR;
            case 4: return YELLOW_TEAM_COLOR;
            case 5: return PURPLE_TEAM_COLOR;
            case 6: return ORANGE_TEAM_COLOR;
            case 7: return LIME_TEAM_COLOR;
            case 8: return PINK_TEAM_COLOR;
            default: return BLUE_TEAM_COLOR;
        }
    }

    @Override
    public void fromData(EntityData entityData) {
        super.fromData(entityData);
        team = entityData.getTeam();
        life = entityData.getLife();
        maxLife = entityData.getMaxLife();
        drawHealthBar = entityData.getDrawHealthBar();
        drawHealthBarTimer = entityData.getDrawHealthBarTimer();
    }

}
