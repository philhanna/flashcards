package com.philhanna.flashcards.deck;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import com.philhanna.flashcards.*;

/**
 * Unit tests for CardImpl
 */
public class TestCardImpl extends BaseTest {

   // ==================================================================
   // Unit tests
   // ==================================================================

   @Test
   public void toggleSwitchesQuestionAndAnswer() {
      Card card = new CardImpl("111", "222");
      card.toggle();
      assertEquals("Question is unexpected", "222", card.getQuestion());
      assertEquals("Answer is unexpected", "111", card.getAnswer());
   }

}
