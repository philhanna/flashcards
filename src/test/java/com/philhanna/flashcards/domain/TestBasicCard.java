package com.philhanna.flashcards.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for BasicCard
 */
public class TestBasicCard extends BaseTest {

   @Test
   void getsQuestion() {
      Card card = new BasicCard("Q", "A");
      assertEquals("Q", card.getQuestion());
   }

   @Test
   void getsAnswer() {
      Card card = new BasicCard("Q", "A");
      assertEquals("A", card.getAnswer());
   }
}
