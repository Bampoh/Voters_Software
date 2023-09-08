package com.example.voters_software;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Settings {


    @FXML
    private TextField Indextxt;

    @FXML
    private TextField canPositiontxt;



    public void  selectPosition()
    {
        int index=Integer.parseInt(Indextxt.getText());
        String pos = canPositiontxt.getText();

        HelloApplication.getindex=index;

        try (Connection connection = Database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE candidateregistration SET Display_index = ?  WHERE Position = ?");
            statement.setInt(1, index);
            statement.setString(2, pos);
            statement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Updated ");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
