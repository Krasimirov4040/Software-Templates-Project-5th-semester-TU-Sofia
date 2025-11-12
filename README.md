# Media Library Project - Software Patterns Course

## Overview
This project is for assignment 1: A media library manager for books, films, music, and other types. It supports adding, removing, filtering by genre, and setting ratings. Demonstrates Iterator (for filtering), Adapter (for legacy books), and extra Observer (for updates). Built in Java with JavaFX GUI and Gson persistence.

## Class Descriptions
### Model Classes (com.student.media_library.model)
- **Media (interface)**: Defines common methods for all media types: getTitle(), getGenre(), getRating(), getYear(), setRating(double), getDescription(). Purpose: Polymorphism for uniform handling.
- **Book (class)**: Implements Media for books. Fields: title, genre, rating, year, author, pageCount. Description method includes all details plus rating.
- **Film (class)**: Implements Media for films. Fields: title, genre, rating, year, director, durationMinutes.
- **Music (class)**: Implements Media for music. Fields: title, genre, rating, year, artist, album, trackLengthSeconds.

### Patterns Classes (com.student.media_library.patterns)
- **Observable (interface)**: Methods: addObserver(), removeObserver(), notifyObservers(). Used by MediaLibrary as subject.
- **Observer (interface)**: Method: update(). Implemented by LibraryDisplayObserver and MediaLibraryApp for notifications.
- **LibraryDisplayObserver (class)**: Console observer that prints library on update. Holds MediaLibrary reference.
- **GenreIterator (class)**: Custom Iterator for genre-filtered traversal. Skips non-matching items.
- **LegacyBook (class)**: Simulates incompatible legacy class with different field names (e.g., bookTitle).
- **BookAdapter (class)**: Adapts LegacyBook to Media interface. Wraps legacy instance and maps methods.

### Core Class (com.student.media_library)
- **MediaLibrary (class)**: Central manager. Implements Observable. Holds media list, adds/removes/sets rating with notifications and saves. Provides filterByGenre() returning Iterable with GenreIterator.

### GUI Classes (com.student.media_library.gui)
- **MediaLibraryApp (class)**: Extends JavaFX Application, implements Observer. Sets up table, buttons, and refreshes on updates.
- **AddMediaDialog (class)**: Dialog for adding media. Uses ChoiceBox for type, dynamic fields, creates Media or Adapter.

### Utility Classes (com.student.media_library.util)
- **PersistenceUtil (class)**: Static methods for saving/loading media list to/from JSON using Gson. Determines type from JSON fields.

## Design Patterns Demonstration
- **Iterator**: GenreIterator allows traversing filtered media without exposing the list. Used in filterByGenre() and GUI filter button.
- **Adapter**: BookAdapter makes LegacyBook compatible with Media. Demo in main/GUI by adding legacy books uniformly.
- **Observer (Extra for Grade 5)**: MediaLibrary notifies observers on changes. Console observer prints, GUI refreshes table.

## How to Run
- GUI: `mvn clean javafx:run`
- Console Demo: Run MediaLibrary.main()

## Technologies
- Java 17, Maven, JavaFX 21, Gson 2.10.1, JUnit (for tests).

## UML Diagram
See docs/uml.mermaid for Mermaid code (paste into mermaid.live).