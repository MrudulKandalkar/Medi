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

    @Test
    public void bookBadmintonAppointment() {

        // ======================================================
        // Get today's day in India
        // ======================================================

        LocalDate today =
                LocalDate.now(
                        ZoneId.of("Asia/Kolkata")
                );

        DayOfWeek day =
                today.getDayOfWeek();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "Today: " +
                today
        );

        System.out.println(
                "Day: " +
                day
        );

        System.out.println(
                "========================================"
        );

        // ======================================================
        // Skip Saturday and Sunday
        // ======================================================

        if (day == DayOfWeek.SATURDAY ||
            day == DayOfWeek.SUNDAY) {

            throw new SkipException(
                    "Appointment automation skipped because today is " +
                    day
            );
        }

        // ======================================================
        // Login credentials
        // ======================================================

        String username;
        String password;

        if (day == DayOfWeek.MONDAY ||
                day == DayOfWeek.TUESDAY 
                ) {

                username = "MrudulKandalkar";
                password = "Megod@143";
            }
            
            else if(day == DayOfWeek.WEDNESDAY) {
            	username = "AdityaOke";
            	password = "Aditya@123";
            }

            // ======================================================
            // Thursday / Friday
            // ======================================================

            else if (day == DayOfWeek.THURSDAY ||
                     day == DayOfWeek.FRIDAY) {

                username = "Thedinesh04";
                password = "142804";
            }
        // ======================================================
        // Safety
        // ======================================================

        else {

            throw new SkipException(
                    "No login configuration for day: " +
                    day
            );
        }

        // ======================================================
        // Print selected user
        // ======================================================

        System.out.println(
                "Selected username: " +
                username
        );

        // Do NOT print password in console.

        // ======================================================
        // Login
        // ======================================================

        LoginPage loginPage =
                new LoginPage(driver);

        loginPage.login(
                username,
                password
        );

        // ======================================================
        // Appointment page
        // ======================================================

        AppointmentPage appointmentPage =
                new AppointmentPage(driver);

        appointmentPage.bookAppointment();
    }
}