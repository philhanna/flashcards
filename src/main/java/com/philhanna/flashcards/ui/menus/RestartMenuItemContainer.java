package com.philhanna.flashcards.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.ui.Main;

/**
 * A menu item on the file menu that causes the deck to be started over from the
 * beginning in a new session
 */
public class RestartMenuItemContainer extends MenuItemContainer {

   /**
    * Creates a new RestartMenuItemContainer
    */
   public RestartMenuItemContainer(Main main) {
      super(main, MenuItems.RESTART.getMenuItem());
      this.menuItem.setEnabled(false);
   }

   /**
    * Calls the doRestart() method in main when the menu item is selected
    * @see com.philhanna.flashcards.ui.Main#doRestart()
    */
   public void actionPerformed(ActionEvent e) {
      main.doRestart();
   }
}
