package com.example.voters_software;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main_Page implements Initializable {

    public Button VoteButton;
    public Button onRegistrationButton;

    Stage stage = new Stage();

    @FXML
    private Pane pollpane;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Pane DashboardPane;


    @FXML
    private AnchorPane MyAnchor;

    @FXML
    private Label userslbl;

    @FXML
    private Label Datelbl;

    @FXML
    private Label Expenditurelbl;

    @FXML
    private Label NoPartieslbl;

    @FXML
    private Label Nopeoplelbl;

    @FXML
    private Label Totalvoteslbl;

    String text="";


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::updateLabelWithDataFromDatabase, 0, 10, TimeUnit.SECONDS);


        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Define a format for the date (you can adjust the format as needed)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        // Format the date using the formatter
        String formattedDate = currentDate.format(formatter);

        // Set the formatted date as the text of the label
        Datelbl.setText("Current Date: " + formattedDate);


    }


    @FXML
    public void onRegistrationButton(ActionEvent actionEvent) throws Exception {
        AnchorPane view = FXMLLoader.load(getClass().getResource("Registration_Page.fxml"));
        mainPane.setCenter(view);
    }


    @FXML
    public void onVoteButtonClick(ActionEvent actionEvent) throws Exception {

        AnchorPane view = FXMLLoader.load(getClass().getResource("Vote_page.fxml"));
        mainPane.setCenter(view);
    }


    @FXML
    public void onLogoutButton(ActionEvent actionEvent) throws Exception {

        stage = (Stage) MyAnchor.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login_Page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }


    public void onDashboardButton(ActionEvent actionEvent) throws Exception {
        mainPane.setCenter(DashboardPane);
    }

    public void onResultButton(ActionEvent actionEvent) throws Exception {
        AnchorPane view = FXMLLoader.load(getClass().getResource("Result_Page.fxml"));
        mainPane.setCenter(view);
    }

    public void onMainSearchbtn(ActionEvent actionEvent) throws Exception {
        AnchorPane view = FXMLLoader.load(getClass().getResource("Search_Page.fxml"));
        mainPane.setCenter(view);
    }

    private void updateLabelWithDataFromDatabase() {
        try (Connection connection = Database.getConnection()) {
            // Connect to the database

            // Execute a query to retrieve data (assuming a "data_table" table)
            String sql = "SELECT No_Users,No_PeopleVoted,No_Parties,Total_Votes,Expenditure FROM records";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rsl = preparedStatement.executeQuery();


            if (rsl.next()) {
                // Read the data from the result set (assuming one column named
                String data1 = rsl.getString("No_Users");
                String data2 = rsl.getString("No_PeopleVoted");
                String data3= rsl.getString("No_Parties");
                String data4 = rsl.getString("Total_Votes");
                String data5= rsl.getString("Expenditure");

                // Update the label on the JavaFX application thread
                Platform.runLater(() -> userslbl.setText(data1));
                Platform.runLater(() -> Nopeoplelbl.setText(data2));
                Platform.runLater(() -> NoPartieslbl.setText(data3));
                Platform.runLater(() -> Totalvoteslbl.setText(data4));
                Platform.runLater(() -> Expenditurelbl.setText(data5));
            }

            // Close resources
            rsl.close();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void setExpenditure() {
        // Create a TextField and set its properties
        TextField textfield = new TextField();
        textfield.setVisible(true);

        // Add the event handler for Enter key press
        textfield.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                String text = textfield.getText();
                textfield.setVisible(false);
                System.out.println("Expenditure : " + text);

                // Update the database with the entered expenditure
                try (Connection connection = Database.getConnection()) {

                    // Execute a query to retrieve data (assuming a "data_table" table)
                    String sql = "UPDATE records SET Expenditure = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, text);
                    preparedStatement.executeUpdate();
                    System.out.println("Successfully added to database && parties : "+text);


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // Add the TextField to the pollpane
        pollpane.getChildren().add(textfield);
    }




    public void setNoParties()
    {
        TextField textfield1=new TextField();
        textfield1.setVisible(true);
        pollpane.getChildren().add(textfield1);



        try (Connection connection = Database.getConnection()) {

            // Execute a query to retrieve data (assuming a "data_table" table)
            String sql = "UPDATE records SET No_Parties = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, textfield1.getText());
            preparedStatement.executeUpdate();
            System.out.println("Successfully added to database && parties : "+textfield1.getText());


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}