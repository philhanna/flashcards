package com.philhanna.flashcards.adapter.in.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Contains configurable properties for the whole application.
 * <p>
 * Defaults are loaded from {@code sample.properties} bundled in the jar.
 * User overrides are loaded from the OS-appropriate config file:
 * <ul>
 *   <li>Linux: {@code $XDG_CONFIG_HOME/flashcards/config.properties}
 *       (defaults to {@code ~/.config/flashcards/config.properties})</li>
 *   <li>Windows: {@code %APPDATA%\flashcards\config.properties}</li>
 *   <li>macOS: {@code ~/Library/Application Support/flashcards/config.properties}</li>
 * </ul>
 * <p>
 * Call {@link #save(String, String)} to persist a changed value back to the
 * user config file.
 */
public class Configuration {

   public static final Properties config = new Properties();

   public static final String DECK_FORMAT;
   public static final String TEXT_EDITOR;
   public static final String CARD_ICON_FILE_NAME;
   public static final int X;
   public static final int Y;
   public static final int WIDTH;
   public static final int HEIGHT;
   public static final String LOOK_AND_FEEL;

   static {
      // Load bundled defaults
      try {
         config.load(Configuration.class.getResourceAsStream("/sample.properties"));
      }
      catch (IOException e) {
         System.out.println("Could not load sample.properties from jar");
         e.printStackTrace();
      }

      // Overlay with user config file if it exists
      File userConfig = getUserConfigFile();
      if (userConfig.exists()) {
         try (FileInputStream fis = new FileInputStream(userConfig)) {
            config.load(fis);
         }
         catch (IOException e) {
            System.out.println("Could not load user config: " + userConfig);
            e.printStackTrace();
         }
      }

      DECK_FORMAT = config.getProperty("deck_format", "xml").toLowerCase();
      CARD_ICON_FILE_NAME = config.getProperty("card_icon", "/cardicon.png");
      X = Integer.parseInt(config.getProperty("x", "50"));
      Y = Integer.parseInt(config.getProperty("y", "50"));
      WIDTH = Integer.parseInt(config.getProperty("width", "600"));
      HEIGHT = Integer.parseInt(config.getProperty("height", "400"));
      TEXT_EDITOR = config.getProperty("text_editor", "gvim");
      LOOK_AND_FEEL = config.getProperty("look_and_feel", "javax.swing.plaf.metal.MetalLookAndFeel");
   }

   /**
    * Updates a property in memory and writes the full config back to the user
    * config file, creating it and its parent directory if necessary.
    *
    * @param key   the property key
    * @param value the new value
    */
   public static void save(String key, String value) {
      config.setProperty(key, value);
      File file = getUserConfigFile();
      file.getParentFile().mkdirs();
      try (FileOutputStream fos = new FileOutputStream(file)) {
         config.store(fos, "Flashcards configuration");
      }
      catch (IOException e) {
         System.out.println("Could not save config: " + file);
         e.printStackTrace();
      }
   }

   /**
    * Returns the OS-appropriate user configuration file path.
    */
   public static File getUserConfigFile() {
      String os = System.getProperty("os.name", "").toLowerCase();
      String home = System.getProperty("user.home");

      String configDir;
      if (os.contains("win")) {
         String appData = System.getenv("APPDATA");
         configDir = (appData != null ? appData : home) + File.separator + "flashcards";
      }
      else if (os.contains("mac")) {
         configDir = home + "/Library/Application Support/flashcards";
      }
      else {
         // Linux / Unix — honour XDG_CONFIG_HOME
         String xdg = System.getenv("XDG_CONFIG_HOME");
         configDir = ((xdg != null && !xdg.isEmpty()) ? xdg : home + "/.config")
               + "/flashcards";
      }

      return new File(configDir, "config.properties");
   }
}
