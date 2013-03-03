package com.philhanna.flashcards.deck;

import com.philhanna.flashcards.Card;

/**
 * The basic implementation of the <code>Card</code> interface, with getters for the
 * question and answer, and the ability to toggle the card from showing its
 * answer to showing its question.
 */
public class CardImpl implements Card {
   private String question;
   private String answer;

   /**
    * Creates a new default card implementation with the specified question and
    * answer
    * @param question the question text
    * @param answer the answer text
    */
   public CardImpl(String question, String answer) {
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

   @Override
   public void toggle() {
      String temp = this.question;
      this.question = this.answer;
      this.answer = temp;
   }
}
