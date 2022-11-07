package com.sep.kitc.controller;

import com.sep.kitc.Application;
import com.sep.kitc.DataHandler;
import com.sep.kitc.RegistryHandler;
import com.sep.kitc.SceneManager;
import com.sep.kitc.common.LoginService;
import com.sep.kitc.common.exception.PlayerHasAlreadyLoggedInException;
import com.sep.kitc.common.exception.UserNotCreatedException;
import com.sep.kitc.common.exception.PasswordIncorrectException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {

    private static Logger LOGGER = Logger.getLogger(Application.class.getName());
    LoginService loginService = RegistryHandler.getLogInService();
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField userPasswordField;
    @FXML
    private Label warningLabel;


    public void loginButtonOnAction(ActionEvent event){
        String username = userNameTextField.getText();
        String password = userPasswordField.getText();
        if (!username.isEmpty() && !password.isEmpty()){
            try {
                String userID = loginService.login(username, password);
                DataHandler.setUserID(userID);
                DataHandler.setUserName(username);
                SceneManager.changeScene("lobby.fxml", 400, 600, event);
            } catch (RemoteException e ) {
                LOGGER.log(Level.SEVERE, "Verbindung mit dem Server unterbrochen");
                throw new RuntimeException(e);
            } catch (UserNotCreatedException e) {
                LOGGER.log(Level.INFO, "Benutzer ist noch nicht registriert");
                warningLabel.setText("Benutzer ist noch nicht registriert");
            } catch (PasswordIncorrectException e) {
                LOGGER.log(Level.SEVERE, "Passwörter stimmen nicht überein");
                warningLabel.setText("Passwörter stimmen nicht überein");
            } catch (PlayerHasAlreadyLoggedInException e) {
                LOGGER.log(Level.SEVERE, "Dieser Spieler hat sich bereits angemeldet");
                warningLabel.setText("Dieser Spieler hat sich bereits angemeldet");
            }
        } else {
            warningLabel.setText("Bitte Benutzernamen und Passwort eingeben");
        }
    }

    public void exitGameButtonOnAction(ActionEvent event){
        System.exit(0);
    }

    public void registerHyperLinkOnAction(ActionEvent event){
        SceneManager.changeScene("register.fxml", 400, 600, event);
    }
}
