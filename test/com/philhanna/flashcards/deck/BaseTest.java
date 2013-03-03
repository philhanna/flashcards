package com.philhanna.flashcards.deck;

import java.io.File;

import org.junit.BeforeClass;

public abstract class BaseTest {

   protected static File testdata;
   
   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
      final File projectRoot = new File(".").getCanonicalFile();
      testdata = new File(projectRoot, "/testdata");
   }
}
