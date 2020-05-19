package sample.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Player {
    boolean replay = false;
    public int velocity = 10;
    final int UP = 1;
    final int DOWN = 2;
    final int RIGHT = 3;
    final int LEFT = 4;
    String path = new File("").getAbsolutePath();
    Rectangle playerModel = new Rectangle();
    Rectangle taleModelTop = new Rectangle();
    List<Rectangle> taleModel = new ArrayList<>();
    public Group tlTop = new Group(taleModelTop);
    public Group playerSub = new Group(tlTop, playerModel);
    String pathModel;
    public int[] posMem = {0, 0};
    public int[] tale = new int[10000];
    public int n = 0;
    public double score;
    private double tempScore = 0;
    private int multiTaleInc = 0;
    File repFile = new File(path + "/out/production/SnakeAreYouOK/memory.txt");
    FileWriter fileWriter = null;
    FileReader fileReader = null;
    Scanner reader;
    Apple apple;
    int[] repBuf = new int[4000];
    int nBuff = 1;
    int iBuff = 0;
    Text testVal;
    boolean deathFlag = false;
    public int gm = 0;

    public boolean borders() {
        boolean borLogic = posMem[0] < 0 ||
                posMem[0] > 1900 + 50 ||
                posMem[1] < 0 ||
                posMem[1] > 1000 + 50;
        if (borLogic) {
            score = -100;
            if (!deathFlag&&!replay) {
                try {
                    fileWriter = new FileWriter(repFile, true);
                    fileWriter.write(posMem[0] + "\n");
                    fileWriter.write(posMem[1] + "\n");
                    fileWriter.write((int) apple.posApple[0] + "\n");
                    fileWriter.write((int) apple.posApple[1] + "\n");
                    if(velocity == 15)
                        fileWriter.write(1 + "\n");
                    else
                        fileWriter.write(0 + "\n");
                    fileWriter.close();
                    deathFlag = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    public boolean bite() {
        for (int i = 20; i <= n * 2; i += 2) {
            boolean biteLogic = posMem[0] > tale[i] - 25 &&
                    posMem[0] < tale[i] + 50 &&
                    posMem[1] > tale[i + 1] - 25 &&
                    posMem[1] < tale[i + 1] + 50;
            if (biteLogic) {
                score = -100;
                if (!deathFlag&&!replay) {
                    try {
                        fileWriter = new FileWriter(repFile, true);
                        fileWriter.write(posMem[0] + "\n");
                        fileWriter.write(posMem[1] + "\n");
                        fileWriter.write((int) apple.posApple[0] + "\n");
                        fileWriter.write((int) apple.posApple[1] + "\n");
                        if(velocity == 15)
                            fileWriter.write(1 + "\n");
                        else
                            fileWriter.write(0 + "\n");
                        fileWriter.close();
                        deathFlag = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void incTale() {
        if (n < 50) {
            taleModel.add(n, new Rectangle());
            tlTop.getChildren().addAll(taleModel.get(n));
            taleModel.get(n).setX(50);
            taleModel.get(n).setY(50);
            taleModel.get(n).setWidth(50);
            taleModel.get(n).setHeight(50);
            taleModel.get(n).setFill(Color.GREEN);
            n++;
            tempScore = score;
        }
        if (tempScore != score) {
            if (multiTaleInc < 6) {
                taleModel.add(n, new Rectangle());
                tlTop.getChildren().addAll(taleModel.get(n));
                taleModel.get(n).setX(tale[20]);
                taleModel.get(n).setY(tale[21]);
                taleModel.get(n).setWidth(50);
                taleModel.get(n).setHeight(50);
                taleModel.get(n).setFill(Color.GREEN);
                n++;
                multiTaleInc++;
            } else {
                multiTaleInc = 0;
                tempScore = score;
            }
        }
    }

    public void stalkingTale() {
        for (int i = 0; i < n; i++) {
            tale[i * 2 + 2] = (int) taleModel.get(i).getX();
            tale[i * 2 + 3] = (int) taleModel.get(i).getY();
            taleModel.get(i).setX(tale[i * 2]);
            taleModel.get(i).setY(tale[i * 2 + 1]);
        }
    }

    public void newImage(String pathName) {
        try {
            pathModel = pathName;
            taleModelTop.setX(50);
            taleModelTop.setY(50);
            taleModelTop.setWidth(50);
            taleModelTop.setHeight(50);
            taleModelTop.setFill(Color.GREEN);
            Image imagePlayer = new Image(new FileInputStream(path + pathName));
            playerModel.setWidth(50);
            playerModel.setHeight(50);
            playerModel.setFill(new ImagePattern(imagePlayer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player(String PathName, double x, double y, Apple ap, boolean rep) {
        try {
            if (rep) {
                fileReader = new FileReader("C:\\Users\\we\\IdeaProjects\\SnakeAreYouOK\\out\\production\\SnakeAreYouOK\\memory.txt");
                reader = new Scanner(fileReader);
                while (reader.hasNextLine()) {
                    repBuf[nBuff - 1] = Integer.parseInt(reader.nextLine(), 10);
                    nBuff++;
                }
                xS = repBuf[iBuff++];
                yS = repBuf[iBuff++];
                xAp = repBuf[iBuff++];
                yAp = repBuf[iBuff++];
                gm = repBuf[nBuff-2];
                nBuff--;
                //testVal = new Text(Integer.toString(xS));       // FOR TESTS
                //playerSub.getChildren().add(testVal);
                //testVal.setX(300);
                //testVal.setY(30);
                reader.close();
                fileReader.close();
            } else {
                fileWriter = new FileWriter(repFile, false);
                fileWriter.close();
            }
            replay = rep;
            apple = ap;
            //apple.refreshApple("/out/production/SnakeAreYouOK/sprites/Apple.png", replay, xAp, yAp);
            taleModelTop.setWidth(50);
            taleModelTop.setHeight(50);
            score = 0;
            pathModel = PathName;
            Image imagePlayer = new Image(new FileInputStream(path + PathName));
            playerModel.setX(x);
            playerModel.setY(y);
            playerModel.setWidth(50);
            playerModel.setHeight(50);
            playerModel.setFill(new ImagePattern(imagePlayer));
        } catch (IOException e) {
            e.printStackTrace();
        }
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!replay) {
                    move();
                    changeDir();
                }
                incTale();
                stalkingTale();
            }
        }.start();
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (replay) {
                    repMovement();
                }
            }
        }.start();
    }

    double delay = 0;

    public void move() {
        tale[1] = posMem[1];
        tale[0] = posMem[0];
        if (pressed == RIGHT) {
            newImage("/out/production/SnakeAreYouOK/sprites/Head.jpg");
            posMem[0] += velocity;
            playerModel.setX(posMem[0]);
            taleModelTop.setX(tale[0]);
            taleModelTop.setY(tale[1]);
        } else {
            if (pressed == LEFT) {
                newImage("/out/production/SnakeAreYouOK/sprites/HeadA.png");
                posMem[0] -= velocity;
                playerModel.setX(posMem[0]);
                taleModelTop.setX(tale[0]);
                taleModelTop.setY(tale[1]);
            } else {
                if (pressed == UP) {
                    newImage("/out/production/SnakeAreYouOK/sprites/HeadW.png");
                    posMem[1] -= velocity;
                    playerModel.setY(posMem[1]);
                    taleModelTop.setX(tale[0]);
                    taleModelTop.setY(tale[1]);
                } else {
                    if (pressed == DOWN) {
                        newImage("/out/production/SnakeAreYouOK/sprites/HeadS.png");
                        posMem[1] += velocity;
                        playerModel.setY(posMem[1]);
                        taleModelTop.setX(tale[0]);
                        taleModelTop.setY(tale[1]);
                    }
                }
            }
        }

    }

    private boolean dir(int direction) {
        boolean logic;
        if (direction >= RIGHT) {
            logic = pressedMem == direction &&
                    block != direction &&
                    Math.abs(delay - posMem[1]) > 50;
        } else {
            logic = pressedMem == direction &&
                    block != direction &&
                    Math.abs(delay - posMem[0]) > 50;
        }
        return logic;
    }

    public void changeDir() {
        if (dir(RIGHT)) {
            pressed = RIGHT;
            block = LEFT;
            delay = posMem[0];
            pressedMem = 0;
        } else {
            if (dir(LEFT)) {
                pressed = LEFT;
                block = RIGHT;
                delay = posMem[0];
                pressedMem = 0;
            } else {
                if (dir(UP)) {
                    pressed = UP;
                    block = DOWN;
                    delay = posMem[1];
                    pressedMem = 0;
                } else {
                    if (dir(DOWN)) {
                        pressed = DOWN;
                        block = UP;
                        delay = posMem[1];
                        pressedMem = 0;
                    }
                }
            }
        }
    }

    int pressed = RIGHT, block = LEFT, pressedMem = 0, pressedMemRep = RIGHT;
    private int xS;
    private int yS;
    public int xAp;
    public int yAp;

    public void movementH(KeyCode key) {
        try {
            if (!replay) {
                fileWriter = new FileWriter(repFile, true);
                if (key == KeyCode.D) {
                    fileWriter.write(posMem[0] + "\n");
                    if (pressedMemRep == UP) {
                        int res = posMem[1] - velocity;
                        fileWriter.write(res + "\n");
                    } else {
                        if (pressedMemRep == DOWN) {
                            int res = posMem[1] + velocity;
                            fileWriter.write(res + "\n");
                        }
                    }
                    //fileWriter.write(Integer.toString(posMem[1]) + "\n");
                    fileWriter.write((int) apple.posApple[0] + "\n");
                    fileWriter.write((int) apple.posApple[1] + "\n");
                    pressedMemRep = RIGHT;
                    pressedMem = RIGHT;
                } else {
                    if (key == KeyCode.A) {
                        fileWriter.write(posMem[0] + "\n");
                        if (pressedMemRep == UP) {
                            int res = posMem[1] - velocity;
                            fileWriter.write(res + "\n");
                        } else {
                            if (pressedMemRep == DOWN) {
                                int res = posMem[1] + velocity;
                                fileWriter.write(res + "\n");
                            }
                        }
                        //fileWriter.write(Integer.toString(posMem[1]) + "\n");
                        fileWriter.write((int) apple.posApple[0] + "\n");
                        fileWriter.write((int) apple.posApple[1] + "\n");
                        pressedMemRep = LEFT;
                        pressedMem = LEFT;
                    } else {
                        if (key == KeyCode.W) {
                            if (pressedMemRep == RIGHT) {
                                int res = posMem[0] + velocity;
                                fileWriter.write(res + "\n");
                            } else {
                                if (pressedMemRep == LEFT) {
                                    int res = posMem[0] - velocity;
                                    fileWriter.write(res + "\n");
                                }
                            }
                            //fileWriter.write(Integer.toString(posMem[0]) + "\n");
                            fileWriter.write(posMem[1] + "\n");
                            fileWriter.write((int) apple.posApple[0] + "\n");
                            fileWriter.write((int) apple.posApple[1] + "\n");
                            pressedMemRep = UP;
                            pressedMem = UP;
                        } else {
                            if (key == KeyCode.S) {
                                if (pressedMemRep == RIGHT) {
                                    int res = posMem[0] + velocity;
                                    fileWriter.write(res + "\n");
                                } else {
                                    if (pressedMemRep == LEFT) {
                                        int res = posMem[0] - velocity;
                                        fileWriter.write(res + "\n");
                                    }
                                }
                                //fileWriter.write(Integer.toString(posMem[0]) + "\n");
                                fileWriter.write(posMem[1] + "\n");
                                fileWriter.write((int) apple.posApple[0] + "\n");
                                fileWriter.write((int) apple.posApple[1] + "\n");
                                pressedMemRep = DOWN;
                                pressedMem = DOWN;
                            }
                        }
                    }
                }
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void repMovement() {
        tale[1] = posMem[1];
        tale[0] = posMem[0];
        if (xS > posMem[0]) {
            newImage("/out/production/SnakeAreYouOK/sprites/Head.jpg");
            posMem[0] += velocity;
            playerModel.setX(posMem[0]);
            taleModelTop.setX(tale[0]);
            taleModelTop.setY(tale[1]);
        } else {
            if (xS < posMem[0]) {
                newImage("/out/production/SnakeAreYouOK/sprites/HeadA.png");
                posMem[0] -= velocity;
                playerModel.setX(posMem[0]);
                taleModelTop.setX(tale[0]);
                taleModelTop.setY(tale[1]);
            } else {
                if (yS < posMem[1]) {
                    newImage("/out/production/SnakeAreYouOK/sprites/HeadW.png");
                    posMem[1] -= velocity;
                    playerModel.setY(posMem[1]);
                    taleModelTop.setX(tale[0]);
                    taleModelTop.setY(tale[1]);
                } else {
                    if (yS > posMem[1]) {
                        newImage("/out/production/SnakeAreYouOK/sprites/HeadS.png");
                        posMem[1] += velocity;
                        playerModel.setY(posMem[1]);
                        taleModelTop.setX(tale[0]);
                        taleModelTop.setY(tale[1]);
                    } else {
                        if (iBuff < nBuff - 4) {
                            //apple.refreshApple("/out/production/SnakeAreYouOK/sprites/Apple.png", replay, xAp, yAp);
                            xS = repBuf[iBuff++];
                            yS = repBuf[iBuff++];
                            xAp = repBuf[iBuff++];
                            yAp = repBuf[iBuff++];
                        }
                    }
                }
            }

        }
    }
}
