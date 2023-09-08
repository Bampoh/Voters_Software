package com.example.voters_software;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Search_Page {


    @FXML
    private TextField Positiontxt;


    @FXML
    private TextField Usernametxt;


    @FXML
    private TextField contacttxt;

    @FXML
    private TextField Passwordtxt;

    @FXML
    private TextField partytxt;

    @FXML
    private TextField gaendertxt;

    @FXML
    private TextField Emailtxt;

    @FXML
    private TextField agtxt;

    @FXML
    private TextField searchtxt;


    Alert alert = new Alert(Alert.AlertType.ERROR);


    public void onSearch() throws Exception {

        String  search = searchtxt.getText();

        try (Connection connection = Database.getConnection()) {
            String sql = "SELECT Username,Age,Party,Number,Pasword,Position,Gender,Email FROM user_registration WHERE VoterId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, search);
            ResultSet rsl = preparedStatement.executeQuery();


            if(rsl.next()==false)
            {
                alert.setTitle(" ERROR ");
                alert.setContentText(" Sorry, Voter Id Not Found");
                alert.showAndWait();

                searchtxt.clear();
                Usernametxt.clear();
                gaendertxt.clear();
                contacttxt.clear();
                Passwordtxt.clear();
                Emailtxt.clear();
                agtxt.clear();
                partytxt.clear();
            }
            else
            {
                alert.setContentText(" Search Successfully");
                alert.setHeaderText(" Search");
                alert.showAndWait();

                Usernametxt.setText(rsl.getString("Username"));
                agtxt.setText(rsl.getString("Age"));
                partytxt.setText(rsl.getString("Party"));
                contacttxt.setText(rsl.getString("Number"));
                Passwordtxt.setText(rsl.getString("Pasword"));
                Positiontxt.setText(rsl.getString("Position"));
                gaendertxt.setText(rsl.getString("Gender"));
                Emailtxt.setText(rsl.getString("Email"));
            }
        }
    }


    public void onDeletebtn() throws Exception {
        String  delete = searchtxt.getText();

        try (Connection connection = Database.getConnection()) {
            String sql = "DELETE FROM user_registration WHERE VoterId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, delete);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0) {

                alert.setTitle("Delete");
                alert.setContentText("Deleted Successfully");
                alert.showAndWait();
                searchtxt.clear();
                Usernametxt.clear();
                gaendertxt.clear();
                contacttxt.clear();
                Passwordtxt.clear();
                Emailtxt.clear();
                agtxt.clear();
                partytxt.clear();

            } else {

                alert.setTitle("Delete");
                alert.setContentText("No Data Was Deleted");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (show an error message, log, etc.)
        }

        }



    public void onUpdatebtn() throws  Exception{

        String update = searchtxt.getText();
        String username=Usernametxt.getText();
        String age=agtxt.getText();
        String party = partytxt.getText();
        String number = contacttxt.getText();
        String password = Passwordtxt.getText();
        String position= Positiontxt.getText();
        String gender= gaendertxt.getText();
        String email= Emailtxt.getText();

        try (Connection connection = Database.getConnection()) {

            String sql = "UPDATE user_registration SET Username = ?, Age = ?, Party = ?, Number = ?, Pasword = ?, Position = ?, Gender = ?, Email = ?  WHERE VoterId = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, update);


            preparedStatement.setString(2, username);
            preparedStatement.setString(3, age);
            preparedStatement.setString(4, party);
            preparedStatement.setString(5, number);
            preparedStatement.setString(6, password);
            preparedStatement.setString(7,position);
            preparedStatement.setString(8,gender);
            preparedStatement.setString(9,email);
            preparedStatement.executeUpdate();

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0) {
                alert.setTitle("Update");
                alert.setContentText("Updated Successfully");
                alert.showAndWait();
            } else {
                alert.setTitle("Update");
                alert.setContentText("No Data Was Updated");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void onclearbtn(){
        searchtxt.clear();
        Usernametxt.clear();
        gaendertxt.clear();
        contacttxt.clear();
        Passwordtxt.clear();
        Emailtxt.clear();
        agtxt.clear();
        partytxt.clear();
        Positiontxt.clear();

    }
}
