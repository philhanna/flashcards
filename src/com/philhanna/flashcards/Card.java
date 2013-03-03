package com.philhanna.flashcards;

/**
 * A flash card having a question and an answer.
 */
public interface Card {

   /**
    * Returns the question text
    */
   public String getQuestion();

   /**
    * Returns the answer text
    */
   public String getAnswer();

   /**
    * Toggles between showing its answer and showing its question. The card must
    * remember its current state.
    */
   public void toggle();
}