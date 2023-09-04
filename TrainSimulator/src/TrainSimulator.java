import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
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
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

}