package net.magicmantis.src.model;

/**
 * Score class: holds information about the performance of an entity during a game.
 */

public class Score {

    private int team;
    private int kills, deaths, damage, hits, shots;

    public Score(Target t) {
        team = t.getTeam();
        kills = 0;
        deaths = 0;
        damage = 0;
        hits = 0;
        shots = 0;
    }

    public int calculateScore() {
        double initScore = ((kills - deaths) * 100 + damage);
        double score = initScore + (initScore * 0.2 * (double) hits / (double) shots);
        return (int) score;
    }

    public void addKill() {
        kills++;
    }

    public void addDeath() {
        deaths++;
    }

    public void addDamage(int amount) {
        damage += amount;
    }

    public void addHit() {
        hits++;
    }

    public void addShot() {
        shots++;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getDamage() {
        return damage;
    }

    public int getHits() {
        return hits;
    }

    public int getShots() {
        return shots;
    }

    public int getAccuracy() {
        return (int) (((double) hits / (double) shots) * 100);
    }

    public int getTeam() {
        return team;
    }

}
