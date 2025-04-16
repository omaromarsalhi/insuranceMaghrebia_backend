package com.maghrebia.user.exception;

public class LinkExpiredException extends RuntimeException {
  public LinkExpiredException(String message) {
    super(message);
  }
}
