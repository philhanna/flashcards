package com.philhanna.flashcards.session;

import java.util.*;

import com.philhanna.flashcards.*;
import com.philhanna.flashcards.ui.*;
import com.philhanna.flashcards.ui.events.*;

/**
 * An implementation of {@link Session} that keeps track of viewed and unviewed
 * cards
 */
public class SessionImpl implements Session, CardChangeListener {

   // ==========================================================
   // Instance variables
   // ==========================================================

   private Stack<SessionCard> unviewedCards = new Stack<SessionCard>();
   private Stack<SessionCard> viewedCards = new Stack<SessionCard>();

   // ==========================================================
   // Instance of an inner class that implements SessionCursor
   // ==========================================================

   private SessionCursor cursor = new SessionCursor() {
      public int getTotalCardCount() {
         return getViewedCardCount() + getUnviewedCardCount();
      }

      public int getUnviewedCardCount() {
         return SessionImpl.this.unviewedCards.size();
      }

      public int getViewedCardCount() {
         return SessionImpl.this.viewedCards.size();
      }
   };
   private long startTime;
   private int cardViewCount;

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new default session implementation using the specified deck
    * @param deck the deck
    */
   public SessionImpl(Deck deck) {

      // For each card in the deck, create a session card for it and add
      // that session card to the list of cards not yet viewed

      for (Card card : deck.getCards()) {
         unviewedCards.add(new SessionCardImpl(card));
      }

      // Now shuffle the deck

      shuffle();

      // Set the start time

      this.startTime = System.currentTimeMillis();
      this.cardViewCount = 0;
   }

   // ==========================================================
   // Instance methods
   // ==========================================================

   /**
    * Randomizes the order of the cards in the deck
    */
   public void shuffle() {
      for (int i = 0; i < 3; ++i)
         Collections.shuffle(unviewedCards);
   }

   /**
    * Moves the cursor to the next card. If there are no more unviewed cards,
    * does nothing.
    */
   public void nextCard() {
      if (!unviewedCards.isEmpty())
         viewedCards.push(unviewedCards.pop());
   }

   /**
    * Moves the cursor back to the previous card. If this was the first unviewed
    * card, does nothing
    */
   public void previousCard() {
      if (!viewedCards.isEmpty())
         unviewedCards.push(viewedCards.pop());
   }

   /**
    * Returns the current card, or <code>null</code>, if there are no more
    * unviewed cards
    */
   public SessionCard getCurrentCard() {
      return unviewedCards.isEmpty() ? null : unviewedCards.peek();
   }

   /**
    * Returns the session cursor
    */
   public SessionCursor getCursor() {
      return cursor;
   }

   /**
    * Rotates the unviewed cards by moving what would be the next card to be
    * viewed so that it is at the bottom of the deck. Used in the
    * {@link SessionContainer#markWrong()} method.
    */
   public void rotate() {
      if (!unviewedCards.isEmpty())
         unviewedCards.add(0, unviewedCards.pop());
   }

   /**
    * Returns the list of cards already viewed. Called in the
    * {@link StatisticsTable#getStatisticsAsHTML(Session)} part of the UI.
    */
   public List<SessionCard> getViewedCards() {
      return viewedCards;
   }

   @Override
   public double getElapsedTime() {
      final long elapsedMillis = System.currentTimeMillis() - startTime;
      final double seconds = elapsedMillis / 1000.0;
      return seconds;
   }

   @Override
   public int getCardViewCount() {
      return cardViewCount;
   }

   @Override
   public void cardChanged(CardChangeEvent event) {
      cardViewCount++;
   }
}
