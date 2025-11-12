package patterns;

import com.student.media_library.model.Media;
import com.student.media_library.model.gui.*;
import media_library.MediaLibrary;

public class LibraryDisplayObserver implements Observer {
    private final MediaLibrary library;
    public LibraryDisplayObserver(MediaLibrary library) {
        this.library = library;
    }

    @Override
    public void update() {
        System.out.println("Library updated! Current items:");
        for (Media media : library.getAllMedia()) {
            System.out.println(media.getDescription());
        }
        System.out.println("---");
    }
}