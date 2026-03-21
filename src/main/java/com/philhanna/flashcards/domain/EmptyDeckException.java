package com.philhanna.flashcards.domain;

/**
 * Thrown when a deck contains no cards
 */
public class EmptyDeckException extends ApplicationException {
   private static final long serialVersionUID = 1L;

   public EmptyDeckException(String msg) {
      super(msg);
   }
}
