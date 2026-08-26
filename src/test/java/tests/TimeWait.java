package tests;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class TimeWait {

    private static final ZoneId IST =
            ZoneId.of("Asia/Kolkata");

    private TimeWait() {
    }

    public static void waitUntilMidnightIST() {

        ZonedDateTime now =
                ZonedDateTime.now(IST);

        ZonedDateTime target =
                now.toLocalDate()
                        .plusDays(1)
                        .atStartOfDay(IST);

        System.out.println("========================================");
        System.out.println("     WAITING FOR 12:00 AM IST");
        System.out.println("========================================");
        System.out.println("Current IST : " + now);
        System.out.println("Target IST  : " + target);

        try {

            while (true) {

                now = ZonedDateTime.now(IST);

                Duration remaining =
                        Duration.between(now, target);

                if (remaining.isZero()
                        || remaining.isNegative()) {
                    break;
                }

                long millis =
                        remaining.toMillis();

                if (millis > 10_000) {
                    Thread.sleep(5_000);
                } else {
                    Thread.sleep(
                            Math.min(millis, 100)
                    );
                }
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Midnight wait was interrupted.",
                    e
            );
        }

        System.out.println("========================================");
        System.out.println("       12:00 AM IST REACHED");
        System.out.println("========================================");
        System.out.println(
                "Execution IST: "
                        + ZonedDateTime.now(IST)
        );
        System.out.println("STARTING SELENIUM");
        System.out.println("========================================");
    }
}
