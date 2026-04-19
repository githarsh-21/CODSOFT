package com.example.quoteapp;

public class Quote {
    private String text;
    private String author;
    private String category;

    public Quote(String text, String author, String category) {
        this.text = text;
        this.author = author;
        this.category = category;
    }

    public String getText() { return text; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
}
