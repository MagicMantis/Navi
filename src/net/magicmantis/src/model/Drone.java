package net.magicmantis.src.model;

import net.magicmantis.src.server.dataStructures.EntityData;

/**
 * Drone class: simplest artificial intelligence ship. Incapable of being controlled, instances of this class
 * will simply identify the nearest enemy ship, move towards it, and fire when in range.
 */
public class Drone extends Ship {

    private Target target; //reference to the nearest enemy ship
    private double targetDistance; //distance to the target

    /**
     * Create a new drone.
     *
     * @param x     - initial starting x location for this drone.
     * @param y     - initial starting y location for this drone.
     * @param team  - the team that this drone belongs to.
     * @param level - reference to the level in which this drone exists
     */
    public Drone(double x, double y, int team, Level level) {
        super(x, y, 16, 16, team, 100, 0.75, 1, level);

        //set the random offset to the ships attacks
        setAccuracy(16);

        //update the droneCount
    }

    /**
     * Update this drone. Attempt to find a target and move into range.
     * If target is in range, aim at the target and fire.
     */
    @Override
    public void update() {
        super.update();
        getTarget();
        if (target == null)
            return;
        moveTowards(target.getX(), target.getY());
        aim();
        if (targetDistance <= 300)
            fireWeapon();
    }

    /**
     * Find the nearest enemy ship and set target.
     */
    public void getTarget() {
        double shortestDistance = -1;
        int targetIndex = 0;
        //loop through all ships
        synchronized (level.getEntityList()) {
            for (int i = 0; i < level.getEntityList().size(); i++) {
                //if the entity is a ship, determine if it is a valid target
                if (level.getEntityList().get(i).isTarget()) {
                    Target e = (Target) level.getEntityList().get(i);
                    //skip if its on the same team
                    if (e.getTeam() == getTeam() || e == this)
                        continue;
                    double xDis = e.getX() - getX();
                    double yDis = e.getY() - getY();
                    double distance = (Math.sqrt(Math.pow(xDis, 2) + Math.pow(yDis, 2)));
                    //if there is no currently selected target, save reference to this ship
                    if (shortestDistance == -1) {
                        shortestDistance = distance;
                        targetIndex = i;
                    }
                    //otherwise, if this is a better target, save reference to this ship
                    else if (shortestDistance > distance) {
                        shortestDistance = distance;
                        targetIndex = i;
                    }
                }
            }
            //set target to saved reference of most suitable target
            if (shortestDistance != -1) {
                target = (Target) level.getEntityList().get(targetIndex);
                targetDistance = shortestDistance;
            }
            //otherwise no target
            else {
                target = null;
            }
        }
    }

    /**
     * Calculate the distance to this drones target.
     *
     * @return double representing the distance between the drone and its target.
     */
    public double getTargetDistance() {
        double xDis = target.getX() - getX();
        double yDis = target.getY() - getY();
        targetDistance = (Math.sqrt(Math.pow(xDis, 2) + Math.pow(yDis, 2)));
        return targetDistance;
    }

    /**
     * Attempted to move towards the specified location.
     *
     * @param targetX - target x location to move to.
     * @param targetY - target y location to move to.
     */
    public void moveTowards(double targetX, double targetY) {
        aim();
        if (targetDistance > 250) {
            accelerate(.2);
        } else if (targetDistance < 250 && targetDistance > 175) {
            decelerate(.02);
        } else if (targetDistance < 175) {
            accelerate(-.02);
        } else {
            decelerate(.02);
        }
    }

    /**
     * Point this drone towards its target.
     */
    public void aim() {
        double targetX = target.getX();
        double targetY = target.getY();
        double targetDir = Math.atan2(targetY - getY(), targetX - getX());
        setFacingDirection(targetDir * 180 / Math.PI);
    }

    /**
     * Create an instance of drone from the data stored in an EntityData object.
     *
     * @param entityData - instance from which to get data.
     */
    @Override
    public void fromData(EntityData entityData) {
        super.fromData(entityData);
    }

}
