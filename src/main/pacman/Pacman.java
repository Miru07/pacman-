package main.pacman;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.time.Instant;
import java.util.List;

public class Pacman {
    private Rectangle pacman;
    private List<List<Integer>> path;
    private AnimationTimer timer;
    private boolean goNorth, goSouth, goEast, goWest;
    private int width;
    private int score;
    private Instant start;



    public Pacman(List<List<Integer>> path,int width, Board board)
    {
        this.width = width;
        this.pacman = new Rectangle(width - 10,width - 10);
        this.pacman.setFill(Color.BLACK);

        this.path = path;
        movePacmanTo(width + width/2,width + width/2);
        this.score = 0;

    }

    /**
     * Getter for the rectangle that represents the main.pacman
     * @return rectangle
     */
    public Rectangle getPacman()
    {
        return this.pacman;
    }

    /**
     * Method that relocates main.pacman with dx steps on x and dy steps on y
     * @param dx can be -1 or 1
     * @param dy can be -1 or 1
     */
    public void movePacmanBy(int dx, int dy)
    {

        if(dx==0 && dy==0)return;

        double cx = pacman.getBoundsInLocal().getWidth()/2;
        double cy = pacman.getBoundsInLocal().getHeight()/2;

        double x = cx + pacman.getLayoutX() + dx;
        double y = cy + pacman.getLayoutY() + dy;

        movePacmanTo(x, y);
    }

    /**
     * Method that moves main.pacman on a given position, if the position is empty
     * @param x position on  x axis
     * @param y position on y axis
     */
    private void movePacmanTo(double x, double y)
    {
        double cx = pacman.getBoundsInLocal().getWidth()/2;
        double cy = pacman.getBoundsInLocal().getHeight()/2;

        int rectPosX = (int) ((x-cx)/width);
        int rectPosY = (int)((y-cy)/width);

        int rectPosX2 = (int) ((x+cx)/width);
        int rectPosY2 = (int)((y+cy)/width);

        Integer pos1 = this.path.get(rectPosY).get(rectPosX);
        Integer pos2 = this.path.get(rectPosY).get(rectPosX2);
        Integer pos3 = this.path.get(rectPosY2).get(rectPosX2);
        Integer pos4 = this.path.get(rectPosY2).get(rectPosX);

        if (x - cx >= 0 && x + cx <= 600 && y - cy >= 0 && y + cy <= 600 &&
                pos1 == 0  && pos2 == 0 && pos3 == 0 && pos4 == 0){

            pacman.relocate(x - cx, y - cy);
        }

    }

    /**
     * Getter for the bounds in parent for the main.pacman
     * @return bounds
     */
    public Bounds BoundsInParent()
    {
        return this.pacman.getBoundsInParent();
    }

    /**
     * Method that moves main.pacman on the board in the direction of the pressed key
     * @param boardScene the board on which the main.pacman is moving
     */
    public void movePacmanOnBoard(Scene boardScene) {
        boardScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        goNorth = true;
                        break;
                    case DOWN:
                        goSouth = true;
                        break;
                    case RIGHT:
                        goEast = true;
                        break;
                    case LEFT:
                        goWest = true;
                        break;

                }
            }
        });

        boardScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        goNorth = false;
                        break;
                    case DOWN:
                        goSouth = false;
                        break;
                    case RIGHT:
                        goEast = false;
                        break;
                    case LEFT:
                        goWest = false;
                        break;
                }
            }
        });

       this.timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                int dx = 0, dy = 0;

                if (goNorth) dy -= 1;
                if (goSouth) dy += 1;
                if (goEast) dx += 1;
                if (goWest) dx -= 1;

                movePacmanBy(dx, dy);
            }
        };

        timer.start();

    }

    /**
     * Method to stop the pacman from moving
     */

    public void stopPacman()
    {
        this.timer.stop();
    }

    public int getScore()
    {
        return this.score;
    }
}
