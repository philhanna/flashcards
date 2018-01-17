package com.philhanna.flashcards.ui.menus;

import javax.swing.JMenu;

import com.philhanna.flashcards.ui.Main;

/**
 * Abstract base class for all menu containers. Handles the
 * {@link #getComponent()} method.
 */
public abstract class MenuContainer {

   protected final Main main;
   protected final JMenu menu;

   /**
    * Default constructor
    */
   public MenuContainer(Main main, JMenu menu) {
      super();
      this.main = main;
      this.menu = menu;
   }

   /**
    * Returns the underlying menu to the menu bar
    */
   public JMenu getComponent() {
      return menu;
   }

}