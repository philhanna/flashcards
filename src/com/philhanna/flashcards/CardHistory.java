package com.philhanna.flashcards;

/**
 * Possible choices for the user's last experience with a card in a
 * session:
 * <ul>
 * <li><code>NEVER_ANSWERED</code></li>
 * <li><code>ANSWERED_RIGHT</code></li>
 * <li><code>ANSWERED_WRONG</code></li>
 * </ul>
 */
public enum CardHistory {
   NEVER_ANSWERED, ANSWERED_RIGHT, ANSWERED_WRONG;
}
