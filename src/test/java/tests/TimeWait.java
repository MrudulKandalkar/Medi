package tests;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeWait {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private TimeWait() {
        // Utility class
    }

    public static void waitUntilMidnightIST() {

        ZonedDateTime now = ZonedDateTime.now(IST);

        // Calculate next midnight in IST
        ZonedDateTime target = now
                .toLocalDate()
                .plusDays(1)
                .atStartOfDay(IST);

        long waitMillis = Duration
                .between(now, target)
                .toMillis();

        System.out.println();
        System.out.println("========================================");
        System.out.println("     APPOINTMENT AUTOMATION");
        System.out.println("========================================");
        System.out.println("Current IST : " + now);
        System.out.println("Target IST  : " + target);
        System.out.println("Waiting for : " + (waitMillis / 1000) + " seconds");
        System.out.println("========================================");
        System.out.println();

        try {

            Thread.sleep(waitMillis);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Waiting for 12:00 AM IST was interrupted.",
                    e
            );
        }

        ZonedDateTime executionTime = ZonedDateTime.now(IST);

        System.out.println();
        System.out.println("========================================");
        System.out.println("       12:00 AM IST REACHED");
        System.out.println("========================================");
        System.out.println("Execution IST : " + executionTime);
        System.out.println("Starting Selenium automation...");
        System.out.println("========================================");
        System.out.println();
    }
}
