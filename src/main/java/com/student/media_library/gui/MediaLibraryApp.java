
package com.student.media_library.gui;

import com.student.media_library.model.gui.AddMediaDialog;
import media_library.MediaLibrary;
import com.student.media_library.model.Media;
import patterns.Observer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MediaLibraryApp extends Application implements Observer {
    private final MediaLibrary library = new MediaLibrary();
    private TableView<Media> tableView;
    private final ObservableList<Media> tableData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        library.addObserver(this); // Register GUI as observer

        // Table setup
        tableView = new TableView<>();
        TableColumn<Media, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        TableColumn<Media, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGenre()));
        TableColumn<Media, Double> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getRating()).asObject());
        TableColumn<Media, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getYear()).asObject());
        TableColumn<Media, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));

        tableView.getColumns().addAll(titleCol, genreCol, ratingCol, yearCol, descCol);
        tableView.setItems(tableData);
        update(); // Initial load

        // Buttons
        VBox layout = getVBox();

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Media Library");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox getVBox() {
        Button addBtn = new Button("Add Media");
        addBtn.setOnAction(e -> new AddMediaDialog(library).show());

        Button removeBtn = new Button("Remove Selected");
        removeBtn.setOnAction(e -> {
            Media selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                library.removeMedia(selected.getTitle());
            } else {
                showAlert("No item selected.");
            }
        });

        Button rateBtn = new Button("Set Rating for Selected");
        rateBtn.setOnAction(e -> {
            Media selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Set Rating");
                dialog.setHeaderText("Enter new rating (0-10):");
                dialog.showAndWait().ifPresent(input -> {
                    try {
                        double rating = Double.parseDouble(input);
                        library.setRating(selected.getTitle(), rating);
                    } catch (NumberFormatException ex) {
                        showAlert("Invalid rating.");
                    }
                });
            } else {
                showAlert("No item selected.");
            }
        });

        Button filterBtn = new Button("Filter by Genre");
        filterBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Filter");
            dialog.setHeaderText("Enter genre:");
            dialog.showAndWait().ifPresent(genre -> {
                tableData.clear();
                library.filterByGenre(genre).forEach(tableData::add);
            });
        });

        Button refreshBtn = new Button("Show All");
        refreshBtn.setOnAction(e -> update());

        // Layout
        VBox layout = new VBox(10, tableView, addBtn, removeBtn, rateBtn, filterBtn, refreshBtn);
        layout.setPadding(new Insets(10));
        return layout;
    }

    @Override
    public void update() {
        tableData.clear();
        tableData.addAll(library.getAllMedia());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}