package main.pacman;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Board {
    private List<List<Integer>> matrix;
    private List<Integer> list;
    private List<Integer> walls;
    private Group r;
    private int width;
    private int numberOfLines;
    private String boardPath;

    public Board(String boardPath, int width, int numberOfLines)
    {
        this.boardPath = boardPath;
        this.width = width;
        this.numberOfLines = numberOfLines;
        this.matrix = new ArrayList<>();
        this.list = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.readBoardFromFile();
        this.r = new Group();
    }

    /**
     * Gets the board from a file and forms a matrix for the representation of it in UI
     */

    private void readBoardFromFile()
    {
        List<String> lines;
        final AtomicInteger counter = new AtomicInteger(0);
        try {
            File file = new File(this.boardPath.trim());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            lines = reader.lines()
                    .flatMap(line -> Arrays.stream(line.split(" ")))
                    .collect(Collectors.toList());

            this.list = lines.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            this.matrix = new ArrayList<>(
                    list.stream()
                            .collect(Collectors.groupingBy(el->counter.getAndIncrement()/this.numberOfLines))
                            .values()
            );

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    /**
     * Getter for the boards matrix
     * @return matrix
     */
    public List<List<Integer>> getMatrix(){
        return this.matrix;
    }

    /**
     * Getter for a list which contains the board in a single line
     * @return list
     */
    public List<Integer> getList(){return this.list;}

    /**
     * Getter for the positions that are empty in the board
     * @return list of empty positions
     */
    public List<Integer> getPath()
    {
        List<Integer> list = this.getList();
        list.replaceAll(x -> x + list.indexOf(x));
        this.walls =  list.stream()
                .filter(x->x != 1)
                .collect(Collectors.toList());

        return this.walls;
    }

    /**
     * Getter for the group that represents the board
     * For 1, the color is darker and that represents a wall
     * For 0, the color is lighter and that represents an empty position
     * @return group of rectangles
     */
    public Group createBoard() {

        int col = numberOfLines, rows = numberOfLines, horizontal = width, vertical = width;

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < rows; j++) {
                Rectangle rect = new Rectangle(horizontal * j, vertical * i, horizontal, vertical);
                rect.setStroke(Color.BLACK);

                if (this.getMatrix().get(i).get(j) == 1) {

                    rect.setFill(Color.DARKSLATEBLUE);

                } else if(this.getMatrix().get(i).get(j) == 0){
                    rect.setFill(Color.ALICEBLUE);
                }
                this.r.getChildren().add(rect);
            }
        }

        return this.r;

    }


    /**
     * Getter for the width of a rectangle in the board
     * @return width
     */
    public int getWidth() {
        return width;
    }
}
