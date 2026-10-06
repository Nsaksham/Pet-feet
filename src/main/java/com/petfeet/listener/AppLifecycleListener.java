package com.petfeet.listener;

import com.petfeet.service.BackgroundServiceManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Starts the background threads when the web application starts and stops them cleanly on shutdown. */
@WebListener
public class AppLifecycleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BackgroundServiceManager.getInstance().start();
        sce.getServletContext().log("PetFeet started: background services running");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        BackgroundServiceManager.getInstance().stop();
        sce.getServletContext().log("PetFeet stopped: background services shut down");
    }
}
