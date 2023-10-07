import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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

public class TrainSimulator2 extends Application {

    private boolean isRunning = false, isReverse = false;
    Pane root;
    int trainConfiguration = 1;

    @Override

    public void start(Stage primaryStage) throws Exception {
        root = new Pane();
        Scene scene = new Scene(root, 1300, 690);
        int[] clickCount = { 1 };

        Image backgroundImage = new Image("background4.png");
        BackgroundSize backgroundSize = new BackgroundSize(1300, 690, false, false, false, false);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        root.setBackground(new Background(background));

        int sizeWeight = 70, sizeHeight = 70;

        ImageView trainImageView1 = new ImageView(createTrainImage());
        trainImageView1.setFitHeight(sizeHeight);
        trainImageView1.setFitWidth(sizeWeight);

        ImageView trainImageView2 = new ImageView(createTrainImage());
        trainImageView2.setFitHeight(sizeHeight);
        trainImageView2.setFitWidth(sizeWeight);

        ImageView voluImageView = new ImageView(createVolumeImage("on"));

        Path railPath1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 0, 479, 154, 50, false);
        Path railPath2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 0, 325, 154, 50, false);

        TrainOne trainOne = new TrainOne(trainImageView1, new PathTransition(), railPath1, 1);
        TrainTwo trainTwo = new TrainTwo(trainImageView2, new PathTransition(), railPath2, 1);

        Media media = null;

        try {
            media = new Media(getClass().getResource("trainRunning.mp3").toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setAutoPlay(false);

        Button solutionButton1 = createStyledButton("Solution 1");
        solutionButton1.setMinWidth(100);

        Button solutionButton2 = createStyledButton("Solution 2");
        solutionButton2.setMinWidth(100);

        Button solutionButton3 = createStyledButton("Solution 3");
        solutionButton3.setMinWidth(100);

        VBox solutions = new VBox();
        solutions.getChildren().addAll(solutionButton1, solutionButton2, solutionButton3);
        solutions.setSpacing(5);

        Button resetButton = createStyledButton("Reset");
        resetButton.setStyle("-fx-background-color: #0db2ff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-text-align: center;");
        resetButton.setTextFill(Color.WHITE);
        resetButton.setOnMouseEntered(e -> {
            resetButton.setStyle(
                "-fx-background-color: #0b9bde; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-text-align: center; -fx-border-color: #fff; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            resetButton.setTextFill(Color.WHITE);
        });

        resetButton.setOnMouseExited(e -> {
            resetButton.setStyle(
                "-fx-background-color: #0db2ff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-text-align: center; -fx-border-width: 0px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            resetButton.setTextFill(Color.WHITE);
        });

        Button playPauseButton = createStyledButton("Play");

        Button changeDirectionAndPositionButton = createStyledRestartButton("Change\nPosition");

        Slider sliderTrain1 = createStyledSlider(0, 10, 0);
        Slider sliderTrain2 = createStyledSlider(0, 10, 0);

        Slider sliderVolume = createStyledVolumeSlider(0, 10, 2);
        
        Text speedTrain1 = createStyledText("Speed: 0 km/h");
        Text speedTrain2 = createStyledText("Speed: 0 km/h");

        VBox train1Box = createStyledVBox("Train 1", sliderTrain1, speedTrain1);
        VBox train2Box = createStyledVBox("Train 2", sliderTrain2, speedTrain2);

        VBox volumeBox = createStyledVolumeVBox(sliderVolume, voluImageView);

        HBox buttonBox = createStyledSpeedHBox(changeDirectionAndPositionButton, resetButton, playPauseButton);
        buttonBox.setMinWidth(315);
        buttonBox.setStyle("-fx-border-color: rgb(0,0,0,0); -fx-spacing: 5px;");

        HBox speedBox = createStyledSpeedHBox(train1Box, train2Box, volumeBox);
        speedBox.setMinWidth(415);
        speedBox.setStyle("-fx-border-color: rgb(0,0,0,0); -fx-spacing: 5px;");

        HBox controlHBox = createStyledSpeedHBox(solutions, buttonBox, speedBox);
        controlHBox.setAlignment(Pos.CENTER);
        controlHBox.setSpacing(1);

        VBox bottomPane = createStyledVBoxContainer(controlHBox);
        bottomPane.translateXProperty().bind(scene.widthProperty().subtract(bottomPane.widthProperty()));
        bottomPane.translateYProperty().set(75);

        Stage welcomeStage = new Stage();
        Pane welcomePane = initializeWelcomePane(welcomeStage);
        Scene welcomeScene = new Scene(welcomePane, 500, 500);
        welcomeStage.setScene(welcomeScene);
        welcomeStage.getIcons().add(new Image("railway.png"));
        welcomeStage.initModality(Modality.APPLICATION_MODAL);
        welcomeStage.setResizable(false);
        welcomeStage.initStyle(StageStyle.UNDECORATED);
        welcomeStage.centerOnScreen();
        welcomeStage.show();

        root.getChildren().addAll(bottomPane);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("railway.png"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Train Simulator");
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0));

        //primaryStage.show();
        pauseTransition.setOnFinished(e -> {
            welcomeStage.close();
            primaryStage.show();
        });
        pauseTransition.play();
    }

    private Path createPath(double[] angles, double x, double y, double length, int numIntermediatePoints,
            boolean reverse) {
        Path path = new Path();

        path.getElements().add(new MoveTo(x, y));

        if (reverse) {
            for (int i = angles.length - 1; i >= 0; i--) {
                double angle = angles[i];
                double x1 = x - length * Math.cos(Math.toRadians(angle));
                double y1 = y + length * Math.sin(Math.toRadians(angle));

                if (i != angles.length - 1) {
                    double controlX = x - (length / 2) * Math.cos(Math.toRadians(angle));
                    double controlY = y + (length / 2) * Math.sin(Math.toRadians(angle));
                    path.getElements().add(new QuadCurveTo(controlX, controlY, x1, y1));
                }

                x = x1;
                y = y1;

                if (i != 0) {
                    for (int j = 1; j <= numIntermediatePoints; j++) {
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
                    double controlX = x + (length / 2) * Math.cos(Math.toRadians(angle));
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

    private Image createVolumeImage(String status) {
        if (status.equals("on")) {
            return new Image(getClass().getResourceAsStream("volumeOn.png"));
        }

        return new Image(getClass().getResourceAsStream("volumeOff.png"));
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

    private Button createStyledRestartButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #0db2ff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-width: 0px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #0b9bde; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-color: #fff; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            button.setTextFill(Color.WHITE);
        });
        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: #0db2ff; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-width: 0px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            button.setTextFill(Color.WHITE);
        });
        
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setAlignment(Pos.CENTER);
        button.setWrapText(true);
        button.setMinWidth(50);
        button.setMinHeight(50);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(2.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setSpread(0.0);
        dropShadow.setColor(Color.BLACK);
        button.setEffect(dropShadow);

        return button;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #0dd410; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-width: 0px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        button.setTextFill(Color.WHITE);
        button.setCursor(Cursor.HAND);
        button.setPrefWidth(80);
        button.setPrefHeight(50);

        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: #09ab0b; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-text-align: center; -fx-border-color: #fff; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            button.setTextFill(Color.WHITE);
        });

        button.setOnMouseExited(e -> {
            button.setStyle(
                "-fx-background-color: #0dd410; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma;  -fx-text-align: center; -fx-border-width: 0px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            button.setTextFill(Color.WHITE);
        });

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(2.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setSpread(0.0);
        dropShadow.setColor(Color.BLACK);
        button.setEffect(dropShadow);

        return button;
    }

    private Slider createStyledSlider(double min, double max, double value) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(2);
        slider.setSnapToTicks(true);
        slider.setMaxWidth(150);
        slider.setMinWidth(150);
        slider.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.7); -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-width: 0px; -fx-border-radius: 5px; -fx-padding: 2px;");
        slider.setCursor(Cursor.HAND);

        return slider;
    }

    private Slider createStyledVolumeSlider(int min, int max, int value) {
        Slider slider = new Slider(min, max, value);
        slider.setShowTickLabels(true);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(5);
        slider.setMinHeight(60);
        slider.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.7); -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-width: 0px; -fx-border-radius: 5px;");
        slider.setCursor(Cursor.HAND);

        return slider;
    }

    private Text createStyledText(String text) {
        Text textLabel = new Text(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-fill: white;");

        return textLabel;
    }

    private VBox createStyledVBox(String text, Slider sliderTrain, Text speedTrain) {
        VBox vBox = new VBox(0);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 0px; -fx-border-radius: 5px; -fx-padding: 2px;");
        vBox.setPrefWidth(100);
        vBox.setPrefHeight(20);
        Label titleLabel = new Label(text);
        titleLabel
                .setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-text-fill: black;");
        speedTrain.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-fill: black;");
        vBox.getChildren().addAll(titleLabel, sliderTrain, speedTrain);

        return vBox;
    }

    private VBox createStyledVolumeVBox(Slider sliderVolume, ImageView volumeImageView) {
        VBox vBox = new VBox(0);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.7); -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 0px; -fx-border-radius: 5px;");
        vBox.setMaxWidth(40);
        vBox.setMaxHeight(100);
        sliderVolume.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.7); -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: Tahoma; -fx-border-color: rgba(0, 0, 0, 0.7); -fx-border-width: 0px; -fx-border-radius: 5px;");
        sliderVolume.setCursor(Cursor.HAND);
        volumeImageView.setFitHeight(20);
        volumeImageView.setFitWidth(20);
        vBox.getChildren().addAll(sliderVolume, volumeImageView);

        return vBox;
    }

    private HBox createStyledSpeedHBox(javafx.scene.Node... children) {
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setMinWidth(880);
        hBox.setMinHeight(105);
        hBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 1); -fx-border-color: rgba(0, 0, 0, 1); -fx-border-width: 0px; -fx-border-radius: 5px;");
        hBox.getChildren().addAll(children);

        return hBox;
    }

    private VBox createStyledVBoxContainer(javafx.scene.Node... children) {
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.8); -fx-border-color: rgba(0, 0, 0, 1); -fx-border-width: 0px; -fx-border-radius: 5px;");
        vBox.setPadding(new Insets(5));
        vBox.setPrefWidth(750);
        vBox.setMaxHeight(90);
        vBox.setLayoutX(0);
        vBox.setLayoutY(500);
        vBox.getChildren().addAll(children);

        return vBox;
    }

    private Image createTrainImage() {
        Random rand = new Random();
        int selectedNumber = rand.nextInt(4) + 1;
        String imageName = "Train" + selectedNumber + ".png";

        return new Image(TrainSimulator2.class.getResourceAsStream(imageName));
    }

    public static void main(String[] args) {
        launch(args);
    }

}

class TrainOne extends Thread {
    private ImageView trainImageView;
    private Path railPath;
    private PathTransition pathTransition;
    private double speed;

    public TrainOne(ImageView traImageView, PathTransition pathTransition, Path railPath, double speed) {
        this.trainImageView = traImageView;
        this.pathTransition = pathTransition;
        this.railPath = railPath;
        this.speed = speed;
    }

    @Override
    public void run() {
        trainImageView.setRotate(0);
        pathTransition.setNode(trainImageView);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        pathTransition.setAutoReverse(false);

        pathTransition.setPath(railPath);
        pathTransition.setRate(speed);
        pathTransition.play();
    }

    public void reset() {
        pathTransition.setDuration(Duration.seconds(10));
        pathTransition.setPath(railPath);
        pathTransition.jumpTo(Duration.ZERO);
        pathTransition.setInterpolator(Interpolator.LINEAR);

        pathTransition.play();
    }

    public void changeDirectionAndPosition() {
        trainImageView.setRotate(180);
        pathTransition.setNode(trainImageView);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        pathTransition.setAutoReverse(false);
        pathTransition.setPath(railPath);
        pathTransition.setRate(speed);
        pathTransition.play();
    }

    public void togglePlayPause() {
        if (pathTransition.getStatus() == PathTransition.Status.RUNNING) {
            pathTransition.pause();
        } else {
            pathTransition.play();
        }
    }
}

class TrainTwo extends Thread {
    private ImageView trainImageView;
    private Path railPath;
    private PathTransition pathTransition;
    private double speed;

    public TrainTwo(ImageView traImageView, PathTransition pathTransition, Path railPath, double speed) {
        this.trainImageView = traImageView;
        this.pathTransition = pathTransition;
        this.railPath = railPath;
        this.speed = speed;
    }

    @Override
    public void run() {
        trainImageView.setRotate(0);
        pathTransition.setNode(trainImageView);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        pathTransition.setAutoReverse(false);

        pathTransition.setPath(railPath);
        pathTransition.setRate(speed);
        pathTransition.play();
    }

    public void reset() {
        pathTransition.setDuration(Duration.seconds(10));
        pathTransition.setPath(railPath);
        pathTransition.jumpTo(Duration.ZERO);
        pathTransition.setInterpolator(Interpolator.LINEAR);

        pathTransition.play();
    }

    public void changeDirectionAndPosition() {
        trainImageView.setRotate(180);
        pathTransition.setNode(trainImageView);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setInterpolator(Interpolator.LINEAR);
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        pathTransition.setAutoReverse(false);
        pathTransition.setPath(railPath);
        pathTransition.setRate(speed);
        pathTransition.play();
    }

    public void togglePlayPause() {
        if (pathTransition.getStatus() == PathTransition.Status.RUNNING) {
            pathTransition.pause();
        } else {
            pathTransition.play();
        }
    }
}