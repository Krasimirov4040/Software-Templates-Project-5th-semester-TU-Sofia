package com.student.media_library.model;

public class Book implements Media {
    private final String title;
    private final String genre;
    private double rating;
    private final int year;
    private final String author;
    private final int pageCount;

    public Book(String title, String genre, double rating, int year, String author, int pageCount) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.author = author;
        this.pageCount = pageCount;
    }

    @Override
    public String getTitle() { return title; }
    @Override
    public String getGenre() { return genre; }
    @Override
    public double getRating() { return rating; }
    @Override
    public int getYear() { return year; }
    @Override
    public void setRating(double rating) {
        if (rating < 0 || rating > 10) throw new IllegalArgumentException("Rating must be between 0 and 10");
        this.rating = rating;
    }
    // ... (rest unchanged)
    @Override
    public String getDescription() {
        return "Book: " + title + " by " + author + " (" + year + ", " + pageCount + " pages, Genre: " + genre + ", Rating: " + rating + ")";
    }
}