package main.pacman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StartApp extends Application {
    private Scene boardScene;
    private Scene mainScene;
    private Board board;
    private Pacman pacman;
    private int width;
    private int numberOfLines;
    List<Ghost> ghostList;
    Group boardRoot;
    private int numberOfGhosts;
    private String username;


    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("PacMan");
        primaryStage.setScene(new Scene(root, 300, 300));
        Image icon = new Image(getClass().getResourceAsStream("PAC-MAN-2-icon.png"));

        primaryStage.getIcons().add(icon);

        ghostList = new ArrayList<>();
        Random r = new Random();
        int randomBoard = r.nextInt(2);
        String boardPath;

        if(randomBoard == 1) {
            boardPath = "PacMan2/board1.txt";
            width = 50;
            numberOfLines = 10;
        }
        else
        {
            boardPath = "PacMan2/board2.txt";
            width = 40;
            numberOfLines = 15;
        }

        Button startButton1 = new Button("Start");
        startButton1.setTextFill(Color.BEIGE);
        startButton1.setStyle("-fx-background-color: Black");
        startButton1.setPrefWidth(100);
        startButton1.setPrefHeight(50);
        startButton1.setFont(new Font("Arial",20.0));

        Button leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setTextFill(Color.BEIGE);
        leaderboardButton.setStyle("-fx-background-color: Black");
        leaderboardButton.setPrefSize(150, 50);
        leaderboardButton.setFont(new Font("Arial", 20.0));

        board = new Board(boardPath,width,numberOfLines);
        this.width = this.board.getWidth();
        pacman = new Pacman(this.board.getMatrix(),width, board);

        Image logoImage = new Image(getClass().getResourceAsStream("logo.png"));
        ImageView logoImageView = new ImageView(logoImage);

        VBox layout = new VBox(15);
        layout.getChildren().addAll(logoImageView, startButton1, leaderboardButton);
        layout.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResourceAsStream("main/pacman/background.jpg"))), CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setAlignment(Pos.CENTER);

        mainScene = new Scene(layout,600,600);
        primaryStage.setScene(mainScene);

        boardRoot = this.board.createBoard();
        boardScene = new Scene(boardRoot);
        boardScene.setFill(Color.BLACK);
        boardRoot.getChildren().add(pacman.getPacman());

        AlertBox alertBox = new AlertBox();
        alertBox.displayNumberOfGhosts();
        startButton1.setOnAction(e->primaryStage.setScene(boardScene));
        leaderboardButton.setOnAction(e->alertBox.displayLeaderboard());

        numberOfGhosts = alertBox.getNumberOfGhosts();
        username = alertBox.getUsername();
        ghostList = IntStream.rangeClosed(1,numberOfGhosts)
                .mapToObj(g -> new Ghost(this.board.getMatrix(),pacman,width))
                .collect(Collectors.toList());


        List<Rectangle> ghosts = ghostList.stream()
                                .map(Ghost::getGhost)
                                .collect(Collectors.toList());

        boardRoot.getChildren().addAll(new ArrayList<>(ghosts));

        primaryStage.show();

        pacman.movePacmanOnBoard(boardScene);

        ghostList.forEach(ghost -> ghost.moveGhost(ghostList, username));

    }

    public static void main(String[] args) {
        launch(args);
    }
}
