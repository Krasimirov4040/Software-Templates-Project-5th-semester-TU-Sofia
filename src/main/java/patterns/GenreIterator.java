package patterns;

import com.student.media_library.model.Media;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class GenreIterator implements Iterator<Media> {
    private final List<Media> mediaList;
    private final String genre;
    private int currentIndex = 0;

    public GenreIterator(List<Media> mediaList, String genre) {
        this.mediaList = mediaList;
        this.genre = genre.toLowerCase();
    }

    @Override
    public boolean hasNext() {
        while (currentIndex < mediaList.size()) {
            if (mediaList.get(currentIndex).getGenre().toLowerCase().equals(genre)) {
                return true;
            }
            currentIndex++;
        }
        return false;
    }

    @Override
    public Media next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more items in genre: " + genre);
        }
        return mediaList.get(currentIndex++);
    }
}