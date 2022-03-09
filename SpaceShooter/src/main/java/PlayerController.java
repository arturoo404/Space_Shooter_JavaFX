import javafx.animation.*;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PlayerController implements Runnable{


    ArrayList <Rectangle> playerLaser = new ArrayList<>();
    ArrayList <Rectangle> enemyShipsLvl1 = new ArrayList<>();
    ArrayList <Rectangle> enemyShipsLvl2 = new ArrayList<>();
    ArrayList <Integer> enemyHpLvl1 = new ArrayList<>();
    ArrayList <Integer> enemyHpLvl2 = new ArrayList<>();
    ArrayList <Rectangle> enemyShotLvl1 = new ArrayList<>();
    ArrayList <Rectangle> enemyShotLvl2 = new ArrayList<>();
    ArrayList <Rectangle> bombShot = new ArrayList<>();

    private int playerLaserSel = 0;
    private int laserTimer = 1;
    private int rocket = 0;
    private double playerHp = 100;

    public static int getScore() {
        return score;
    }

    private static int score = 0;
    private static int lvl1ShipChance = 3000;
    private static int lvl2ShipChance = 4500;
    private int bombNumber = 5;
    private int gameLvl = 1;
    private boolean laserWeaponSel = true, levelUp = false;

    @FXML
    private AnchorPane pane;

    @FXML
    private Rectangle rectangle;

    @FXML
    private ProgressBar playerHpBar;

    @FXML
    private Label scoreLabel, bombUpdate, levelUpLabel;

    @FXML
    private ImageView imageView;

    private BooleanProperty  goUp = new SimpleBooleanProperty();
    private BooleanProperty  goDown = new SimpleBooleanProperty();
    private BooleanProperty  goRight = new SimpleBooleanProperty();
    private BooleanProperty goLeft = new SimpleBooleanProperty();
    private BooleanProperty shot = new SimpleBooleanProperty();
    private BooleanProperty rocketKey = new SimpleBooleanProperty();

    private BooleanBinding keyPress = goUp.or(goDown).or(goLeft).or(goRight).or(shot).or(rocketKey);



    public PlayerController(AnchorPane pane, Rectangle rectangle, ProgressBar playerHpBar, Label scoreLabel, ImageView imageView, Label bombUpdate, Label levelUp) {
        this.pane = pane;
        this.rectangle = rectangle;
        this.playerHpBar = playerHpBar;
        this.scoreLabel = scoreLabel;
        this.imageView = imageView;
        this.bombUpdate = bombUpdate;
        this.levelUpLabel = levelUp;
        score = 0;
        checker.start();
        run();

        keyPress.addListener(((observableValue, aBoolean, t1) -> {
            if(!aBoolean){
                timer.start();
            } else {
                timer.stop();
            }
        }));

    }

    public PlayerController() {
    }

    private void movementSetUp(){
        pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
                                 @Override
                                 public void handle(KeyEvent event) {
                                     switch (event.getCode()){
                                         case W:
                                             goUp.set(true);
                                             break;
                                         case A:
                                             goLeft.set(true);
                                             break;
                                         case S:
                                             goDown.set(true);
                                             break;
                                         case D:
                                             goRight.set(true);
                                             break;
                                         case R:
                                             shot.set(true);
                                             break;
                                         case N:
                                             if (laserWeaponSel){
                                                 playerLaserSel = 1;
                                                 laserWeaponSel = false;
                                             }else {
                                                 playerLaserSel = 0;
                                                 laserWeaponSel = true;
                                             }
                                             break;
                                         case T:
                                             rocketKey.set(true);
                                             rocket = 1;
                                             break;
                                     }
                                 }
                             }
        );

        pane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case W:
                        goUp.set(false);
                        break;
                    case A:
                        goLeft.set(false);
                        break;
                    case S:
                        goDown.set(false);
                        break;
                    case D:
                        goRight.set(false);
                        break;
                    case R:
                        shot.set(false);
                        laserTimer--;
                        break;
                    case T:
                        rocketKey.set(false);
                        laserTimer--;
                        break;
                }
            }
        });
    }

    Timeline timelineLvl1 = new Timeline(new KeyFrame(Duration.millis(lvl1ShipChance), e -> {
        Rectangle enemy = enemyShipLvl1();
        enemyShipsLvl1.add(enemy);
        enemyHpLvl1.add(5);
        pane.getChildren().add(enemy);
    }));

    Timeline lvlUpTimeLine = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        levelUp =false;
        gameLvl++;
    }));

    Timeline timelineEnemyShotLvl1 = new Timeline(new KeyFrame(Duration.millis(2000), e -> {
        for (int i = 0; i < enemyShipsLvl1.size(); i++){
            double y = enemyShipsLvl1.get(i).getTranslateY() + 80;
            double x = enemyShipsLvl1.get(i).getLayoutX() + 50;
            Rectangle enemyShot = enemyShotLaser(x, y, 1);
            enemyShotLvl1.add(enemyShot);
            pane.getChildren().add(enemyShotLvl1.get(enemyShotLvl1.size() - 1));
        }
    }));

    Timeline timelineEnemyShotLvl2 = new Timeline(new KeyFrame(Duration.millis(2500), e -> {
        for(int i = 0; i < enemyShipsLvl2.size(); i++){
            double y = enemyShipsLvl2.get(i).getTranslateY();
            double x = enemyShipsLvl2.get(i).getLayoutX();
            Rectangle enemyShotLeft = enemyShotLaser(x, y + 70, 2);
            Rectangle enemyShotRight = enemyShotLaser(x + 60, y + 70, 2);
            enemyShotLvl2.add(enemyShotLeft);
            pane.getChildren().add(enemyShotLvl2.get(enemyShotLvl2.size() - 1));
            enemyShotLvl2.add(enemyShotRight);
            pane.getChildren().add(enemyShotLvl2.get(enemyShotLvl2.size() - 1));
        }
    }));

    Timeline timeLineLvl2 = new Timeline(new KeyFrame(Duration.millis(lvl2ShipChance), e -> {

        Rectangle enemyLvl1 = enemyShipLvl1();
        enemyShipsLvl1.add(enemyLvl1);
        enemyHpLvl1.add(5);
        pane.getChildren().add(enemyLvl1);

        Rectangle enemy = enemyShipLvl2();
        enemyShipsLvl2.add(enemy);
        enemyHpLvl2.add(10);
        pane.getChildren().add(enemy);
    }));

    Timeline timeLineLvl3 = new Timeline(new KeyFrame(Duration.millis(3200), e -> {

        Rectangle enemyLvl1 = enemyShipLvl1();
        enemyShipsLvl1.add(enemyLvl1);
        enemyHpLvl1.add(5);
        pane.getChildren().add(enemyLvl1);

        Rectangle enemy = enemyShipLvl2();
        enemyShipsLvl2.add(enemy);
        enemyHpLvl2.add(10);
        pane.getChildren().add(enemy);

    }));

    Timeline bombRecoveryTL = new Timeline(new KeyFrame(Duration.millis(3000), e ->{
        bombNumber++;
    }));

    AnimationTimer checker = new AnimationTimer() {
        @Override
        public void handle(long l) {
        collisionLaserEnemyLvl1();
        collisionLaserEnemyLvl2();
        removeEnemy();
        playerHpUpdate();
        scoreUpdate();
        weaponUpdate();
        gameLvlUpdate();
        playerCollisionEnemyLaser();
        bombUpdateLabel();
        levelUpdate();
        enemyCollisionWithBomb();
            try {
                endGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            double yPos = rectangle.getLayoutY();
            double xPos = rectangle.getLayoutX();
            double y = 0, x = 0;
            if(goUp.get()){
                y -= 5;
            }
            if(goDown.get()){
                y += 5;
            }
            if(goLeft.get()){
                x -= 10;
            }
            if(goRight.get()){
                x += 10;
            }
            if(shot.get() && laserTimer <= 1 && playerLaserSel == 0){
                rocket = 0;
                playerLaser.add(rectangle(xPos + 380, yPos + 760));
                pane.getChildren().add(playerLaser.get(playerLaser.size() - 1));
                laserTimer++;
            }
            if (shot.get() && laserTimer <= 1 && playerLaserSel == 1){
                rocket = 0;
                playerLaser.add(rectangle(xPos + 350, yPos + 780));
                pane.getChildren().add(playerLaser.get(playerLaser.size() - 1));
                playerLaser.add(rectangle(xPos + 410, yPos + 760));
                pane.getChildren().add(playerLaser.get(playerLaser.size() - 1));
                laserTimer++;
            }
            if(rocketKey.get() && laserTimer <= 1 && bombNumber > 0){
                bombShot.add(rectangle(xPos + 380, yPos + 760));
                pane.getChildren().add(bombShot.get(bombShot.size() - 1));
                laserTimer++;
                bombNumber -= 1;
                rocket = 0;
            }

            move(x,y);
        }
    };

    private void move(double x, double y){
        if(x == 0 && y == 0) return;


        final double cx = rectangle.getBoundsInLocal().getWidth()  / 2;
        final double cy = rectangle.getBoundsInLocal().getHeight() / 2;


        double xr = cx + rectangle.getLayoutX() + x;
        double yr = cy + rectangle.getLayoutY() + y;

        moveTo(xr, yr);


    }
    private void moveTo(double x, double y){
        final double cx = rectangle.getBoundsInLocal().getWidth()  / 2;
        final double cy = rectangle.getBoundsInLocal().getHeight() / 2;

        if(x - cx >= -350 &&
                x + cx <= 450 &&
           y - cy >= -800 &&
           y + cy <= 200){
            rectangle.setLayoutX(x - cx);
            rectangle.setLayoutY(y - cy);
        }
    }

    private Rectangle rectangle(double x, double y){
        int trnTime = 1000;
        Rectangle rec = new Rectangle();
        IdImage idImage = new IdImage();
        if (playerLaserSel == 0 && rocket == 0){
            rec.setLayoutY(y);
            rec.setLayoutX(x);
            rec.setWidth(9);
            rec.setHeight(54);
            rec.setFill(new ImagePattern(idImage.singleLaser));
            trnTime = 1300;
        }
        if (playerLaserSel == 1 && rocket == 0){
            rec.setLayoutY(y);
            rec.setLayoutX(x);
            rec.setWidth(13);
            rec.setHeight(54);
            rec.setFill(new ImagePattern(idImage.doubleLaser));
            trnTime = 1800;
        }
        if(rocket == 1){
            rec.setLayoutY(y);
            rec.setLayoutX(x);
            rec.setWidth(13);
            rec.setHeight(57);
            rec.setFill(new ImagePattern(idImage.rocket));
            trnTime = 5000;
        }
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(rec);
        transition.setDuration(Duration.millis(trnTime));
        transition.setToY(-900);
        transition.play();
        transition.setOnFinished(e -> {
            pane.getChildren().remove(rec);
            playerLaser.remove(rec);
        });

        return rec;
    }

    private Rectangle enemyShotLaser(double x, double y, int shots){
        int speed = 1000;
        Rectangle rectangle = new Rectangle();
        IdImage idImage = new IdImage();
        if (shots == 1){
            rectangle.setLayoutY(y);
            rectangle.setLayoutX(x);
            rectangle.setWidth(9);
            rectangle.setHeight(37);
            rectangle.setFill(new ImagePattern(idImage.enemyLaserLvl1));
            speed = 2300;
        }
        if (shots == 2){
            rectangle.setLayoutY(y);
            rectangle.setLayoutX(x);
            rectangle.setWidth(9);
            rectangle.setHeight(57);
            rectangle.setFill(new ImagePattern(idImage.enemyLaserLvl2));
            speed = 3500;
        }

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(rectangle);
        transition.setDuration(Duration.millis(speed));
        transition.setToY(1000);
        transition.play();
        return rectangle;
    }

    private Rectangle enemyShipLvl1(){
        Rectangle rectangle = new Rectangle();
        IdImage idImage = new IdImage();
        Random random = new Random();
        int shipSkin = random.nextInt(2);
        int shipStart = random.nextInt(600) + 100;
        if(shipSkin == 0){
            rectangle.setLayoutX(shipStart);
            rectangle.setLayoutY(0);
            rectangle.setWidth(104);
            rectangle.setHeight(84);
            rectangle.setFill(new ImagePattern(idImage.enemyBlueLvl1));
        }

        if(shipSkin == 1){
            rectangle.setLayoutX(shipStart);
            rectangle.setLayoutY(0);
            rectangle.setWidth(104);
            rectangle.setHeight(84);
            rectangle.setFill(new ImagePattern(idImage.enemyOrangeLvl1));
        }

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(5));
        transition.setToY(1000);
        transition.setNode(rectangle);
        transition.play();

        return rectangle;
    }

    private Rectangle enemyShipLvl2(){
        Rectangle rectangle = new Rectangle();
        IdImage idImage = new IdImage();
        Random random = new Random();
        int shipSkin = random.nextInt(2);
        int shipStart = random.nextInt(600) + 100;
        if(shipSkin == 0){
            rectangle.setLayoutX(shipStart);
            rectangle.setLayoutY(0);
            rectangle.setWidth(82);
            rectangle.setHeight(84);
            rectangle.setFill(new ImagePattern(idImage.enemyBlackLvl2));
        }

        if(shipSkin == 1){
            rectangle.setLayoutX(shipStart);
            rectangle.setLayoutY(0);
            rectangle.setWidth(82);
            rectangle.setHeight(84);
            rectangle.setFill(new ImagePattern(idImage.enemyGreenLvl2));
        }

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(10));
        transition.setToY(1000);
        transition.setNode(rectangle);
        transition.play();

        return rectangle;
    }

    private Rectangle explosionEnemy(double x, double y){
        IdImage idImage = new IdImage();
        Rectangle rec = new Rectangle();
        rec.setLayoutX(x + 40);
        rec.setLayoutY(y + 40);
        rec.setWidth(20);
        rec.setHeight(20);
        rec.setFill(new ImagePattern(idImage.explosionEnemy));

        ScaleTransition scale = new ScaleTransition();
        scale.setNode(rec);
        scale.setDuration(Duration.millis(500));
        scale.setByX(5);
        scale.setByY(5);
        scale.play();
        scale.setOnFinished(e ->
                pane.getChildren().remove(rec));
        return rec;
    }

    private void collisionLaserEnemyLvl1(){
        try {
            for (int i = 0; i < playerLaser.size(); i++){
                for (int j = 0; j < enemyShipsLvl1.size(); j++){
                    if(playerLaser.get(i).getBoundsInParent().intersects(enemyShipsLvl1.get(j).getBoundsInParent())){
                        int hp = enemyHpLvl1.get(j);
                        hp -= 1;
                        if (hp <= 0){
                            Rectangle rectangle = explosionEnemy(enemyShipsLvl1.get(j).getLayoutX(), enemyShipsLvl1.get(j).getTranslateY());
                            pane.getChildren().add(rectangle);
                            pane.getChildren().remove(enemyShipsLvl1.get(j));
                            enemyShipsLvl1.remove(j);
                            enemyHpLvl1.remove(j);
                            score += 5;
                            playerHpUp(1);
                        }
                        if (hp > 0){
                            enemyHpLvl1.set(j,hp);
                            pane.getChildren().remove(playerLaser.get(i));
                            playerLaser.remove(i);
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e){
        }

    }

    private void enemyCollisionWithBomb(){
        try {
            for (int i = 0; i < bombShot.size(); i++){

                for (int j = 0; j < enemyShipsLvl1.size(); j++){
                    if(bombShot.get(i).getBoundsInParent().intersects(enemyShipsLvl1.get(j).getBoundsInParent())){
                    Rectangle rectangle = explosionEnemy(enemyShipsLvl1.get(j).getLayoutX(), enemyShipsLvl1.get(j).getTranslateY());
                    pane.getChildren().add(rectangle);
                    pane.getChildren().remove(enemyShipsLvl1.get(j));
                    enemyShipsLvl1.remove(j);
                    enemyHpLvl1.remove(j);
                    score += 10;
                    pane.getChildren().remove(bombShot.get(i));
                    bombShot.remove(i);
                    playerHpUp(1);
                    }
                }

                for (int j = 0; j < enemyShipsLvl2.size(); j++){
                    if(bombShot.get(i).getBoundsInParent().intersects(enemyShipsLvl2.get(j).getBoundsInParent())){
                    Rectangle rectangle = explosionEnemy(enemyShipsLvl2.get(j).getLayoutX(), enemyShipsLvl2.get(j).getTranslateY());
                    pane.getChildren().add(rectangle);
                    pane.getChildren().remove(enemyShipsLvl2.get(j));
                    enemyShipsLvl2.remove(j);
                    enemyHpLvl2.remove(j);
                    score += 20;
                    pane.getChildren().remove(bombShot.get(i));
                    bombShot.remove(i);
                    playerHpUp(2);
                }
                }
            }

        }catch (IndexOutOfBoundsException e){

        }
    }

    private void playerCollisionEnemyLaser(){
        try {
            for (int i = 0; i < enemyShotLvl1.size(); i++){
                if (rectangle.getBoundsInParent().intersects(enemyShotLvl1.get(i).getBoundsInParent())){
                    playerHp -= 2;
                    pane.getChildren().remove(enemyShotLvl1.get(i));
                    enemyShotLvl1.remove(i);
                }
            }

            for (int i = 0; i < enemyShotLvl2.size(); i++){
                if (rectangle.getBoundsInParent().intersects(enemyShotLvl2.get(i).getBoundsInParent())){
                    playerHp -=5;
                    pane.getChildren().remove(enemyShotLvl2.get(i));
                    enemyShotLvl2.remove(i);
                }
            }

        }catch (IndexOutOfBoundsException e){

        }
    }

    private void collisionLaserEnemyLvl2(){
        try {
            for (int i = 0; i < playerLaser.size(); i++){
                for (int j = 0; j < enemyShipsLvl2.size(); j++){
                    if(playerLaser.get(i).getBoundsInParent().intersects(enemyShipsLvl2.get(j).getBoundsInParent())){
                        int hp = enemyHpLvl2.get(j);
                        hp -= 1;
                        if (hp <= 0){
                            Rectangle rectangle = explosionEnemy(enemyShipsLvl2.get(j).getLayoutX(), enemyShipsLvl2.get(j).getTranslateY());
                            pane.getChildren().add(rectangle);
                            pane.getChildren().remove(enemyShipsLvl2.get(j));
                            enemyShipsLvl2.remove(j);
                            enemyHpLvl2.remove(j);
                            score += 10;
                            playerHpUp(2);
                        }
                        if(hp > 0){
                            enemyHpLvl2.set(j,hp);
                            pane.getChildren().remove(playerLaser.get(i));
                            playerLaser.remove(i);
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e){
        }

    }

    private void removeEnemy() throws IndexOutOfBoundsException{
        try {
            for (int i = 0; i < enemyShipsLvl1.size(); i++){
                if(enemyShipsLvl1.get(i).getTranslateY() >= 800){
                    playerHp -= 5;
                    pane.getChildren().remove(enemyShipsLvl1.get(i));
                    enemyShipsLvl1.remove(i);
                    enemyHpLvl1.remove(i);
                }
            }
            for (int i = 0; i < enemyShipsLvl2.size(); i++){
                if(enemyShipsLvl2.get(i).getTranslateY() >= 800){
                    playerHp -= 10;
                    pane.getChildren().remove(enemyShipsLvl2.get(i));
                    enemyShipsLvl2.remove(i);
                    enemyHpLvl2.remove(i);
                }
            }
        }catch (IndexOutOfBoundsException e){
        }
    }

    private void playerHpUp(double hp){
        double a = playerHp + hp;
        if(a >= 100){
            playerHp = 100;
        }
        if(a < 100){
            playerHp = a;
        }
    }

    private void playerHpUpdate(){
        double hp = playerHp / 100;
        playerHpBar.setStyle("-fx-accent:  #00FF00;");
        playerHpBar.setProgress(hp);
    }

    private void scoreUpdate(){
        scoreLabel.setText("Score: " + score);
    }

    private void weaponUpdate(){
        IdImage idImage = new IdImage();
        if(laserWeaponSel){
            imageView.setImage(idImage.singleLaser);
        }else{
            imageView.setImage(idImage.doubleLaserIv);
        }
    }

    private void gameLvlUpdate(){
        if(score >= 5){
            timeLineLvl2.play();
            timelineLvl1.pause();
            timelineEnemyShotLvl2.play();
            if (gameLvl == 1){
                levelUp = true;
            }
        }
        if (score >= 20){
            timeLineLvl3.play();
            timeLineLvl2.pause();
            if (gameLvl == 2){
                levelUp = true;
            }
        }
    }

    private void endGame() throws IOException {
        try {
            if(playerHp <= 0){
                Parent root = FXMLLoader.load(getClass().getResource("FXML/endGameWindow.fxml"));
                Stage stage = (Stage) pane.getScene().getWindow();
                stage.setScene(new Scene(root));
                checker.stop();
            }
        }catch (NullPointerException e){
        }

    }

    private void bombUpdateLabel(){
        final int maxBomb = 5;
        if(bombNumber > 0){
            bombUpdate.setText(String.valueOf(bombNumber));
        }
        if (bombNumber == 0){
            bombUpdate.setText("0");
        }
        if (bombNumber < maxBomb){
            bombRecoveryTL.play();
        }
        if(bombNumber == maxBomb){
            bombRecoveryTL.pause();
        }
    }

    private void levelUpdate(){
        if (levelUp){
            levelUpLabel.setVisible(true);
            lvlUpTimeLine.play();
        }
        if (!levelUp){
            levelUpLabel.setVisible(false);
            lvlUpTimeLine.pause();
        }
    }


    @Override
    public void run() {
        movementSetUp();
        timelineLvl1.setCycleCount(Animation.INDEFINITE);
        timeLineLvl2.setCycleCount(Animation.INDEFINITE);
        timelineEnemyShotLvl1.setCycleCount(Animation.INDEFINITE);
        timelineEnemyShotLvl2.setCycleCount(Animation.INDEFINITE);
        bombRecoveryTL.setCycleCount(Animation.INDEFINITE);
        timeLineLvl3.setCycleCount(Animation.INDEFINITE);
        lvlUpTimeLine.setCycleCount(Animation.INDEFINITE);
        timelineLvl1.play();
        timelineEnemyShotLvl1.play();
    }
}
