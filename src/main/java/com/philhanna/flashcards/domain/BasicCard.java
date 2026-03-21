package com.philhanna.flashcards.domain;

/**
 * The basic implementation of the <code>Card</code> interface, with getters for the
 * question and answer.
 */
public class BasicCard implements Card {
   private final String question;
   private final String answer;

   /**
    * Creates a new default card implementation with the specified question and
    * answer
    * @param question the question text
    * @param answer the answer text
    */
   public BasicCard(String question, String answer) {
      this.question = question;
      this.answer = answer;
   }

   @Override
   public String getAnswer() {
      return this.answer;
   }

   @Override
   public String getQuestion() {
      return this.question;
   }
}
