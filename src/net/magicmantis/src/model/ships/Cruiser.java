package net.magicmantis.src.model.ships;

import net.magicmantis.src.model.Level;

/**
 * Created by Joseph on 2/18/2016.
 */
public class Cruiser extends Ship {

    public Cruiser(double x, double y, int team, Level level)
    {
        super(x, y, 32, 32, team, 100, 0.75, 1, level);

        //set the random offset to the ships attacks
        setAccuracy(16);

        //update the droneCount
    }
}
