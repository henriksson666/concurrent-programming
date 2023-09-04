import java.net.URISyntaxException;
import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class TrainSimulator extends Application {

    private boolean isPlaying = false;
    private boolean isReverse = false;
    Pane root;
    int configuration = 1;
    private PathTransition train1Transition, train2Transition;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene(root, 1300, 690);
        int[] clickCount = { 1 };

        Image backgroundImage = new Image("background4.png");
        BackgroundSize backgroundSize = new BackgroundSize(1300, 690, false, false, false, false);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        root.setBackground(new Background(background));

        int sizeWeight = 70;
        int sizeHeight = 50;

        ImageView trainImageView1 = new ImageView(createTrainImage());
        trainImageView1.setFitWidth(sizeWeight);
        trainImageView1.setFitHeight(sizeHeight);

        ImageView trainImageView2 = new ImageView(createTrainImage());
        trainImageView2.setFitWidth(sizeWeight);
        trainImageView2.setFitHeight(sizeHeight);

        ImageView volumeImageView = new ImageView(createVolumeImage("on"));

        Path railPath1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 0, 479, 154, 50, false);
        Path railPath2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 0, 325, 154, 50, false);

        trainImageView1.setRotate(0);
        trainImageView2.setRotate(0);
        trainImageView1.setTranslateX(-35);
        trainImageView1.setTranslateY(451);
        trainImageView2.setTranslateX(-35);
        trainImageView2.setTranslateY(301);

        createRails(clickCount[0], trainImageView1, trainImageView2, isReverse);

        Media media = null;

        try {
            media = new Media(getClass().getResource("trainSound.mp3").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        Button resetButton = createStyledButton("Reset");
        resetButton.setStyle("-fx-background-color: rgba(255, 221, 8, 0.7);");
        resetButton.setTextFill(Color.BLACK);

        Button playPauseButton = createStyledButton("Play");
        playPauseButton.setStyle("-fx-background-color: rgb(115, 183, 50);");

        Button changeDirectionAndPosition = createStyledRestartButton("\tRestart \nChange Position");
        changeDirectionAndPosition.setStyle("-fx-background-color: rgba(212, 175, 55, 0.7);");

        Slider sliderTrain1 = createStyledSlider(0, 10, 0);
        Slider sliderTrain2 = createStyledSlider(0, 10, 0);

        Slider sliderVolume = createStyledVolumeSlider(0, 50, 10);
        
        Text speedTrain1 = createStyledText("Speed: 0 km/h");
        Text speedTrain2 = createStyledText("Speed: 0 km/h");

        VBox train1Box = createStyledVBox("Train 1", sliderTrain1, speedTrain1);
        VBox train2Box = createStyledVBox("Train 2", sliderTrain2, speedTrain2);
        
        VBox volumeBox = createdStyledVolumeVBox(sliderVolume, volumeImageView);

        HBox speedBox = createStyledSpeedHBox(train1Box, train2Box, volumeBox);

        HBox controlHBox = createStyledSpeedHBox(changeDirectionAndPosition, resetButton, playPauseButton, speedBox);

        VBox bottomPane = createStyledVBoxContainer(controlHBox);
        bottomPane.translateXProperty().bind(scene.widthProperty().subtract(bottomPane.widthProperty()));
        bottomPane.translateYProperty().set(82);

        Stage welcomeStage = new Stage();
        Pane welcomePane = initializeWelcomePane(welcomeStage);
        Scene welcomeScene = new Scene(welcomePane, 500, 500);
        welcomeStage.setScene(welcomeScene);
        welcomeStage.getIcons().add(new Image("trainIcon.png"));
        welcomeStage.initModality(Modality.APPLICATION_MODAL);
        welcomeStage.setResizable(false);
        welcomeStage.initStyle(StageStyle.UNDECORATED);
        welcomeStage.centerOnScreen();
        welcomeStage.show();

        root.getChildren().addAll(bottomPane);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("trainIcon.png"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Train Simulator");
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(3));
        pauseTransition.setOnFinished(e -> {
            welcomeStage.close();
            primaryStage.show();
        });
        pauseTransition.play();
        
    }

    

    private Pane initializeWelcomePane(Stage welcomeStage) {
        Pane pane = new Pane();

        ImageView backgroundImageView = new ImageView(new Image("cover.png"));
        BackgroundSize backgroundSize = new BackgroundSize(500, 500, false, false, false, false);
        backgroundImageView.setFitWidth(500);
        backgroundImageView.setFitHeight(500);
        BackgroundImage background = new BackgroundImage(backgroundImageView.getImage(), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        pane.setBackground(new Background(background));

        return pane;
    }



    private VBox createStyledVBoxContainer(javafx.scene.Node... children) {
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 1px; -fx-border-radius: 5px;");
        vBox.setPadding(new Insets(5));
        vBox.setPrefWidth(750);
        vBox.setMaxHeight(90);
        vBox.setLayoutX(0);
        vBox.setLayoutY(500);       
        vBox.getChildren().addAll(children);

        return vBox;
    }



    private HBox createStyledSpeedHBox(javafx.scene.Node... children) {
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 1px; -fx-border-radius: 5px;");
        hBox.getChildren().addAll(children);

        return hBox;
    }

    private VBox createdStyledVolumeVBox(Slider sliderVolume, ImageView volumeImageView) {
        VBox vBox = new VBox(0);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 1px; -fx-border-radius: 5px;");
        vBox.setPrefWidth(70);
        sliderVolume.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 1px; -fx-border-radius: 5px;");
        sliderVolume.setCursor(Cursor.HAND);
        vBox.getChildren().addAll(sliderVolume, volumeImageView);

        return vBox;
    }

    private Image createVolumeImage(String status) {
        if (status == "on") {
            return new Image(getClass().getResourceAsStream("volumeOn.png"));
        }

        return new Image(getClass().getResourceAsStream("volumeOff.png"));
    }

    private VBox createStyledVBox(String text, Slider sliderTrain1, Text speedTrain1) {
        VBox vBox = new VBox(0);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 1px; -fx-border-radius: 5px;");
        vBox.setPrefWidth(180);
        vBox.setPrefHeight(20);
        Label titleLabel = new Label(text);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-text-fill: black;");
        vBox.getChildren().addAll(titleLabel, sliderTrain1, speedTrain1);

        return vBox;
    }

    private Text createStyledText(String text) {
        Text textLabel = new Text(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-fill: white;");

        return textLabel;
    }

    private Slider createStyledVolumeSlider(int min, int max, int value) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickLabels(true);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 1px; -fx-border-radius: 5px;");
        slider.setCursor(Cursor.HAND);

        return slider;
    }

    private Slider createStyledSlider(double min, double max, double value) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        slider.setPrefWidth(200);
        slider.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 1px; -fx-border-radius: 5px;");
        slider.setCursor(Cursor.HAND);

        return slider;
    }

    private Button createStyledRestartButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 1px; -fx-border-radius: 5px;");
        button.setTextFill(Color.BLACK);
        button.setCursor(Cursor.HAND);
        button.setAlignment(Pos.CENTER);
        button.setWrapText(true);
        button.setMinWidth(50);
        button.setMinHeight(50);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(2.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        button.setEffect(dropShadow);

        return button;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 1px; -fx-border-radius: 5px;");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setPrefWidth(80);
        button.setPrefHeight(50);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(2.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        button.setEffect(dropShadow);

        return button;        
    }

    private void createRails(int configuration, ImageView train1, ImageView train2, boolean isReverse2) {
        Path rail1 = null, rail2 = null;
        Boolean isReverseTrain1 = false, isReverseTrain2 = false;

        root.getChildren().removeAll(train1, train2);

        if (configuration == 1) {
            rail1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 0, 476, 154, 50, false);
            rail2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 0, 324, 154, 50, false);
        } else if (configuration == 2) {
            rail1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 0, 476, 154, 50, false);
            rail2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 1310, 324, 154, 50, true);
            isReverseTrain2 = true;
        } else if (configuration == 3) {
            rail1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 1310, 476, 154, 50, true);
            rail2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 0, 324, 154, 50, false);
            isReverseTrain1 = true;
        } else if (configuration == 4) {
            rail1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 1310, 475, 154, 50, true);
            rail2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 1310, 324, 154, 50, true);
            isReverseTrain1 = true;
            isReverseTrain2 = true;
        }

        root.getChildren().addAll(train1, train2);

        train1Transition = createPathTransition(train1, rail1);
        train2Transition = createPathTransition(train2, rail2);

        if (isReverseTrain1 && isReverseTrain2) {
            train1.setRotate(-180);
            train2.setRotate(-180);
        } else if (isReverseTrain1) {
            train1.setRotate(-180);
        } else if (isReverseTrain2) {
            train2.setRotate(-180);
        }

        train1Transition.setPath(rail1);
        train2Transition.setPath(rail2);
    }

    private PathTransition createPathTransition(Node trainImageNode, Path rail) {
        PathTransition pathTransition = new PathTransition();
        trainImageNode.setRotate(0);
        pathTransition.setNode(trainImageNode);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        pathTransition.setAutoReverse(false);

        pathTransition.setPath(rail);

        return pathTransition;
    }

    private Path createPath(double[] angles, double x, double y, double length, int numIntermediatePoints,
            boolean isReverse) {
        Path path = new Path();
        path.setStroke(Color.BLUE);
        path.getElements().add(new MoveTo(x, y));

        if (isReverse) {
            for (int i = angles.length - 1; i >= 0; i--) {
                double angle = angles[i];
                double x1 = x + length * Math.cos(Math.toRadians(angle));
                double y1 = y + length * Math.sin(Math.toRadians(angle));

                if (i != angles.length - 1) {
                    double controlX = x - (length / 2) * Math.cos(Math.toRadians(angle));
                    double controlY = y - (length / 2) * Math.sin(Math.toRadians(angle));
                    path.getElements().add(new QuadCurveTo(controlX, controlY, x1, y1));
                }

                x = x1;
                y = y1;

                if (i != 0) {
                    for (int j = 0; j <= numIntermediatePoints; j++) {
                        double t = (double) j / (numIntermediatePoints + 1);
                        double intermediateX = x * (1 - t) + x1 * t;
                        double intermediateY = y * (1 - t) + y1 * t;
                        path.getElements().add(new LineTo(intermediateX, intermediateY));
                    }
                }
            }
        } else {
            for (int i = 0; i < angles.length; i++) {
                double angle = angles[i];
                double x1 = x + length * Math.cos(Math.toRadians(angle));
                double y1 = y + length * Math.sin(Math.toRadians(angle));

                if (i != 0) {
                    double controlX = x - (length / 2) * Math.cos(Math.toRadians(angle));
                    double controlY = y - (length / 2) * Math.sin(Math.toRadians(angle));
                    path.getElements().add(new QuadCurveTo(controlX, controlY, x1, y1));
                }

                x = x1;
                y = y1;

                if (i != angles.length - 1) {
                    for (int j = 1; j <= numIntermediatePoints; j++) {
                        double t = (double) j / (numIntermediatePoints + 1);
                        double intermediateX = x * (1 - t) + x1 * t;
                        double intermediateY = y * (1 - t) + y1 * t;
                        path.getElements().add(new LineTo(intermediateX, intermediateY));
                    }
                }
            }
        }

        return path;
    }

    private Image createTrainImage() {
        Random rand = new Random();
        int selectedNumber = rand.nextInt(4) + 1;
        String imageName = "train" + selectedNumber + ".png";

        return new Image(TrainSimulator.class.getResourceAsStream(imageName));
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

}