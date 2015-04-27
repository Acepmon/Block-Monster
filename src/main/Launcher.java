package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Acep / D.Tsogtbayar
 */
public class Launcher extends Application implements conf.Config {
    
    private ObservableList<MediaPlayer> sounds;
    
    @Override
    public void start(Stage window) {
        this.loadResources();
        Game game = new Game(window, sounds);
        
        window.setResizable(RESIZABLE);
        window.setTitle(TITLE);
        window.setScene(game.getScene());
        window.sizeToScene();
        window.show();
    }
    
    private void loadResources() {
        sounds = FXCollections.observableArrayList();
        Media beep_media = new Media("file:///C:/Users/JAVA%20M2/Documents/NetBeansProjects/Block-Monster/resources/space-beep.mp3");
        Media background_media = new Media("file:///C:/Users/JAVA%20M2/Documents/NetBeansProjects/Block-Monster/resources/dust.mp3");
        MediaPlayer beep = new MediaPlayer(beep_media);
        MediaPlayer atmosphere = new MediaPlayer(background_media);
        beep.setVolume(1);
        beep.setStopTime(Duration.millis(200));
        beep.setStartTime(Duration.millis(50));
        beep.setOnStopped(() -> sounds.get(0).play());
        atmosphere.setVolume(0);
        sounds.add(beep);
        sounds.add(atmosphere);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
