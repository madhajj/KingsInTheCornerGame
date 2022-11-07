package com.sep.kitc.controller;

import com.sep.kitc.Application;
import com.sep.kitc.DataHandler;
import com.sep.kitc.RegistryHandler;
import com.sep.kitc.SceneManager;
import com.sep.kitc.common.LoginService;
import com.sep.kitc.common.exception.PlayerCannotBeDeletedException;
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

public class DeleteAccountController {

    private static Logger LOGGER = Logger.getLogger(Application.class.getName());
    LoginService loginService = RegistryHandler.getLogInService();
    @FXML
    private TextField userNameTextField;
    @FXML
    private PasswordField userPasswordField;
    @FXML
    private Label deleteWarnungLabel;


    /**
     * Diese Methode löscht das Konto eines Spielers
     * @param event
     */
    public void deleteAccountButtonOnAction(ActionEvent event){

        String username = userNameTextField.getText();
        String password = userPasswordField.getText();

        if (!username.isEmpty() && !password.isEmpty()){
            try {
                loginService.unregister(username, password, DataHandler.getUserID());
                SceneManager.changeScene("login.fxml", 400, 600, event);
            } catch (RemoteException e) {
                LOGGER.log(Level.SEVERE, "Verbindung zu dem Server wurde getrennt");
                throw new RuntimeException(e);
            } catch (UserNotCreatedException e) {
                LOGGER.log(Level.INFO, "Benutzer ist nicht registriert");
                deleteWarnungLabel.setText("Benutzer ist nicht registriert");
            } catch (PasswordIncorrectException e) {
                LOGGER.log(Level.SEVERE, "Passwörter stimmen nicht überein");
                deleteWarnungLabel.setText("Passwörter stimmen nicht überein");
            } catch (PlayerCannotBeDeletedException e) {
                LOGGER.log(Level.SEVERE, "Du hast nicht die Rechte dieses Konto zu löschen");
                deleteWarnungLabel.setText("Du hast nicht die Rechte dieses Konto zu löschen");
            }
        } else {
            deleteWarnungLabel.setText("Benutzernamen und Passwort eingeben");
        }
    }

    public void backButtonOnAction(ActionEvent event){
        SceneManager.changeScene("accountManagement.fxml", 400, 600, event);
    }
}
