/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deleted;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.value.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.*;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WebviewCaptureDemo extends Application {

    private static final String HOME_LOC = "http://docs.oracle.com/javafx/2/get_started/animation.htm";

    private WebView webView;

    private File captureFile = new File("cap.png");

    public static void main(String[] args) {
        Application.launch(WebviewCaptureDemo.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        webView = new WebView();
        webView.setPrefSize(1000, 8000);

        final TextField location = new TextField();
        location.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!location.getText().startsWith("http")) {
                    location.setText("http://" + location.getText());
                }
                webView.getEngine().load(location.getText());
            }
        });
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldLocation, String newLocation) {
                location.setText(newLocation);
            }
        });

        ScrollPane webViewScroll = new ScrollPane();
        webViewScroll.setContent(webView);
        webViewScroll.setPrefSize(800, 300);

        final Button capture = new Button("Capture");

        final ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);

        final TextField prefWidth = new TextField("1000");
        final TextField prefHeight = new TextField("8000");

        HBox controls = new HBox(10);
        controls.getChildren().addAll(capture, progress, prefWidth, prefHeight);

        final ImageView imageView = new ImageView();
        ScrollPane imageViewScroll = makeScrollable(imageView);
        imageViewScroll.setPrefSize(800, 300);

        final PauseTransition pt = new PauseTransition();
        pt.setDuration(Duration.millis(3000));
        pt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                WritableImage image = webView.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                try {
                    ImageIO.write(bufferedImage, "png", captureFile);
                    imageView.setImage(new Image(captureFile.toURI().toURL().toExternalForm()));
                    System.out.println("Captured WebView to: " + captureFile.getAbsoluteFile());
                    progress.setVisible(false);
                    capture.setDisable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        capture.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                NumberStringConverter converter = new NumberStringConverter();
                double W = converter.fromString(prefWidth.getText()).doubleValue();
                double H = converter.fromString(prefHeight.getText()).doubleValue();

// ensure that the capture size has a reasonable min size and is within the limits of what JavaFX is capable of processing.
                if (W < 100) {
                    W = 100;
                    prefWidth.setText("100");
                }

                if (W > 2000) {
                    W = 2000;
                    prefWidth.setText("2000");
                }

                if (H < 100) {
                    H = 100;
                    prefHeight.setText("100");
                }

                if (H > 16000) {
                    H = 16000;
                    prefHeight.setText("16000");
                }

                webView.setPrefWidth(W);
                webView.setPrefHeight(H);

                pt.play();
                capture.setDisable(true);
                progress.setVisible(true);
            }
        });

        webView.getEngine().load(HOME_LOC);

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 10; -fx-background-color: cornsilk;");
        layout.getChildren().setAll(
                location,
                webViewScroll,
                controls,
                imageViewScroll,
                new Label("Capture File: " + captureFile.getAbsolutePath())
        );
        VBox.setVgrow(imageViewScroll, Priority.ALWAYS);

        stage.setScene(new Scene(layout));
        stage.show();
    }

    private ScrollPane makeScrollable(final ImageView imageView) {
        final ScrollPane scroll = new ScrollPane();
        final StackPane centeredImageView = new StackPane();

        centeredImageView.getChildren().add(imageView);
        scroll.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                centeredImageView.setPrefSize(
                        Math.max(imageView.prefWidth(bounds.getHeight()), bounds.getWidth()),
                        Math.max(imageView.prefHeight(bounds.getWidth()), bounds.getHeight())
                );
            }
        });
        scroll.setContent(centeredImageView);

        return scroll;
    }

}
