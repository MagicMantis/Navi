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

    private boolean remainingTeams[] = {false,false,false,false,false,false,false,false};
    private int winner;

    public Results() {
        names = new HashMap<Target, String>();
        scores = new HashMap<String, Score>();
    }

    public ArrayList<String> getScoreReport() {
        ArrayList<String> report = new ArrayList<String>();
        for (Entry<String, Score> e : sortByValue(scores).entrySet()) {
            Score s = e.getValue();
            report.add(s.getTeam()+"\t"+e.getKey()+"\t"+s.getKills()+"\t"+s.getDeaths()+"\t"+s.getDamage()+"\t"+s.getAccuracy()+"%\t"+s.calculateScore());
        }
        return report;
    }

    public int getWinner() {
        return winner;
    }

    public Score getScore(Target e) {
        return scores.get(names.get(e));
    }

    private void defeatTeam(int team) {
        remainingTeams[team] = false;
        int teams = 0;
        int win = -1;
        for (int i = 0; i < remainingTeams.length; i++) {
            if (remainingTeams[i] == true) {
                teams++;
                win = i;
            }
        }
        if (teams == 1) winner = win;
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
        scores.put(name, new Score(e));
    }

    public void addScore(Target e, String name) {
        if (!names.containsKey(e)) {
            names.put(e, name);
            scores.put(name, new Score(e));
        }
    }

    public void addKill(Target e) {
        if (!names.containsKey(e)) addScore(e);
        scores.get(names.get(e)).addKill();
    }

    public void addDeath(Target e) {
        if (Target.getTeamCount()[e.getTeam()-1] == 0) defeatTeam(e.getTeam()-1);
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
        for (int i = 0; i < 3; i++) {
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
