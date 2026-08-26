package tests;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

import org.testng.SkipException;
import org.testng.annotations.Test;

import base.BaseTest1;
import pages.AppointmentPage1;
import pages.LoginPage;

public class AppointmentTest1 extends BaseTest1 {

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

            username = "shreyashhh1";
            password = "$Hrey123";
        }

        // ======================================================
        // WEDNESDAY
        // ======================================================

        else if (day == DayOfWeek.WEDNESDAY) {

            username = "Dileep Dil";
            password = "Dil@lov3";
        }

        // ======================================================
        // THURSDAY / FRIDAY
        // ======================================================

        else if (day == DayOfWeek.THURSDAY ||
                 day == DayOfWeek.FRIDAY) {

            username = "Aditya2504";
            password = "Aditya@25";
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

        // Do NOT print password.

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
        // APPOINTMENT PAGE 1
        // ======================================================

        AppointmentPage1 appointmentPage1 =
                new AppointmentPage1(driver);

        appointmentPage1.bookAppointment();
    }
}
