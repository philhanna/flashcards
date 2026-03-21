package com.philhanna.flashcards.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.philhanna.flashcards.domain.BaseTest;
import com.philhanna.flashcards.domain.Card;

/**
 * Unit tests for BasicCard
 */
public class TestBasicCard extends BaseTest {

   // ==================================================================
   // Unit tests
   // ==================================================================

   @Test
   void toggleSwitchesQuestionAndAnswer() {
      Card card = new BasicCard("111", "222");
      card.toggle();
      assertEquals("222", card.getQuestion(), "Question is unexpected");
      assertEquals("111", card.getAnswer(), "Answer is unexpected");
   }

}
