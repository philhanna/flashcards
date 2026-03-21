package com.philhanna.flashcards.adapter.in.ui.menus;

import javax.swing.JMenu;

import com.philhanna.flashcards.adapter.in.ui.Main;

/**
 * Abstract base class for all menu containers. Handles the
 * {@link #getComponent()} method.
 */
public abstract class AbstractMenu {

   protected final Main main;
   protected final JMenu menu;

   /**
    * Default constructor
    */
   public AbstractMenu(Main main, JMenu menu) {
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