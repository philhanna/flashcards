package com.philhanna.flashcards.adapter.out.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.philhanna.flashcards.domain.ApplicationException;
import com.philhanna.flashcards.domain.BasicCard;
import com.philhanna.flashcards.domain.Card;
import com.philhanna.flashcards.domain.Deck;
import com.philhanna.flashcards.domain.EmptyDeckException;
import com.philhanna.flashcards.domain.InvalidCardException;

/**
 * An implementation of {@link Deck} that reads a titled deck of cards
 * from a SQLite database file.
 * <p>
 * Expected schema:
 * <pre>
 * CREATE TABLE deck (title TEXT NOT NULL);
 * CREATE TABLE card (
 *   id       INTEGER PRIMARY KEY AUTOINCREMENT,
 *   question TEXT NOT NULL,
 *   answer   TEXT NOT NULL,
 *   position INTEGER NOT NULL DEFAULT 0
 * );
 * </pre>
 */
public class SqliteDeck implements Deck {

   private final String title;
   private final List<Card> cards;

   public SqliteDeck(File file) throws ApplicationException {
      final String url = "jdbc:sqlite:" + file.getAbsolutePath();
      try (Connection conn = DriverManager.getConnection(url)) {
         this.title = loadTitle(conn);
         this.cards = loadCards(conn);
      }
      catch (SQLException e) {
         throw new ApplicationException(
               "Failed to read SQLite deck: " + file.getName() + ": " + e.getMessage(), e);
      }
   }

   private static String loadTitle(Connection conn) throws SQLException {
      try (Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery("SELECT title FROM deck LIMIT 1")) {
         if (rs.next()) {
            return rs.getString("title");
         }
         return "Untitled";
      }
   }

   private static List<Card> loadCards(Connection conn)
         throws SQLException, ApplicationException {
      List<Card> list = new ArrayList<>();
      try (Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery(
               "SELECT question, answer FROM card ORDER BY position, id")) {
         int index = 0;
         while (rs.next()) {
            index++;
            String question = rs.getString("question");
            String answer = rs.getString("answer");
            if (question == null || question.isBlank()) {
               throw new InvalidCardException("No question found on card " + index + " in deck");
            }
            if (answer == null || answer.isBlank()) {
               throw new InvalidCardException("No answer found on card " + index + " in deck");
            }
            list.add(new BasicCard(question, answer));
         }
      }
      if (list.isEmpty()) {
         throw new EmptyDeckException("Empty deck - no cards found");
      }
      return list;
   }

   @Override
   public String getTitle() {
      return title;
   }

   @Override
   public List<Card> getCards() {
      return cards;
   }

}
