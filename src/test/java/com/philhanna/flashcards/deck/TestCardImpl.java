package com.philhanna.flashcards.deck;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.philhanna.flashcards.BaseTest;
import com.philhanna.flashcards.Card;

/**
 * Unit tests for CardImpl
 */
public class TestCardImpl extends BaseTest {

   // ==================================================================
   // Unit tests
   // ==================================================================

   @Test
   void toggleSwitchesQuestionAndAnswer() {
      Card card = new CardImpl("111", "222");
      card.toggle();
      assertEquals("222", card.getQuestion(), "Question is unexpected");
      assertEquals("111", card.getAnswer(), "Answer is unexpected");
   }

}
