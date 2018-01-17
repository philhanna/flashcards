package com.philhanna.flashcards;

import java.io.IOException;
import java.util.Properties;

import com.philhanna.flashcards.ui.Main;

/**
 * Contains configurable properties for the whole application
 */
public class Configuration {

   public static final Properties config = new Properties();

   public static final String TEXT_EDITOR;
   public static final String CARD_ICON_FILE_NAME;
   public static final int X;
   public static final int Y;
   public static final int WIDTH;
   public static final int HEIGHT;
   public static final String LOOK_AND_FEEL;
   
   static {
      try {
         config.load(Main.class.getResourceAsStream("/config.properties"));
      }
      catch (IOException e) {
         System.out.println("Could not load config.properties");
         e.printStackTrace();
      }
      CARD_ICON_FILE_NAME = config.getProperty("card_icon", "/cardicon.png");
      X = Integer.parseInt(config.getProperty("x", "50"));
      Y = Integer.parseInt(config.getProperty("y", "50"));
      WIDTH = Integer.parseInt(config.getProperty("width", "600"));
      HEIGHT = Integer.parseInt(config.getProperty("height", "400"));
      TEXT_EDITOR = config.getProperty("text_editor", "gvim");
      LOOK_AND_FEEL = config.getProperty("look_and_feel", "javax.swing.plaf.metal.MetalLookAndFeel");
   }
}
