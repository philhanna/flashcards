package com.philhanna.flashcards.ui.menus;

import javax.swing.JMenuItem;

/**
 * The set of all menu items. Each one contains the actual
 * {@link JMenuItem} that it corresponds to.
 */
public enum MenuItems {
   OPEN(new JMenuItem("Open")),
   EDIT(new JMenuItem("Edit")),
   RESTART(new JMenuItem("Restart")),
   EXIT( new JMenuItem("Exit")),
   TOGGLE(new JMenuItem(ToggleMenuItemContainer.ANSWERS)),
   RESET(new JMenuItem("Reset window size")),
   ABOUT(new JMenuItem("About")),
   ;

   private JMenuItem menuItem;

   private MenuItems(JMenuItem menuItem) {
      this.menuItem = menuItem;
   }

   /**
    * Returns the menu item associated with this value
    */
   public JMenuItem getMenuItem() {
      return menuItem;
   }
}
