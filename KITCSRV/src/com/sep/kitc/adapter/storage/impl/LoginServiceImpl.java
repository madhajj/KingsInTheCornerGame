package com.sep.kitc.adapter.storage.impl;

import com.sep.kitc.adapter.storage.entity.Player;
import com.sep.kitc.adapter.storage.service.RegistrationService;
import com.sep.kitc.common.LoginService;
import com.sep.kitc.common.exception.*;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginServiceImpl implements LoginService {

    private static Logger LOGGER = Logger.getLogger(LoginService.class.getName());
    private RegistrationService registrationService;

    public LoginServiceImpl() {
        registrationService = new RegistrationService();
    }



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
    @Override
    public synchronized String login(String username, String password) throws RemoteException, UserNotCreatedException, PasswordIncorrectException, PlayerHasAlreadyLoggedInException {
        Player player = new Player(username, password);
        if (registrationService.isRegistered(player)) {
            if (registrationService.checkIfPasswordCorrect(player)){
                Optional<String> userIdentifier = registrationService.getUserIdentifier(player);
                if (!registrationService.checkIfLoggedIn(userIdentifier.get())){
                    registrationService.logPlayerIn(userIdentifier.get());
                    return userIdentifier.get();
                } else {
                    LOGGER.log(Level.SEVERE, "Spieler hat sich bereits angemeldet");
                    throw new PlayerHasAlreadyLoggedInException("Spieler hat sich bereits angemeldet");
                }
            } else {
                LOGGER.log(Level.SEVERE, "Passwörter stimmen nicht überein");
                throw new PasswordIncorrectException("Passwörter stimmen nicht überein");
            }
        } else {
            LOGGER.log(Level.SEVERE, "Benutzer ist noch nicht registriert");
            throw new UserNotCreatedException("Benutzer ist noch nicht registriert");
        }
    }

    /**
     * Meldet einen Spieler ab. Zudem wird dieser aus der Liste der angemeldeten Spieler entfernt.
     * Dadurch kann sich der Spieler später wieder anmelden.
     *
     * @throws RemoteException falls die Verbindung unterbrochen wird.
     */
    @Override
    public void logout(String userID) throws RemoteException {
        registrationService.logOut(userID);
    }

    /**
     * F&uuml;hrt die Registrierung durch und damit auch ein <i>auto-login</i>.
     *
     * @param username Benutzername
     * @param password Passwort
     * @return die vom Server zugeteilte Benutzerkennung
     * @throws RemoteException falls es bei der Kommunikation zu einem Problem kommt
     * @throws RegisterException falls ein Benutzer mit diesem Namen bereits existiert
     */
    @Override
    public synchronized String register(String username, String password) throws RemoteException, RegisterException {
        return registrationService.register(new Player(username, password));
    }

    /**
     * Löscht den Benutzer mit dem gegebenen Benutzernamen und Passwort. Somit wird dieser Benutzer
     * auch aus dem System gelöscht. Dadurch kann man sich mit diesen Daten zukünftig nicht mehr anmelden.
     *
     * @param username Benutzernamen
     * @param password Passwort
     * @throws RemoteException falls es bei der Kummunikation zu einem Problem kommt.
     * @throws UserNotCreatedException  falls der Benutzer nicht registriert ist.
     */
    @Override
    public synchronized void unregister(String username, String password, String userID) throws RemoteException, UserNotCreatedException, PasswordIncorrectException, PlayerCannotBeDeletedException {
        registrationService.unregister(new Player(username, password), userID);
    }

    /**
     * Gibt die Bestenliste als eine HashMap zurück.
     *
     * @throws RemoteException falls die Verbindung unterbrochen wird.
     */
    @Override
    public HashMap<String, Integer> getRankList() throws RemoteException {
        return registrationService.getRank();
    }
}
