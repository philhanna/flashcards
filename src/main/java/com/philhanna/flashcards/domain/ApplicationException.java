package com.philhanna.flashcards.domain;

/**
 * Base class for all exception in the flashcard application
 */
public class ApplicationException extends Exception {
   private static final long serialVersionUID = 1L;

   /**
    * Creates a new application exception with the specified detail message
    * @param message the detail message
    */
   public ApplicationException(String message) {
      super(message);
   }

   /**
    * Creates a new application exception with the specified detail message and cause
    * @param message the detail message
    * @param cause the underlying cause
    */
   public ApplicationException(String message, Throwable cause) {
      super(message, cause);
   }
}
