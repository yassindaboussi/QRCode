package QRCODE;

import com.google.zxing.ReaderException;
import QRCODE.MainQR;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class QRCodeController implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private TextArea areaInputGen;
    @FXML
    private ImageView imgQRCodeGen;
    private Image genQRCodeImg; // Generated QR Code image
    @FXML
    private ImageView imgQRCodeReader;
    @FXML
    private TextArea areaResultReader;
    private FileChooser imageChooser;
    private FileChooser pngImageSaveChooser;
    private FileChooser textSaveChooser;

    @FXML
    private HBox titleBar;
    @FXML
    private HBox title1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Init file chooser
        pngImageSaveChooser = new FileChooser();
        pngImageSaveChooser.setTitle("Save Image");
        pngImageSaveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image (*.png)", "*.png "));

        // Init File chooser for image
        imageChooser = new FileChooser();
        imageChooser.setTitle("Select Image File");
        imageChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image File", "*.png", "*.jpg", "*.jpeg"));

        // Init File chooser for saving text
        textSaveChooser = new FileChooser();
        textSaveChooser.setTitle("Select Saving File");
        textSaveChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
        areaInputGenerator(areaInputGen);
    }

    /* Start generator methods */
    public void onGenerate() {
        String foregroundColor = "#2E3437";
        String backgroundColor = "#FFFFFF";
        if (!areaInputGen.getText().isEmpty()) {
            genQRCodeImg = QRCodeEngine.encode(areaInputGen.getText(), Integer.parseInt("300"), Integer.parseInt("300"), foregroundColor, backgroundColor);
            if (genQRCodeImg != null) {
                imgQRCodeGen.setImage(genQRCodeImg);
            }
        }

    }

    @FXML
    public void onExportGen() {
        if (genQRCodeImg == null) {
            return;
        }

        // Choose path of save
        File file = pngImageSaveChooser.showSaveDialog(MainQR.stage);
        // Write in a file
        if (file != null) {
            Utils.exportImage(genQRCodeImg, "png", file);
        }
    }

    /* End generator methods */

 /* Start reader methods */
    @FXML
    public void onLoadReader() {
        File file = imageChooser.showOpenDialog(MainQR.stage);

        if (file != null) {
            areaResultReader.setText(null);
            imgQRCodeReader.setImage(new Image(file.toURI().toString()));
//            System.out.println("  " + file.getName().toString()); //Get Name
//            System.out.println("  " + file.toURI().toString()); //Get Pat

            ChangeListener<String> textFieldListener = (observable, oldValue, newValue) -> {
                System.out.println("newValue " + newValue);
                if (!newValue.isEmpty()) {
                    System.out.println("Picture is Changed !");
                    try {
                        onRead();
                    } catch (ReaderException ex) {
                        ex.getStackTrace();
                    }
                }
            };
            textFieldListener.changed(null, null, file.toURI().toString());
        }
    }

    public void onRead() throws ReaderException {
        if (imgQRCodeReader.getImage() == null) {
            return;
        }

        String decodedText = QRCodeEngine.decode(imgQRCodeReader.getImage());
        if (decodedText == null) {
            
        } else {
            areaResultReader.setText(decodedText);
        }
    }

    @FXML
    public void onExportReader() {
        if (areaResultReader.getText() == null && areaResultReader.getText().trim().isEmpty()) {
            return;
        }

        // Choose path of save
        File file = textSaveChooser.showSaveDialog(MainQR.stage);
        // Write in a file
        if (file != null) {
            Utils.exportText(areaResultReader.getText().trim(), file);
        }
    }

    void areaInputGenerator(TextArea TextAreaa) {
        TextAreaa.textProperty().addListener((observable, oldValue, newValue) -> {
            //System.out.println("textfield changed from " + oldValue + " to " + newValue);
            String WordTyped = areaInputGen.getText();
            if (!WordTyped.isEmpty()) {
                onGenerate();
            }
        });
    }

    @FXML
    private void close(MouseEvent event) {
        MainQR.stage.close();
    }


}
