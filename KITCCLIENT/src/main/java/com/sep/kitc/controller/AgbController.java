package com.sep.kitc.controller;

import com.sep.kitc.SceneManager;
import javafx.event.ActionEvent;

public class AgbController {
    public void backToRegisterButtonOnAction(ActionEvent event){
        SceneManager.changeScene("register.fxml", 400, 600, event);
    }
}
