package com.scheduling.universityschedule_backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple utility for logging debug information separately.
 */
public class CustomLogger {
    // Create a logger with a distinct name.
    private static final Logger logger = LoggerFactory.getLogger("CustomLogger");

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logError(String message, Throwable t) {
        logger.error(message, t);
    }
    public static void logError(String message) {
        logger.error(message);
    }
}
