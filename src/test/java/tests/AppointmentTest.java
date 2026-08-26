package tests;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

import org.testng.SkipException;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.AppointmentPage;
import pages.LoginPage;

public class AppointmentTest extends BaseTest {

    private static final ZoneId IST =
            ZoneId.of("Asia/Kolkata");

    @Test
    public void bookBadmintonAppointment() {

        // ======================================================
        // WAIT UNTIL 12:00 AM IST
        // ======================================================

        TimeWait.waitUntilMidnightIST();

        // ======================================================
        // GET TODAY'S DATE AFTER MIDNIGHT
        // ======================================================

        LocalDate today =
                LocalDate.now(IST);

        DayOfWeek day =
                today.getDayOfWeek();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "Today: " + today
        );

        System.out.println(
                "Day: " + day
        );

        System.out.println(
                "========================================"
        );

        // ======================================================
        // SKIP SATURDAY AND SUNDAY
        // ======================================================

        if (day == DayOfWeek.SATURDAY ||
            day == DayOfWeek.SUNDAY) {

            throw new SkipException(
                    "Appointment automation skipped because today is "
                    + day
            );
        }

        // ======================================================
        // LOGIN CREDENTIALS
        // ======================================================

        String username;
        String password;

        // ======================================================
        // MONDAY / TUESDAY
        // ======================================================

        if (day == DayOfWeek.MONDAY ||
            day == DayOfWeek.TUESDAY) {

            username = "MrudulKandalkar";
            password = "Megod@143";
        }

        // ======================================================
        // WEDNESDAY
        // ======================================================

        else if (day == DayOfWeek.WEDNESDAY) {

            username = "AdityaOke";
            password = "Aditya@123";
        }

        // ======================================================
        // THURSDAY / FRIDAY
        // ======================================================

        else if (day == DayOfWeek.THURSDAY ||
                 day == DayOfWeek.FRIDAY) {

            username = "Thedinesh04";
            password = "142804";
        }

        // ======================================================
        // SAFETY
        // ======================================================

        else {

            throw new SkipException(
                    "No login configuration for day: "
                    + day
            );
        }

        // ======================================================
        // PRINT SELECTED USER
        // ======================================================

        System.out.println(
                "Selected username: " + username
        );

        // DO NOT PRINT PASSWORD

        // ======================================================
        // LOGIN
        // ======================================================

        LoginPage loginPage =
                new LoginPage(driver);

        loginPage.login(
                username,
                password
        );

        // ======================================================
        // APPOINTMENT PAGE
        // ======================================================

        AppointmentPage appointmentPage =
                new AppointmentPage(driver);

        appointmentPage.bookAppointment();
    }
}
