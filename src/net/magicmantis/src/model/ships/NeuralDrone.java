package net.magicmantis.src.model.ships;

import net.magicmantis.src.model.Level;
import net.magicmantis.src.view.Game;

/**
 * NerualDrone class: this drone implements a neural network to learn advanced behaviors
 */
public class NeuralDrone extends Drone {

    private double inputs[];
    private double outputs[];
    private double weights[][];

    /**
     * Create a new drone.
     *
     * @param x     - initial starting x location for this drone.
     * @param y     - initial starting y location for this drone.
     * @param team  - the team that this drone belongs to.
     * @param level - reference to the level in which this drone exists
     */
    public NeuralDrone(double x, double y, int team, Level level) {
        super(x, y, team, level);

        inputs = new double[10];
        for (int i = 0; i < 10; i++) inputs[i] = 0;
        weights = new double[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++)
                weights[i][j] = Game.rand.nextDouble();
        }
        outputs = new double[10];
        for (int i = 0; i < 10; i++) outputs[i] = 0;
    }

    @Override
    public void update() {
        super.update();
        inputs[0] = getTarget().getX();
        inputs[1] = getTarget().getY();
        inputs[2] = getDirection();
        for (int i = 0; i < 3; i++) {
            outputs[0] += inputs[i]*weights[0][i];
            outputs[1] += inputs[i]*weights[1][i];
            outputs[2] += inputs[i]*weights[2][i];
            outputs[3] += inputs[i]*weights[3][i];
        }

        if (outputs[0] >  weights[4][0]) accelerate(.2);
        if (outputs[1] >  weights[4][1]) decelerate(.02);
        if (outputs[2] >  weights[4][2]) rotate(2);
        if (outputs[3] >  weights[4][3]) rotate(-2);
        System.out.println(outputs[0]+" "+outputs[1]+" "+outputs[2]+" "+outputs[3]);
    }
}
