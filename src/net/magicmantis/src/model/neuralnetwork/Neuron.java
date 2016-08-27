package net.magicmantis.src.model.neuralnetwork;

import net.magicmantis.src.view.Game;

import java.util.ArrayList;

/**
 * Neuron class: node in a neural network used to control in game AI.
 */
public class Neuron {

    private int inputNumber;
    private ArrayList<Double> weights;

    public Neuron(int inputNumber) {
        this.inputNumber = inputNumber;
        for (int i = 0; i < inputNumber+1; i++) {
            weights.add(Game.rand.nextGaussian()*2-1);
        }
    }

}
