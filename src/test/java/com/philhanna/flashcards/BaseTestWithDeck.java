package com.philhanna.flashcards;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.BeforeEach;
import org.w3c.dom.Document;

import com.philhanna.flashcards.deck.XmlDeck;

/**
 * Abstract base class for unit tests that work with a loaded test deck
 */
public abstract class BaseTestWithDeck extends BaseTest {

   // ==================================================================
   // Constants and class variables
   // ==================================================================

   private static final String TEST_DECK = "/Best_Picture_Awards.xml";

   // ==================================================================
   // Instance variables
   // ==================================================================

   protected Deck deck;

   // ==================================================================
   // Fixtures
   // ==================================================================

   @BeforeEach
   public void setUp() throws Exception {
      super.setUp();
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      final DocumentBuilder db = dbf.newDocumentBuilder();
      final InputStream inputStream = getClass().getResourceAsStream(TEST_DECK);
      final Document doc = db.parse(inputStream);
      deck = new XmlDeck(doc);
   }

}
