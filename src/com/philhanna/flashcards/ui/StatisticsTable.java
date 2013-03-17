package com.philhanna.flashcards.ui;

import java.awt.Component;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.philhanna.flashcards.CardStatistics;
import com.philhanna.flashcards.session.*;

/**
 * A container for the session statistics,
 * <p>
 * <img src="doc-files/StatisticsTable-1.png"/>
 * <p>
 * @see StatisticsPanelContainer
 */
public class StatisticsTable {

   // ==========================================================
   // Class variables and constants
   // ==========================================================
   
   private static final DecimalFormat PCTFMT = new DecimalFormat("###.00%");
   private static final DecimalFormat AVGFMT = new DecimalFormat("##0.## seconds");

   // ==========================================================
   // Class methods
   // ==========================================================

   private String getStatisticsAsHTML(Session session) {

      // Get the list of cards viewed in this session

      List<SessionCard> cards = session.getViewedCards();
      int n = cards.size();

      // The denominator for the statistics is the number of cards plus
      // the number of times they were missed
      
      double denom = n;

      // Count the cards in one of four categories, depending on how
      // many times they were missed

      int[] count = new int[4];
      for (SessionCard card : cards) {
         CardStatistics stats = card.getStatistics();
         int missedTimes = stats.getTimesAnsweredWrong();
         denom += missedTimes;
         switch (missedTimes) {
            case 0:
               count[0] += 1;
               break;
            case 1:
               count[1] += 1;
               break;
            case 2:
               count[2] += 1;
               break;
            default:
               count[3] += 1;
               break;
         }
      }

      final double rating = n / denom;
      final String ratingString = PCTFMT.format(rating);
      
      final double avg = session.getElapsedTime() / session.getCardViewCount();
      final String avgString = AVGFMT.format(avg);

      final StringBuffer sb = new StringBuffer();
      sb.append("<html>");
      sb.append("<head><style>td {font-size: 16pt;}</style></head>");
      sb
            .append("<table border='0' cellpadding='0' cellspacing='3' width='500'>");
      sb.append(addRow("Number of cards:", n, n));
      sb.append(addRow("Number answered correctly:", count[0], n));
      sb.append(addRow("Number missed once:", count[1], n));
      sb.append(addRow("Number missed twice:", count[2], n));
      sb.append(addRow("Number missed more than twice:", count[3], n));
      sb.append(addRow("Average time per card:", avgString));
      sb.append(addSeparator());
      sb.append(addRow("Rating:", ratingString));
      sb.append("</table>");
      sb.append("</html>");
      final String text = sb.toString();
      return text;
   }

   private String addRow(String label, int k, int n) {
      double denom = n;
      double pct = k / denom;
      String pctString = PCTFMT.format(pct);
      StringBuffer sb = new StringBuffer();
      sb.append("<tr>");
      sb.append("<td align='left'>").append(label).append("</td>");
      sb.append("<td align='right'>").append(k).append("</td>");
      sb.append("<td align='right'>").append(pctString).append("</td>");
      sb.append("</tr>");
      String text = sb.toString();
      return text;
   }

   private Object addSeparator() {
      StringBuffer sb = new StringBuffer();
      sb.append("<tr>");
      sb.append("<td colspan='3'>").append("<hr/>").append("</td>");
      sb.append("</tr>");
      String text = sb.toString();
      return text;
   }

   private Object addRow(String label, String value) {
      final StringBuffer sb = new StringBuffer();
      sb.append("<tr>");
      sb.append("<td colspan='2' align='left'>").append(label).append("</td>");
      sb.append("<td align='right'>").append(value).append("</td>");
      sb.append("</tr>");
      final String text = sb.toString();
      return text;
   }

   // ==========================================================
   // Instance variables
   // ==========================================================

   private JPanel panel;
   private JLabel table;

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new statistics table
    * @param sc the session container
    */
   public StatisticsTable(SessionContainer sc) {
      this.panel = new JPanel();
      String text = getStatisticsAsHTML(sc.getSession());
      this.table = new JLabel(text);
      this.panel.add(this.table);
   }

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Returns the statistics table to the statistics panel container
    * @see StatisticsPanelContainer
    */
   public Component getComponent() {
      return this.panel;
   }
}
