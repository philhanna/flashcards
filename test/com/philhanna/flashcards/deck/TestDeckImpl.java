package com.philhanna.flashcards.deck;

import static org.junit.Assert.*;

import java.io.*;
import java.util.List;

import javax.xml.parsers.*;

import org.junit.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.philhanna.flashcards.*;

public class TestDeckImpl extends BaseTest {

   private static boolean firstTime = true;
   private static DocumentBuilderFactory dbf;
   private static DocumentBuilder db;
   private static File inputFile;
   private static Document doc;

   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
      if (!firstTime)
         return;
      firstTime = false;
      BaseTest.setUpBeforeClass();
      dbf = DocumentBuilderFactory.newInstance();
      db = dbf.newDocumentBuilder();
      inputFile = new File(testdata, "Best_Picture_Awards.xml");
      doc = db.parse(inputFile);
   }

   @Test
   public void getsCardsFromXML() throws SAXException, ApplicationException {
      List<Card> cardList = DeckImpl.getCardsFromXML(doc);
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
      assertTrue("No 2002 card found", found);
   }

   @Test
   public void getsTitleFromXML() throws SAXException {
      final String expected = "Academy Award-winning Best Pictures";
      final String actual = DeckImpl.getTitleFromXML(doc);
      assertEquals(expected, actual);
   }

   @Test
   public void getsCards() throws SAXException, ApplicationException {
      final Deck deck = new DeckImpl(doc);
      final List<Card> cardList = deck.getCards();
      final Card firstCard = cardList.get(0);
      final String actual = firstCard.getQuestion();
      final String expected = "1928";
      assertEquals(expected, actual);
   }

   @Test
   public void throwsExceptionForEmptyDeck() throws Exception {
      Document emptyDoc = db.newDocument();
      try {
         new DeckImpl(emptyDoc);
         fail("Should have thrown EmptyDeckException");
      }
      catch (EmptyDeckException e) {
         // This is the expected behavior
      }
   }

   private static final Document reloadDocument() throws SAXException, IOException {
      return db.parse(inputFile);
   }

   @Test
   public void throwsExceptionForMissingQuestion() throws SAXException, IOException {
      final Document doc = reloadDocument();
      final Element elemCard = (Element) doc.getElementsByTagName("card").item(2);
      final Element elemQuestion = (Element) elemCard.getElementsByTagName("question").item(0);
      elemCard.removeChild(elemQuestion);
      try {
         new DeckImpl(doc);
         fail("Should have thrown InvalidCardException");
      }
      catch (ApplicationException e) {
         final String expected = "No question found on card 3 in deck";
         final String actual = e.getMessage();
         assertEquals(expected, actual);
      }
   }

   @Test
   public void throwsExceptionForMissingAnswer() throws SAXException, IOException {
      final Document doc = reloadDocument();
      final Element elemCard = (Element) doc.getElementsByTagName("card").item(2);
      final Element elemAnswer = (Element) elemCard.getElementsByTagName("answer").item(0);
      elemCard.removeChild(elemAnswer);
      try {
         new DeckImpl(doc);
         fail("Should have thrown InvalidCardException");
      }
      catch (ApplicationException e) {
         final String expected = "No answer found on card 3 in deck";
         final String actual = e.getMessage();
         assertEquals(expected, actual);
      }
   }

   @Test
   public void getsTitle() throws SAXException, ApplicationException {
      final Deck deck = new DeckImpl(doc);
      final String expected = "Academy Award-winning Best Pictures";
      final String actual = deck.getTitle();
      assertEquals(expected, actual);
   }

   @Test
   public void togglesEntireDeck() throws SAXException, ApplicationException {
      final Deck deck = new DeckImpl(doc);
      final List<Card> cardList = deck.getCards();
      final Card firstCard = cardList.get(0);

      // Toggle every card in the deck

      deck.toggle();
      assertEquals("Wings", firstCard.getQuestion());

      // Now toggle back and see if the card has been flipped

      deck.toggle();
      assertEquals("1928", firstCard.getQuestion());
   }

}
