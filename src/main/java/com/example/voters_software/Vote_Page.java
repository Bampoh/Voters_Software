package com.example.voters_software;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Vote_Page {


    @FXML
    public Pane Voter_Pane;
    @FXML
    private Label timerlbl;
    private int elapsedSeconds = 0;

    private CheckBox selectedCheckbox = null;
    private int[] candidateVotes;
    private boolean isRunning = false;
    private boolean end=false;
    private Timeline timeline;

    private int retrievedIndex = 0;
    private int totalVotes = 0;
    int count_index=0;
    private int currentDisplayIndex = 1;





    @FXML
    public void initialize() throws Exception {
        DisplayNumberOfPanes();
        int numberOfCandidates = getNumberOfPanesFromDatabase(count_index);
        candidateVotes = new int[numberOfCandidates];
    }



    private void moveToNextDisplayIndex() {
        currentDisplayIndex++;
    }


    @FXML
    void onNextButtonClick(ActionEvent event) throws Exception {
        Voter_Pane.getChildren().clear();
        moveToNextDisplayIndex();
        DisplayNumberOfPanes();
    }




    public void DisplayNumberOfPanes() throws Exception {
        int numberOfPanes = getNumberOfPanesFromDatabase(currentDisplayIndex);
        System.out.println("\nRetrieved Panes for Display_index " + currentDisplayIndex + ": " + numberOfPanes);
        int spacingY = 100;

        for (int i = 1; i <= numberOfPanes; i++) {
            Pane votePane = createVotePane(currentDisplayIndex);
            votePane.setTranslateY((i - 1) * spacingY);
            Voter_Pane.getChildren().add(votePane);
        }
    }





    private int getNumberOfPanesFromDatabase(int displayIndex) {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM candidateregistration WHERE Display_index = ?");
            statement.setInt(1, displayIndex);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                // Handle the case when no matching records are found
                return -1; // or any other appropriate default value
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately (e.g., log it)
            return -1; // Return an appropriate default value
        }
    }











    private Pane createVotePane(int synchCountIndex) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT Lastname, Department, Position, Image FROM candidateregistration WHERE Display_index = ?");
            statement.setInt(1, synchCountIndex);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Retrieve candidate information
                String name = resultSet.getString("Lastname");
                String party = resultSet.getString("Department");
                String position = resultSet.getString("Position");
                byte[] imageData = resultSet.getBytes("Image");

                // Create the Pane and UI elements
                Pane flowPane = new Pane();
                flowPane.setPrefSize(618, 89);
                flowPane.setStyle("-fx-background-color:white;\n" +
                        "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.5),10,0.0,0,3);\n" + "-fx-background-radius: 30 30 30 30;");

                flowPane.setLayoutX(8);
                flowPane.setLayoutY(11);

                ImageView image = new ImageView();
                image.setLayoutX(15);
                image.setLayoutY(10);

                TextField nametxt = new TextField(name);
                TextField partyttxt = new TextField(party);
                TextField positiontxtx = new TextField(position);

                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                Image candidateImage = new Image(bis);
                image.setImage(candidateImage);

                image.setFitHeight(65);
                image.setFitWidth(83);


                nametxt.setPrefWidth(100);
                nametxt.setPromptText("Name");
                nametxt.setEditable(false);
                nametxt.setStyle("-fx-control-inner-background: white; -fx-text-box-border: white;  -fx-font-weight: bold;"); // Set background color to white


                partyttxt.setPrefWidth(100);
                partyttxt.setPromptText("Department");
                partyttxt.setEditable(false);
                partyttxt.setStyle("-fx-control-inner-background: white;  -fx-font-weight: bold; -fx-text-box-border: white; "); // Set background color to white


                positiontxtx.setPrefWidth(100);
                positiontxtx.setPromptText("Position");
                positiontxtx.setEditable(false);
                positiontxtx.setStyle("-fx-control-inner-background: white;  -fx-font-weight: bold; -fx-text-box-border: white;"); // Set background color to white



                CheckBox voteCheckbox = new CheckBox("Vote");
                voteCheckbox.setOnAction(event -> {

                    if(End()==true)
                    {
                        voteCheckbox.setDisable(true);
                    }
                    int totalVotes = calculateTotalVotes();
                    updateTotalVotesInDatabase(totalVotes);
                   // handleVoteCheckbox(voteCheckbox, index - 1);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Voted Successfully.");
                    alert.showAndWait();
                });


                int useVariableX = 120;
                int useVariableY = 16;

                voteCheckbox.setLayoutX(460);
                voteCheckbox.setLayoutY(16);

                nametxt.setLayoutX(useVariableX);
                partyttxt.setLayoutX(useVariableX + 124);
                positiontxtx.setLayoutX(useVariableX + 280);

                nametxt.setLayoutY(useVariableY);
                partyttxt.setLayoutY(useVariableY);
                positiontxtx.setLayoutY(useVariableY);
                voteCheckbox.setLayoutX(560);

                flowPane.getChildren().addAll(image, nametxt, partyttxt, positiontxtx, voteCheckbox);

                return flowPane;
            }
            return null;
        }
    }






    private void handleVoteCheckbox(CheckBox checkbox, int candidateIndex) {
        if (checkbox.isSelected()) {
            candidateVotes[candidateIndex]++;
        } else {
            candidateVotes[candidateIndex]--;
        }
        System.out.println("Candidate " + (candidateIndex + 1) + " votes: " + candidateVotes[candidateIndex]);
        int totalVotes = calculateTotalVotes();
        System.out.println("Total Votes: " + totalVotes);

    }





    @FXML
    void clearPanesAndDatabase() {
        // Clear all panes in the Voter_Pane
        Voter_Pane.getChildren().clear();

        try (Connection connection = Database.getConnection()) {
            PreparedStatement statements = connection.prepareStatement("DELETE FROM candidateregistration");
            PreparedStatement statement = connection.prepareStatement("ALTER TABLE candidateregistration AUTO_INCREMENT = 1");
            statements.executeUpdate();
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }






    private int calculateTotalVotes() {
        // Calculate the total votes by summing up individual votes
        int totalVotes = 0;
        for (int candidateVote : candidateVotes) {
            totalVotes += candidateVote;
        }
        return totalVotes;
    }




    private void updateTotalVotesInDatabase(int TotalVotes) {
        TotalVotes = calculateTotalVotes();
        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE candidateregistration SET TotalVotes = ?");
            statement.setInt(1, TotalVotes);
            statement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Updated ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateIndividualVotesInDatabase() {
        try (Connection connection = Database.getConnection()) {
            for (int i = 0; i < candidateVotes.length; i++) {
                int individualVotes = candidateVotes[i];
                int candidateId = i + 1; // Assuming candidate IDs start from 1
                PreparedStatement statement = connection.prepareStatement("UPDATE candidateregistration SET IndividualVotes = ? WHERE id = ?");
                statement.setInt(1, individualVotes);
                statement.setInt(2, candidateId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





    public void Starts(ActionEvent event) {
        if (!isRunning) {
            isRunning = true;
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> timer()));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    public boolean End() {
        if (isRunning) {
            isRunning = false;
            timeline.pause();
            timerlbl.setText(elapsedSeconds + " seconds");
        }
        end=true;
        return end;
    }

    public void timer(){
        elapsedSeconds++;
        timerlbl.setText(elapsedSeconds + " seconds");
        timeline.setCycleCount(Animation.INDEFINITE);
    }


}