package media_library;
import util.PersistenceUtil;
import java.util.Iterator;
import com.student.media_library.model.Media;
import patterns.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MediaLibrary implements Observable {
    private final List<Media> mediaItems = new ArrayList<>();
    private final List<Observer> observers = new ArrayList<>();

    public MediaLibrary() {

        List<Media> loaded = PersistenceUtil.load();
        mediaItems.addAll(loaded);
    }

    public void addMedia(Media media) {
        mediaItems.add(media);
        notifyObservers();
        PersistenceUtil.save(mediaItems); // Save after change
    }

    public void removeMedia(String title) {
        mediaItems.removeIf(m -> m.getTitle().equalsIgnoreCase(title));
        notifyObservers();
        PersistenceUtil.save(mediaItems);
    }

    public void setRating(String title, double rating) {
        for (Media m : mediaItems) {
            if (m.getTitle().equalsIgnoreCase(title)) {
                m.setRating(rating);
                notifyObservers();
                PersistenceUtil.save(mediaItems);
                return;
            }
        }
        throw new NoSuchElementException("Media not found: " + title);
    }

    public Iterable<Media> filterByGenre(String genre) {
        return () -> new GenreIterator(mediaItems, genre);
    }

    public List<Media> getAllMedia() {
        return new ArrayList<>(mediaItems); // Defensive copy
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    // Temporary main for console testing
    public static void main(String[] args) {
        MediaLibrary library = new MediaLibrary();

        // Add observer for console display
        LibraryDisplayObserver display = new LibraryDisplayObserver(library);
        library.addObserver(display);

        // Demo add
        library.addMedia(new com.student.media_library.model.Book("The Hobbit", "Fantasy", 8.5, 1937, "J.R.R. Tolkien", 310));
        library.addMedia(new com.student.media_library.model.Film("Inception", "Sci-Fi", 9.0, 2010, "Christopher Nolan", 148));
        library.addMedia(new com.student.media_library.model.Music("Bohemian Rhapsody", "Rock", 9.5, 1975, "Queen", "A Night at the Opera", 354));

        // Demo adapter
        LegacyBook legacy = new LegacyBook("Old Book", "Mystery", 7.0, 1950, "Agatha Christie", 200);
        library.addMedia(new BookAdapter(legacy));

        // Demo set rating
        library.setRating("Inception", 9.2);

        // Demo remove
        library.removeMedia("Old Book");

        // Demo filter with iterator
        System.out.println("Sci-Fi items:");
        Iterator<Media> sciFiIterator = new GenreIterator(library.getAllMedia(), "Sci-Fi");
        while (sciFiIterator.hasNext()) {
            System.out.println(sciFiIterator.next().getDescription());
        }
    }
}