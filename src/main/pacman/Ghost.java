package main.pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

public class Ghost {
    private Rectangle ghost;
    private List<List<Integer>> path;
    private Timeline timeline;
    private Pacman pacman;
    private int width;
    private LocalDateTime start;


    public Ghost(List<List<Integer>> path, Pacman pacman,int width)
    {
        this.width = width;

        this.ghost = new Rectangle(this.width - 10,this.width - 10);
        //ghost = new Rectangle(40,40);
        this.path = path;
        this.pacman = pacman;


        Random r = new Random();
        int randomImage = r.nextInt(10);
        String imagePath = "phantom" + (randomImage + 1) + ".png";
        Image img = new Image(getClass().getResourceAsStream(imagePath));
        ghost.setFill(new ImagePattern(img));

        if(width == 50)
            ghostTo(225,175);
        else
            ghostTo(140,220);

    }

    /**
     * Moves a ghost randomly on the board
     * @param ghostList list of ghosts to be closed
     * @param username user's name
     */
    public void moveGhost(List<Ghost> ghostList, String username)
    {
        start = LocalDateTime.now();
        this.timeline = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent event) {

                double x = ghost.getLayoutX();
                double y = ghost.getLayoutY();

                if(ghost.getBoundsInParent().intersects(pacman.BoundsInParent()))
                {
                    System.out.println("GHOST TOUCHED MEEEEE");
                    AlertBox alertBox = new AlertBox();
                    LocalDateTime end = LocalDateTime.now();
                    alertBox.display(ghostList, username, ChronoUnit.SECONDS.between(start, end));
                    timeline.stop();
                    pacman.stopPacman();
                    return;
                }

                Random r = new Random();
                int randomMove = r.nextInt(4);

                if (randomMove == 0 && checkPath(x, y - width)) {
                    ghost.setLayoutX(x);
                    ghost.setLayoutY(y - width);
                } else if (randomMove == 1 && checkPath(x, y + width)) {
                    ghost.setLayoutX(x);
                    ghost.setLayoutY(y + width);
                } else if (randomMove == 2 && checkPath(x + width, y)) {
                    ghost.setLayoutX(x + width);
                    ghost.setLayoutY(y);
                } else if (randomMove == 3 && checkPath(x - width, y)) {
                    ghost.setLayoutX(x - width);
                    ghost.setLayoutY(y);
                }

            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);

        timeline.play();

    }

    /**
     * Checks if the position is empty so that the ghost can move
     * @param dx position on x axis
     * @param dy position on y axis
     * @return true if the position is empty, false otherwise
     */
    private boolean checkPath(double dx, double dy)
    {

        int x2 = (int)(dx/width);
        int y2 = (int)(dy/width);

        int x = (int)((dx +(width - 10))/width);
        int y =(int)((dy + (width - 10))/width);


        return this.path.get(y).get(x) == 0 && this.path.get(y2).get(x2) == 0
                && this.path.get(y).get(x2) == 0 && this.path.get(y2).get(x) == 0;


    }

    /**
     * Method to stop the ghost from moving
     */
    public void stopGhost()
    {
        this.timeline.stop();
    }

    /**
     * Getter for the rectangle that represents the ghost
     * @return rectangle
     */
    public Rectangle getGhost()
    {
        return this.ghost;
    }

    /**
     * Method that puts the ghost on a given position, if the position is empty
     * @param x positon on x axis
     * @param y position on y axi
     */
    private void ghostTo(double x, double y)
    {
        double cx = this.ghost.getBoundsInLocal().getWidth()/2;
        double cy = this.ghost.getBoundsInLocal().getHeight()/2;

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

            ghost.relocate(x - cx, y - cy);
        }
    }

}
