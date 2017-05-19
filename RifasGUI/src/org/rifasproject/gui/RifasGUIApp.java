/*
 * RifasGUIApp.java
 */

package org.rifasproject.gui;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The main class of the application.
 */
public class RifasGUIApp extends SingleFrameApplication {

    private ApplicationContext beanFactory;

    public ApplicationContext getBeanFactory() {
        return beanFactory;
    }

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        this.beanFactory = new ClassPathXmlApplicationContext("beans.xml");
        show(new RifasGUIView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of RifasGUIApp
     */
    public static RifasGUIApp getApplication() {
        return Application.getInstance(RifasGUIApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        SplashScreenDialog splashScreen = new SplashScreenDialog();
        splashScreen.init();

        launch(RifasGUIApp.class, args);
    }
}
