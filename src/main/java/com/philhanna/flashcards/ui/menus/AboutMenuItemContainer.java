package com.philhanna.flashcards.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.ui.Main;

/**
 * A menu item on the help menu that displays the help about dialog box to be displayed
 */
public class AboutMenuItemContainer extends MenuItemContainer {

   /**
    * Creates a new ResetMenuItemContainer
    */
   public AboutMenuItemContainer(Main main) {
      super(main, MenuItems.ABOUT.getMenuItem());
   }

   /**
    * Calls the doResetSize() method in main when the menu item is selected
    * @see com.philhanna.flashcards.ui.Main#doResetSize()
    */
   public void actionPerformed(ActionEvent e) {
      main.doHelpAbout();
   }
}
