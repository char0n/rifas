/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rifasproject.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;

/**
 *
 * @author char0n
 */
public class SplashScreenDialog {

    SplashScreen screen;
    Graphics2D splashGraphics;

    public void init() {
        this.screen         = SplashScreen.getSplashScreen();
        if (this.screen == null) return;

        this.splashGraphics = screen.createGraphics();
        this.splashGraphics.setColor(new Color(255, 255, 255));
        this.splashGraphics.setFont(new Font("Arial", Font.BOLD, 11));
        this.splashGraphics.drawString("Loading application context...", 20, 261);
        this.splashGraphics.drawString("Version: 1.0b", 20, 275);
        this.screen.update();
    }
}
