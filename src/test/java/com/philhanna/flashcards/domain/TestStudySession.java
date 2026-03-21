package com.philhanna.flashcards.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.*;

import com.philhanna.flashcards.domain.session.*;

/**
 * Unit tests for StudySession
 */
public class TestStudySession extends BaseTest {

   private DeckSession deckSession;
   private StudySession studySession;

   @BeforeEach
   public void setUp() throws Exception {
      deckSession = new DeckSession(deckOf(
            "Q1", "A1",
            "Q2", "A2",
            "Q3", "A3"));
      studySession = new StudySession(deckSession);
   }

   @Test
   void getSessionReturnsUnderlyingSession() {
      assertSame(deckSession, studySession.getSession());
   }

   @Test
   void markRightMarksCurrentCardCorrect() {
      SessionCard card = deckSession.getCurrentCard();
      studySession.markRight();
      assertEquals(CardHistory.ANSWERED_RIGHT, card.getStatistics().getHistory());
   }

   @Test
   void markRightAdvancesToNextCard() {
      SessionCard before = deckSession.getCurrentCard();
      studySession.markRight();
      assertNotEquals(before, deckSession.getCurrentCard());
   }

   @Test
   void markRightMovesCardToViewed() {
      studySession.markRight();
      assertEquals(1, deckSession.getViewedCards().size());
   }

   @Test
   void markWrongMarksCurrentCardIncorrect() {
      SessionCard card = deckSession.getCurrentCard();
      studySession.markWrong();
      assertEquals(CardHistory.ANSWERED_WRONG, card.getStatistics().getHistory());
   }

   @Test
   void markWrongKeepsCardInUnviewed() {
      int before = deckSession.getCursor().getUnviewedCardCount();
      studySession.markWrong();
      assertEquals(before, deckSession.getCursor().getUnviewedCardCount());
   }

   @Test
   void markWrongDoesNotAdvanceViewedCount() {
      studySession.markWrong();
      assertEquals(0, deckSession.getViewedCards().size());
   }

   @Test
   void nextCardAdvances() {
      studySession.nextCard();
      assertEquals(1, deckSession.getViewedCards().size());
   }

   @Test
   void previousCardGoesBack() {
      studySession.nextCard();
      studySession.previousCard();
      assertEquals(0, deckSession.getViewedCards().size());
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
