package sample.main;

import javafx.animation.AnimationTimer;
import sample.model.Player;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.Apple;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TheGame {
    boolean replay;
    private long start = System.currentTimeMillis();
    private long runningTime = start;
    public byte gMode;
    String path = new File("").getAbsolutePath();
    Player pl;
    Apple ap;
    Text scoreSpace;
    FlowPane back;
    Group rootNode;
    Scene pixelart1Town;
    Stage globalPlay;
    boolean blocker = false;

    public void playerCollectApple() {
        double x = pl.posMem[0] + 25;
        double y = pl.posMem[1] + 25;
        boolean logic = x > ap.posApple[0] - 25 &&
                        x < ap.posApple[0] + 50 &&
                        y > ap.posApple[1] - 25 &&
                        y < ap.posApple[1] + 50;
        if (logic) {
            start = runningTime;
            pl.score += 100;
            scoreSpace.setText("SCORE: " + String.valueOf(pl.score));
            ap.refreshApple("/out/production/SnakeAreYouOK/sprites/Apple.png", replay, pl.xAp , pl.yAp);
            pl.incTale();
        }
    }

    TheGame(Stage Play, byte mode, boolean rep) {
        if (rep) replay = true;
        else replay = false;
        ap = new Apple("/out/production/SnakeAreYouOK/sprites/Apple.png", 0, 0);
        pl = new Player("/out/production/SnakeAreYouOK/sprites/Head.jpg", 25, 0, ap, replay);
        ap.refreshApple("/out/production/SnakeAreYouOK/sprites/Apple.png", replay, pl.xAp, pl.yAp);
        if(replay){
            gMode = (byte) pl.gm;
        }else {
            gMode = mode;

        }
        scoreSpace = new Text("SCORE: " + String.valueOf(pl.score));
        back = new FlowPane();
        rootNode = new Group(back, pl.playerSub, ap.appleSub, scoreSpace);
        pixelart1Town = new Scene(rootNode);
        globalPlay = Play;
        Play.setScene(pixelart1Town);
        Play.setTitle("The Game");
        Play.setMaximized(true);
        Play.setFullScreen(true);
        Play.show();
        scoreSpace.setX(10);
        scoreSpace.setY(40);
        scoreSpace.setFont(Font.font("Verdana", 35));
        pixelart1Town.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                    KeyCode key = event.getCode();
                if(!replay) {
                    if(!blocker)
                    pl.movementH(key);
                }
                    anyKey(key);
            }
        });

        try {
            Image image = new Image(new FileInputStream(path + "/out/production/SnakeAreYouOK/sprites/GameBack.jpg"));
            ImageView backGround = new ImageView(image);
            back.getChildren().addAll(backGround);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (gMode == 1) {
            pl.velocity = 15;
        }

        new AnimationTimer() {
            @Override
            public void handle(long l) {
                playerCollectApple();
                gameOver();
                if (gMode == 1) {
                    if (runningTime - start > 3000) {
                        ap.refreshApple("/out/production/SnakeAreYouOK/sprites/Apple.png", replay, pl.xAp, pl.yAp);
                        start = runningTime;
                    } else runningTime = System.currentTimeMillis();

                }
            }
        }.start();
    }

    int n = 0;
    boolean enterPressed = false;

    public void gameOver() {
        if ((pl.bite() || pl.borders())) {
            blocker = true;
            if (n == 0) {
                enterPressed = false;
                n++;
                Text textMessage = new Text("GAME OVERRRRRRR!!!!");
                textMessage.setX(250);
                textMessage.setY(500);
                textMessage.setFont(Font.font("Verdana", 140));
                Text textMessage2 = new Text("PRESS ENTER TO CONTINUE...");
                textMessage2.setX(400);
                textMessage2.setY(550);
                textMessage2.setFont(Font.font("Verdana", 35));
                pl.velocity = 0;
                rootNode.getChildren().addAll(textMessage, textMessage2);
            }
            if (enterPressed && n == 1) {
                n++;
                globalPlay.close();
                new TheGame(new Stage(), gMode, replay);
            }
        }
    }

    public void anyKey(KeyCode Key) {
        if (Key == KeyCode.ENTER) {
            enterPressed = true;
        } else enterPressed = false;
    }
}

