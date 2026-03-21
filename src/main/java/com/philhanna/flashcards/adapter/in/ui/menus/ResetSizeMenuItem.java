package com.philhanna.flashcards.adapter.in.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.adapter.in.ui.Main;

/**
 * A menu item on the view menu that causes the default window size and position
 * to be restored
 */
public class ResetSizeMenuItem extends AbstractMenuItem {

   /**
    * Creates a new ResetSizeMenuItem
    */
   public ResetSizeMenuItem(Main main) {
      super(main, MenuItems.RESET.getMenuItem());
   }

   /**
    * Calls the doResetSize() method in main when the menu item is selected
    * @see com.philhanna.flashcards.adapter.in.ui.Main#doResetSize()
    */
   public void actionPerformed(ActionEvent e) {
      main.doResetSize();
   }
}
