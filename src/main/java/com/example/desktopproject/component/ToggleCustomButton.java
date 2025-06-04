package com.example.desktopproject.component;

import com.example.desktopproject.model.Monetary;
import javafx.animation.TranslateTransition;
import javafx.beans.property.*;
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

    // Couleurs pour les deux états
    private Color leftStateColor = Color.web("#2a9e93");  // Couleur pour l'état gauche
    private Color rightStateColor = Color.web("#1c6b63");  // Couleur pour l'état droit

    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final StringProperty leftValue = new SimpleStringProperty("€");
    private final StringProperty rightValue = new SimpleStringProperty("$");
    private final StringProperty selectedLabel = new SimpleStringProperty();
    private final ObjectProperty<EventHandler<ActionEvent>> onAction = new SimpleObjectProperty<>();

    public ToggleCustomButton() {
        initGraphics();
        registerListeners();
        updateLabels();
    }

    private void initGraphics() {
        selectedLabel.set(Monetary.unit);

        setAlignment(Pos.CENTER);

        // Configuration du fond avec couleur initiale
        background.setWidth(WIDTH);
        background.setHeight(HEIGHT);
        background.setArcWidth(HEIGHT);
        background.setArcHeight(HEIGHT);
        background.setFill(leftStateColor);  // Couleur initiale

        // Configuration du curseur
        thumb.setPrefSize(THUMB_WIDTH, HEIGHT - 4);
        thumb.setStyle("-fx-background-radius: " + (HEIGHT / 2) + "px; -fx-background-color: white;");
        thumb.setTranslateX(selectedLabel.get() == "€" ? WIDTH - THUMB_WIDTH - 2 : 2);
        thumb.setTranslateY(2);

        // Positionnement des labels avec taille augmentée
        leftLabel.setTextFill(Color.WHITE);
        rightLabel.setTextFill(Color.WHITE);

        leftLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        rightLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        leftLabel.setTranslateX(10);
        rightLabel.setTranslateX(WIDTH - 20);

        // Centrage vertical des labels
        double labelY = HEIGHT / 2 - 9;
        leftLabel.setTranslateY(labelY);
        rightLabel.setTranslateY(labelY);

        // Création du conteneur principal
        Pane switchContainer = new Pane();
        switchContainer.getChildren().addAll(background, leftLabel, rightLabel, thumb);
        switchContainer.setPrefSize(WIDTH, HEIGHT);

        getChildren().add(switchContainer);

        // Valeur initiale
        selectedLabel.set(leftValue.get());
    }

    private void registerListeners() {
        thumb.setOnMouseClicked(event -> toggle());
        background.setOnMouseClicked(event -> toggle());

        // Mise à jour de la position et de la couleur lors du changement de sélection
        selected.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                animateThumb(2);
                selectedLabel.set(rightValue.get());
                background.setFill(rightStateColor);  // Changement de couleur pour l'état droit
            } else {
                animateThumb(WIDTH - THUMB_WIDTH - 2);
                selectedLabel.set(leftValue.get());
                background.setFill(leftStateColor);  // Changement de couleur pour l'état gauche
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

    // Méthodes pour personnaliser les couleurs des états
    public void setLeftStateColor(Color color) {
        this.leftStateColor = color;
        if (!selected.get()) {
            background.setFill(color);
        }
    }

    public void setRightStateColor(Color color) {
        this.rightStateColor = color;
        if (selected.get()) {
            background.setFill(color);
        }
    }


    private void updateLabels() {
        leftLabel.setText(leftValue.get());
        rightLabel.setText(rightValue.get());
    }

    public void toggle() {
        selected.set(!selected.get());
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onAction.get();
    }

    // Méthodes complètes pour la propriété onAction
    public final void setOnAction(EventHandler<ActionEvent> value) {
        onAction.set(value);
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }

    public final String getSelectedLabel() {
        return selectedLabel.get();
    }

    public final void setSelectedLabel(String labelText) {
        selectedLabel.set(labelText);
    }

    public final StringProperty selectedLabelProperty() {
        return selectedLabel;
    }

    public void setLeftValue(String value) {
        leftValue.set(value);
        updateLabels();
        if (!selected.get()) {
            selectedLabel.set(value);
        }
    }

    public void setRightValue(String value) {
        rightValue.set(value);
        updateLabels();
        if (selected.get()) {
            selectedLabel.set(value);
        }
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean value) {
        selected.set(value);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}