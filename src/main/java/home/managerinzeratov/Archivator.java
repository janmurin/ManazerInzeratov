/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.managerinzeratov;

import entity.Inzerat;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static javafx.concurrent.Worker.State.FAILED;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Janco1
 */
public class Archivator {

    private WebView view;
    private WebEngine engine;
    public Inzerat archivovanyInzerat;
    public Database database;

    public Archivator() {
        createScene();
    }

    private void createScene() {

        Platform.runLater(new Runnable() {
            private Scene scene;
            Number width = 1000;
            Number height = 2000;

            private ScrollPane webViewScroll = new ScrollPane();
            private JFXPanel jfxPanel = new JFXPanel();

            @Override
            public void run() {

                view = new WebView();
                view.setPrefSize(1000, 2000);
                engine = view.getEngine();

                engine.titleProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, final String newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                // MainForm2.this.setTitle("Manažér realitných inzerátov: " + aktualnyInzerat.getNazov());
                            }
                        });
                    }
                });

                engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(final WebEvent<String> event) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                //lblStatus.setText(event.getData());
                            }
                        });
                    }
                });

                engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            private String nazov;

                            @Override
                            public void run() {
                                //progressBar.setValue(newValue.intValue());
                                if (newValue.intValue() == 100) {
                                    System.out.println("tlacim obrazok");
                                    nazov = archivovanyInzerat.getNazov().replaceAll(" ", "_");
                                    File folder = new File("archiv");
                                    File[] fileList = folder.listFiles();
                                    for (int i = 0; i < fileList.length; i++) {
                                        String filename = fileList[i].getName().split(".png")[0];
                                        if (nazov.equals(filename)) {
                                            nazov = nazov + "1";
                                        }
                                    }
                                    archivovanyInzerat.setArchivCesta("archiv/" + nazov + ".png");

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            WritableImage image = view.snapshot(new SnapshotParameters(), null);
                                            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                                            try {
                                                File captureFile = new File("archiv/" + nazov + ".png");
                                                ImageIO.write(bufferedImage, "png", captureFile);
                                                System.out.println("Captured WebView to: " + captureFile.getAbsoluteFile());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    database.updateArchivInzeratArchivCesta(archivovanyInzerat.getAktualny_link(),"archiv/" + nazov + ".png");
                                }
                            }
                        });
                    }
                });

                engine.getLoadWorker()
                        .exceptionProperty()
                        .addListener(new ChangeListener<Throwable>() {

                            public void changed(ObservableValue<? extends Throwable> o, Throwable old, final Throwable value) {
                                if (engine.getLoadWorker().getState() == FAILED) {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
//                                            JOptionPane.showMessageDialog(
//                                                    panel,
//                                                    (value != null)
//                                                            ? engine.getLocation() + "\n" + value.getMessage()
//                                                            : engine.getLocation() + "\nUnexpected error.",
//                                                    "Loading error...",
//                                                    JOptionPane.ERROR_MESSAGE);
                                        }
                                    });
                                }
                            }
                        });

                webViewScroll.setContent(view);
                scene = new Scene(webViewScroll);
                jfxPanel.setScene(scene);
            }
        });
    }

    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String tmp = toURL(url);

                if (tmp == null) {
                    tmp = toURL("http://" + url);
                }

                engine.load(tmp);
            }
        });
    }

    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
            return null;
        }
    }
}
