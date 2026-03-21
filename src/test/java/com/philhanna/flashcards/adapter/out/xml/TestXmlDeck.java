package com.philhanna.flashcards.adapter.out.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.List;

import javax.xml.parsers.*;

import org.junit.jupiter.api.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.philhanna.flashcards.domain.*;

/**
 * Unit tests for XmlDeck
 */
public class TestXmlDeck extends BaseTest {

   // ==================================================================
   // Class constants and variables
   // ==================================================================

   private static final String TEST_DECK = "/Best_Picture_Awards.xml";

   // ==================================================================
   // Instance variables
   // ==================================================================

   private Document doc;

   // ==================================================================
   // Fixtures
   // ==================================================================

   @BeforeEach
   public void setUp() throws Exception {
      super.setUp();
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      final DocumentBuilder db = dbf.newDocumentBuilder();
      final InputStream inputStream = getClass().getResourceAsStream(TEST_DECK);
      doc = db.parse(inputStream);
   }

   @AfterEach
   public void tearDown() throws Exception {
      super.tearDown();
   }

   // ==================================================================
   // Unit tests
   // ==================================================================

   @Test
   void getsCardsFromXML() throws SAXException, ApplicationException {
      List<Card> cardList = XmlDeck.getCardsFromXML(doc);
      assertNotNull(cardList);
      assertEquals("1928", cardList.get(0).getQuestion());
      assertEquals("Wings", cardList.get(0).getAnswer());
      boolean found = false;
      for (Card card : cardList) {
         if (card.getQuestion().equals("2002")) {
            found = true;
            assertEquals("Chicago", card.getAnswer());
            break;
         }
      }
      assertTrue(found, "No 2002 card found");
   }

   @Test
   void getsTitleFromXML() throws SAXException {
      final String expected = "Academy Award-winning Best Pictures";
      final String actual = XmlDeck.getTitleFromXML(doc);
      assertEquals(expected, actual);
   }

   @Test
   void getsCards() throws SAXException, ApplicationException {
      final Deck deck = new XmlDeck(doc);
      final List<Card> cardList = deck.getCards();
      final Card firstCard = cardList.get(0);
      final String actual = firstCard.getQuestion();
      final String expected = "1928";
      assertEquals(expected, actual);
   }

   @Test
   void throwsExceptionForEmptyDeck() throws Exception {
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      final DocumentBuilder db = dbf.newDocumentBuilder();
      final Document emptyDoc = db.newDocument();
      assertThrows(EmptyDeckException.class, () -> new XmlDeck(emptyDoc));
   }

   @Test
   void throwsExceptionForMissingQuestion() throws SAXException {
      final Element elemCard = (Element) doc.getElementsByTagName("card").item(2);
      final Element elemQuestion = (Element) elemCard.getElementsByTagName("question").item(0);
      elemCard.removeChild(elemQuestion);
      ApplicationException ex = assertThrows(ApplicationException.class, () -> new XmlDeck(doc));
      assertEquals("No question found on card 3 in deck", ex.getMessage());
   }

   @Test
   void throwsExceptionForMissingAnswer() throws SAXException {
      final Element elemCard = (Element) doc.getElementsByTagName("card").item(2);
      final Element elemAnswer = (Element) elemCard.getElementsByTagName("answer").item(0);
      elemCard.removeChild(elemAnswer);
      ApplicationException ex = assertThrows(ApplicationException.class, () -> new XmlDeck(doc));
      assertEquals("No answer found on card 3 in deck", ex.getMessage());
   }

   @Test
   void getsTitle() throws SAXException, ApplicationException {
      final Deck deck = new XmlDeck(doc);
      final String expected = "Academy Award-winning Best Pictures";
      final String actual = deck.getTitle();
      assertEquals(expected, actual);
   }

}
