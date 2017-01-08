package net.magicmantis.src.server;

import net.magicmantis.src.model.Results;
import net.magicmantis.src.model.Target;
import net.magicmantis.src.server.dataStructures.UserData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * OnlineGame: class containing information about players and status of an online game currently
 * being hosted on the server.
 */
public class OnlineGame {

    private int maxPlayers; //maximum players that can join the lobby
    private int playerCount; //current players in the lobby
    private ArrayList<User> players; //references to all the current players
    private HashMap<Integer, UserData> userData; //references to all the current players
    private Map<String, Object> options; //stores options that affect the execution of this level (such as friendly fire)
    private int id; //unique id for this game to distinguish from all other games on the server
    private int hostID; //id of the user who is hosting the lobby (currently no host)

    private OnlineLevel level; //the current level for this online game
    private boolean started; //whether or not the game has been started
    private boolean running; //whether or not the game is currently running (can change between running and stopped);
    private boolean ended;

    private Results results;
    private int ticker;

    /**
     * Create a new Online Game for players to join.
     *
     * @param id - unique identifier for this online game.
     * @param maxPlayers - maximum players that can join this game.
     */
    public OnlineGame(int id, int maxPlayers) {
        this.maxPlayers = maxPlayers;
        this.id = id;
        this.started = false;
        this.running = false;
        this.ended = false;
        this.ticker = 0;

        this.results = new Results();

        players = new ArrayList<User>();
        userData = new HashMap<Integer, UserData>();

        //define options
        options = new HashMap<String, Object>();
        options.put("droneMax", 50);
        options.put("friendlyFire", false);
        options.put("allowTeams", true);
        options.put("spawnFactories", true);
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public HashMap<Integer, UserData> getUserData() { return userData; }

    public boolean isFull() {
        return (playerCount == maxPlayers);
    }

    public void addPlayer(User user) {
        players.add(user);

        //determine starting team
        int team = 1;
        for (int i = 1; i < maxPlayers+1; i++) {
            for (UserData u : userData.values()) {
                if (u.getTeam() == i) {
                    System.out.println("Found team "+u.getTeam()+": "+u.getUsername());
                    team++;
                    break;
                }
            }
            if (team == i) break;
        }

        //add new userdata input
        userData.put(user.getID(), new UserData(user.getID(), user.getUsername(), playerCount, team));
        playerCount++;
    }

    public void removeUser(User user) {
        for (User u : players) {
            if (u.equals(user)) {
                players.remove(u);
                break;
            }
        }
        int playerNumber = userData.get(user.getID()).getPlayerNumber();
        for (UserData u : userData.values()) {
            if (u.getPlayerNumber() > playerNumber) u.setPlayerNumber(u.getPlayerNumber()-1);
        }
        userData.remove(user.getID());
        playerCount--;
        if (players.isEmpty()) endGame();
    }

    public int getID() {
        return id;
    }

    public int getHostID() {
        return hostID;
    }

    public Results getResults() { return results; }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public void setUserData(HashMap<Integer, UserData> userData) {
        this.userData = userData;
    }

    public void changeTeam(int playerID, boolean direction) {
        UserData user = userData.get(playerID);
        if ((boolean) options.get("allowTeams")) {
            if (direction) user.setTeam(user.getTeam()%8+1);
            else user.setTeam((user.getTeam()+6)%8+1);
        }
        else {
            for (boolean badteam = true; badteam; ) {
                if (direction) user.setTeam(user.getTeam() % 8 + 1);
                else user.setTeam((user.getTeam() + 6) % 8 + 1);
                badteam = false;
                for (UserData otherUser : userData.values()) {
                    if (otherUser.equals(user)) continue;
                    if (otherUser.getTeam() == user.getTeam()) badteam = true;
                }
            }
        }
    }

    /**
     * Start the online game and notify all connected players.
     */
    public boolean start() {
        level = new OnlineLevel(this, 2000, 2000, options);
        started = running = true;
        Thread gameLoop = new Thread() {
            public void run() {
                while (isRunning()) {
                    update();
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        gameLoop.start();
        return started;
    }

    public boolean startNoLoop() {
        if (started) return true;
        level = new OnlineLevel(this, 2000, 2000, options);
        return (started = true);
    }

    public void update() {
        if (players.isEmpty()) endGame();
        players.forEach(this::updateMovement);
        level.update();
        level.updateEntityList();

        //periodic checks
        ticker = (ticker + 1) % 1000;
        if (ticker % 60 == 0) {
            level.results.store();
        }
        if (ticker % 20 == 0) {
            checkVictory();
        }
    }

    private void checkVictory() {
        int teamsRemaining = 0;
        for (int i : level.results.getTeamCount()) {
            System.out.print(i+" ");
            if (i > 0) teamsRemaining++;
        }
        System.out.println();
        if (teamsRemaining <= 1) {
            level.results.store();
            results = level.results;
            endGame();
        }
    }

    private void updateMovement(User u) {
        if (u.userInput.get("left"))
            level.getPlayer(u.getID()).rotate(2);
        if (u.userInput.get("right"))
            level.getPlayer(u.getID()).rotate(-2);
        if (u.userInput.get("up"))
            level.getPlayer(u.getID()).accelerate(.5);
        if (u.userInput.get("down"))
            level.getPlayer(u.getID()).accelerate(-0.5);
        if (u.userInput.get("space"))
            level.getPlayer(u.getID()).decelerate(.03);
        if (u.userInput.get("d"))
            level.getPlayer(u.getID()).fireWeapon();
    }

    public void endGame() {
        System.out.println(playerCount);
        System.out.println("Ended Game with no Connected Players.");
        ended = true;
        running = false;
    }

    public boolean isJoinable() {
        return !started;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isEnded() { return ended; }

    public OnlineLevel getLevel() {
        return level;
    }

    public Map getOptions() {
        return options;
    }

    public void setOptions(Map<String,Object> options) {
        this.options = options;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
