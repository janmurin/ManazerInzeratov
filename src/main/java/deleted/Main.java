/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deleted;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("VBox Test");

    VBox vb = new VBox();
    vb.setPadding(new Insets(10, 50, 50, 50));
    vb.setSpacing(10);

    Label lbl = new Label("VBox");
    lbl.setFont(Font.font("Amble CN", FontWeight.BOLD, 24));
    vb.getChildren().add(lbl);

    Button btn1 = new Button();
    btn1.setText("Button1");
    vb.getChildren().add(btn1);

    Button btn2 = new Button();
    btn2.setText("Button2");
    vb.getChildren().add(btn2);

    Button btn3 = new Button();
    btn3.setText("Button3");
    vb.getChildren().add(btn3);

    Button btn4 = new Button();
    btn4.setText("Button4");
    vb.getChildren().add(btn4);

    // Adding VBox to the scene
    Scene scene = new Scene(vb);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
