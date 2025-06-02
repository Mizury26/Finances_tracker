package com.example.desktopproject.component;

import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ToggleCustomButton extends HBox {
    private final double WIDTH = 80;
    private final double HEIGHT = 35;
    private final double THUMB_WIDTH = 30;

    private final Rectangle background = new Rectangle();
    private final StackPane thumb = new StackPane();
    private final Label leftLabel = new Label();
    private final Label rightLabel = new Label();

    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final StringProperty leftValue = new SimpleStringProperty("€");
    private final StringProperty rightValue = new SimpleStringProperty("$");
    private final StringProperty label = new SimpleStringProperty();
    private final ObjectProperty<EventHandler<ActionEvent>> onAction = new SimpleObjectProperty<>();

    public ToggleCustomButton() {
        initGraphics();
        registerListeners();
        updateLabels();
    }

    private void initGraphics() {
        setAlignment(Pos.CENTER);

        // Configuration du fond avec couleur bleue
        background.setWidth(WIDTH);
        background.setHeight(HEIGHT);
        background.setArcWidth(HEIGHT);
        background.setArcHeight(HEIGHT);
        background.setFill(Color.web("#3498db"));

        // Option alternative: bleu clair
        // background.setFill(Color.web("#86B5F5"));

        // Configuration du curseur
        thumb.setPrefSize(THUMB_WIDTH, HEIGHT - 4);
        thumb.setStyle("-fx-background-radius: " + (HEIGHT/2) + "px; -fx-background-color: white;");
        thumb.setTranslateX(WIDTH - THUMB_WIDTH - 2);
        thumb.setTranslateY(2);

        // Positionnement des labels avec taille augmentée
        leftLabel.setTextFill(Color.WHITE);
        rightLabel.setTextFill(Color.WHITE);

        // Augmentation de la taille des labels
        leftLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        rightLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        leftLabel.setTranslateX(10);
        rightLabel.setTranslateX(WIDTH - 20);

        // Centrage vertical des labels
        double labelY = HEIGHT / 2 - 9; // Ajustement pour centrer verticalement avec la nouvelle taille
        leftLabel.setTranslateY(labelY);
        rightLabel.setTranslateY(labelY);

        // Création du conteneur principal
        Pane switchContainer = new Pane();
        switchContainer.getChildren().addAll(background, leftLabel, rightLabel, thumb);
        switchContainer.setPrefSize(WIDTH, HEIGHT);

        getChildren().add(switchContainer);

        // Valeur initiale
        label.set(leftValue.get());
    }

    private void registerListeners() {
        thumb.setOnMouseClicked(event -> toggle());
        background.setOnMouseClicked(event -> toggle());

        // Mise à jour de la position lors du changement de sélection
        selected.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                animateThumb(2);
                label.set(rightValue.get());
            } else {
                animateThumb(WIDTH - THUMB_WIDTH - 2);
                label.set(leftValue.get());
            }

            if (onAction.get() != null) {
                onAction.get().handle(new ActionEvent());
            }
        });
    }

    private void animateThumb(double newX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), thumb);
        transition.setToX(newX);
        transition.play();
    }

    private void updateLabels() {
        leftLabel.setText(leftValue.get());
        rightLabel.setText(rightValue.get());
    }

    public void toggle() {
        selected.set(!selected.get());
    }

    // Méthodes complètes pour la propriété onAction
    public final void setOnAction(EventHandler<ActionEvent> value) {
        onAction.set(value);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onAction.get();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }

    public final String getLabel() {
        return label.get();
    }

    public final void setLabel(String labelText) {
        label.set(labelText);
    }

    public final StringProperty labelProperty() {
        return label;
    }

    public void setLeftValue(String value) {
        leftValue.set(value);
        updateLabels();
        if (!selected.get()) {
            label.set(value);
        }
    }

    public void setRightValue(String value) {
        rightValue.set(value);
        updateLabels();
        if (selected.get()) {
            label.set(value);
        }
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }
}