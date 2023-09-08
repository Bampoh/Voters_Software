package com.example.voters_software;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class User_Register {

    @FXML
    private TextField Userpartytxt;

    @FXML
    private TextField Usergendertxt;

    @FXML
    private TextField Useremailtxt;

    @FXML
    private TextField Userpasswordtxt;

    @FXML
    private TextField Useragetxt;

    @FXML
    private TextField Usernametxt;

    @FXML
    private TextField UserNumbertxt;

    @FXML
    private TextField Userpositiontxt;

    @FXML
    private ChoiceBox<String> Roletxt;


    private int count;

    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public void initialize()
    {
        Roletxt.setItems(FXCollections.observableArrayList("User "," Admin"));
    }




    public void onSavebtn() throws Exception
    {
        String voterid=generateVoterID(10);
        String username=Usernametxt.getText();
        String age=Useragetxt.getText();
        String party = Userpartytxt.getText();
        String number = UserNumbertxt.getText();
        String password = Userpasswordtxt.getText();
        String position= Userpositiontxt.getText();
        String gender= Usergendertxt.getText();
        String email= Useremailtxt.getText();
       String Role=Roletxt.getValue();

        try (Connection connection = Database.getConnection()) {
            String sql = "INSERT INTO user_registration (VoterId,Username,Age,Party,Number,Pasword,Position,Gender,Email,Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, voterid);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, age);
            preparedStatement.setString(4, party);
            preparedStatement.setString(5, number);
            preparedStatement.setString(6, password);
            preparedStatement.setString(7,position);
            preparedStatement.setString(8,gender);
            preparedStatement.setString(9,email);
            preparedStatement.setString(10,Role);
            preparedStatement.executeUpdate();

            alert.setContentText("Registered Successful");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Database.getConnection().close();

        alert.setTitle("Saved");
        alert.setHeaderText(" Voter Id");
        alert.setContentText("Saved Successfully " + " Voter Id : "+voterid);
        alert.showAndWait();

    }



    private String generateVoterID(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder voterId = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            voterId.append(characters.charAt(index));
        }
        return voterId.toString();
    }


    public void onUserclearbtn()
    {
        Userpasswordtxt.clear();
        Useragetxt.clear();
        Usernametxt.clear();
        UserNumbertxt.clear();
        Userpositiontxt.clear();
        Userpartytxt.clear();
        Usergendertxt.clear();
        Useremailtxt.clear();



    }


}



