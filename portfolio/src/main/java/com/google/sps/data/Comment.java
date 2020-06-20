package com.google.sps.data;

public final class Comment {

  private final long id;
  private final String author;
  private final String message;
  private final long timestamp;

  public Comment(long id, String author, String message, long timestamp) {
    this.id = id;
    this.author = author;
    this.message = message;
    this.timestamp = timestamp;
  }
}