package com.philhanna.flashcards.session;

import static org.junit.Assert.*;

import org.junit.*;

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

   @Before
   public void setUp() throws Exception {
      card = new SessionCardImpl(new CardImpl("Who is buried in Grant's Tomb?", "Grant"));
   }

   // ==================================================================
   // Unit tests
   // ==================================================================

   @Test
   public void getStatisticsAlwaysReturnsNonNull() {
      assertNotNull(card.getStatistics());
   }

   @Test
   public void getHistoryIsClearFirstTime() {
      CardStatistics cs = card.getStatistics();
      CardHistory ch = cs.getHistory();
      assertNotNull(ch);
      assertEquals(CardHistory.NEVER_ANSWERED, ch);
   }

   @Test
   public void testMarkRight() {

      // Should be clear first time

      assertEquals(CardHistory.NEVER_ANSWERED, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());

      // After marking right, should be in the "ANSWERED_RIGHT" state
      // and the historical counts should be updated

      card.markRight();
      assertEquals(CardHistory.ANSWERED_RIGHT, card.getStatistics().getHistory());
      assertEquals(1, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());

      // After marking right a second time, should still be in the
      // "ANSWERED_RIGHT" state and the historical counts should be
      // updated

      card.markRight();
      assertEquals(CardHistory.ANSWERED_RIGHT, card.getStatistics().getHistory());
      assertEquals(2, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());
   }

   @Test
   public void testMarkWrong() {

      // Should be clear first time

      assertEquals(CardHistory.NEVER_ANSWERED, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());

      // After marking wrong, should be in the "ANSWERED_WRONG" state
      // and the historical counts should be updated

      card.markWrong();
      assertEquals(CardHistory.ANSWERED_WRONG, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(1, card.getStatistics().getTimesAnsweredWrong());

      // After marking wrong a second time, should still be in the
      // "ANSWERED_WRONG" state and the historical counts should be
      // updated

      card.markWrong();
      assertEquals(CardHistory.ANSWERED_WRONG, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(2, card.getStatistics().getTimesAnsweredWrong());
   }

   @Test
   public void testMarkRightAfterWrong() {

      // Should be clear first time

      assertEquals(CardHistory.NEVER_ANSWERED, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(0, card.getStatistics().getTimesAnsweredWrong());

      // After marking wrong, should be in the "ANSWERED_WRONG" state
      // and the historical counts should be updated

      card.markWrong();
      assertEquals(CardHistory.ANSWERED_WRONG, card.getStatistics().getHistory());
      assertEquals(0, card.getStatistics().getTimesAnsweredRight());
      assertEquals(1, card.getStatistics().getTimesAnsweredWrong());

      // After marking right, should still be in the
      // "ANSWERED_RIGHT" state and the historical counts should be
      // updated

      card.markRight();
      assertEquals(CardHistory.ANSWERED_RIGHT, card.getStatistics().getHistory());
      assertEquals(1, card.getStatistics().getTimesAnsweredRight());
      assertEquals(1, card.getStatistics().getTimesAnsweredWrong());
   }

}
