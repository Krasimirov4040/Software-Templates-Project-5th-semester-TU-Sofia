package com.student.media_library.model;

public interface Media {
    String getTitle();
    String getGenre();
    double getRating();
    int getYear();
    void setRating(double rating);
    String getDescription();
}