package com.example.voters_software;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Result_Page implements Initializable {
    @FXML
    private BarChart<String, Number> my_barchart;

    @FXML
    private AnchorPane resultpane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create a series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        try (Connection connection = Database.getConnection()) {
            String sql = "SELECT Lastname, IndividualVotes FROM candidateregistration";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String candidateName = resultSet.getString("Lastname");
                int individualVotes = resultSet.getInt("IndividualVotes");

                // Add data to the series with candidateName as x-value and individualVotes as y-value
                series.getData().add(new XYChart.Data<>(candidateName, individualVotes));
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        my_barchart.getData().add(series);
    }
}
