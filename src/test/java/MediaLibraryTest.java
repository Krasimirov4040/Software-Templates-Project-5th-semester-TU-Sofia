//package com.student.media_library;

import com.student.media_library.model.Book;
import com.student.media_library.model.Media;
import media_library.MediaLibrary;
import patterns.BookAdapter;
import patterns.GenreIterator;
import patterns.LegacyBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.List;

class MediaLibraryTest {
    private MediaLibrary library;

    @BeforeEach
    void setUp() {
        library = new MediaLibrary();
        // Clear any loaded items to isolate tests (remove all media)
        List<Media> allMedia = library.getAllMedia();
        allMedia.forEach(m -> library.removeMedia(m.getTitle()));
    }

    @Test
    void testAddAndGetAll() {
        Book book = new Book("Test Book", "Test Genre", 5.0, 2020, "Author", 100);
        library.addMedia(book);
        assertEquals(1, library.getAllMedia().size());
        assertEquals("Test Book", library.getAllMedia().get(0).getTitle());
    }

    @Test
    void testSetRating() {
        Book book = new Book("Test Book", "Test Genre", 5.0, 2020, "Author", 100);
        library.addMedia(book);
        library.setRating("Test Book", 8.0);
        assertEquals(8.0, library.getAllMedia().get(0).getRating(), 0.001); // Use delta for double comparison
    }

    @Test
    void testIteratorFilter() {
        library.addMedia(new Book("Book1", "Fantasy", 5.0, 2020, "Author", 100));
        library.addMedia(new Book("Book2", "Sci-Fi", 6.0, 2021, "Author2", 200));
        Iterator<Media> iterator = new GenreIterator(library.getAllMedia(), "Sci-Fi");
        assertTrue(iterator.hasNext());
        assertEquals("Book2", iterator.next().getTitle());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testAdapter() {
        LegacyBook legacy = new LegacyBook("Legacy Title", "Mystery", 7.0, 1950, "Old Author", 150);
        BookAdapter adapter = new BookAdapter(legacy);
        assertEquals("Legacy Title", adapter.getTitle());
        adapter.setRating(9.0);
        assertEquals(9.0, adapter.getRating(), 0.001);
    }
}