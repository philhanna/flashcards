package com.philhanna.flashcards;

import java.io.File;

import javax.xml.parsers.*;

import org.junit.*;
import org.w3c.dom.Document;

import com.philhanna.flashcards.deck.DeckImpl;

/**
 * Abstract base class for unit tests that work with a loaded test deck
 */
public abstract class BaseTestWithDeck extends BaseTest {

   // ==================================================================
   // Constants and class variables
   // ==================================================================

   private static final String TEST_DECK = "Best_Picture_Awards.flc";
   private static boolean firstTime = true;
   private static Document doc;

   // ==================================================================
   // Constants and class variables
   // ==================================================================

   protected Deck deck;

   // ==================================================================
   // Fixtures
   // ==================================================================

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {

      // Run only once

      if (!firstTime)
         return;
      firstTime = false;

      // Call super

      BaseTest.setUpBeforeClass();

      // Load the test deck XML into a DOM object. The deck itself will
      // be constructed each time a test is run, since it is not
      // immutable

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      File inputFile = new File(testdata, TEST_DECK);
      doc = db.parse(inputFile);
   }

   @Before
   public void setUp() throws Exception {
      deck = new DeckImpl(doc);
   }

}
