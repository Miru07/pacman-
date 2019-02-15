package main.pacman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.List;

public class AlertBox {

    private Integer numberOfGhosts;
    private String username;
    private UserRepository userRepository;

    public AlertBox()
    {
        userRepository = new UserRepository();
    }

    /**
     *  Method that creates a dialog box for the end of the game.
     * When the button "Exit" is pressed the game is closed
     * @param ghostList - list of ghosts to be stopped when the game ends
     * @param userName - user's name
     * @param score - the number of seconds till pacman met a ghost. the final score is score * number_of_ghosts
     */
    public void display(List<Ghost> ghostList, String userName, long score)
    {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("End game :(");
        Image icon = new Image(getClass().getResourceAsStream("PAC-MAN-2-icon.png"));
        dialogStage.getIcons().add(icon);
        dialogStage.setMaxWidth(300);

        Label label =  new Label();
        label.setText("The evil ghost touched me :(");
        label.setTextFill(Color.BEIGE);

        Label nameLabel = new Label();
        nameLabel.setText("User: " + userName);
        nameLabel.setTextFill(Color.BEIGE);

        Label scoreLabel = new Label();
        scoreLabel.setText("Score: " + Math.toIntExact(score*ghostList.size()));
        scoreLabel.setTextFill(Color.BEIGE);

        Button close = new Button("Exit");
        close.setOnAction(e->System.exit(0));

        Button leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setOnAction(e->displayLeaderboard());

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label,nameLabel,scoreLabel, leaderboardButton,close);
        layout.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResourceAsStream("main/pacman/background.jpg"))), CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(layout,300,300);
        dialogStage.setScene(dialogScene);
        dialogStage.show();

        for (Ghost aGhostList : ghostList) {
            aGhostList.stopGhost();
        }

        User user = new User();
        user.setName(userName);
        user.setScore(Math.toIntExact(score*ghostList.size()));

        java.util.Date d = new java.util.Date();
        Date date = new Date(d.getTime());
        user.setResultDate(date);

        userRepository.findAll();
        userRepository.addUserToList(user);
        userRepository.addUsersToDatabase();

    }

    /**
     * Method that creates a dialog box for introducing the username and a number of ghosts at the beginning of the game
     */
    public void displayNumberOfGhosts()
    {
        Stage infoStage = new Stage();
        infoStage.initModality(Modality.APPLICATION_MODAL);
        Image icon = new Image(getClass().getResourceAsStream("PAC-MAN-2-icon.png"));
        infoStage.getIcons().add(icon);

        TextField nameField = new TextField();
        nameField.setMaxWidth(200);
        Label nameLabel = new Label();
        nameLabel.setText("Username: ");
        nameLabel.setTextFill(Color.BEIGE);

        TextField textField = new TextField();
        textField.setMaxWidth(200);
        Label ghostLabel = new Label();
        ghostLabel.setText("Number of ghosts: ");
        ghostLabel.setTextFill(Color.BEIGE);

        Button getInfo = new Button("Add");
        getInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                username = nameField.getText();
                numberOfGhosts = Integer.parseInt(textField.getText());
                infoStage.close();
            }
        });

        VBox layout = new VBox(15);
        layout.getChildren().addAll(nameLabel,nameField,ghostLabel,textField,getInfo);
        layout.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResourceAsStream("main/pacman/background.jpg"))), CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setAlignment(Pos.CENTER);

        Scene numberScene = new Scene(layout,300,300);
        infoStage.setScene(numberScene);
        infoStage.showAndWait();

    }

    /**
     * Getter for the number of ghosts
     * @return number of ghosts
     */
    public Integer getNumberOfGhosts()
    {
        return this.numberOfGhosts;
    }

    /**
     * Getter for the username
     * @return username
     */
    public String getUsername(){ return this.username; }

    /**
     * Method that dispalys the leaderboard.
     */

    public void displayLeaderboard()
    {
        userRepository.findAll();
        String leaderBoard = "";
        for(int i = 0; i < userRepository.getUserList().size(); i++)
        {
            leaderBoard += (i+1)
                        + "."
                        + userRepository.getUserList().get(i).getName()
                        + " : "
                        + userRepository.getUserList().get(i).getScore()
                        + "("
                        +userRepository.getUserList().get(i).getResultDate()
                        + ")\n";
        }

        Stage leaderboardStage = new Stage();
        leaderboardStage.initModality(Modality.APPLICATION_MODAL);
        leaderboardStage.setTitle("Leaderboard");
        Image icon = new Image(getClass().getResourceAsStream("PAC-MAN-2-icon.png"));
        leaderboardStage.getIcons().add(icon);

        Label leaderboardLabel = new Label();
        leaderboardLabel.setText(leaderBoard);
        leaderboardLabel.setTextFill(Color.BEIGE);

        VBox layout = new VBox(15);
        layout.getChildren().addAll(leaderboardLabel);
        layout.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image(getClass().getClassLoader().getResourceAsStream("main/pacman/background.jpg"))), CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setAlignment(Pos.CENTER);

        Scene leaderboardScene = new Scene(layout,300,300);
        leaderboardStage.setScene(leaderboardScene);
        leaderboardStage.showAndWait();
    }
}
