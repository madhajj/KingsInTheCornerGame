package com.sep.kitc.adapter.storage.service;

import com.sep.kitc.adapter.storage.entity.Bot;
import com.sep.kitc.adapter.storage.entity.GameRoom;
import com.sep.kitc.adapter.storage.entity.Player;
import com.sep.kitc.adapter.storage.entity.RegisteredPlayer;
import com.sep.kitc.common.exception.PlayerCannotBeDeletedException;
import com.sep.kitc.common.exception.UserNotCreatedException;
import com.sep.kitc.common.exception.PasswordIncorrectException;
import com.sep.kitc.common.exception.RegisterException;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationService implements Serializable {
    private static Logger LOGGER = Logger.getLogger(RegistrationService.class.getName());

    /**
     * Diese HashMap speichert als Key den Benutzernamen eines Spielers, und als Value
     * das dazugehörige Spieler-Objekt.
     */
    private static Map<String, RegisteredPlayer> registry;

    private static ArrayList<String> loggedIn;
    private int botCounter = 0;

    public RegistrationService() {
        registry = new HashMap<>();
        loggedIn = new ArrayList<>();
    }
    public int getBotCounter() {
        return botCounter;
    }

    /**
     * überprüft, ob das Passwort des Benutzers korrekt ist
     * @param player Spieler
     * @return "true" falls korrekt, ansonsten "false".
     */
    public boolean checkIfPasswordCorrect(Player player) {
        return registry.get(player.getUsername()).getPassword().equals(player.getPassword());
    }


    /**
     * überprüft, ob ein Spieler sich schon angemeldet hat.
     * @param userID die Benutzerkennung des Spielers.
     * @return "true" falls korrekt, ansonsten "false".
     */
    public boolean checkIfLoggedIn(String userID) {
        return loggedIn.contains(userID);
    }


    /**
     * Fügt dem Spiel einen einfachen Level-Bot hinzu.
     * @return die Benutzerkennung des Bots. Diese wird
     * später der Spielerliste im Spielraum beigefügt.
     */
    public String addEasyBotInRegistry(GameRoom gameRoom){
        Bot botPlayer = new Bot("Easybot" + getBotCounter(), "69420");
        RegisteredPlayer bot = new RegisteredPlayer(botPlayer, "Easybot" + getBotCounter());
        bot.setDifficulty("Easy");
        bot.setGameRoom(gameRoom);
        bot.setBot(true);
        bot.setReady();
        registry.put(bot.getUsername(), bot);
        botCounter++;
        return bot.getUserIdent();
    }

    /**
     * Fügt dem Spiel einen mittleren Level-Bot hinzu.
     * @return die Benutzerkennung des Bots. Diese wird
     * später der Spielerliste im Spielraum beigefügt.
     */
    public String addMediumBotInRegistry(GameRoom gameRoom){
        Bot botPlayer = new Bot("Mediumbot" + getBotCounter(), "69420");
        RegisteredPlayer bot = new RegisteredPlayer(botPlayer, "Mediumbot" + getBotCounter());
        bot.setDifficulty("Medium");
        bot.setGameRoom(gameRoom);
        bot.setBot(true);
        bot.setReady();
        registry.put(bot.getUsername(), bot);
        botCounter++;
        return bot.getUserIdent();
    }

    /**
     * Entfernt einen Bot aus dem Spiel.
     * @param bot Der bot der entfernt werden sollte.
     */
    public void removeBot(RegisteredPlayer bot){
        registry.remove(bot.getUserIdent());
    }

    /**
     * Überprüft, ob ein Spieler registriert ist.
     * @param player Spieler den man überprüfen will.
     * @return "true" falls korrekt, ansonsten "false".
     */
    public boolean isRegistered(Player player) {
        return registry.containsKey(player.getUsername());
    }

    /**
     * Registriert einen Spieler und fügt seine Daten der registry HashMap hinzu.
     * @param player der Spieler der registriert werden soll.
     * @return die einzigartige Benutzerkennung des Spielers.
     * @throws RegisterException falls dieser Spieler bereits registriert ist.
     */
    public String register(Player player) throws RegisterException {
        if (!isRegistered(player)) {
            String userIdentifier = UUID.randomUUID().toString();
            RegisteredPlayer toRegister = new RegisteredPlayer(player, userIdentifier);
            registry.put(player.getUsername(), toRegister);
            return userIdentifier;
        } else {
            //Falls der Benutzer bereits registriert ist, darf keine erneute Registrierung erfolgen.
            LOGGER.log(Level.SEVERE, "Benutzer ist bereits registriert");
            throw new RegisterException("Benutzer ist bereits registriert");
        }
    }

    /**
     * Löscht einen Spieler und entfernt seine Daten aus der registry HashMap.
     * @param player den Spieler den man löschen will.
     * @throws UserNotCreatedException falls es den Spieler nicht geben sollte bzw. dieser
     * in der HashMap nicht vorhanden ist.
     * @throws PasswordIncorrectException falls das gegebene Passwort nicht korrekt ist.
     */
    public void unregister(Player player, String userID) throws UserNotCreatedException, PasswordIncorrectException, PlayerCannotBeDeletedException {
        if (isRegistered(player)){
            if (checkIfPasswordCorrect(player)){
                if (getUserIdentifier(player).get().equals(userID)){
                    registry.remove(player.getUsername());
                } else {
                    LOGGER.log(Level.SEVERE, "Du hast nicht die Rechte diesen Spieler zu löschen");
                    throw new PlayerCannotBeDeletedException("Du hast nicht die Rechte diesen Spieler zu löschen");
                }
            } else {
                LOGGER.log(Level.SEVERE, "Passwörter stimmen nicht überein");
                throw new PasswordIncorrectException("Passwörter stimmen nicht überein");
            }
        } else {
            //Falls der Benutzer noch nicht registriert worden ist, kann man diesen Benutzer auch nicht löschen
            LOGGER.log(Level.SEVERE, "Benutzer ist nicht registriert");
            throw new UserNotCreatedException("Benutzer ist nicht registriert");
        }
    }

    /**
     * Entfernt einen Spieler aus der "LoggedIn" Liste. Somit
     * ist man als Spieler dadurch nicht mehr angemeldet.
     * @param userID die Benutzerkennung des Spielers.
     */
    public void logOut(String userID){
        loggedIn.remove(userID);
    }

    /**
     * Gibt eine eindeutige Benutzerkennung des Spielers zurück.
     * @param player der Spieler, dessen Benutzerkennung wir wissen wollen.
     * @return die eindeutige Benutzerkennung des Spielers.
     */
    public Optional<String> getUserIdentifier(Player player) {
        Optional<RegisteredPlayer> registeredPlayer = Optional.of(registry.get(player.getUsername()));
        if (registeredPlayer.isPresent()) {
            return Optional.of(registeredPlayer.get().getUserIdent());
        }
        return Optional.empty();
    }

    /**
     * Gibt den Benutzernamen des Spielers mithilfe der gegebenen Benutzerkennung zurück.
     * @param userID die Benutzerkennung des Spielers.
     * @return den Benutzernamen des Spielers
     * @throws UserNotCreatedException falls es den Spieler nicht geben sollte bzw. dieser
     * in der HashMap nicht vorhanden ist.
     */
    public String getUserNameOfPlayer(String userID) throws UserNotCreatedException{
        Optional<RegisteredPlayer> first = registry.values().stream().filter(registeredPlayer -> registeredPlayer.getUserIdent().equals(userID)).findFirst();
        if (first.isPresent()) {
            return first.get().getUsername();
        } else {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung gibt es nicht!");
            throw new UserNotCreatedException("Spieler mit dieser Benutzerkennung gibt es nicht!");
        }
    }

    /**
     * Wenn ein Spieler sich anmeldet, dann wird dieser der Liste beigefügt.
     * @param userID die Benutzerkennung des Spielers, der sich angemeldet hat.
     */
    public void logPlayerIn(String userID) {
        loggedIn.add(userID);
    }

    /**
     * Gibt ein Registered Player Objekt zurück, was zu der gegebenen Benutzerkennung passt.
     * @param userID die Benutzerkennung des Spielers.
     * @return das Registered Player Objekt.
     * @throws UserNotCreatedException falls es den Spieler nicht geben sollte bzw. dieser
     * in der HashMap nicht vorhanden ist.
     */
    public static RegisteredPlayer getRegisteredPlayer(String userID) throws UserNotCreatedException{
        Optional<RegisteredPlayer> first = registry.values().stream().filter(registeredPlayer -> registeredPlayer.getUserIdent().equals(userID)).findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            LOGGER.log(Level.SEVERE, "Spieler mit dieser Benutzerkennung gibt es nicht!");
            throw new UserNotCreatedException("Spieler mit dieser Benutzerkennung gibt es nicht!");
        }
    }

    /**
     * Diese Methode aktualisiert die Bestenliste in der Lobby und gibt diese zurück.
     * Hier werden jeweils der Benutzername und der dazugehörige Score
     * in eine Hashmap gespeichert. Diese kann dann vom Client abgerufen werden.
     * @return die RankList Hashmap
     */
    public HashMap<String, Integer> getRank(){
        HashMap<String, Integer> rankList = new HashMap<>();
        registry.forEach((username, player) -> {
            if (!player.isBot()){
                rankList.put(username, player.getTotalScore());
            }
        });
        return sortMap(rankList);
    }

    /**
     * Sortiert eine HashMap.
     * @param hashMap die HashMap die sortiert werden soll.
     * @return die sortierte HashMap.
     */
    public HashMap<String, Integer> sortMap(HashMap<String, Integer> hashMap) {
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        hashMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }
}