package com.sep.kitc.controller;

import com.sep.kitc.Application;
import com.sep.kitc.DataHandler;
import com.sep.kitc.RegistryHandler;
import com.sep.kitc.SceneManager;
import com.sep.kitc.common.LoginService;
import com.sep.kitc.common.exception.PasswordIncorrectException;
import com.sep.kitc.common.exception.PlayerHasAlreadyLoggedInException;
import com.sep.kitc.common.exception.RegisterException;
import com.sep.kitc.common.exception.UserNotCreatedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController {
    private static Logger LOGGER = Logger.getLogger(Application.class.getName());
    LoginService loginService = RegistryHandler.getLogInService();
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField userPasswordField;
    @FXML
    private PasswordField userPasswordFieldRepeat;
    @FXML
    private Label warningLabel;
    @FXML
    private CheckBox agbCheckBox;

    /**
     * Diese Methode registriert einen Spieler und gibt ihm dann seine einzigartige
     * Benutzerkennung (UserID). Diese Benutzerkennung wird im Data handler gespeichert.
     * Um einen Spieler registrieren zu können, müssen die Eingabedaten stimmen.
     * Danach wird die Scene gewechselt.
     */
    public void registerButtonOnAction(ActionEvent event){
        String username = userNameTextField.getText();
        String password = userPasswordField.getText();
        String passwordRepeat = userPasswordFieldRepeat.getText();

        if (agbCheckBox.isSelected()){
            if (!username.isEmpty() && !password.isEmpty() && !passwordRepeat.isEmpty()){
                if (password.equals(passwordRepeat)){
                    try {
                        String userID = loginService.register(username, password);
                        loginService.login(username, password);
                        DataHandler.setUserID(userID);
                        DataHandler.setUserName(username);
                        SceneManager.changeScene("registerCompleted.fxml", 400, 600, event);
                    } catch (RemoteException e) {
                        LOGGER.log(Level.SEVERE, "Verbindung mit dem Server wurde unterbrochen");
                        throw new RuntimeException(e);
                    } catch (RegisterException e) {
                        LOGGER.log(Level.INFO, "Benutzername ist bereits vergeben");
                        warningLabel.setText("Benutzername ist bereits vergeben");
                    } catch (UserNotCreatedException e) {
                        LOGGER.log(Level.INFO, "Dieser Spieler existierst nicht");
                        warningLabel.setText("Dieser Spieler existierst nicht");
                    } catch (PlayerHasAlreadyLoggedInException e) {
                        LOGGER.log(Level.INFO, "Dieser Spieler hat sich bereits eingeloggt");
                    } catch (PasswordIncorrectException e) {
                        LOGGER.log(Level.INFO, "Das Passwort wurde falsch eingegeben");
                    }
                } else {
                    warningLabel.setText("Passwörter müssen übereinstimmen");
                }
            } else {
                warningLabel.setText("Benutzernamen und Passwort eingeben");
            }
        } else {
            warningLabel.setText("AGB muss bestätigt werden");
        }
    }

    /**
     * Diese Methode wechselt die Scene, wenn man den Hyperlink geklickt hat.
     */
    public void agbHyperLinkOnAction(ActionEvent event){
        SceneManager.changeScene("agb.fxml", 400, 600, event);
    }

    /**
     * Diese Methode wechselt die Scene, wenn man den Zurück-Knopf gedrückt hat.
     */
    public void backButtonOnAction(ActionEvent event){
        SceneManager.changeScene("login.fxml", 400, 600, event);
    }
}
