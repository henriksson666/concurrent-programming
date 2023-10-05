/* ***************************************************************
* Autor............: Joao Henrique Silva Pinto
* Matricula........: 202210958
* Inicio...........: 18/08/2023
* Ultima alteracao.: 02/09/2023
* Nome.............: Principal.java
* Funcao...........: Aplicacao JavaFX que simula trens em uma linha ferroviaria
*************************************************************** */

import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Principal extends Application {

  private boolean isPlaying = false,  isReverse = false;
  Pane root;
  int configuration = 1;
  private PathTransition train1Transition;
  private PathTransition train2Transition;

  @Override
  public void start(Stage primaryStage) {
    root = new Pane(); // Create a pane to hold the trains
    Scene scene = new Scene(root, 1300, 690); // Create a scene
    int[] clickCount = { 1 }; // Keep track of the number of clicks

    Image backgroundImage = new Image("Background4.png"); // Create the background image
    BackgroundSize backgroundSize = new BackgroundSize(1300, 690, false, false, false, false);
    BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);

    root.setBackground(new Background(background)); // Set the background image

    int sizeWidth = 70;
    int sizeHeight = 50;
    ImageView trainImageView1 = new ImageView(createImageTrain()); // Create the train 1
    trainImageView1.setFitWidth(sizeWidth);
    trainImageView1.setFitHeight(sizeHeight);

    ImageView trainImageView2 = new ImageView(createImageTrain()); // Create the train 2
    trainImageView2.setFitWidth(sizeWidth);
    trainImageView2.setFitHeight(sizeHeight);

    // Create paths for trains
    Path railPath1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 0, 479, 154, 50, false);
    Path railPath2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 0, 325, 154, 50, false);

    // Set the initial rotation and translation for clickCount[0] == 1
    trainImageView1.setRotate(0);
    trainImageView2.setRotate(0);
    trainImageView1.setTranslateX(-35);
    trainImageView1.setTranslateY(451);
    trainImageView2.setTranslateX(-35);
    trainImageView2.setTranslateY(301);

    // Set up the initial configuration
    createRails(clickCount[0], trainImageView1, trainImageView2, isReverse);

    // Create the buttons
    Button resetButton = createStyledButton("Reset");
    resetButton.setStyle("-fx-background-color: rgba(255, 221, 8, 0.7);");
    resetButton.setTextFill(Color.BLACK);
    resetButton.cursorProperty().set(Cursor.HAND);

    Button playPauseButton = createStyledButton("Play");
    playPauseButton.setStyle("-fx-background-color: rgb(115, 183, 50);");
    playPauseButton.cursorProperty().set(Cursor.HAND);

    Button changeDirectionButton = createStyledRestartButton("\tRestart \nChange Direction");
    changeDirectionButton.setStyle("-fx-background-color: rgba(212, 175, 55, 0.7);");
    changeDirectionButton.cursorProperty().set(Cursor.HAND);

    // Create the sliders
    Slider sliderSpeedTrain1 = createStyledSlider(0, 10, 0);
    Slider sliderSpeedTrain2 = createStyledSlider(0, 10, 0);

    // Create the speed labels
    Text speedTextTrain1 = createStyledText("Speed: 0 km/h");
    Text speedTextTrain2 = createStyledText("Speed: 0 km/h");

    // Create the speed boxes
    VBox trainBox1 = createStyledVBox("Train 1", sliderSpeedTrain1, speedTextTrain1);
    VBox trainBox2 = createStyledVBox("Train 2", sliderSpeedTrain2, speedTextTrain2);

    HBox speedHBox = createStyledHBox(trainBox1, trainBox2);
    speedHBox.setStyle("-fx-background-color: rgb(117,68,16); -fx-border-width: 10; -fx-pref-height: 50;");

    // Create the control box to add the buttons, sliders and speed boxes
    HBox controlBox = createStyledHBox(changeDirectionButton, resetButton, playPauseButton, speedHBox);
    controlBox.setStyle("-fx-background-color: rgba(255, 212, 161, 1); -fx-padding:5px;");

    // Add the control box to the the pane
    VBox bottomPane = createStyledVBoxContainer(controlBox);
    bottomPane.translateXProperty().bind(scene.widthProperty().subtract(bottomPane.widthProperty()));
    bottomPane.translateYProperty().set(82);

    // Create the welcome screen
    Stage welcomeStage = new Stage();
    Pane welcomePane = initialPane(welcomeStage);
    Scene welcomeScene = new Scene(welcomePane, 500, 500);
    welcomeStage.setScene(welcomeScene);
    welcomeStage.setTitle("Welcome");
    welcomeStage.getIcons().add(new Image("railway.png"));
    welcomeStage.initModality(Modality.APPLICATION_MODAL);
    welcomeStage.setResizable(false);
    welcomeStage.initStyle(StageStyle.UNDECORATED);
    welcomeStage.centerOnScreen();
    welcomeStage.show();

    root.getChildren().addAll(bottomPane); // Add the bottomPane to the root pane

    // Set the main stage
    primaryStage.setScene(scene);
    primaryStage.setTitle("Train Simulator");
    primaryStage.setResizable(false);
    primaryStage.getIcons().add(new Image("railway.png"));
    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    pause.setOnFinished(e -> {
      primaryStage.show();
      welcomeStage.close();
    });
    pause.play();

    /* Event handlers */
    // Reset the trains to the beginning
    resetButton.setOnAction(e -> {
      if (sliderSpeedTrain1.getValue() > 0) { // Begin of 1st condition for train1
        train1Transition.setDuration(Duration.seconds(10));
        train1Transition.setPath(railPath1); // Set the Path for train1
        train1Transition.jumpTo(Duration.ZERO); // Reset position to the beginning
        train1Transition.setInterpolator(Interpolator.LINEAR);
        train1Transition.play(); // Start from the beginning
      } else if (sliderSpeedTrain1.getValue() == 0) { // End of 1st condition and begin of 2nd condition
        train1Transition.jumpTo(Duration.ZERO);
        train1Transition.pause();
      } // end of 2nd condition

      if (sliderSpeedTrain2.getValue() > 0) { // Begin of 1st condition for train2
        train2Transition.setDuration(Duration.seconds(10));
        train2Transition.setPath(railPath2); // Set the Path for train2
        train2Transition.jumpTo(Duration.ZERO);
        ; // Reset position to the beginning
        train2Transition.setInterpolator(Interpolator.LINEAR);
        train2Transition.play(); // Start from the beginning
      } else if (sliderSpeedTrain2.getValue() == 0) { // End of 1st condition and begin of 2nd condition
        train2Transition.jumpTo(Duration.ZERO);
        train2Transition.pause();
      } // end of 2nd condition
    });

    // Change the direction of the trains and update the paths
    changeDirectionButton.setOnAction(e -> {
      sliderSpeedTrain2.setValue(0);
      sliderSpeedTrain1.setValue(0);
      clickCount[0] = clickCount[0] % 4 + 1; // Increment the click count

      // Set the initial position and direction for each train
      if (clickCount[0] == 1) { // Begin of 1st condition
        trainImageView1.setTranslateX(-35);
        trainImageView1.setTranslateY(451);
        trainImageView2.setTranslateX(-35);
        trainImageView2.setTranslateY(301);
        isReverse = false;
      } else if (clickCount[0] == 2) { // End of 1st condition and begin of 2nd condition
        trainImageView1.setTranslateX(-35);
        trainImageView1.setTranslateY(451);
        trainImageView2.setTranslateX(1275);
        trainImageView2.setTranslateY(301);
        isReverse = true;
      } else if (clickCount[0] == 3) { // End of 2nd condition and begin of 3rd condition
        trainImageView1.setTranslateX(1275);
        trainImageView1.setTranslateY(451);
        trainImageView2.setTranslateX(-35);
        trainImageView2.setTranslateY(301);
        isReverse = true;
      } else if (clickCount[0] == 4) { // End of 3rd condition and begin of 4th condition
        trainImageView1.setTranslateX(1275);
        trainImageView1.setTranslateY(451);
        trainImageView2.setTranslateX(1275);
        trainImageView2.setTranslateY(301);
        trainImageView1.setRotate(180);
        /* trainImageView2.setRotate(180); */
        isReverse = true;
      } // End of 4th condition

      trainImageView1.setImage(createImageTrain());
      trainImageView2.setImage(createImageTrain());

      updateButton(changeDirectionButton, clickCount[0]); // Update the button color

      createRails(clickCount[0], trainImageView1, trainImageView2, isReverse); // Create the rails and update the
                                                                             // path transitions
    });

    // Play or pause the trains
    playPauseButton.setOnAction(e -> {
      if (isPlaying) { // Begin of 1st condition
        playPauseButton.setText("Play");

        // Pause transitions
        if (train1Transition != null) {
          train1Transition.pause();
          train2Transition.pause();
        }

      } else { // End of 1st condition and begin of 2nd condition
        // Verify the state of the sliders
        boolean train1Running = sliderSpeedTrain1.getValue() > 0;
        boolean train2Running = sliderSpeedTrain2.getValue() > 0;

        // Play transitions
        if (train1Running) {
          train1Transition.play();
        }

        // Play transitions
        if (train2Running) {
          train2Transition.play();
        }

        // Update the button text
        playPauseButton.setText("Pause");
      }

      isPlaying = !isPlaying; // Toggle play/pause state
    });

    // Disable the buttons when the trains are playing
    if (!isPlaying) {
      changeDirectionButton.disableProperty().bind(playPauseButton.textProperty().isEqualTo("Pause"));
      resetButton.disableProperty().bind(playPauseButton.textProperty().isEqualTo("Play"));
    }

    // Update the button color
    playPauseButton.textProperty().addListener((observable, oldValue, newValue) -> {
      if ("Play".equals(newValue)) { // Begin of 1st condition
        playPauseButton.setStyle("-fx-background-color: green;");
      } else { //
        playPauseButton.setStyle("-fx-background-color: rgb(217, 54, 39);");
      }
    });

    // Update the speed of the train 1
    sliderSpeedTrain1.valueProperty().addListener((observable, oldValue, newValue) -> {
      train1Transition.setRate(newValue.doubleValue() / 50);

      // Play transitions
      if (isPlaying) {
        train1Transition.play();
      }

      // Format the speed value shown in the label
      String formattedValue = String.format("%.0f", newValue.doubleValue() * 10);
      speedTextTrain1.setText("Speed: " + formattedValue + " km/h");
    });

    // Update the speed of the train 2
    sliderSpeedTrain2.valueProperty().addListener((observable, oldValue, newValue) -> {
      train2Transition.setRate(newValue.doubleValue() / 50);

      // Play transitions
      if (isPlaying) {
        train2Transition.play();
      }

      // Format the speed value shown in the label
      String formattedValue = String.format("%.0f", newValue.doubleValue() * 10);
      speedTextTrain2.setText("Speed: " + formattedValue + " km/h");
    });
  }

  /**
   * Creates and returns an Image object with a random train image
   * 
   * This method randomly selects a train image from a set of images
   * numbers from 1 to 4 (inclusive) and loads it into an Image object.
   * The images are expected to be named "TrainX.png", where X is the
   * selected number.
   * 
   * @return An Image object representing a train.
   */
  private Image createImageTrain() {
    Random rand = new Random();
    int selectedNumber = rand.nextInt(4) + 1;

    String imageName = "Train" + selectedNumber + ".png";

    return new Image(Principal.class.getResourceAsStream(imageName));
  }

  /**
   * Creates and returns a styled Button with the specified text.
   * 
   * This method creates a JavaFX button with the given text and applies
   * various styles to it, such as background color, text color, font size,
   * and drop shadow. The button will have a fixed width and height, a white
   * semi-transparent backgroound with white text, and a drop shadow effect.
   * 
   * @param text The text to be displayed on the button.
   * @return A styled Button with the specified text.
   */
  private Button createStyledButton(String text) {
    Button button = new Button(text);
    button.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");
    button.setTextFill(Color.WHITE);
    button.setPrefWidth(80);
    button.setPrefHeight(50);

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(2.0);
    dropShadow.setOffsetX(3.0);
    dropShadow.setOffsetY(3.0);
    dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
    button.setEffect(dropShadow);
    button.setStyle("-fx-font-family: Tahoma; -fx-font-size: 14px;");

    return button;
  }

  /**
   * Creates and returns a styled Button for restarting with the specified text.
   * 
   * This method creates a JavaFX Button with the given text and applies
   * various styles to it, such as background color, text color, font size.
   * The button has a golden semi-transparent background with black text, centered
   * text, and a drop shadow effect.
   * 
   * @param text The text to be displayed on the restart Button.
   * @return A styled Button for restarting with the specified text.
   */
  private Button createStyledRestartButton(String text) {
    Button button = new Button(text);
    button.setStyle("-fx-background-color: rgba(212, 175, 55, 0.7);");
    button.setTextFill(Color.BLACK);
    button.setAlignment(Pos.CENTER);
    button.setWrapText(true);
    button.setMinWidth(70);
    button.setPrefHeight(50);

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(2.0);
    dropShadow.setOffsetX(3.0);
    dropShadow.setOffsetY(3.0);
    dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
    button.setEffect(dropShadow);
    button.setStyle("-fx-font-family: Tahoma;-fx-font-size: 12px;");

    return button;
  }

  /**
   * Creates and returns a styled Slider with the specified parameters.
   * 
   * This method creates a JavaFX Slider with the given minimum, maximum,
   * and initial value, and applies various styles to it, such as background
   * color, thumb color, track color, tick marks and labels color, and tick.
   * The slider will have a semi-transparent white background, a blue thumb,
   * a gray track, red tick marks and labels, and a tick unit of 2.
   * 
   * @param min   The minimum value of the Slider.
   * @param max   The maximum value of the Slider.
   * @param value The initial value of the Slider.
   * @return A styled Slider with the specified parameters.
   */
  private Slider createStyledSlider(double min, double max, double value) {
    Slider slider = new Slider(min, max, value);

    slider.setMajorTickUnit(2);
    slider.setShowTickLabels(true);
    // Customize the background color
    slider.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");

    // Customize the thumb (dragging handle) color
    slider.setStyle("-fx-thumb-color: #3498db;");

    // Customize the track (slider bar) color
    slider.setStyle("-fx-track-color: #bdc3c7;");

    // Customize the tick marks and labels color
    slider.setStyle("-fx-tick-label-fill: #e74c3c;");

    // Customize the tick marks and labels font size
    slider.setStyle("-fx-tick-label-font-size: 12px;");

    return slider;
  }

  /**
   * Creates and returns a styled Text element with the specified text.
   * 
   * This method creates a JavaFX Text element with the given text and applies
   * various styles to it, such as font family and font size. The text will
   * have a font family of Tahoma and a font size of 16px.
   * 
   * @param text The text to be displayed on the Text element.
   * @return A styled Text element with the specified text content.
   */
  private Text createStyledText(String text) {
    Text textLabel = new Text(text);
    textLabel.setStyle("-fx-font-family: Tahoma;-fx-font-size: 16px;");
    return textLabel;
  }

  /**
   * Creates and returns a styled VBox containing a titles, a Slider, and Text
   * elements.
   * 
   * This method creates a JavaFX VBox containing a title, a Slider, and a Text
   * element
   * and applies various styles to it, such as background color, border color,
   * border
   * width, border radius, padding, and spacing. The VBox will have a
   * semi-transparent
   * background, a border with a width of 3px, a border radius of 5px, a border
   * color
   * of rgb(117,68,16), and a padding of 2px.
   * 
   * @param title  The title to be displayed at the top of the VBox.
   * @param slider The Slider element to be displayed in the middle of the VBox.
   * @param text   The Text element to be displayed at the bottom of the VBox.
   * @return A styled VBox containing a titles, a Slider, and Text elements.
   */
  private VBox createStyledVBox(String title, Slider slider, Text text) {
    VBox vBox = new VBox(0);
    vBox.setAlignment(Pos.CENTER);
    vBox.setStyle(
        "-fx-background-color: rgba(255, 212, 161, 1); -fx-background-radius: 8px; -fx-border-width: 3px; -fx-border-radius: 5px; -fx-border-color: rgb(117,68,16); -fx-padding:2px;");
    vBox.setPrefWidth(180);
    vBox.setPrefHeight(20);
    slider.setStyle("-fx-background-color: rgba(255, 212, 161, 0.7);");
    Label titleLabel = new Label(title);
    titleLabel.setStyle("-fx-font-family: Tahoma;-fx-font-size: 16px;");
    vBox.getChildren().addAll(titleLabel, slider, text);
    return vBox;
  }

  /**
   * Creates and returns a styled HBox containing the specified children.
   * 
   * This method creates a JavaFX HBox containing the specified children and
   * applies various styles to it, such as background color, padding, and spacing.
   * The HBox will have a semi-transparent background, a padding of 5px, and a
   * spacing of 5px.
   * 
   * @param children The child nodes to be included in the HBox.
   * @return A styled HBox containing the specified child nodes.
   */
  private HBox createStyledHBox(javafx.scene.Node... children) {
    HBox hBox = new HBox(5);
    hBox.setAlignment(Pos.CENTER);
    hBox.setPadding(new Insets(5));
    hBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");
    hBox.getChildren().addAll(children);
    return hBox;
  }

  /**
   * Creates and returns a styled VBox containing the specified child nodes.
   * 
   * Creates a JavaFX VBox containing the specified child nodes and applies
   * various styles to it, such as background color, border color, border
   * width, border radius, padding, and spacing. The VBox will have a
   * semi-transparent background, a border with a width of 3px, a border
   * radius of 5px, a border color of rgb(117,68,16), and a padding of 5px.
   * 
   * @param children The child nodes to be included in the VBox container.
   * @return A styled VBox containing the specified child nodes.
   */
  private VBox createStyledVBoxContainer(javafx.scene.Node... children) {
    VBox vBox = new VBox(10);
    vBox.setAlignment(Pos.CENTER);
    vBox.setStyle("-fx-background-color: rgb(117,68,16); -fx-background-radius: 5px; -fx-border-radius: 5px");
    vBox.setPadding(new Insets(5));
    vBox.setPrefWidth(750);
    vBox.setMaxHeight(90);
    vBox.setLayoutX(0);
    vBox.setLayoutY(500);
    vBox.setAlignment(Pos.CENTER);
    vBox.getChildren().addAll(children);
    return vBox;
  }

  /**
   * Updates the visual style of a Button based on a given integer value.
   * 
   * This method modifies the appearance of the provided Button by changing its
   * background color.
   * The background color is determined by the given integer value, which can
   * range from 1 to 4 (inclusive). The background color will be:
   * - rgba(212, 175, 55, 0.7) for 1 (a golden semi-transparent background).
   * - rgba(192, 192, 192, 0.7) for 2 (a silver semi-transparent background)
   * - rgba(181, 166, 66, 0.7) for 3 (a bronze semi-transparent background).
   * - rgba(227, 222, 219, 0.7) for 4 (a light gray semi-transparent background).
   * 
   * @param changeDirectionButton The Button whose style needs to be updated.
   * @param i                     An integer value indicating the desired
   *                              background color.
   */
  private void updateButton(Button changeDirectionButton, int i) {
    if (i == 1) {
      changeDirectionButton.setStyle("-fx-background-color: rgba(212, 175, 55, 0.7);");
    } else if (i == 2) {
      changeDirectionButton.setStyle("-fx-background-color: rgba(192, 192, 192, 0.7);");
      ;
    } else if (i == 3) {
      changeDirectionButton.setStyle("-fx-background-color: rgba(181, 166, 66, 0.7);");
    } else if (i == 4) {
      changeDirectionButton.setStyle("-fx-background-color: rgba(227, 222, 219, 0.7);");
    }
  }

  /**
   * Creates and configures a PathTransition animation for a train image along a
   * given rail path.
   * 
   * This method creates a JavaFX PathTransition animation for a train image node
   * to
   * move along a specified rail path. The animation will be configured to move
   * the
   * train image node along the rail path in a linear fashion, with a duration of
   * 10
   * seconds, and to repeat indefinitely. The animation will be configured to not
   * auto-reverse.
   * 
   * @param trainImage The Node representing the train image.
   * @param rail       The Path representing the rail along which the train image
   *                   will move.
   * @return A configured PathTransition animation for a train image along a given
   *         rail path.
   */
  private PathTransition createPathTransition(Node trainImage, Path rail) {
    PathTransition pathTransition = new PathTransition();
    trainImage.setRotate(0);

    // pathTransition.setDuration(Duration.seconds(10));
    pathTransition.setNode(trainImage);
    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
    pathTransition.setInterpolator(Interpolator.LINEAR);
    pathTransition.setCycleCount(PathTransition.INDEFINITE);
    pathTransition.setAutoReverse(false);

    // Set initial path
    pathTransition.setPath(rail);

    return pathTransition;
  }

  /**
   * Creates and configures rail paths and path transitions for two two trains
   * based on a specified configuration.
   * 
   * This method generates rail paths and configures path transitions for two
   * trains
   * based on a specified configuration. The configuration can be 1, 2, 3, or 4,
   * and
   * determines the initial position and direction of the trains. The trains will
   * move along the rail paths in a linear fashion, with a duration of 10 seconds,
   * and will repeat indefinitely. The trains will not auto-reverse.
   * 
   * @param configuration    An integer representing the desired rail
   *                         configuration (1, 2, 3, or 4).
   * @param train1           A ImageView representing the first train.
   * @param train2           A ImageView representing the second train.
   * @param startTransitions A boolean indicating whether the path transitions
   *                         should be started.
   */
  public void createRails(int configuration, ImageView train1, ImageView train2, boolean startTransitions) {
    Path rail1 = null, rail2 = null;

    // Remove the trains from the root pane if they are already there
    root.getChildren().removeAll(train1, train2);
    Boolean railReverse1 = false,  railReverse2 = false;

    if (configuration == 1) {
      rail1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 0, 476, 154, 50, false);
      rail2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 0, 324, 154, 50, false);
    } else if (configuration == 2) {
      rail1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 0, 476, 154, 50, false);
      rail2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 1310, 324, 154, 50, true);
      railReverse2 = true;
    } else if (configuration == 3) {
      rail1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 1310, 476, 154, 50, true);
      rail2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 0, 324, 154, 50, false);
      railReverse1 = true;
    } else if (configuration == 4) {
      rail1 = createPath(new double[] { 0, 30, 0, -30, 0, 30, 0, -30, 0 }, 1310, 475, 154, 50, true);
      rail2 = createPath(new double[] { 0, -30, 0, 30, 0, -30, 0, 30, 0 }, 1310, 324, 154, 50, true);
      railReverse1 = true;
      railReverse2 = true;
    }

    // Add the trains back to the root pane
    root.getChildren().addAll(train1, train2);

    // Create and update the path transitions
    train1Transition = createPathTransition(train1, rail1);
    train2Transition = createPathTransition(train2, rail2);

    // Rotate the trains if necessary
    if (railReverse1 && railReverse2) {
      train1.setRotate(180);
      train2.setRotate(180);
    } else if (railReverse1) {
      train1.setRotate(180);
    } else if (railReverse2) {
      train2.setRotate(180);
    }

    // Update the path transitions
    train1Transition.setPath(rail1);
    train2Transition.setPath(rail2);
  }

  /**
   * Creates a Path object with specified angles, starting point, length, and
   * number of intermediate points.
   * 
   * This method generates a JavaFX Path object that represents a curved path
   * based on the provided parameters.
   * The path is defined by a series of angles, a starting point (x, y), a length
   * for the segments between angles,
   * and the number of intermediate points to create smooth curves. The path is
   * created in a counter-clockwise
   * direction by default, but can be reversed by setting the reverse parameter to
   * true.
   * 
   * @param angles                An array of angles (in degrees) specifying the
   *                              direction of path segments.
   * @param x                     The starting x coordinate of the path.
   * @param y                     The starting y coordinate of the path.
   * @param length                The length of each path segments.
   * @param numIntermediatePoints The number of intermediate points to create
   *                              smooth curves.
   * @param reverse               A boolean indicating whether the path should be
   *                              created in a clockwise direction.
   * @return A Path object representing the generated curved path.
   */
  private Path createPath(double[] angles, double x, double y, double length, int numIntermediatePoints,
      boolean reverse) {
    Path path = new Path();
    path.setStroke(Color.BLUE);
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
        double y1 = y - length * Math.sin(Math.toRadians(angle));

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

  /**
   * Creates and returns an initial JavaFX Pane with a background image for the
   * application's primary stage.
   * 
   * This method sets up an initial JavaFX Pane to be displayed in the primary
   * stage of the application.
   * It includes a background image specified by the file "cover.png".
   * 
   * @param primaryStage The primary stage of the JavaFX application.
   * @return An initial Pane with a background image.
   */
  private Pane initialPane(Stage primaryStage) {

    Pane pane = new Pane();

    ImageView backgroundImage = new ImageView(new Image("cover.png"));
    BackgroundSize backgroundSize = new BackgroundSize(500, 500, false, false, false, false);
    backgroundImage.setFitWidth(500);
    backgroundImage.setFitHeight(500);
    Background background = new Background(new BackgroundImage(backgroundImage.getImage(),
        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize));
    pane.setBackground(background);

    return pane;
  }

  /**
   * The entry point for launching the JavaFX application.
   * 
   * This method serves as the entry point for starting the JavaFX application.
   * It invokes the JavaFX application's launch method with the provided
   * command-line arguments, if any.
   * 
   * @param args The command-line arguments provided to the application (if any).
   */
  public static void main(String[] args) {
    launch(args);
  }
}