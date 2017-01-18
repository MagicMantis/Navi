package net.magicmantis.src.model;

import net.magicmantis.src.server.dataStructures.EntityData;

/**
 * Created by Joseph on 12/15/2015.
 */
public abstract class Spawner extends Structure {

    private int spawnCooldown; //timer until next player can be spawned from the
    private int spawnRate; //number of ticks between each spawn
    private boolean active;

    public Spawner(double x, double y, int width, int height, int team, int life, int spawnRate, Level level) {
        super(x, y, width, height, team, life, level);
        this.spawnRate = spawnRate;
        this.spawnCooldown = spawnRate;
        active = false;
    }

    @Override
    public void update() {
        super.update();
        if (active) {
            if (spawnCooldown == 0) {
                spawnEntity();
                spawnCooldown = spawnRate;
            } else spawnCooldown--;
        }
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public int getSpawnCooldown() {
        return spawnCooldown;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public boolean isActive() {
        return active;
    }

    public void fromData(EntityData entityData) {
        super.fromData(entityData);
        spawnCooldown = entityData.getSpawnCooldown();
        spawnRate = entityData.getSpawnRate();
        active = entityData.isActive();
    }

    public abstract void spawnEntity();

}
