package com.philhanna.flashcards.adapter.in.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.adapter.in.ui.Main;

/**
 * A menu item on the help menu that displays the help about dialog box to be displayed
 */
public class AboutMenuItem extends AbstractMenuItem {

   /**
    * Creates a new ResetSizeMenuItem
    */
   public AboutMenuItem(Main main) {
      super(main, MenuItems.ABOUT.getMenuItem());
   }

   /**
    * Calls the doResetSize() method in main when the menu item is selected
    * @see com.philhanna.flashcards.adapter.in.ui.Main#doResetSize()
    */
   public void actionPerformed(ActionEvent e) {
      main.doHelpAbout();
   }
}
