import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {

    public static int selectShip;

    @FXML
    private RadioButton radioBlue, radioRed, radioGreen;

    @FXML
    private Button blueFx, greenFx, redFx, startGameFx;

    @FXML
    private Label labelSelect;

    private String buttonClick = "-fx-background-color: #00A8CC;";
    private String buttonNormal = "@../windowStyle/windowStyle.css";

    @FXML
    public void startGame() throws IOException {
        if(radioRed.isSelected() || radioGreen.isSelected() || radioBlue.isSelected()){
            if(radioBlue.isSelected()){
                selectShip = 1;
            }
            if (radioRed.isSelected()) {
                selectShip = 2;
            }
            if (radioGreen.isSelected()) {
                selectShip = 3;
            }
            Parent root = FXMLLoader.load(getClass().getResource("FXML/gameScreen.fxml"));
            Stage window = (Stage) startGameFx.getScene().getWindow();
            window.setScene(new Scene(root));
        }
        labelSelect.setText("Select Your Ship");
    }

    @FXML
    private void buttonBlue(){
        radioBlue.setSelected(true);
        radioGreen.setSelected(false);
        radioRed.setSelected(false);
        blueFx.setStyle(buttonClick);
        redFx.setStyle(buttonNormal);
        greenFx.setStyle(buttonNormal);
    }

    @FXML
    private void buttonRed(){
        radioBlue.setSelected(false);
        radioGreen.setSelected(false);
        radioRed.setSelected(true);
        blueFx.setStyle(buttonNormal);
        redFx.setStyle(buttonClick);
        greenFx.setStyle(buttonNormal);
    }

    @FXML
    private void buttonGreen(){
        radioBlue.setSelected(false);
        radioGreen.setSelected(true);
        radioRed.setSelected(false);
        blueFx.setStyle(buttonNormal);
        redFx.setStyle(buttonNormal);
        greenFx.setStyle(buttonClick);
    }

    @FXML
    private void exitGame(){
        Platform.exit();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectShip = 0;
        IdImage idImage = new IdImage();
        blueFx.setGraphic(new ImageView(idImage.blueShip));
        redFx.setGraphic(new ImageView(idImage.redShip));
        greenFx.setGraphic(new ImageView(idImage.greenShip));

    }
}
