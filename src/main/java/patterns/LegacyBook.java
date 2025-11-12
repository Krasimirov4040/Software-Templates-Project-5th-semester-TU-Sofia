package patterns;

public class LegacyBook {
    private final String bookTitle;
    private final String bookGenre;
    private double bookRating;
    private final int bookYear;
    private final String authorName;
    private final int pages;

    public LegacyBook(String bookTitle, String bookGenre, double bookRating, int bookYear, String authorName, int pages) {
        this.bookTitle = bookTitle;
        this.bookGenre = bookGenre;
        this.bookRating = bookRating;
        this.bookYear = bookYear;
        this.authorName = authorName;
        this.pages = pages;

    }

    public String getBookTitle() { return bookTitle; }
    public String getBookGenre() { return bookGenre; }
    public double getBookRating() { return bookRating; }
    public int getBookYear() { return bookYear; }
    public void setBookRating(double rating) { this.bookRating = rating; }
    public String getAuthorName() { return authorName; }
    public int getPages() { return pages; }
    public LegacyBook() {
        this("", "", 0.0, 0, "", 0);
    }
}