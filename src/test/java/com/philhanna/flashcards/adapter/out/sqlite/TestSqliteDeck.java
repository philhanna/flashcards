package com.philhanna.flashcards.adapter.out.sqlite;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.philhanna.flashcards.domain.*;

/**
 * Unit tests for SqliteDeck
 */
public class TestSqliteDeck {

   @TempDir
   File tempDir;

   // ------------------------------------------------------------------
   // Happy path
   // ------------------------------------------------------------------

   @Test
   void loadsTitleCorrectly() throws Exception {
      File db = createDb("My Deck",
            new String[][] {{"Q1", "A1"}});
      assertEquals("My Deck", new SqliteDeck(db).getTitle());
   }

   @Test
   void fallsBackToUntitledWhenDeckTableIsEmpty() throws Exception {
      File db = createDbNoTitle(new String[][] {{"Q1", "A1"}});
      assertEquals("Untitled", new SqliteDeck(db).getTitle());
   }

   @Test
   void loadsCorrectNumberOfCards() throws Exception {
      File db = createDb("Deck", new String[][] {
            {"Q1", "A1"}, {"Q2", "A2"}, {"Q3", "A3"}});
      assertEquals(3, new SqliteDeck(db).getCards().size());
   }

   @Test
   void loadsCardsWithCorrectContent() throws Exception {
      File db = createDb("Deck", new String[][] {{"Capital?", "Paris"}});
      var card = new SqliteDeck(db).getCards().get(0);
      assertEquals("Capital?", card.getQuestion());
      assertEquals("Paris",    card.getAnswer());
   }

   @Test
   void loadsCardsInPositionOrder() throws Exception {
      // Insert in reverse position order to verify ORDER BY position is applied
      File db = tempDir.toPath().resolve("order.db").toFile();
      try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + db)) {
         conn.createStatement().executeUpdate(
               "CREATE TABLE deck (title TEXT NOT NULL);" +
               "CREATE TABLE card (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
               "  question TEXT NOT NULL, answer TEXT NOT NULL, " +
               "  position INTEGER NOT NULL DEFAULT 0);");
         conn.createStatement().executeUpdate("INSERT INTO deck VALUES ('Test')");
         try (PreparedStatement ps = conn.prepareStatement(
               "INSERT INTO card (question, answer, position) VALUES (?, ?, ?)")) {
            ps.setString(1, "Second"); ps.setString(2, "B"); ps.setInt(3, 2); ps.executeUpdate();
            ps.setString(1, "First");  ps.setString(2, "A"); ps.setInt(3, 1); ps.executeUpdate();
            ps.setString(1, "Third");  ps.setString(2, "C"); ps.setInt(3, 3); ps.executeUpdate();
         }
      }
      var cards = new SqliteDeck(db).getCards();
      assertEquals("First",  cards.get(0).getQuestion());
      assertEquals("Second", cards.get(1).getQuestion());
      assertEquals("Third",  cards.get(2).getQuestion());
   }

   // ------------------------------------------------------------------
   // Validation errors
   // ------------------------------------------------------------------

   @Test
   void throwsEmptyDeckException() throws Exception {
      File db = createDb("Empty", new String[0][]);
      assertThrows(EmptyDeckException.class, () -> new SqliteDeck(db));
   }

   @Test
   void throwsInvalidCardExceptionForBlankQuestion() throws Exception {
      File db = createDb("Deck", new String[][] {{"", "A1"}});
      assertThrows(InvalidCardException.class, () -> new SqliteDeck(db));
   }

   @Test
   void throwsInvalidCardExceptionForBlankAnswer() throws Exception {
      File db = createDb("Deck", new String[][] {{"Q1", ""}});
      assertThrows(InvalidCardException.class, () -> new SqliteDeck(db));
   }

   @Test
   void throwsApplicationExceptionForNonDatabaseFile() throws Exception {
      File bad = tempDir.toPath().resolve("bad.db").toFile();
      bad.createNewFile(); // empty file — not a valid SQLite database
      assertThrows(ApplicationException.class, () -> new SqliteDeck(bad));
   }

   // ------------------------------------------------------------------
   // Helpers
   // ------------------------------------------------------------------

   private File createDb(String title, String[][] cards) throws Exception {
      File db = tempDir.toPath().resolve(title.replace(" ", "_") + ".db").toFile();
      try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + db)) {
         conn.createStatement().executeUpdate(
               "CREATE TABLE deck (title TEXT NOT NULL);" +
               "CREATE TABLE card (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
               "  question TEXT NOT NULL, answer TEXT NOT NULL, " +
               "  position INTEGER NOT NULL DEFAULT 0);");
         try (PreparedStatement ps = conn.prepareStatement(
               "INSERT INTO deck (title) VALUES (?)")) {
            ps.setString(1, title);
            ps.executeUpdate();
         }
         try (PreparedStatement ps = conn.prepareStatement(
               "INSERT INTO card (question, answer, position) VALUES (?, ?, ?)")) {
            for (int i = 0; i < cards.length; i++) {
               ps.setString(1, cards[i][0]);
               ps.setString(2, cards[i][1]);
               ps.setInt(3, i + 1);
               ps.executeUpdate();
            }
         }
      }
      return db;
   }

   /** Creates a db with no row in the deck table (title falls back to Untitled) */
   private File createDbNoTitle(String[][] cards) throws Exception {
      File db = tempDir.toPath().resolve("notitle.db").toFile();
      try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + db)) {
         conn.createStatement().executeUpdate(
               "CREATE TABLE deck (title TEXT NOT NULL);" +
               "CREATE TABLE card (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
               "  question TEXT NOT NULL, answer TEXT NOT NULL, " +
               "  position INTEGER NOT NULL DEFAULT 0);");
         try (PreparedStatement ps = conn.prepareStatement(
               "INSERT INTO card (question, answer, position) VALUES (?, ?, ?)")) {
            for (int i = 0; i < cards.length; i++) {
               ps.setString(1, cards[i][0]);
               ps.setString(2, cards[i][1]);
               ps.setInt(3, i + 1);
               ps.executeUpdate();
            }
         }
      }
      return db;
   }
}
