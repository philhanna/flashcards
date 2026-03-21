package com.philhanna.flashcards.adapter.in.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.adapter.in.ui.Main;

/**
 * A menu item on the file menu that causes the deck to be started over from the
 * beginning in a new session
 */
public class RestartMenuItem extends AbstractMenuItem {

   /**
    * Creates a new RestartMenuItem
    */
   public RestartMenuItem(Main main) {
      super(main, MenuItems.RESTART.getMenuItem());
      this.menuItem.setEnabled(false);
   }

   /**
    * Calls the doRestart() method in main when the menu item is selected
    * @see com.philhanna.flashcards.adapter.in.ui.Main#doRestart()
    */
   public void actionPerformed(ActionEvent e) {
      main.doRestart();
   }
}
