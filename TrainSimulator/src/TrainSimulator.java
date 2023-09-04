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