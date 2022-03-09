import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EndGameWindow implements Initializable {

    @FXML
    private Label scoreLabel;

    @FXML
    private void playAgain() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("FXML/mainScreen.fxml"));
        Stage stage = (Stage) scoreLabel.getScene().getWindow();
        stage.setScene(new Scene(parent));

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PlayerController game = new PlayerController();
        scoreLabel.setText("Your score: " + game.getScore());
    }
}
