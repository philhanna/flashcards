package com.philhanna.flashcards.adapter.in.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * A container for a panel that is displayed at the end of a deck,
 * showing the session statistics.  These include:
 * <ul>
 * <li>The number of cards in the deck</li>
 * <li>The number answered correctly</li>
 * <li>The number missed once</li>
 * <li>The number missed twice</li>
 * <li>The number missed more than twice</li>
 * </ul>
 * The panel consists of an upper part with the table of statistics and
 * a button panel along the bottom.
 * @see StatisticsTable
 * @see SummaryButtonPanel
 */
public class SummaryPanel {
   private JPanel panel;

   /**
    * Creates a new SummaryPanel
    * @param sc the session container
    */
   public SummaryPanel(SessionPanel sc) {
      this.panel = new JPanel();
      this.panel.setBorder(BorderFactory
            .createTitledBorder("Session Statistics"));
      this.panel.setLayout(new BorderLayout());
      this.panel.add(new StatisticsTable(sc).getComponent(), "Center");
      this.panel.add(new SummaryButtonPanel(sc).getComponent(), "South");
   }

   /**
    * Returns the contained panel to the parent session container
    * @see SessionPanel#displayStatistics()
    */
   public Component getComponent() {
      return this.panel;
   }
}
