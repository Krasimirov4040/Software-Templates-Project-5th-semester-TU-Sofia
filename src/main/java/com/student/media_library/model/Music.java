package com.student.media_library.model;

public class Music implements Media {
    private final String title;
    private final String genre;
    private double rating;
    private final int year;
    private final String artist;
    private final String album;
    private final int trackLengthSeconds;

    public Music(String title, String genre, double rating, int year, String artist, String album, int trackLengthSeconds) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.artist = artist;
        this.album = album;
        this.trackLengthSeconds = trackLengthSeconds;
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
        return "Music: " + title + " by " + artist + " from " + album + " (" + year + ", " + trackLengthSeconds + " sec, Genre: " + genre + ", Rating: " + rating + ")";
    }
}