package com.student.media_library.model.gui;
import javafx.collections.FXCollections;
import com.student.media_library.model.Book;
import com.student.media_library.model.Film;
import com.student.media_library.model.Media;
import com.student.media_library.model.Music;
import media_library.MediaLibrary;
import patterns.BookAdapter;
import patterns.LegacyBook;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddMediaDialog {
    private final MediaLibrary library;

    public AddMediaDialog(MediaLibrary library) {
        this.library = library;
    }

    public void show() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Media");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Common fields
        TextField titleField = new TextField();
        TextField genreField = new TextField();
        TextField ratingField = new TextField("0.0");
        TextField yearField = new TextField();

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Genre:"), 0, 1);
        grid.add(genreField, 1, 1);
        grid.add(new Label("Rating (0-10):"), 0, 2);
        grid.add(ratingField, 1, 2);
        grid.add(new Label("Year:"), 0, 3);
        grid.add(yearField, 1, 3);

        // Type choice
        ChoiceBox<String> typeChoice = new ChoiceBox<>(FXCollections.observableArrayList("Book", "Film", "Music", "Legacy Book"));
        grid.add(new Label("Type:"), 0, 4);
        grid.add(typeChoice, 1, 4);

        // Dynamic fields (show based on type)
        TextField field1 = new TextField();
        TextField field2 = new TextField();
        Label label1 = new Label();
        Label label2 = new Label();
        grid.add(label1, 0, 5);
        grid.add(field1, 1, 5);
        grid.add(label2, 0, 6);
        grid.add(field2, 1, 6);

        typeChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;
            switch (newVal) {
                case "Book" -> {
                    label1.setText("Author:");
                    label2.setText("Page Count:");
                }
                case "Film" -> {
                    label1.setText("Director:");
                    label2.setText("Duration (min):");
                }
                case "Music" -> {
                    label1.setText("Artist:");
                    label2.setText("Album:");
                    grid.add(new Label("Track Length (sec):"), 0, 7);
                    TextField field3 = new TextField();
                    grid.add(field3, 1, 7);
                }
                case "Legacy Book" -> {
                    label1.setText("Author:");
                    label2.setText("Pages:");
                }
            }
        });

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                String title = titleField.getText();
                String genre = genreField.getText();
                double rating = Double.parseDouble(ratingField.getText());
                int year = Integer.parseInt(yearField.getText());

                Media media = null;
                String selectedType = typeChoice.getValue();
                if (selectedType == null) throw new IllegalArgumentException("Select type.");

                switch (selectedType) {
                    case "Book" -> media = new Book(title, genre, rating, year, field1.getText(), Integer.parseInt(field2.getText()));
                    case "Film" -> media = new Film(title, genre, rating, year, field1.getText(), Integer.parseInt(field2.getText()));
                    case "Music" -> {
                        // For Music, field2 is album, add field3 for track length
                        TextField field3 = (TextField) grid.getChildren().stream().filter(node -> GridPane.getRowIndex(node) == 7 && GridPane.getColumnIndex(node) == 1).findFirst().orElse(null);
                        media = new Music(title, genre, rating, year, field1.getText(), field2.getText(), Integer.parseInt(field3.getText()));
                    }
                    case "Legacy Book" -> {
                        LegacyBook legacy = new LegacyBook(title, genre, rating, year, field1.getText(), Integer.parseInt(field2.getText()));
                        media = new BookAdapter(legacy);
                    }
                }

                library.addMedia(media);
                dialog.close();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid input: " + ex.getMessage());
                alert.show();
            }
        });

        grid.add(addButton, 1, 8);

        Scene scene = new Scene(grid, 400, 400);
        dialog.setScene(scene);
        dialog.show();
    }
}