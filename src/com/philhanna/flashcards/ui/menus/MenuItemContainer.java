package com.philhanna.flashcards.ui.menus;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.philhanna.flashcards.ui.Main;

/**
 * Abstract base class for all menu item containers. Handles the
 * {@link #getComponent()} method and sets up the action listener for the menu
 * item (which is implemented by subclasses).
 */
public abstract class MenuItemContainer implements ActionListener {

   protected final Main main;
   protected final JMenuItem menuItem;

   /**
    * Default constructor
    */
   public MenuItemContainer(Main main, JMenuItem menuItem) {
      super();
      this.main = main;
      this.menuItem = menuItem;
      this.menuItem.addActionListener(this);
   }

   /**
    * Returns the underlying menu item to the file menu
    */
   public JMenuItem getComponent() {
      return menuItem;
   }

}