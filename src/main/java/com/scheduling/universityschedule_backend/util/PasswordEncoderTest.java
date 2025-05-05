package com.scheduling.universityschedule_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderTest {

    public static void passgen() {
        // This will print the encoded password to the console
        for (int i = 0; i < 7; i++) {
            CustomLogger.logInfo("testuser" + i);
            CustomLogger.logInfo("password :" + new BCryptPasswordEncoder().encode("testpass"+i));
        }
    }
}
