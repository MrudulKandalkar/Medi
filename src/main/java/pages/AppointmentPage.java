package pages;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import net.sourceforge.tess4j.Tesseract;

public class AppointmentPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ==========================================================
    // Employee Locators
    // ==========================================================

    private final By employeeSearchField =
            By.id("tags");

    private final By addEmployeeButton =
            By.id("btnadd");

    private final By autocompleteResults =
            By.cssSelector(
                    "ul.ui-autocomplete li.ui-menu-item"
            );

    // ==========================================================
    // CAPTCHA Locators
    // ==========================================================

    private final By captchaImage =
            By.cssSelector("img[alt='Captcha']");

    private final By captchaField =
            By.id("Captcha");

    private final By saveButton =
            By.id("btnSave");

    // ==========================================================
    // Date
    // ==========================================================

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ==========================================================
    // Appointment Page Locators
    // ==========================================================

    private final By calendarButton =
            By.cssSelector("img.ui-datepicker-trigger");

    private final By dateField =
            By.id("DateFrom");

    private final By activityDropdown =
            By.id("Activity");

    private final By showAppointmentButton =
            By.id("btnshow");

    private final By datepickerMonth =
            By.cssSelector(".ui-datepicker-month");

    private final By datepickerYear =
            By.cssSelector(".ui-datepicker-year");

    private final By nextMonthButton =
            By.cssSelector(".ui-datepicker-next");

    // ==========================================================
    // Constructor
    // ==========================================================

    public AppointmentPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
    }

    // ==========================================================
    // Employee Configuration
    // ==========================================================

    private static class Employee {

        String searchText;
        String fullName;

        Employee(String searchText, String fullName) {

            this.searchText = searchText;
            this.fullName = fullName;
        }
    }

    // ==========================================================
    // Time Slot XPath
    // ==========================================================

    private By getTimeSlot(String timeSlot) {

        String xpath =
                "//div[@id='result']" +
                "//a[contains(@class,'shortcut')]" +
                "[.//span[contains(normalize-space(.), '" +
                timeSlot +
                "')]]";

        return By.xpath(xpath);
    }

    // ==========================================================
    // MAIN APPOINTMENT FLOW
    // ==========================================================

    public void bookAppointment() {

        // ------------------------------------------------------
        // Get today's date
        // ------------------------------------------------------

        LocalDate today =
                LocalDate.now(
                        ZoneId.of("Asia/Kolkata")
                );

        DayOfWeek todayDay =
                today.getDayOfWeek();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "Today: " +
                today.format(DATE_FORMAT)
        );

        System.out.println(
                "Day: " +
                todayDay
        );

        System.out.println(
                "========================================"
        );

        // ------------------------------------------------------
        // Skip Saturday and Sunday
        // ------------------------------------------------------

        if (todayDay == DayOfWeek.SATURDAY ||
            todayDay == DayOfWeek.SUNDAY) {

            throw new SkipException(
                    "Today is " +
                    todayDay +
                    ". Appointment automation skipped."
            );
        }

        // ------------------------------------------------------
        // Appointment date = today + 7 days
        // ------------------------------------------------------

        LocalDate appointmentDate =
                today.plusDays(7);

        System.out.println(
                "Appointment date: " +
                appointmentDate.format(DATE_FORMAT)
        );

        // ------------------------------------------------------
        // Determine employees based on day
        // ------------------------------------------------------

        List<Employee> employees =
                getEmployeesForToday(todayDay);

        System.out.println(
                "Employees selected for today:"
        );

        for (Employee employee : employees) {

            System.out.println(
                    "Search: " +
                    employee.searchText +
                    " | Name: " +
                    employee.fullName
            );
        }

        // ------------------------------------------------------
        // 1. Open calendar
        // ------------------------------------------------------

        openCalendar();

        // ------------------------------------------------------
        // 2. Select date
        // ------------------------------------------------------

        selectDate(appointmentDate);

        // ------------------------------------------------------
        // 3. Select Badminton Court 3
        // ------------------------------------------------------

        selectBadmintonCourt3();

        // ------------------------------------------------------
        // 4. Show Appointment
        // ------------------------------------------------------

        clickShowAppointment();

        // ------------------------------------------------------
        // 5. Select time slot
        // ------------------------------------------------------

        clickTimeSlot(
                "06:00 PM To 07:00 PM"
        );

        // ------------------------------------------------------
        // 6. Add employees based on day
        // ------------------------------------------------------

        addPlayers(employees);

        // ------------------------------------------------------
        // 7. CAPTCHA
        // ------------------------------------------------------

        solveCaptchaAndSave();
    }

    // ==========================================================
    // GET EMPLOYEES BASED ON DAY
    // ==========================================================

    private List<Employee> getEmployeesForToday(
            DayOfWeek day) {

        List<Employee> employees =
                new ArrayList<>();

        // ------------------------------------------------------
        // Monday / Tuesday / Wednesday
        // ------------------------------------------------------

        if (day == DayOfWeek.MONDAY ||
            day == DayOfWeek.TUESDAY 
            ) {

            employees.add(
                    new Employee(
                            "mrudul",
                            "mrudul Kandalkar"
                    )
            );

            employees.add(
                    new Employee(
                            "arya",
                            "arya Choudhari"
                    )
            );
        }
        
        else if(day == DayOfWeek.WEDNESDAY) {
        	employees.add(
                    new Employee(
                            "aditya",
                            "aditya Oke"
                    )
            );

            employees.add(
                    new Employee(
                            "aditya",
                            "aditya Chakre"
                    )
            );
        }
        	

        // ------------------------------------------------------
        // Thursday / Friday
        // ------------------------------------------------------

        else if (day == DayOfWeek.THURSDAY ||
                 day == DayOfWeek.FRIDAY) {

            employees.add(
                    new Employee(
                            "dinesh",
                            "dinesh Devalienaik"
                    )
            );

            employees.add(
                    new Employee(
                            "aditi",
                            "aditi Mali"
                    )
            );
        }

        // ------------------------------------------------------
        // Safety check
        // ------------------------------------------------------

        if (employees.isEmpty()) {

            throw new SkipException(
                    "No employee configuration found for day: " +
                    day
            );
        }

        return employees;
    }

    // ==========================================================
    // OPEN CALENDAR
    // ==========================================================

    private void openCalendar() {

        WebElement calendar =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                calendarButton
                        )
                );

        calendar.click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("ui-datepicker-div")
                )
        );

        System.out.println(
                "Calendar opened."
        );
    }

    // ==========================================================
    // SELECT DATE
    // ==========================================================

    private void selectDate(
            LocalDate targetDate) {

        int targetDay =
                targetDate.getDayOfMonth();

        int targetMonth =
                targetDate.getMonthValue() - 1;

        int targetYear =
                targetDate.getYear();

        navigateToMonth(
                targetMonth,
                targetYear
        );

        String dateXPath =
                "//div[@id='ui-datepicker-div']" +
                "//td[@data-handler='selectDay']" +
                "[@data-month='" +
                targetMonth +
                "']" +
                "[@data-year='" +
                targetYear +
                "']" +
                "//a[normalize-space(text())='" +
                targetDay +
                "']";

        WebElement date =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath(dateXPath)
                        )
                );

        System.out.println(
                "Selecting date: " +
                targetDate.format(DATE_FORMAT)
        );

        date.click();

        wait.until(
                driver -> {

                    String value =
                            driver.findElement(
                                    dateField
                            ).getAttribute("value");

                    return value != null &&
                           !value.isBlank();
                }
        );

        System.out.println(
                "Selected date: " +
                driver.findElement(
                        dateField
                ).getAttribute("value")
        );
    }

    // ==========================================================
    // NAVIGATE MONTH
    // ==========================================================

    private void navigateToMonth(
            int targetMonth,
            int targetYear) {

        for (int i = 0; i < 12; i++) {

            String displayedMonth =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    datepickerMonth
                            )
                    ).getText();

            String displayedYear =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    datepickerYear
                            )
                    ).getText();

            int currentMonth =
                    monthNameToNumber(
                            displayedMonth
                    );

            int currentYear =
                    Integer.parseInt(
                            displayedYear
                    );

            if (currentMonth == targetMonth &&
                currentYear == targetYear) {

                return;
            }

            WebElement nextButton =
                    driver.findElement(
                            nextMonthButton
                    );

            String classes =
                    nextButton.getAttribute("class");

            if (classes != null &&
                classes.contains("ui-state-disabled")) {

                throw new IllegalStateException(
                        "Target month is not available."
                );
            }

            nextButton.click();

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(
                                    ".ui-datepicker-calendar"
                            )
                    )
            );
        }

        throw new IllegalStateException(
                "Could not navigate to target month."
        );
    }

    // ==========================================================
    // MONTH NAME -> NUMBER
    // ==========================================================

    private int monthNameToNumber(
            String month) {

        return switch (
                month.toLowerCase()
        ) {

            case "january" ->
                    0;

            case "february" ->
                    1;

            case "march" ->
                    2;

            case "april" ->
                    3;

            case "may" ->
                    4;

            case "june" ->
                    5;

            case "july" ->
                    6;

            case "august" ->
                    7;

            case "september" ->
                    8;

            case "october" ->
                    9;

            case "november" ->
                    10;

            case "december" ->
                    11;

            default ->
                    throw new IllegalArgumentException(
                            "Unknown month: " +
                            month
                    );
        };
    }

    // ==========================================================
    // SELECT BADMINTON COURT 3
    // ==========================================================

    private void selectBadmintonCourt3() {

        WebElement activity =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                activityDropdown
                        )
                );

        Select select =
                new Select(activity);

        select.selectByVisibleText(
                "Badminton Court 2"
        );

        System.out.println(
                "Selected: Badminton Court 2"
        );
    }

    // ==========================================================
    // SHOW APPOINTMENT
    // ==========================================================

    private void clickShowAppointment() {

        WebElement button =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                showAppointmentButton
                        )
                );

        button.click();

        System.out.println(
                "Clicked Show Appointment."
        );

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(
                                "#result a.shortcut"
                        )
                )
        );

        System.out.println(
                "Appointment slots loaded."
        );
    }

    // ==========================================================
    // CLICK TIME SLOT
    // ==========================================================

    private void clickTimeSlot(
            String timeSlot) {

        By slotLocator =
                getTimeSlot(timeSlot);

        WebElement slot =
                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                slotLocator
                        )
                );

        System.out.println(
                "Found slot: " +
                timeSlot
        );

        String className =
                slot.getAttribute("class");

        System.out.println(
                "Slot class: " +
                className
        );

        if (className != null &&
            className.contains("Booked")) {

            System.out.println(
                    "WARNING: Slot is BOOKED."
            );
        }

        else if (
                className != null &&
                className.contains("Available")) {

            System.out.println(
                    "Slot is AVAILABLE."
            );
        }

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        slotLocator
                )
        ).click();

        System.out.println(
                "Clicked slot: " +
                timeSlot
        );
    }

    // ==========================================================
    // ADD PLAYERS
    // ==========================================================

    private void addPlayers(
            List<Employee> employees) {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "Adding employees..."
        );

        System.out.println(
                "========================================"
        );

        for (Employee employee : employees) {

            selectAndAddEmployee(
                    employee.searchText,
                    employee.fullName
            );
        }
        
        

        System.out.println(
                "All employees added successfully."
        );
    }

    // ==========================================================
    // SEARCH + SELECT + ADD EMPLOYEE
    // ==========================================================

//    private void selectAndAddEmployee(
//            String searchText,
//            String employeeFullName) {
//
//        WebElement searchBox =
//                wait.until(
//                        ExpectedConditions.elementToBeClickable(
//                                employeeSearchField
//                        )
//                );
//
//        searchBox.clear();
//
//        searchBox.sendKeys(
//                searchText
//        );
//
//        System.out.println(
//                "Searching employee: " +
//                searchText
//        );
//
//        // ------------------------------------------------------
//        // Wait for autocomplete
//        // ------------------------------------------------------
//
//        wait.until(
//                ExpectedConditions.visibilityOfElementLocated(
//                        autocompleteResults
//                )
//        );
//
//        // ------------------------------------------------------
//        // Employee exact-name XPath
//        // Case insensitive
//        // ------------------------------------------------------
//
//        String employeeXPath =
//                "//ul[contains(@class,'ui-autocomplete')]" +
//                "//li[contains(@class,'ui-menu-item')]" +
//                "//a[" +
//                "normalize-space(" +
//                "translate(text()," +
//                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," +
//                "'abcdefghijklmnopqrstuvwxyz')" +
//                ")='" +
//                employeeFullName.toLowerCase() +
//                "'" +
//                "]";
//
//        By employeeLocator =
//                By.xpath(employeeXPath);
//
//        WebElement employee =
//                wait.until(
//                        ExpectedConditions.elementToBeClickable(
//                                employeeLocator
//                        )
//                );
//
//        System.out.println(
//                "Found employee: " +
//                employeeFullName
//        );
//
//        employee.click();
//
//        // ------------------------------------------------------
//        // Verify selected employee
//        // ------------------------------------------------------
//
//        wait.until(
//                driver -> {
//
//                    String value =
//                            driver.findElement(
//                                    employeeSearchField
//                            ).getAttribute("value");
//
//                    return value != null &&
//                           value.equalsIgnoreCase(
//                                   employeeFullName
//                           );
//                }
//        );
//
//        System.out.println(
//                "Selected employee: " +
//                driver.findElement(
//                        employeeSearchField
//                ).getAttribute("value")
//        );
//
//        // ------------------------------------------------------
//        // Click Add
//        // ------------------------------------------------------
//
//        wait.until(
//                ExpectedConditions.elementToBeClickable(
//                        addEmployeeButton
//                )
//        ).click();
//        
//
//        System.out.println(
//                "Clicked Add for: " +
//                employeeFullName
//        );
//
//        // ------------------------------------------------------
//        // Wait until search box is cleared
//        // ------------------------------------------------------
//
//        wait.until(
//                driver -> {
//
//                    String value =
//                            driver.findElement(
//                                    employeeSearchField
//                            ).getAttribute("value");
//
//                    return value == null ||
//                           value.isBlank();
//                }
//        );
//
//        System.out.println(
//                "Successfully added: " +
//                employeeFullName
//        );
//    }
    
    private void selectAndAddEmployee(
            String searchText,
            String employeeFullName) {

        System.out.println("========================================");
        System.out.println("Searching employee: " + searchText);
        System.out.println("Expected employee: " + employeeFullName);
        System.out.println("========================================");

        // ------------------------------------------------------
        // 1. Find search box
        // ------------------------------------------------------

        WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        employeeSearchField
                )
        );

        // ------------------------------------------------------
        // 2. Clear previous value
        // ------------------------------------------------------

        searchBox.clear();

        // ------------------------------------------------------
        // 3. Enter employee search
        // ------------------------------------------------------

        searchBox.sendKeys(searchText);

        // ------------------------------------------------------
        // 4. Wait for autocomplete
        // ------------------------------------------------------

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        autocompleteResults
                )
        );

        // ------------------------------------------------------
        // 5. Find exact employee name
        // ------------------------------------------------------

        String employeeXPath =
                "//ul[contains(@class,'ui-autocomplete')]" +
                "//li[contains(@class,'ui-menu-item')]" +
                "//a[" +
                "normalize-space(" +
                "translate(text()," +
                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," +
                "'abcdefghijklmnopqrstuvwxyz')" +
                ")='" +
                employeeFullName.toLowerCase() +
                "'" +
                "]";

        By employeeLocator = By.xpath(employeeXPath);

        WebElement employee = wait.until(
                ExpectedConditions.elementToBeClickable(
                        employeeLocator
                )
        );

        System.out.println(
                "Found employee: " + employeeFullName
        );

        // ------------------------------------------------------
        // 6. Click employee
        // ------------------------------------------------------

        employee.click();

        System.out.println(
                "Clicked employee: " + employeeFullName
        );

        // ------------------------------------------------------
        // 7. IMPORTANT:
        // Wait for autocomplete to disappear
        // ------------------------------------------------------

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        autocompleteResults
                )
        );

        System.out.println(
                "Employee selected successfully: " +
                employeeFullName
        );

        // ------------------------------------------------------
        // 8. Get current search box value
        // ------------------------------------------------------

        String selectedValue =
                driver.findElement(
                        employeeSearchField
                ).getAttribute("value");

        System.out.println(
                "Search box value after selection: [" +
                selectedValue +
                "]"
        );

        // ------------------------------------------------------
        // 9. Make sure something was selected
        // ------------------------------------------------------

        if (selectedValue == null ||
            selectedValue.trim().isEmpty()) {

            throw new IllegalStateException(
                    "Employee selection failed. Search box is empty after selecting: " +
                    employeeFullName
            );
        }

        // ------------------------------------------------------
        // 10. Click ADD
        // ------------------------------------------------------

        WebElement addButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        addEmployeeButton
                )
        );

        addButton.click();

        System.out.println(
                "Clicked Add for: " +
                employeeFullName
        );

        // ------------------------------------------------------
        // 11. Wait until search box is cleared
        // ------------------------------------------------------

        wait.until(driver -> {

            String value =
                    driver.findElement(
                            employeeSearchField
                    ).getAttribute("value");

            return value == null ||
                   value.trim().isEmpty();
        });

        System.out.println(
                "Successfully added: " +
                employeeFullName
        );

        System.out.println("----------------------------------------");
    }

    // ==========================================================
    // CAPTCHA
    // ==========================================================

    private void solveCaptchaAndSave() {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "Reading CAPTCHA..."
        );

        System.out.println(
                "========================================"
        );

        WebElement captcha =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                captchaImage
                        )
                );

        try {

            // --------------------------------------------------
            // Capture CAPTCHA
            // --------------------------------------------------

            byte[] screenshot =
                    captcha.getScreenshotAs(
                            OutputType.BYTES
                    );

            BufferedImage captchaImageData =
                    ImageIO.read(
                            new ByteArrayInputStream(
                                    screenshot
                            )
                    );

            if (captchaImageData == null) {

                throw new IllegalStateException(
                        "Could not read CAPTCHA image."
                );
            }

            System.out.println(
                    "CAPTCHA image captured."
            );

            // --------------------------------------------------
            // Create Tesseract
            // --------------------------------------------------

            Tesseract tesseract =
                    new Tesseract();

            // --------------------------------------------------
            // Tessdata path
            // --------------------------------------------------

            String tessDataPath =
                    new File(
                            "src/test/resources/tessdata"
                    ).getAbsolutePath();

            System.out.println(
                    "Tessdata path: " +
                    tessDataPath
            );

            // --------------------------------------------------
            // Verify tessdata folder
            // --------------------------------------------------

            File tessDataFolder =
                    new File(
                            tessDataPath
                    );

            if (!tessDataFolder.exists()) {

                throw new IllegalStateException(
                        "Tessdata folder does not exist: " +
                        tessDataPath
                );
            }

            // --------------------------------------------------
            // Verify English trained data
            // --------------------------------------------------

            File englishData =
                    new File(
                            tessDataFolder,
                            "eng.traineddata"
                    );

            if (!englishData.exists()) {

                throw new IllegalStateException(
                        "eng.traineddata does not exist: " +
                        englishData.getAbsolutePath()
                );
            }

            System.out.println(
                    "eng.traineddata found."
            );

            // --------------------------------------------------
            // Configure Tesseract
            // --------------------------------------------------

            tesseract.setDatapath(
                    tessDataPath
            );

            tesseract.setLanguage(
                    "eng"
            );

            // --------------------------------------------------
            // OCR
            // --------------------------------------------------

            String captchaText =
                    tesseract.doOCR(
                            captchaImageData
                    );

            System.out.println(
                    "Raw CAPTCHA OCR: [" +
                    captchaText +
                    "]"
            );

            // --------------------------------------------------
            // Clean CAPTCHA
            // --------------------------------------------------

            String expression =
                    cleanCaptchaText(
                            captchaText
                    );

            System.out.println(
                    "Detected CAPTCHA expression: " +
                    expression
            );

            // --------------------------------------------------
            // Calculate answer
            // --------------------------------------------------

            int answer =
                    calculateCaptcha(
                            expression
                    );

            System.out.println(
                    "CAPTCHA Answer: " +
                    answer
            );

            // --------------------------------------------------
            // Enter answer
            // --------------------------------------------------

            WebElement captchaInput =
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    captchaField
                            )
                    );

            captchaInput.clear();

            captchaInput.sendKeys(
                    String.valueOf(answer)
            );

            System.out.println(
                    "CAPTCHA answer entered."
            );

            // --------------------------------------------------
            // Save
            // --------------------------------------------------

            WebElement save =
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    saveButton
                            )
                    );

            save.click();

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Clicked SAVE successfully."
            );

            System.out.println(
                    "========================================"
            );

        } catch (Exception e) {

            System.out.println(
                    "CAPTCHA processing failed."
            );

            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to read/solve CAPTCHA.",
                    e
            );
        }
    }

    // ==========================================================
    // CLEAN CAPTCHA TEXT
    // ==========================================================

//    private String cleanCaptchaText(
//            String captchaText) {
//
//        if (captchaText == null ||
//            captchaText.isBlank()) {
//
//            throw new IllegalStateException(
//                    "CAPTCHA OCR returned empty text."
//            );
//        }
//
//        String text =
//                captchaText
//                        .toLowerCase()
//                        .replaceAll("\\s+", "")
//                        .replace("=", "")
//                        .replace("?", "")
//                        .replace(":", "")
//                        .trim();
//
//        // ------------------------------------------------------
//        // Common OCR corrections
//        // ------------------------------------------------------
//
//        text = text
//                .replace("o", "0")
//                .replace("i", "1")
//                .replace("l", "1")
//                .replace("s", "5")
//                .replace("b", "8");
//
//        // ------------------------------------------------------
//        // Multiplication symbol
//        // ------------------------------------------------------
//
//        text = text.replace("×", "x");
//
//        return text;
//    }
    
    private String cleanCaptchaText(String captchaText) {

        if (captchaText == null || captchaText.isBlank()) {

            throw new IllegalStateException(
                    "CAPTCHA OCR returned empty text."
            );
        }

        System.out.println(
                "Original OCR text: [" +
                captchaText +
                "]"
        );

        // ------------------------------------------------------
        // Convert to lowercase
        // ------------------------------------------------------

        String text =
                captchaText
                        .toLowerCase()
                        .trim();

        // ------------------------------------------------------
        // IMPORTANT:
        // CAPTCHA answer is BEFORE '='
        //
        // Example:
        // 20+5=2?
        //
        // We only want:
        // 20+5
        // ------------------------------------------------------

        if (text.contains("=")) {

            text =
                    text.substring(
                            0,
                            text.indexOf("=")
                    );
        }

        // ------------------------------------------------------
        // Remove whitespace
        // ------------------------------------------------------

        text =
                text.replaceAll("\\s+", "");

        // ------------------------------------------------------
        // Remove anything that is not:
        // numbers
        // + - * / x ×
        // ------------------------------------------------------

        text =
                text.replaceAll(
                        "[^0-9+\\-*/x×]",
                        ""
                );

        // ------------------------------------------------------
        // Common OCR corrections
        // ------------------------------------------------------

        text = text
                .replace("o", "0")
                .replace("i", "1")
                .replace("l", "1")
                .replace("s", "5")
                .replace("b", "8");

        // ------------------------------------------------------
        // Multiplication sign
        // ------------------------------------------------------

        text = text.replace("×", "x");

        // ------------------------------------------------------
        // Final validation
        // ------------------------------------------------------

        if (text.isBlank()) {

            throw new IllegalStateException(
                    "Could not extract CAPTCHA expression from OCR: " +
                    captchaText
            );
        }

        System.out.println(
                "Cleaned CAPTCHA expression: [" +
                text +
                "]"
        );

        return text;
    }

    // ==========================================================
    // CALCULATE CAPTCHA
    // ==========================================================

    private int calculateCaptcha(
            String expression) {

        System.out.println(
                "Calculating: " +
                expression
        );

        // ------------------------------------------------------
        // Addition
        // Example: 7+8
        // ------------------------------------------------------

        if (expression.contains("+")) {

            String[] numbers =
                    expression.split("\\+");

            if (numbers.length != 2) {

                throw new IllegalArgumentException(
                        "Invalid addition CAPTCHA: " +
                        expression
                );
            }

            int first =
                    Integer.parseInt(
                            numbers[0]
                    );

            int second =
                    Integer.parseInt(
                            numbers[1]
                    );

            return first + second;
        }

        // ------------------------------------------------------
        // Subtraction
        // Example: 8-3
        // ------------------------------------------------------

        if (expression.contains("-")) {

            String[] numbers =
                    expression.split("-");

            if (numbers.length != 2) {

                throw new IllegalArgumentException(
                        "Invalid subtraction CAPTCHA: " +
                        expression
                );
            }

            int first =
                    Integer.parseInt(
                            numbers[0]
                    );

            int second =
                    Integer.parseInt(
                            numbers[1]
                    );

            return first - second;
        }

        // ------------------------------------------------------
        // Multiplication *
        // ------------------------------------------------------

        if (expression.contains("*")) {

            String[] numbers =
                    expression.split("\\*");

            if (numbers.length != 2) {

                throw new IllegalArgumentException(
                        "Invalid multiplication CAPTCHA: " +
                        expression
                );
            }

            int first =
                    Integer.parseInt(
                            numbers[0]
                    );

            int second =
                    Integer.parseInt(
                            numbers[1]
                    );

            return first * second;
        }

        // ------------------------------------------------------
        // Multiplication x
        // ------------------------------------------------------

        if (expression.contains("x")) {

            String[] numbers =
                    expression.split("x");

            if (numbers.length != 2) {

                throw new IllegalArgumentException(
                        "Invalid multiplication CAPTCHA: " +
                        expression
                );
            }

            int first =
                    Integer.parseInt(
                            numbers[0]
                    );

            int second =
                    Integer.parseInt(
                            numbers[1]
                    );

            return first * second;
        }

        // ------------------------------------------------------
        // Division
        // ------------------------------------------------------

        if (expression.contains("/")) {

            String[] numbers =
                    expression.split("/");

            if (numbers.length != 2) {

                throw new IllegalArgumentException(
                        "Invalid division CAPTCHA: " +
                        expression
                );
            }

            int first =
                    Integer.parseInt(
                            numbers[0]
                    );

            int second =
                    Integer.parseInt(
                            numbers[1]
                    );

            if (second == 0) {

                throw new ArithmeticException(
                        "Cannot divide by zero."
                );
            }

            return first / second;
        }

        throw new IllegalArgumentException(
                "Could not understand CAPTCHA expression: " +
                expression
        );
    }
}