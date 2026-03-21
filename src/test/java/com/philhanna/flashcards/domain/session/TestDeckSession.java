package com.philhanna.flashcards.domain.session;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;

import com.philhanna.flashcards.domain.*;

/**
 * Unit tests for DeckSession
 */
public class TestDeckSession extends BaseTest {

   // A fixed 3-card deck for use across tests
   private static final Deck THREE_CARD_DECK = deckOf(
         "Q1", "A1",
         "Q2", "A2",
         "Q3", "A3");

   private DeckSession session;

   @BeforeEach
   public void setUp() throws Exception {
      session = new DeckSession(THREE_CARD_DECK);
   }

   // ------------------------------------------------------------------
   // Cursor / navigation counts
   // ------------------------------------------------------------------

   @Test
   void startsWithAllCardsUnviewed() {
      assertEquals(3, session.getCursor().getTotalCardCount());
      assertEquals(3, session.getCursor().getUnviewedCardCount());
      assertEquals(0, session.getCursor().getViewedCardCount());
   }

   @Test
   void nextCardMovesOneCardToViewed() {
      session.nextCard();
      assertEquals(2, session.getCursor().getUnviewedCardCount());
      assertEquals(1, session.getCursor().getViewedCardCount());
   }

   @Test
   void previousCardMovesCardBackToUnviewed() {
      session.nextCard();
      session.previousCard();
      assertEquals(3, session.getCursor().getUnviewedCardCount());
      assertEquals(0, session.getCursor().getViewedCardCount());
   }

   @Test
   void previousCardDoesNothingAtStart() {
      session.previousCard();
      assertEquals(3, session.getCursor().getUnviewedCardCount());
      assertEquals(0, session.getCursor().getViewedCardCount());
   }

   @Test
   void nextCardDoesNothingWhenExhausted() {
      session.nextCard();
      session.nextCard();
      session.nextCard();
      session.nextCard(); // one extra — should be a no-op
      assertEquals(0, session.getCursor().getUnviewedCardCount());
      assertEquals(3, session.getCursor().getViewedCardCount());
   }

   @Test
   void totalCardCountIsStableAfterNavigation() {
      session.nextCard();
      session.nextCard();
      assertEquals(3, session.getCursor().getTotalCardCount());
   }

   // ------------------------------------------------------------------
   // getCurrentCard
   // ------------------------------------------------------------------

   @Test
   void getCurrentCardIsNotNullAtStart() {
      assertNotNull(session.getCurrentCard());
   }

   @Test
   void getCurrentCardIsNullWhenAllViewed() {
      session.nextCard();
      session.nextCard();
      session.nextCard();
      assertNull(session.getCurrentCard());
   }

   // ------------------------------------------------------------------
   // getViewedCards
   // ------------------------------------------------------------------

   @Test
   void viewedCardsIsEmptyAtStart() {
      assertTrue(session.getViewedCards().isEmpty());
   }

   @Test
   void viewedCardsGrowsAfterNavigation() {
      session.nextCard();
      session.nextCard();
      assertEquals(2, session.getViewedCards().size());
   }

   // ------------------------------------------------------------------
   // rotate
   // ------------------------------------------------------------------

   @Test
   void rotateKeepsUnviewedCount() {
      int before = session.getCursor().getUnviewedCardCount();
      session.rotate();
      assertEquals(before, session.getCursor().getUnviewedCardCount());
   }

   @Test
   void rotateChangesCurrentCard() {
      SessionCard before = session.getCurrentCard();
      session.rotate();
      assertNotEquals(before, session.getCurrentCard());
   }

   @Test
   void rotateOnSingleCardDeckIsSafe() {
      DeckSession single = new DeckSession(deckOf("Q", "A"));
      SessionCard before = single.getCurrentCard();
      single.rotate();
      // only one card — still present and unchanged
      assertNotNull(single.getCurrentCard());
      assertEquals(before, single.getCurrentCard());
   }

   // ------------------------------------------------------------------
   // cardViewCount / recordCardView
   // ------------------------------------------------------------------

   @Test
   void cardViewCountStartsAtZero() {
      assertEquals(0, session.getCardViewCount());
   }

   @Test
   void recordCardViewIncrementsCount() {
      session.recordCardView();
      session.recordCardView();
      assertEquals(2, session.getCardViewCount());
   }

   // ------------------------------------------------------------------
   // elapsedTime
   // ------------------------------------------------------------------

   @Test
   void elapsedTimeIsNonNegative() {
      assertTrue(session.getElapsedTime() >= 0.0);
   }

   // ------------------------------------------------------------------
   // Helper
   // ------------------------------------------------------------------

   private static Deck deckOf(String... pairs) {
      List<Card> cards = new java.util.ArrayList<>();
      for (int i = 0; i < pairs.length; i += 2)
         cards.add(new BasicCard(pairs[i], pairs[i + 1]));
      return new Deck() {
         public String getTitle() { return "Test"; }
         public List<Card> getCards() { return cards; }
      };
   }
}
