package com.philhanna.flashcards.domain;

/**
 * Thrown when a card is missing required fields (question or answer)
 */
public class InvalidCardException extends ApplicationException {
   private static final long serialVersionUID = 1L;

   public InvalidCardException(String msg) {
      super(msg);
   }
}
