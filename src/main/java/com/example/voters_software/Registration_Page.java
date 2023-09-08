package com.example.voters_software;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;


public class Registration_Page {

    public String Firstname, Lastname, Contact, Education, Constituency, Nationality, Email, gender, Party,Position;
    String imageFilePath;



    @FXML
    private TextField Contacttxt;

    @FXML
    private TextField Contituencytxt;

    @FXML
    private TextField Educationtxt;

    @FXML
    private TextField Emailtxt;

    @FXML
    private TextField Firstnametxt;

    @FXML
    private TextField Lastnametxt;

    @FXML
    private TextField Nationalitytxt;


    @FXML
    private TextField Partytxt;

    @FXML
    private TextField Positiontxt;



    @FXML
    private ComboBox<String> Gender;





    private byte[] imageData; // Declare imageData as a field


    @FXML
    public void initialize() {

        Gender.setItems(FXCollections.observableArrayList("Female", "Male"));

    }

    public static byte[] readImageFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }



    @FXML
    void onImageButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageFilePath = selectedFile.getAbsolutePath();

            try {
                imageData = readImageFile(imageFilePath);

                // Now you have the image data as a byte array (imageData)
                // You can insert it into your database or use it as needed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void onSubmitButtonClicked() throws Exception {

        Firstname = Firstnametxt.getText();
        Lastname = Lastnametxt.getText();
        Contact = Contacttxt.getText();
        Email = Emailtxt.getText();
        Nationality = Nationalitytxt.getText();
        Constituency = Contituencytxt.getText();
        Education = Educationtxt.getText();
        gender = Gender.getValue();
        Party=Partytxt.getText();
        Position=Positiontxt.getText();
        int totalvotess=0;
        int individualvotes=0;


        try (Connection connection = Database.getConnection()) {
            String sql = "INSERT INTO candidateregistration (Firstname,Lastname,Contact ,Education_Level,Constituency,Nationality,Email,Image,Gender,Department,Position,TotalVotes,IndividualVotes,Display_index) VALUES (?,?,?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Firstname);
            preparedStatement.setString(2, Lastname);
            preparedStatement.setString(3, Contact);
            preparedStatement.setString(4, Education);
            preparedStatement.setString(5, Constituency);
            preparedStatement.setString(6, Nationality);
            preparedStatement.setString(7, Email);


            //Set the image data as a byte array
            if (imageData != null) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
                preparedStatement.setBinaryStream(8, inputStream, imageData.length);
            } else {
                preparedStatement.setNull(8, Types.BLOB); // Handle case when no image is selected
            }

            preparedStatement.setString(9, gender);
            preparedStatement.setString(10, Party);
            preparedStatement.setString(11, Position);
            preparedStatement.setInt(12,totalvotess);
            preparedStatement.setInt(13,individualvotes);
            preparedStatement.setInt(14, 0);


            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(" Saved Successfully");
            alert.showAndWait();
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onResetButton()
    {
        Firstnametxt.clear();
        Lastnametxt.clear();
        Contituencytxt.clear();
        Contacttxt.clear();
        Emailtxt.clear();
        Educationtxt.clear();
        Gender.setValue(null);
        Nationalitytxt.clear();
        Partytxt.clear();
        Positiontxt.clear();
    }


}