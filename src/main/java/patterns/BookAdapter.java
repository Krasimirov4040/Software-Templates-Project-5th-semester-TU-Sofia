package patterns;

import com.student.media_library.model.Media;

public class BookAdapter implements Media {
    private final LegacyBook legacyBook;

    public BookAdapter(LegacyBook legacyBook) {
        this.legacyBook = legacyBook;
    }

    @Override
    public String getTitle() { return legacyBook.getBookTitle(); }
    @Override
    public String getGenre() { return legacyBook.getBookGenre(); }
    @Override
    public double getRating() { return legacyBook.getBookRating(); }
    @Override
    public int getYear() { return legacyBook.getBookYear(); }
    @Override
    public void setRating(double rating) { legacyBook.setBookRating(rating); }

    @Override
    public String getDescription() {
        return "Adapted Book: " + legacyBook.getBookTitle() + " by " + legacyBook.getAuthorName() + " (" + legacyBook.getBookYear() + ", " + legacyBook.getPages() + " pages, Genre: " + legacyBook.getBookGenre() + ", Rating: " + legacyBook.getBookRating() + ")";
    }
}