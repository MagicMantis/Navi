package net.magicmantis.src.model;

import net.magicmantis.src.model.ships.Drone;
import net.magicmantis.src.model.ships.NeuralDrone;
import net.magicmantis.src.view.Game;

import java.util.Map;

/**
 * TrainingField class: room for training neural network of drone artificial intelligence
 */
public class TrainingField extends Level {

    public TrainingField(Game game, int width, int height) {
        super(game, width, height);
    }

    @Override
    public void generate(){
        generateNeural();
    }

    public void generateNeural() {
        int centerx = getWidth()/2;
        int centery = getHeight()/2;
        int team = 1;
        for (double i = -Math.PI; i < Math.PI; i += Math.PI/5) {
            new NeuralDrone(centerx+Math.sin(i)*500, centery+Math.cos(i)*500, team, this);
            team++;
        }
    }

}
