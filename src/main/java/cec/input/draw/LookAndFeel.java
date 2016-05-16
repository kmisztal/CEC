/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.input.draw;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Krzysztof
 */

public class LookAndFeel {

    /**
     * ustawia look & feel systemu dla aplikacji okienkowej
     */
    public static void doIt() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
    }
}
