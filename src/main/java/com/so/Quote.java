package com.so;

public class Quote {

    private String text;
    private String author;
    private int rating;

    public Quote(String text, String author, int rating) {
        this.text = text;
        this.author = author;
        this.rating = rating;
    }

    public Quote() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
