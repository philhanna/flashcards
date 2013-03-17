package com.philhanna.flashcards.session;

import static org.junit.Assert.*;

import org.junit.*;

import com.philhanna.flashcards.*;
import com.philhanna.flashcards.deck.CardImpl;

public class TestSessionCardImpl extends BaseTest {

   private static boolean firstTime = true;
   private SessionCard card = null;

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
      if (!firstTime)
         return;
      firstTime = false;
      BaseTest.setUpBeforeClass();
   }

   @Before
   public void setUp() throws Exception {
      card = new SessionCardImpl(new CardImpl("Who is buried in Grant's Tomb?", "Grant"));
   }

   @Test
   public void getStatisticsAlwaysReturnsNonNull() {
      assertNotNull(card.getStatistics());
   }
   
   @Test
   public void getHistoryIsClearFirstTime() {
      CardStatistics cs = card.getStatistics();
      CardHistory ch = cs.getHistory();
      assertNotNull(ch);
   }

   @Test
   public void testMarkRight() {
      fail("Not yet implemented");
   }

   @Test
   public void testMarkWrong() {
      fail("Not yet implemented");
   }

}
