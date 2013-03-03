package com.philhanna.flashcards.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.prefs.Preferences;

import javax.swing.*;

import org.xml.sax.SAXException;

import com.philhanna.flashcards.*;
import com.philhanna.flashcards.ui.menus.MenuBarContainer;

/**
 * Entry point for flashcard program. This program creates the main application
 * frame and populates its menu bar and listeners. When a file is chosen from
 * the <code>File-&gt;Open</code> menu option , the program creates and starts a
 * <code>SessionContainer</code> for the file.
 * @see OpenDialogHandler
 * @see SessionContainer
 */
public class Main {

   // ==========================================================
   // Class variables and constants
   // ==========================================================

   public static final String VERSION = "2.1.1";
   
   // ==========================================================
   // Class methods
   // ==========================================================

   /**
    * Entry point, which creates a <code>Main</code> object and displays its
    * frame. If a file name is specified on the command line, opens that file
    * first.
    */
   public static final void main(String[] args) {
      Main main = new Main();
      if (args.length > 0) {
         String fileName = args[0];
         main.setFile(new File(fileName), false);
      }
      main.getFrame().setVisible(true);
   }

   // ==========================================================
   // Instance variables
   // ==========================================================

   private JFrame frame;
   private File file;
   private SessionContainer sc;

   // ==========================================================
   // Constructors
   // ==========================================================

   /**
    * Creates a new Main program
    */
   public Main() {
      try {
         UIManager.setLookAndFeel(Configuration.LOOK_AND_FEEL);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      frame = new JFrame();
      frame.setTitle("Flashcards");
      frame.setBounds(getBounds());
      frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent event) {
            doExit();
         }
      });
      frame.setJMenuBar(new MenuBarContainer(this).getComponent());
      URL iconURL = super.getClass().getResource(
            Configuration.CARD_ICON_FILE_NAME);
      frame.setIconImage(new ImageIcon(iconURL).getImage());
   }

   // ==========================================================
   // Internal methods
   // ==========================================================

   /**
    * Returns a {@link java.awt.Rectangle} that describes the upper left corner,
    * width, and length of a rectangle that will be centered on the screen. The
    * width and length are based on whatever was saved the last time this
    * program was run.
    */
   private Rectangle getBounds() {
      final int x = getLastX();
      final int y = getLastY();
      final int width = getLastWidth();
      final int height = getLastHeight();
      Rectangle r = new Rectangle(x, y, width, height);
      return r;
   }

   /**
    * Starts a new session with the specified file
    * @param file the file
    * @param toggle true if deck should be turned over
    */
   private void startSession(File file, boolean toggle) {
      try {
         sc = new SessionContainer(this, file, toggle);
         JPanel panel = this.sc.getComponent();
         frame.getContentPane().add(panel, "Center");
         frame.validate();
      }
      catch (SAXException e) {
         displayErrorMessage(e.getMessage());
      }
      catch (ApplicationException e) {
         displayErrorMessage(e.getMessage());
      }
   }

   /**
    * Saves the x-position preference
    */
   private void setLastX() {
      setLastX(frame.getX());
   }

   /**
    * Saves the y-position preference
    */
   private void setLastY() {
      setLastY(frame.getY());
   }

   /**
    * Saves the height preference
    */
   private void setLastHeight() {
      setLastHeight(frame.getHeight());
   }

   /**
    * Saves the width preference
    */
   private void setLastWidth() {
      setLastWidth(frame.getWidth());
   }

   // ==========================================================
   // Instance methods
   // ==========================================================

   /**
    * Displays the specified error message in a message box
    * @param message the error message
    */
   public void displayErrorMessage(String message) {
      JOptionPane.showMessageDialog(this.frame, message, "Error",
            JOptionPane.ERROR_MESSAGE);
   }

   /**
    * Handles the <code>Exit</code> menu option, which saves the current width
    * and height preferences and then disposes of the frame.
    */
   public void doExit() {
      if (this.frame != null) {
         setLastX();
         setLastY();
         setLastWidth();
         setLastHeight();
         this.frame.dispose();
      }
   }

   /**
    * Handles the <code>Open</code> menu option. This causes the
    * {@link OpenDialogHandler#run} method to be called. If a file is selected,
    * the {@link #setFile(File, boolean)} method will be called to start a new
    * session with that file.
    */
   public void doOpen() {
      OpenDialogHandler od = new OpenDialogHandler(this);
      od.run();
   }

   /**
    * Handles the <code>Restart</code> menu option.
    */
   public void doRestart() {
      if (sc != null) {
         sc.restart();
      }
   }

   /**
    * Invokes an editor on the current file
    */
   public void doEdit() {
      String[] cmd = { Configuration.TEXT_EDITOR, file.toString() };
      try {
         Runtime.getRuntime().exec(cmd);
      }
      catch (IOException e) {
         displayErrorMessage(e.getMessage());
      }
   }

   /**
    * Handles the <code>Reset side</code> menu option. This causes the frame to
    * be set back to its default width and height, and saves those values in the
    * preferences.
    */
   public void doResetSize() {
      setLastX(Configuration.X);
      setLastY(Configuration.Y);
      setLastWidth(Configuration.WIDTH);
      setLastHeight(Configuration.HEIGHT);
      frame.setBounds(getBounds());
   }

   /**
    * Switches the card display mode from showing the question to showing the
    * answer, and vice versa.
    * @param toggle if true, toggles the file
    */
   public void doToggle(boolean toggle) {
      setFile(this.file, toggle);
   }

   /**
    * Displays the help about box
    */
   public void doHelpAbout() {
      final String title = "About Flashcards";
      final Component message = createHelpAboutMessage();
      final URL iconURL = getClass().getResource(Configuration.CARD_ICON_FILE_NAME);
      final Icon icon = new ImageIcon(iconURL);
      final int type = JOptionPane.INFORMATION_MESSAGE;
      JOptionPane.showMessageDialog(this.frame, message, title, type, icon);
   }

   private Component createHelpAboutMessage() {
      
      Font baseFont = frame.getFont();
      
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(3, 1, 0, 10));
      
      JPanel pnl1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel lbl1 = new JLabel("Flashcards " + VERSION);
      lbl1.setFont(new Font(baseFont.getName(), baseFont.getStyle(), (int)(baseFont.getSize() * 1.5)));
      pnl1.add(lbl1);
      panel.add(pnl1);
      
      JPanel pnl2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel lbl2 = new JLabel("A computer-based flash cards program");
      pnl2.add(lbl2);
      panel.add(pnl2);

      JPanel pnl3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel lbl3 = new JLabel("Phil Hanna - ph1204@gmail.com");
      lbl3.setFont(new Font(baseFont.getName(), baseFont.getStyle(), (int)(baseFont.getSize() * 0.85)));
      pnl3.add(lbl3);
      panel.add(pnl3);

      return panel;
   }

   /**
    * Returns the current file
    * @return the file
    */
   public File getFile() {
      return this.file;
   }

   /**
    * Returns the {@link javax.swing.JFrame}
    */
   public JFrame getFrame() {
      return this.frame;
   }

   /**
    * Returns the frame height
    */
   public int getHeight() {
      return frame.getHeight();
   }

   /**
    * Returns the path the directory containing the last deck that was used
    */
   public File getLastDirectory() {
      File file = null;
      try {
         Preferences node = Preferences.userNodeForPackage(Main.class);
         String path = node.get("directory", ".");
         file = new File(path).getCanonicalFile();
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      return file;
   }

   /**
    * Returns the x-position of the frame the last time it was saved
    */
   public int getLastX() {
      final Preferences node = Preferences.userNodeForPackage(Main.class);
      final String xString = node.get("x", String.valueOf(Configuration.X));
      final int x = Integer.parseInt(xString);
      return x;
   }

   /**
    * Returns the y-position of the frame the last time it was saved
    */
   public int getLastY() {
      final Preferences node = Preferences.userNodeForPackage(Main.class);
      final String yString = node.get("y", String.valueOf(Configuration.Y));
      final int y = Integer.parseInt(yString);
      return y;
   }

   /**
    * Returns the height of the frame the last time it was saved
    */
   public int getLastHeight() {
      final Preferences node = Preferences.userNodeForPackage(Main.class);
      final String heightString = node.get("height", String
            .valueOf(Configuration.HEIGHT));
      final int height = Integer.parseInt(heightString);
      return height;
   }

   /**
    * Returns the width of the frame the last time it was saved
    */
   public int getLastWidth() {
      final Preferences node = Preferences.userNodeForPackage(Main.class);
      final String widthString = node.get("width", String
            .valueOf(Configuration.WIDTH));
      final int width = Integer.parseInt(widthString);
      return width;
   }

   /**
    * Returns the frame width
    */
   public int getWidth() {
      return frame.getWidth();
   }

   /**
    * Starts a new session with the specified file
    * @param file the file
    * @param toggle if true, toggles whether the question or answer side of the
    *        card is shown
    */
   public void setFile(File file, boolean toggle) {
      this.file = file;
      Container content = this.frame.getContentPane();
      content.removeAll();
      content.setVisible(false);
      content.validate();
      content.setVisible(true);
      if (file == null) {
         setTitle(null);
         this.sc = null;
      }
      else {
         setLastDirectory(file);
         startSession(file, toggle);
      }
   }

   /**
    * Remembers where the last deck was loaded from
    * @param file
    */
   public void setLastDirectory(File file) {
      try {
         file = file.getCanonicalFile();
      }
      catch (IOException e) {
         e.printStackTrace();
      }
      if (!file.isDirectory())
         file = file.getParentFile();
      Preferences node = Preferences.userNodeForPackage(Main.class);
      node.put("directory", file.toString());
   }

   /**
    * Stores the specified x-position as the preference. Called internally with
    * the current frame x-position.
    * @param x the preferred x-position
    */
   public void setLastX(int x) {
      final Preferences node = Preferences.userNodeForPackage(Main.class);
      node.put("x", String.valueOf(x));
   }

   /**
    * Stores the specified y-position as the preference. Called internally with
    * the current frame y-position.
    * @param y the preferred y-position
    */
   public void setLastY(int y) {
      final Preferences node = Preferences.userNodeForPackage(Main.class);
      node.put("y", String.valueOf(y));
   }

   /**
    * Stores the specified height as the preference. Called internally with the
    * current frame height.
    * @param height the preferred height
    */
   public void setLastHeight(int height) {
      final Preferences node = Preferences.userNodeForPackage(Main.class);
      node.put("height", String.valueOf(height));
   }

   /**
    * Stores the specified width as the preference. Called internally with the
    * current frame width.
    * @param width the preferred width
    */
   public void setLastWidth(int width) {
      final Preferences node = Preferences.userNodeForPackage(Main.class);
      node.put("width", String.valueOf(width));
   }

   /**
    * Sets the screen title to the specified string
    * @param title the screen title
    */
   public void setTitle(String title) {
      if (title == null)
         title = "Flashcards";
      this.frame.setTitle(title);
   }
}
