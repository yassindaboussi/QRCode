package QRCODE;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

public class MainQR extends Application {

    public static Stage stage;
    double xOffset, yOffset;

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("QRCode.fxml"));

            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.TRANSPARENT);
            MainQR.stage = stage;
            stage.show();

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Image image = new Image("/img/iconApp.png");
        stage.getIcons().add(image);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
