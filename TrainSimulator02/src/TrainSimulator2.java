import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.stage.Stage;
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