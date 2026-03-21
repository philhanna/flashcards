package com.philhanna.flashcards.adapter.in.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.adapter.in.ui.Main;

/**
 * A menu item on the file menu that causes application to exit
 */
public class ExitMenuItem extends AbstractMenuItem {

   /**
    * Creates a new ExitMenuItem
    */
   public ExitMenuItem(Main main) {
      super(main, MenuItems.EXIT.getMenuItem());
   }

   /**
    * Calls the doExit() method in main when the menu item is selected
    * @see com.philhanna.flashcards.adapter.in.ui.Main#doExit()
    */
   public void actionPerformed(ActionEvent e) {
      main.doExit();
   }
}
