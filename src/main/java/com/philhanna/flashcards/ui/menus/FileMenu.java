package com.philhanna.flashcards.ui.menus;

import javax.swing.JMenu;

import com.philhanna.flashcards.ui.Main;

/**
 * The first menu. Constructed by the {@link AppMenuBar}.
 * @see OpenMenuItem
 * @see RestartMenuItem
 * @see ExitMenuItem
 */
public class FileMenu extends AbstractMenu {

   /**
    * Creates a new FileMenu and adds menu items to it
    * @param main
    */
   public FileMenu(Main main) {
      super(main, new JMenu("File"));
      menu.add(new OpenMenuItem(main).getComponent());
      menu.add(new EditMenuItem(main).getComponent());
      menu.add(new RestartMenuItem(main).getComponent());
      menu.addSeparator();
      menu.add(new ExitMenuItem(main).getComponent());
   }

   /**
    * Returns the completed menu to the menu bar container
    * @see AppMenuBar
    */
   public JMenu getComponent() {
      return menu;
   }
}
