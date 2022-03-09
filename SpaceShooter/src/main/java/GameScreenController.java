import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable, Runnable {

    private Image playerShipImage;

    private int playerShip;

    @FXML
    private Button button;

    @FXML
    private Rectangle rectangle;

    @FXML
    private AnchorPane gameScr;

    @FXML
    private ProgressBar hpPlayer;

    @FXML
    private Label scoreLabel, bombNumber, levelUp;

    @FXML
    private ImageView selectWeaponIv, background, bombGraphic;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        run();
        IdImage idImage = new IdImage();
        background.setImage(idImage.background);
        bombGraphic.setImage(idImage.rocket);
    }

    private void rectangle(){
        rectangle = new Rectangle(350,800,70,70);
        gameScr.getChildren().add(rectangle);
        rectangle.setFill(new ImagePattern(playerShipImage));
    }

    private void image(){
        IdImage idImage = new IdImage();
        if(playerShip == 1){
            playerShipImage = idImage.blueShip;
        }
        if (playerShip == 2){
            playerShipImage  = idImage.redShip;
        }
        if (playerShip == 3){
            playerShipImage = idImage.greenShip;
        }
    }


    @Override
    public void run() {
        playerShip = MainScreen.selectShip;
        image();
        rectangle();
        button.setManaged(false);
        PlayerController controller = new PlayerController(gameScr, rectangle, hpPlayer, scoreLabel, selectWeaponIv, bombNumber, levelUp);
    }
}
