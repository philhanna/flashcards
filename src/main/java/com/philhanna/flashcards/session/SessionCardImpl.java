package com.philhanna.flashcards.session;

import com.philhanna.flashcards.*;
import com.philhanna.flashcards.deck.CardImpl;

/**
 * An implementation of the <code>SessionCard</code> interface that
 * combines the card functions (get question, get answer, toggle) with
 * the statistics-keeping required for a session.
 */
public class SessionCardImpl extends CardImpl implements SessionCard {

   // ==========================================================
   // Instance variables
   // ==========================================================

   private CardHistory lastAnswer = CardHistory.NEVER_ANSWERED;
   private int timesAnsweredRight = 0;
   private int timesAnsweredWrong = 0;

   // ==========================================================
   // Inner class that implements the CardStatistics interface
   // ==========================================================

   private CardStatistics statistics = new CardStatistics() {
      public CardHistory getHistory() {
         return SessionCardImpl.this.lastAnswer;
      }

      public int getTimesAnsweredRight() {
         return SessionCardImpl.this.timesAnsweredRight;
      }

      public int getTimesAnsweredWrong() {
         return SessionCardImpl.this.timesAnsweredWrong;
      }
   };

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new default session card implementation, delegating the
    * implementation of the card interface methods to a card object
    * passed as a parameter.
    * @param card the card it contains
    */
   public SessionCardImpl(Card card) {
      super(card.getQuestion(), card.getAnswer());
   }

   // ==========================================================
   // Instance methods
   // ==========================================================

   /**
    * Returns the card statistics
    */
   public CardStatistics getStatistics() {
      return this.statistics;
   }

   /**
    * Marks an answer as correct
    */
   public void markRight() {
      this.lastAnswer = CardHistory.ANSWERED_RIGHT;
      this.timesAnsweredRight += 1;
   }

   /**
    * Marks an answer as incorrect
    */
   public void markWrong() {
      this.lastAnswer = CardHistory.ANSWERED_WRONG;
      this.timesAnsweredWrong += 1;
   }
}
