package com.sep.kitc.common;

import com.sep.kitc.common.exception.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface LoginService extends Remote, Serializable {
    /**
     * Behandelt das Login f&uuml;r einen bestehenden Benutzer.
     * Benutzer muss davor dem System bekannt sein, die Registrierung
     * ist nur einmal notwendig.
     *
     * @param username Benutzername
     * @param password Passwort
     * @return die vom Server zugeteilte Benutzerkennung
     * @throws RemoteException falls es bei der Kommunikation zu einem Problem kommt
     * @throws UserNotCreatedException  falls der Benutzer noch nicht registriert ist
     */
    String login(String username, String password) throws RemoteException, UserNotCreatedException, PasswordIncorrectException, PlayerHasAlreadyLoggedInException;

    /**
     * Meldet einen Spieler ab. Zudem wird dieser aus der Liste der angemeldeten Spieler entfernt.
     * Dadurch kann sich der Spieler später wieder anmelden.
     * @throws RemoteException falls die Verbindung unterbrochen wird.
     */
    void logout(String userID) throws RemoteException;

    /**
     * F&uuml;hrt die Registrierung durch und damit auch ein <i>auto-login</i>.
     *
     * @param username Benutzername
     * @param password Passwort
     * @return die vom Server zugeteilte Benutzerkennung
     * @throws RemoteException falls es bei der Kommunikation zu einem Problem kommt
     * @throws RegisterException falls ein Benutzer mit diesem Namen bereits existiert
     */
    String register(String username, String password) throws RemoteException, RegisterException;

    /**
     * Löscht den Benutzer mit dem gegebenen Benutzernamen und Passwort. Somit wird dieser Benutzer
     * auch aus dem System gelöscht. Dadurch kann man sich mit diesen Daten zukünftig nicht mehr anmelden.
     *
     * @param username Benutzernamen
     * @param password Passwort
     * @throws RemoteException falls es bei der Kommunikation zu einem Problem kommt.
     * @throws UserNotCreatedException  falls der Benutzer nicht registriert ist.
     */
    void unregister(String username, String password, String userID) throws RemoteException, UserNotCreatedException, PasswordIncorrectException, PlayerCannotBeDeletedException;

    /**
     * Gibt die Bestenliste als eine HashMap zurück.
     * @throws RemoteException falls die Verbindung unterbrochen wird.
     */
    HashMap<String, Integer> getRankList() throws RemoteException;
}
