package com.philhanna.flashcards.session;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.philhanna.flashcards.*;
import com.philhanna.flashcards.deck.CardImpl;

/**
 * Unit tests for SessionCardImpl
 */
public class TestSessionCardImpl extends BaseTest {

   // ==================================================================
   // Instance variables
   // ==================================================================

   private SessionCard card = null;

   // ==================================================================
   // Fixtures
   // ==================================================================

   @BeforeEach
   public void setUp() throws Exception {
      card = new SessionCardImpl(new CardImpl("Who is buried in Grant's Tomb?", "Grant"));
   }

   // ==================================================================
   // Unit tests
   // ==================================================================

   @Test
   void getStatisticsAlwaysReturnsNonNull() {
      assertNotNull(card.getStatistics());
   }

   @Test
   void getHistoryIsClearFirstTime() {
      CardStatistics cs = card.getStatistics();
      CardHistory ch = cs.getHistory();
      assertNotNull(ch);
      assertEquals(CardHistory.NEVER_ANSWERED, ch);
   }

   @Test
   void testMarkRight() {
      assertEquals(CardHistory.NEVER_ANSWERED, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());

      card.markRight();
      assertEquals(CardHistory.ANSWERED_RIGHT, card.getStatistics().getHistory());
      assertEquals(1, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());

      card.markRight();
      assertEquals(CardHistory.ANSWERED_RIGHT, card.getStatistics().getHistory());
      assertEquals(2, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());
   }

   @Test
   void testMarkWrong() {
      assertEquals(CardHistory.NEVER_ANSWERED, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());

      card.markWrong();
      assertEquals(CardHistory.ANSWERED_WRONG, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(1, card.getStatistics().getTimesAnsweredWrong());

      card.markWrong();
      assertEquals(CardHistory.ANSWERED_WRONG, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(2, card.getStatistics().getTimesAnsweredWrong());
   }

   @Test
   void testMarkRightAfterWrong() {
      assertEquals(CardHistory.NEVER_ANSWERED, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());

      card.markWrong();
      assertEquals(CardHistory.ANSWERED_WRONG, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(1, card.getStatistics().getTimesAnsweredWrong());

      card.markRight();
      assertEquals(CardHistory.ANSWERED_RIGHT, card.getStatistics().getHistory());
      assertEquals(1, card.getStatistics().getTimesAnsweredRight());
      assertEquals(1, card.getStatistics().getTimesAnsweredWrong());
   }

}
