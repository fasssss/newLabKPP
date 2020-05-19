package sample.main;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        MainMenu play = new MainMenu(stage);
        play.eventOnPlay();
        play.eventOnContinue();
        play.eventOnExit();
    }
}