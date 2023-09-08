

package com.example.voters_software;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Login_page {

        Alert alert= new Alert(Alert.AlertType.INFORMATION);

        @FXML
        private TextField Passwordtxt;

        @FXML
        private TextField VoterIdtxt;

        Stage stage2 = new Stage();

        Stage Loginstage=new Stage();

        @FXML
        private AnchorPane scenePane;

        @FXML
        private ComboBox<String> myRoletxt;

        Stage stage1;

        int IncreaseUsers=0;
        String ChangeUsers;


 public void initialize()
 {
         myRoletxt.setItems(FXCollections.observableArrayList("Admin","User"));
 }


public void onExitButtonClick(ActionEvent event){

        Loginstage=(Stage) scenePane.getScene().getWindow();
        Loginstage.close();
}



        @FXML
        protected void onLoginButtonClick() throws Exception {

                String voterid = VoterIdtxt.getText();
                String password = Passwordtxt.getText();
                String role=myRoletxt.getValue();

                try (Connection connection = Database.getConnection()) {
                        String sql = "SELECT VoterId, Pasword, Role FROM user_registration WHERE Pasword = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);

                        // Set the parameter value for the prepared statement
                        preparedStatement.setString(1, role);

                        ResultSet resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {
                                if (resultSet.getString("Pasword").equalsIgnoreCase(password) && resultSet.getString("VoterId").equalsIgnoreCase(voterid) && resultSet.getString("Role").equalsIgnoreCase(role) )  {

                                        if(role.equalsIgnoreCase("User")){

                                                // Get an instance of the Voting_Page class


                                                stage1 = (Stage) scenePane.getScene().getWindow();
                                                stage1.close();

                                                alert.setTitle("Login");
                                                alert.setContentText("Successfully Login");
                                                alert.showAndWait();

                                                IncreaseUsers++;
                                                ChangeUsers=String.valueOf(IncreaseUsers);
                                                System.out.println(" IncreaseUsers : "+ IncreaseUsers +" ChangeUsers : "+ChangeUsers);
                                                updateLabelWithDataFromDatabase();

                                                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main_Page.fxml"));
                                                Scene scene = new Scene(fxmlLoader.load());
                                                Loginstage.setScene(scene);
                                                Loginstage.show();
                                        }


                                } else if(role.equalsIgnoreCase("Admin")){

                                        stage1 = (Stage) scenePane.getScene().getWindow();
                                        stage1.close();

                                        alert.setTitle("Login");
                                        alert.setContentText("Successfully Login");
                                        alert.showAndWait();

                                        IncreaseUsers++;
                                        ChangeUsers=String.valueOf(IncreaseUsers);
                                        System.out.println(" IncreaseUsers : "+ IncreaseUsers +" ChangeUsers : "+ChangeUsers);
                                        updateLabelWithDataFromDatabase();

                                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main_Page.fxml"));
                                        Scene scene = new Scene(fxmlLoader.load());
                                        Loginstage.setScene(scene);
                                        Loginstage.show();

                                }

                        }
                        else {
                                 alert.setTitle("Login");
                                alert.setContentText("Wrong Username or Password");
                                alert.showAndWait();
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                }




/*
                stage1 = (Stage) scenePane.getScene().getWindow();
                stage1.close();

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Main_Page.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Loginstage.setScene(scene);
                Loginstage.show();
*/
        }

        private void updateLabelWithDataFromDatabase() {
                try (Connection connection = Database.getConnection()) {

                        ChangeUsers=String.valueOf(IncreaseUsers);


                        // Execute a query to retrieve data (assuming a "data_table" table)
                        String sql = "UPDATE records SET No_Users = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, ChangeUsers);
                        preparedStatement.executeUpdate();
                        System.out.println("Successfully added to database");

                } catch (SQLException e) {
                        e.printStackTrace();
                }
        }

        @FXML
        protected void onRegisterButtonClick() {

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("User_Registration.fxml"));
                try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage2.setScene(scene);
                        stage2.show();


                } catch (IOException e) {
                        System.out.println(" Error : " + e.getMessage());
                }

        }


}

