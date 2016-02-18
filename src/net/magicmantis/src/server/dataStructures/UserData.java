package net.magicmantis.src.server.dataStructures;

/**
 * UserData class: This class's purpose is to transfer data about players across the network.
 * This includes information such as username, kills, deaths, etc. It will be identifiable by
 * the player id of the ship.
 */
public class UserData {

    private int playerID;
    private String username; //in-game identifier
    private int playerNumber; //order in the lobby
    private int kills, deaths; //kills scored and times this user died
    private int team; //the team that this user's player belongs to

    public UserData(int playerID, String username, int playerNumber, int team) {
        this.playerID = playerID;
        this.username = username;
        this.playerNumber = playerNumber;
        this.team = team;
        kills = deaths = 0;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}
