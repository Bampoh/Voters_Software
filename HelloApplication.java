package com.example.voters_software;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;


public class HelloApplication extends Application {

    public static int getindex=0;

    @Override
    public void start(Stage stage1) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Vote_page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage1.setTitle("Login in to Vote");
        stage1.setScene(scene);
        stage1.initStyle(StageStyle.UNDECORATED);
        stage1.show();

    }


    public static void main(String[] args) {


        launch();

    }
}
