# concurrent-programming
The `Train` class is a JavaFX application simulating two trains moving on configurable rail paths with velocity control.

Here's a detailed README file for your `Train` class:

# Train Class

The `Train` class is a JavaFX application that simulates the movement of two trains along predefined rail paths on a graphical user interface (GUI). This application allows users to control the behavior of the trains, change their paths, adjust their velocities, and more.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Usage](#usage)
- [Application Features](#application-features)
- [Class Structure](#class-structure)
- [Important Methods](#important-methods)
- [Event Handling](#event-handling)
- [Contributors](#contributors)
- [License](#license)

## Overview

The `Train` class is a JavaFX application developed to provide a visual representation of trains moving along predefined rail paths. It utilizes JavaFX for the GUI components and animations. The application creates a graphical environment with two trains represented as rectangles, and these trains move along specified paths using path transitions.

## Prerequisites

Before running this application, you should have the following:

- **Java Development Kit (JDK)**: Ensure that you have Java JDK 8 or later installed on your system.

## Usage

To use the `Train` application, follow these steps:

1. **Compile**: Compile the Java source code, including the `Train.java` file, using the Java compiler.

   ```bash
   javac Train.java
   ```

2. **Run**: Execute the compiled Java application.

   ```bash
   java Train
   ```

3. **GUI**: The application's GUI will appear, allowing you to interact with the simulation.

## Application Features

- **Train Animation**: The application features two trains represented as rectangles that move along predefined paths.
- **Configuration Changes**: Users can change the configuration of the rail paths to create different scenarios.
- **Velocity Control**: You can adjust the velocity of each train independently using sliders.
- **Play/Pause**: The "Play" button allows you to start or pause the animation.
- **Reset/Restart**: Buttons are available to reset the simulation or restart from the beginning.
- **Change Direction**: The "Change Direction" button lets you change the direction of the trains on the rail paths.

## Class Structure

The `Train` class extends the `Application` class from JavaFX and provides the main entry point for the application. Key components and methods include:

- **GUI Setup**: Configuration and setup of the graphical user interface, including trains, rail paths, buttons, sliders, and labels.
- **Path Transitions**: Creation and management of path transitions for the trains' movement.
- **Event Handling**: Handling of user interactions such as button clicks, slider adjustments, and path configuration changes.
- **Path Calculation**: Methods to calculate angles and paths based on user-defined parameters.

## Important Methods

### `createTrain(double x, double y, Color color)`

This method creates and returns a rectangular train shape with the specified position (x, y) and color.

### `createPathTransition(Shape trainImage, Path rail)`

Creates a path transition for a given train image to move along a specified rail path.

### `createRails(int configuration, Rectangle train1, Rectangle train2, boolean startTransitions)`

Generates rail paths based on the chosen configuration and restarts path transitions if required.

### `calculateAngle(Path path, double t)`

Calculates the angle of rotation for a train based on its position along a path at time t.

### `createPath(double[] angles, double x, double y, double length, int numIntermediatePoints, boolean reverse)`

Creates a custom path based on provided angles and other parameters.

### `reversePath(Path originalPath)`

Reverses the elements of a given path to change the direction of movement.

## Event Handling

The application handles various user interactions through event handlers:

- **Reset Button**: Resets the animation and repositions the trains.
- **Restart Button**: Restarts the animation from the beginning.
- **Change Direction Button**: Changes the configuration of the rail paths.
- **Play/Pause Button**: Toggles between playing and pausing the animation.
- **Slider Controls**: Adjust the velocity of each train.

## Contributors

- João Henrique Silva Pinto: https://github.com/henriksson666

## License

This project is licensed under the [MIT License](LICENSE). You are free to use, modify, and distribute it as per the terms of the license.

Feel free to customize and enhance this README as needed, and make sure to include your actual GitHub profile link and any other relevant contributors if you are collaborating on this project with others.
