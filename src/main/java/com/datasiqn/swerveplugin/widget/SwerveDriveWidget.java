package com.datasiqn.swerveplugin.widget;

import com.datasiqn.swerveplugin.ModuleLocation;
import com.datasiqn.swerveplugin.ModuleState;
import com.datasiqn.swerveplugin.SwerveSpeeds;
import com.datasiqn.swerveplugin.data.SwerveDriveData;
import edu.wpi.first.shuffleboard.api.components.CurvedArrow;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.Arrays;

@Description(name = "SwerveDrive", dataTypes = SwerveDriveData.class)
@ParametrizedController("SwerveDrive.fxml")
public class SwerveDriveWidget extends SimpleAnnotatedWidget<SwerveDriveData> {
    @FXML
    private StackPane root;

    @FXML
    private StackPane drivetrain;

    @FXML
    private Polygon facingArrow;

    @FXML
    private Polygon shape;

    @FXML
    private Pane velocityArrow;

    @FXML
    private StackPane modules;

    @FXML
    private void initialize() {
        dataProperty().addListener(((observableValue, oldData, newData) -> {
            double headingRadians = newData.getHeadingRadians();
            drivetrain.setRotate(-Math.toDegrees(headingRadians));

            SwerveSpeeds speeds = newData.getSpeeds();
            double speedsMagnitude = speeds.magnitude() * getScaleFactor(root.getWidth(), root.getHeight()) / newData.getMaxVelocity();
            if (speedsMagnitude >= 0.1) {
                Shape velocityArrowShape = CurvedArrow.createStraight(speedsMagnitude, speeds.angleRadians(), 0, 20);
                velocityArrowShape.setStyle("-fx-fill: -swatch-100");
                velocityArrow.getChildren().setAll(velocityArrowShape);
            } else {
                velocityArrow.getChildren().clear();
            }

            onResize(root.getWidth(), root.getHeight());
        }));

        root.widthProperty().addListener((observable, oldWidth, newWidth) -> onResize(newWidth.doubleValue(), root.getHeight()));
        root.heightProperty().addListener((observable, oldHeight, newHeight) -> onResize(root.getWidth(), newHeight.doubleValue()));
    }

    private void onResize(double width, double height) {
        double scaleFactor = getScaleFactor(width, height);

        ObservableList<Node> modulesChildren = modules.getChildren();
        ObservableList<Double> shapePoints = shape.getPoints();

        shapePoints.clear();
        modulesChildren.clear();

        ModuleState[] states = getData().getStates();
        ModuleLocation[] locations = normalizeLocations(Arrays.stream(states).map(ModuleState::location).toArray(ModuleLocation[]::new), scaleFactor);

        for (int i = 0; i < states.length; i++) {
            ModuleState state = states[i];
            ModuleLocation normalizedLocation = locations[i];

            double x = -normalizedLocation.y();
            double y = -normalizedLocation.x();
            shapePoints.addAll(x, y);

            modulesChildren.add(createModuleGraphic(x, y, state.velocity() * 100 / getData().getMaxVelocity(), state.angleRadians()));
        }
    }

    @Override
    public Pane getView() {
        return root;
    }

    private static StackPane createModuleGraphic(double x, double y, double speed, double rotationRadians) {
        double rotationDegrees = Math.toDegrees(rotationRadians);

        Rectangle module = new Rectangle(30, 30);
        module.setStyle("-fx-fill: -swatch-light-gray");

        Rectangle wheel = new Rectangle(10, 30);
        wheel.setStyle("-fx-fill: -swatch-gray");
        wheel.setRotate(-rotationDegrees);

        Shape velocityArrow = CurvedArrow.createStraight(speed, -rotationRadians - Math.PI / 2, 0, 10);
        velocityArrow.setStyle("-fx-fill: -confirmation-color-confirmed");
        velocityArrow.setVisible(Math.abs(speed) >= 0.1);

        Pane velocityArrowPane = new Pane(velocityArrow);
        velocityArrowPane.setMaxWidth(0);
        velocityArrowPane.setMaxHeight(0);

        Polygon facing = new Polygon(
                -5, 5,
                0, -5,
                5, 5
        );
        facing.setStyle("-fx-fill: -swatch-200");
        facing.setRotate(-rotationDegrees);

        StackPane pane = new StackPane(module, wheel, velocityArrowPane, facing);
        pane.setTranslateX(x);
        pane.setTranslateY(y);
        return pane;
    }

    private static ModuleLocation[] normalizeLocations(ModuleLocation[] locations, double scaleFactor) {
        if (locations.length == 0) return locations;

        double max = Double.NEGATIVE_INFINITY;
        for (ModuleLocation location : locations) {
            max = Math.max(location.x(), max);
            max = Math.max(location.y(), max);
        }

        ModuleLocation[] normalized = new ModuleLocation[locations.length];
        for (int i = 0; i < locations.length; i++) {
            normalized[i] = locations[i].multiply(scaleFactor / max);
        }

        return normalized;
    }

    private static double getScaleFactor(double width, double height) {
        return Math.min(width, height) / 2 - 100;
    }
}
