import java.util.Random;

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
import javafx.scene.paint.Color;
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
            }
        }
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