package com.philhanna.flashcards.adapter.out;

import java.io.File;

import com.philhanna.flashcards.adapter.out.sqlite.SqliteDeckLoader;
import com.philhanna.flashcards.adapter.out.xml.XmlDeckLoader;
import com.philhanna.flashcards.port.out.DeckLoader;

/**
 * Returns the appropriate {@link DeckLoader} for a given file based on
 * its extension.
 * <ul>
 *   <li>{@code .xml} → {@link XmlDeckLoader}</li>
 *   <li>{@code .db} / {@code .sqlite} → {@link SqliteDeckLoader}</li>
 * </ul>
 */
public class DeckLoaderFactory {

   private DeckLoaderFactory() {}

   /**
    * Returns a {@link DeckLoader} appropriate for the given file.
    * @param file the deck file to be loaded
    * @return a loader that can handle the file's format
    * @throws IllegalArgumentException if the file extension is not recognised
    */
   public static DeckLoader forFile(File file) {
      final String name = file.getName().toLowerCase();
      if (name.endsWith(".xml")) {
         return new XmlDeckLoader();
      }
      if (name.endsWith(".db") || name.endsWith(".sqlite")) {
         return new SqliteDeckLoader();
      }
      throw new IllegalArgumentException(
            "Unsupported deck file format: " + file.getName());
   }
}
