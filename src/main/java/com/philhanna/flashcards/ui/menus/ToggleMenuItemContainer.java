package com.philhanna.flashcards.ui.menus;

import java.awt.event.ActionEvent;

import com.philhanna.flashcards.ui.Main;

/**
 * A menu item on the view menu that causes the card display to toggle between
 * answer/question and question/answer. This menu item differs from others in
 * that its text changes as it is toggled.
 */
public class ToggleMenuItemContainer extends MenuItemContainer {

   public static final String QUESTIONS = "Show questions";
   public static final String ANSWERS = "Show answers";

   private boolean showingQuestions = true;

   /**
    * Creates a new ToggleMenuItemContainer
    */
   public ToggleMenuItemContainer(Main main) {
      super(main, MenuItems.TOGGLE.getMenuItem());
   }

   /**
    * Calls the doToggle() method in main when the menu item is selected
    * @see com.philhanna.flashcards.ui.Main#doOpen()
    */
   public void actionPerformed(ActionEvent e) {
      main.doToggle(this.showingQuestions);
      showingQuestions = !showingQuestions;
      menuItem.setText((this.showingQuestions) ? ANSWERS : QUESTIONS);
   }
}
