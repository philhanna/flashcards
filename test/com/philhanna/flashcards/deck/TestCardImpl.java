package com.philhanna.flashcards.deck;

import static org.junit.Assert.assertEquals;

import org.junit.*;

import com.philhanna.flashcards.Card;

public class TestCardImpl extends BaseTest {

   @Test
   public void testToggle() {
      Card card = new CardImpl("111", "222");
      card.toggle();
      assertEquals("Question is unexpected", "222", card.getQuestion());
      assertEquals("Answer is unexpected", "111", card.getAnswer());
   }

}
