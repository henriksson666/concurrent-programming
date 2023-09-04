import java.util.Random;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.stage.Stage;

public class TrainSimulator extends Application {

    private boolean isPlaying = false;
    private boolean isReverse = false;
    Pane root;
    int configuration = 1;
    private PathTransition train1Transition, train2Transition;

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Scene scene = new Scene (root, 1300, 690);
        int[] clickCount = {1};

        Image backgroundImage = new Image("background4.png");
        BackgroundSize backgroundSize = new BackgroundSize(1300, 690, false, false, false, false);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
        root.setBackground(new Background(background));

        int sizeWeight = 70;
        int sizeHeight = 50;

        ImageView trainImageView1 = new ImageView(createImageTrain());
        trainImageView1.setFitWidth(sizeWeight);
        trainImageView1.setFitHeight(sizeHeight);

        ImageView trainImageView2 = new ImageView(createImageTrain());
        trainImageView2.setFitWidth(sizeWeight);
        trainImageView2.setFitHeight(sizeHeight);

        Path railPath1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 0, 479, 154, 50, false);
        Path railPath2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 0, 325, 154, 50, false);
        
        trainImageView1.setRotate(0);
        trainImageView2.setRotate(0);
        trainImageView1.setTranslateX(-35);
        trainImageView1.setTranslateY(451);
        trainImageView2.setTranslateX(-35);
        trainImageView2.setTranslateY(301);

        createRails(clickCount[0], trainImageView1, trainImageView2, isReverse);
    }

    private void createRails(int configuration, ImageView train1, ImageView train2, boolean isReverse2) {
        Path rail1 = null, rail2 = null;
        Boolean isReverseTrain1 = false, isReverseTrain2 = false;

        root.getChildren().removeAll(train1, train2);

        if (configuration == 1) {
            rail1 = createPath(new double[]{0, 30, 0, -30, 0, 30, 0, -30, 0}, 0, 476, 154, 50, false);
            rail2 = createPath(new double[]{0, -30, 0, 30, 0, -30, 0, 30, 0}, 0, 324, 154, 50, false);
        } else if (configuration == 2) {
            rail1 = createPath(new double[]{0, 30, 0, -30, 0, 30, 0, -30, 0}, 0, 476, 154, 50, false);
            rail2 = createPath(new double[]{0, -30, 0, 30, 0, -30, 0, 30, 0}, 1310, 324, 154, 50, true);
            isReverseTrain2 = true;
        } else if (configuration == 3) {
            rail1 = createPath(new double[]{0, 30, 0, -30, 0, 30, 0, -30, 0}, 1310, 476, 154, 50, true);
            rail2 = createPath(new double[]{0, -30, 0, 30, 0, -30, 0, 30, 0}, 0, 324, 154, 50, false);
            isReverseTrain1 = true;
        } else if (configuration == 4) {
            rail1 = createPath(new double[]{0, 30, 0, -30, 0, 30, 0, -30, 0}, 1310, 475, 154, 50, true);
            rail2 = createPath(new double[]{0, -30, 0, 30, 0, -30, 0, 30, 0}, 1310, 324, 154, 50, true);
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
        }else if (isReverseTrain2) {
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

    private Path createPath(double[] angles, double x, double y, double length, int numIntermediatePoints, boolean isReverse) {
        Path path = new Path();
        path.setStroke(Color.BLUE);
        path.getElements().add(new MoveTo(x, y));

        if (isReverse) {
            for (int i = angles.length -1; i >= 0; i--) {
                double angle = angles[i];
                double x1 = x + length * Math.cos(Math.toRadians(angle));
                double y1 = y + length * Math.sin(Math.toRadians(angle));

                if (i != angles.length -1){
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

                if (i != angles.length -1) {
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

    private Image createImageTrain() {
        Random rand = new Random();
        int selectedNumber = rand.nextInt(4) + 1;
        String imageName = "train" + selectedNumber + ".png";

        return new Image(TrainSimulator.class.getResourceAsStream(imageName));
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

}