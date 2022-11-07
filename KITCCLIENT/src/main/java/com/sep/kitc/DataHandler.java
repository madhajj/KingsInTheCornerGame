package com.sep.kitc;

/**
 * Diese Klasse speichert die Daten des Benutzers,
 * damit sie später im Spiel verwendet werden können.
 */
public class DataHandler {

    private static String userID;
    private static String userName;
    private static String gameRoomName;
    private static int score;

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        DataHandler.score = score;
    }
    public static String getUserID() {
        return userID;
    }
    public static String getUsername() {
        return userName;
    }
    public static void setUserName(String userName) {
        DataHandler.userName = userName;
    }
    public static void setUserID(String userID) {
        DataHandler.userID = userID;
    }
    public static String getGameRoomName() {
        return gameRoomName;
    }
    public static void setGameRoomName(String gameRoomName) {
        DataHandler.gameRoomName = gameRoomName;
    }
}
