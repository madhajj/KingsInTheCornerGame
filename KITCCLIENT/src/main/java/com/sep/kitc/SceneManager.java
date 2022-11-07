package com.sep.kitc;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneManager {
    private static Logger LOGGER = Logger.getLogger(Application.class.getName());
    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    public static void changeScene(String file, int resolutionHeight, int resolutionWidth, ActionEvent event){
        try {
            URL url = new File("src/main/resources/com/sep/kitc/" + file).toURI().toURL();
            root = FXMLLoader.load(url);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root, resolutionWidth, resolutionHeight);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim wechseln von den Scenes");
            throw new RuntimeException(e);
        }
    }

    public static void changeSceneWithoutEvent(String file, int resolutionHeight, int resolutionWidth, Button button){
        try {
            URL url = new File("src/main/resources/com/sep/kitc/" + file).toURI().toURL();
            root = FXMLLoader.load(url);
            stage = (Stage)button.getScene().getWindow();
            scene = new Scene(root, resolutionWidth, resolutionHeight);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fehler beim wechseln von den Scenes");
            throw new RuntimeException(e);
        }
    }
}
