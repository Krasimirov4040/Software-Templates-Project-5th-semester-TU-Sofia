package com.student.media_library.model;

public class Film implements Media {
    private final String title;
    private final String genre;
    private double rating;
    private final int year;
    private final String director;
    private final int durationMinutes;

    public Film(String title, String genre, double rating, int year, String director, int durationMinutes) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.director = director;
        this.durationMinutes = durationMinutes;
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

    @Override
    public String getDescription() {
        return "Film: " + title + " directed by " + director + " (" + year + ", " + durationMinutes + " min, Genre: " + genre + ", Rating: " + rating + ")";
    }
}