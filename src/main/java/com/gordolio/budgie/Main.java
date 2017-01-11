package com.gordolio.budgie;

import static com.gordolio.budgie.dialog.ExceptionDialog.exceptionDialog;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
   public static void main(String[] args) {
      launch(args);
   }

   @Override
   public void start(Stage stage) throws Exception {
       Thread.setDefaultUncaughtExceptionHandler((t,e)-> Platform.runLater(()->exceptionDialog(stage,t,e)));
       try {
           reallyStart(stage);
       } catch(Throwable t) {
           exceptionDialog(stage, Thread.currentThread(), t);
       }
   }

   public void reallyStart(Stage stage) throws Exception {
      URL fxml = getClass().getClassLoader().getResource("view/MainView.fxml");
      FXMLLoader loader = new FXMLLoader(fxml);
      stage.getIcons().add(new Image("icon/r2d2.png"));
      Pane page = loader.load();
      Scene scene = new Scene(page);
      stage.setScene(scene);
      stage.setTitle("R2D2 Budgie Trainer");
      stage.setWidth(300);
      stage.setHeight(300);
      stage.show();
   }

}
