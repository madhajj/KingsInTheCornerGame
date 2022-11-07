package com.sep.kitc.controller;

import com.sep.kitc.SceneManager;
import javafx.event.ActionEvent;

public class RegisterCompletedController {
    public void backToLobbyButtonOnAction(ActionEvent event){
        SceneManager.changeScene("lobby.fxml", 400, 600, event);
    }
}
