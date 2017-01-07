package net.magicmantis.src.model;

import net.magicmantis.src.view.Game;

import java.util.*;
import java.util.Map.Entry;

/**
 * Results class: object to hold information about a game to be displayed when completed.
 */
public class Results {

    private HashMap<Target, String> names;
    private HashMap<String, Score> scores;

    public Results() {
        names = new HashMap<Target, String>();
        scores = new HashMap<String, Score>();
    }

    public String getScoreReport() {
        String report = "";
        for (Entry<String, Score> e : sortByValue(scores).entrySet()) {
            Score s = e.getValue();
            report += e.getKey()+":\tKills: "+s.getKills()+"\tDeaths: "+s.getDeaths()+"\tDamage: "+
                    s.getDamage()+"\tAccuracy: "+s.getAccuracy()+"%\tScore: "+s.calculateScore() +"\n";
        }
        return report;
    }

    public Score getScore(Target e) {
        return scores.get(names.get(e));
    }

    /**
     * This function adds a score entry for the supplied Target
     *
     * @param e - the Target who's score is to be recorded
     */
    public void addScore(Target e) {
        String name;
        do {
             name = generateName();
        } while (scores.containsKey(name));
        names.put(e, name);
        scores.put(name, new Score());
    }

    public void addScore(Target e, String name) {
        System.out.println(scores.get(names.get(e)));
        System.out.println(names.containsKey(e));
    }

    public void addKill(Target e) {
        if (!names.containsKey(e)) addScore(e);
        scores.get(names.get(e)).addKill();
    }

    public void addDeath(Target e) {
        if (!names.containsKey(e)) addScore(e);
        scores.get(names.get(e)).addDeath();
    }

    public void addDamage(Target e, int amount) {
        if (!names.containsKey(e)) addScore(e);
        scores.get(names.get(e)).addDamage(amount);
    }

    public void addHit(Target e) {
        if (!names.containsKey(e)) addScore(e);
        scores.get(names.get(e)).addHit();
    }

    public void addShot(Target e) {
        if (!names.containsKey(e)) addScore(e);
        scores.get(names.get(e)).addShot();
    }

    private String generateName() {
        String name = "drone-";
        for (int i = 0; i < 5; i++) {
            int r = Game.rand.nextInt(36);
            char c = (char) (r + ((r < 10) ? 48 : 55));
            name += c;
        }
        return name;
    }

    public static Map<String, Score> sortByValue( Map<String, Score> map ) {
        List<Entry<String, Score>> list =
                new LinkedList<Entry<String, Score>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<String, Score>>()
        {
            @Override
            public int compare( Map.Entry<String, Score> o1, Map.Entry<String, Score> o2 )
            {
                int score1 = o1.getValue().calculateScore();
                int score2 = o2.getValue().calculateScore();
                return score1 > score2 ? -1 : score1 < score2 ? 1 : 0;
            }
        } );

        Map<String, Score> result = new LinkedHashMap<String, Score>();
        for (Map.Entry<String, Score> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }

}
