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

    // ==========================================================
    // DRIVER / WAIT
    // ==========================================================

    private final WebDriver driver;

    private final WebDriverWait wait;

    private static final ZoneId IST =
            ZoneId.of("Asia/Kolkata");

    private static final Duration WAIT_TIME =
            Duration.ofSeconds(20);

    // ==========================================================
    // DATE FORMAT
    // ==========================================================

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ==========================================================
    // EMPLOYEE LOCATORS
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
    // CAPTCHA LOCATORS
    // ==========================================================

    private final By captchaImage =
            By.cssSelector(
                    "img[alt='Captcha']"
            );

    private final By captchaField =
            By.id("Captcha");

    private final By saveButton =
            By.id("btnSave");

    // ==========================================================
    // DATE / CALENDAR LOCATORS
    // ==========================================================

    private final By calendarButton =
            By.cssSelector(
                    "img.ui-datepicker-trigger"
            );

    private final By dateField =
            By.id("DateFrom");

    private final By datepickerContainer =
            By.id("ui-datepicker-div");

    private final By datepickerMonth =
            By.cssSelector(
                    ".ui-datepicker-month"
            );

    private final By datepickerYear =
            By.cssSelector(
                    ".ui-datepicker-year"
            );

    private final By nextMonthButton =
            By.cssSelector(
                    ".ui-datepicker-next"
            );

    // ==========================================================
    // APPOINTMENT LOCATORS
    // ==========================================================

    private final By activityDropdown =
            By.id("Activity");

    private final By showAppointmentButton =
            By.id("btnshow");

    private final By appointmentResults =
            By.cssSelector(
                    "#result a.shortcut"
            );

    // ==========================================================
    // APPOINTMENT CONFIGURATION
    // ==========================================================

    private static final String ACTIVITY =
            "Badminton Court 1";

    private static final String TIME_SLOT =
            "05:00 PM To 06:00 PM";

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public AppointmentPage(WebDriver driver) {

        if (driver == null) {

            throw new IllegalArgumentException(
                    "WebDriver cannot be null."
            );
        }

        this.driver = driver;

        this.wait =
                new WebDriverWait(
                        driver,
                        WAIT_TIME
                );
    }

    // ==========================================================
    // EMPLOYEE MODEL
    // ==========================================================

    private static class Employee {

        private final String searchText;

        private final String fullName;

        Employee(
                String searchText,
                String fullName) {

            this.searchText = searchText;

            this.fullName = fullName;
        }
    }

    // ==========================================================
    // MAIN APPOINTMENT FLOW
    // ==========================================================

    public void bookAppointment() {

        System.out.println();
        System.out.println(
                "========================================"
        );

        System.out.println(
                "STARTING APPOINTMENT BOOKING"
        );

        System.out.println(
                "========================================"
        );

        // ------------------------------------------------------
        // GET TODAY IN IST
        // ------------------------------------------------------

        LocalDate today =
                LocalDate.now(IST);

        DayOfWeek todayDay =
                today.getDayOfWeek();

        System.out.println(
                "Today: " +
                today.format(DATE_FORMAT)
        );

        System.out.println(
                "Day: " +
                todayDay
        );

        // ------------------------------------------------------
        // SKIP WEEKENDS
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
        // APPOINTMENT DATE
        // ------------------------------------------------------

        LocalDate appointmentDate =
                today.plusDays(7);

        System.out.println(
                "Appointment date: " +
                appointmentDate.format(
                        DATE_FORMAT
                )
        );

        // ------------------------------------------------------
        // EMPLOYEES
        // ------------------------------------------------------

        List<Employee> employees =
                getEmployeesForToday(
                        todayDay
                );

        System.out.println();
        System.out.println(
                "Employees selected:"
        );

        for (Employee employee : employees) {

            System.out.println(
                    " - " +
                    employee.fullName
            );
        }

        System.out.println();

        // ======================================================
        // STEP 1 - OPEN CALENDAR
        // ======================================================

        openCalendar();

        // ======================================================
        // STEP 2 - SELECT DATE
        // ======================================================

        selectDate(
                appointmentDate
        );

        // ======================================================
        // STEP 3 - SELECT ACTIVITY
        // ======================================================

        selectActivity();

        // ======================================================
        // STEP 4 - SHOW APPOINTMENTS
        // ======================================================

        clickShowAppointment();

        // ======================================================
        // STEP 5 - SELECT TIME SLOT
        // ======================================================

        clickTimeSlot(
                TIME_SLOT
        );

        // ======================================================
        // STEP 6 - ADD EMPLOYEES
        // ======================================================

        addPlayers(
                employees
        );

        // ======================================================
        // STEP 7 - CAPTCHA + SAVE
        // ======================================================

        solveCaptchaAndSave();

        System.out.println();
        System.out.println(
                "========================================"
        );

        System.out.println(
                "APPOINTMENT BOOKING FLOW COMPLETED"
        );

        System.out.println(
                "========================================"
        );
    }

    // ==========================================================
    // GET EMPLOYEES FOR TODAY
    // ==========================================================

    private List<Employee> getEmployeesForToday(
            DayOfWeek day) {

        List<Employee> employees =
                new ArrayList<>();

        // ------------------------------------------------------
        // MONDAY / TUESDAY
        // ------------------------------------------------------

        if (day == DayOfWeek.MONDAY ||
            day == DayOfWeek.TUESDAY) {

            employees.add(
                    new Employee(
                            "shreyash",
                            "shreyash Kulkarni"
                    )
            );

            employees.add(
                    new Employee(
                            "sahadev",
                            "sahadev Gondalekar"
                    )
            );
        }

        // ------------------------------------------------------
        // WEDNESDAY
        // ------------------------------------------------------

        else if (day == DayOfWeek.WEDNESDAY) {

            employees.add(
                    new Employee(
                            "dileep",
                            "dileep Kumar"
                    )
            );

            employees.add(
                    new Employee(
                            "meghanath",
                            "meghanath Sai"
                    )
            );
        }

        // ------------------------------------------------------
        // THURSDAY / FRIDAY
        // ------------------------------------------------------

        else if (day == DayOfWeek.THURSDAY ||
                 day == DayOfWeek.FRIDAY) {

            employees.add(
                    new Employee(
                            "aditya",
                            "aditya Dhaygude"
                    )
            );

            employees.add(
                    new Employee(
                            "tejas",
                            "tejas Divgi"
                    )
            );
        }

        // ------------------------------------------------------
        // SAFETY
        // ------------------------------------------------------

        if (employees.isEmpty()) {

            throw new SkipException(
                    "No employee configuration found for: " +
                    day
            );
        }

        return employees;
    }

    // ==========================================================
    // OPEN CALENDAR
    // ==========================================================

    private void openCalendar() {

        System.out.println(
                "Opening calendar..."
        );

        WebElement calendar =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                calendarButton
                        )
                );

        calendar.click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        datepickerContainer
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

        System.out.println(
                "Selecting date: " +
                targetDate.format(DATE_FORMAT)
        );

        int targetDay =
                targetDate.getDayOfMonth();

        int targetMonth =
                targetDate.getMonthValue() - 1;

        int targetYear =
                targetDate.getYear();

        // ------------------------------------------------------
        // Navigate to target month
        // ------------------------------------------------------

        navigateToMonth(
                targetMonth,
                targetYear
        );

        // ------------------------------------------------------
        // Find exact date
        // ------------------------------------------------------

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

        By dateLocator =
                By.xpath(dateXPath);

        WebElement date =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                dateLocator
                        )
                );

        date.click();

        // ------------------------------------------------------
        // Verify date field
        // ------------------------------------------------------

        wait.until(driver -> {

            String value =
                    driver.findElement(
                            dateField
                    ).getAttribute(
                            "value"
                    );

            return value != null &&
                   !value.trim().isEmpty();
        });

        String selectedDate =
                driver.findElement(
                        dateField
                ).getAttribute(
                        "value"
                );

        System.out.println(
                "Selected date: " +
                selectedDate
        );
    }

    // ==========================================================
    // NAVIGATE DATEPICKER MONTH
    // ==========================================================

    private void navigateToMonth(
            int targetMonth,
            int targetYear) {

        for (int attempt = 0;
             attempt < 12;
             attempt++) {

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
                            displayedYear.trim()
                    );

            System.out.println(
                    "Calendar currently showing: " +
                    displayedMonth +
                    " " +
                    currentYear
            );

            if (currentMonth == targetMonth &&
                currentYear == targetYear) {

                System.out.println(
                        "Target month reached."
                );

                return;
            }

            WebElement nextButton =
                    wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    nextMonthButton
                            )
                    );

            String classes =
                    nextButton.getAttribute(
                            "class"
                    );

            if (classes != null &&
                classes.contains(
                        "ui-state-disabled"
                )) {

                throw new IllegalStateException(
                        "Next month button is disabled. " +
                        "Target date is unavailable."
                );
            }

            wait.until(
                    ExpectedConditions.elementToBeClickable(
                            nextMonthButton
                    )
            ).click();

            // Small wait for datepicker refresh
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            datepickerContainer
                    )
            );
        }

        throw new IllegalStateException(
                "Could not navigate to target month: " +
                targetMonth +
                "/" +
                targetYear
        );
    }

    // ==========================================================
    // MONTH NAME TO NUMBER
    // ==========================================================

    private int monthNameToNumber(
            String month) {

        if (month == null) {

            throw new IllegalArgumentException(
                    "Month cannot be null."
            );
        }

        return switch (
                month.trim().toLowerCase()
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
    // SELECT ACTIVITY
    // ==========================================================

    private void selectActivity() {

        System.out.println(
                "Selecting activity: " +
                ACTIVITY
        );

        WebElement activity =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                activityDropdown
                        )
                );

        Select select =
                new Select(activity);

        select.selectByVisibleText(
                ACTIVITY
        );

        // ------------------------------------------------------
        // Verify selection
        // ------------------------------------------------------

        String selected =
                select.getFirstSelectedOption()
                      .getText()
                      .trim();

        System.out.println(
                "Selected activity: " +
                selected
        );

        if (!selected.equalsIgnoreCase(
                ACTIVITY
        )) {

            throw new IllegalStateException(
                    "Activity selection failed. " +
                    "Expected: " +
                    ACTIVITY +
                    " | Actual: " +
                    selected
            );
        }
    }

    // ==========================================================
    // SHOW APPOINTMENT
    // ==========================================================

    private void clickShowAppointment() {

        System.out.println(
                "Clicking Show Appointment..."
        );

        WebElement button =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                showAppointmentButton
                        )
                );

        button.click();

        System.out.println(
                "Show Appointment clicked."
        );

        // ------------------------------------------------------
        // Wait for appointment result
        // ------------------------------------------------------

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        appointmentResults
                )
        );

        System.out.println(
                "Appointment slots loaded."
        );
    }

    // ==========================================================
    // GET TIME SLOT LOCATOR
    // ==========================================================

    private By getTimeSlot(
            String timeSlot) {

        String xpath =
                "//div[@id='result']" +
                "//a[contains(@class,'shortcut')]" +
                "[.//span[contains(normalize-space(.), '" +
                timeSlot +
                "')]]";

        return By.xpath(xpath);
    }

    // ==========================================================
    // CLICK TIME SLOT
    // ==========================================================

    private void clickTimeSlot(
            String timeSlot) {

        System.out.println(
                "Searching time slot: " +
                timeSlot
        );

        By slotLocator =
                getTimeSlot(
                        timeSlot
                );

        WebElement slot =
                wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                                slotLocator
                        )
                );

        String className =
                slot.getAttribute(
                        "class"
                );

        System.out.println(
                "Time slot class: " +
                className
        );

        // ------------------------------------------------------
        // Check booked status
        // ------------------------------------------------------

        if (className != null &&
            className.toLowerCase().contains(
                    "booked"
            )) {

            throw new IllegalStateException(
                    "Time slot is already BOOKED: " +
                    timeSlot
            );
        }

        // ------------------------------------------------------
        // Check availability
        // ------------------------------------------------------

        if (className != null &&
            className.toLowerCase().contains(
                    "available"
            )) {

            System.out.println(
                    "Time slot is AVAILABLE."
            );
        }

        // ------------------------------------------------------
        // Click
        // ------------------------------------------------------

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        slotLocator
                )
        ).click();

        System.out.println(
                "Clicked time slot: " +
                timeSlot
        );
    }

    // ==========================================================
    // ADD PLAYERS
    // ==========================================================

    private void addPlayers(
            List<Employee> employees) {

        System.out.println();
        System.out.println(
                "========================================"
        );

        System.out.println(
                "ADDING EMPLOYEES"
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

    private void selectAndAddEmployee(
            String searchText,
            String employeeFullName) {

        System.out.println();
        System.out.println(
                "----------------------------------------"
        );

        System.out.println(
                "Searching employee: " +
                searchText
        );

        System.out.println(
                "Expected name: " +
                employeeFullName
        );

        // ------------------------------------------------------
        // Search box
        // ------------------------------------------------------

        WebElement searchBox =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                employeeSearchField
                        )
                );

        searchBox.clear();

        searchBox.sendKeys(
                searchText
        );

        // ------------------------------------------------------
        // Wait for autocomplete
        // ------------------------------------------------------

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        autocompleteResults
                )
        );

        // ------------------------------------------------------
        // Exact case-insensitive employee XPath
        // ------------------------------------------------------

        String lowerName =
                employeeFullName.toLowerCase();

        String employeeXPath =
                "//ul[contains(@class,'ui-autocomplete')]" +
                "//li[contains(@class,'ui-menu-item')]" +
                "//a[" +
                "normalize-space(" +
                "translate(" +
                "text()," +
                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," +
                "'abcdefghijklmnopqrstuvwxyz'" +
                ")" +
                ")='" +
                lowerName +
                "'" +
                "]";

        By employeeLocator =
                By.xpath(
                        employeeXPath
                );

        WebElement employee =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                employeeLocator
                        )
                );

        System.out.println(
                "Employee found: " +
                employeeFullName
        );

        // ------------------------------------------------------
        // Click employee
        // ------------------------------------------------------

        employee.click();

        System.out.println(
                "Employee clicked."
        );

        // ------------------------------------------------------
        // Wait for autocomplete to disappear
        // ------------------------------------------------------

        try {

            wait.until(
                    ExpectedConditions.invisibilityOfElementLocated(
                            autocompleteResults
                    )
            );

        } catch (Exception ignored) {

            System.out.println(
                    "Autocomplete did not disappear immediately; continuing."
            );
        }

        // ------------------------------------------------------
        // Verify selected value
        // ------------------------------------------------------

        String selectedValue =
                driver.findElement(
                        employeeSearchField
                ).getAttribute(
                        "value"
                );

        System.out.println(
                "Search box after selection: [" +
                selectedValue +
                "]"
        );

        if (selectedValue == null ||
            selectedValue.trim().isEmpty()) {

            throw new IllegalStateException(
                    "Employee was not selected: " +
                    employeeFullName
            );
        }

        // ------------------------------------------------------
        // Click Add
        // ------------------------------------------------------

        WebElement addButton =
                wait.until(
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
        // Wait for search field to clear
        // ------------------------------------------------------

        wait.until(driver -> {

            String value =
                    driver.findElement(
                            employeeSearchField
                    ).getAttribute(
                            "value"
                    );

            return value == null ||
                   value.trim().isEmpty();
        });

        System.out.println(
                "Successfully added: " +
                employeeFullName
        );

        System.out.println(
                "----------------------------------------"
        );
    }

    // ==========================================================
    // CAPTCHA + SAVE
    // ==========================================================

    private void solveCaptchaAndSave() {

        System.out.println();
        System.out.println(
                "========================================"
        );

        System.out.println(
                "CAPTCHA PROCESSING"
        );

        System.out.println(
                "========================================"
        );

        try {

            // --------------------------------------------------
            // Wait for CAPTCHA image
            // --------------------------------------------------

            WebElement captcha =
                    wait.until(
                            ExpectedConditions.visibilityOfElementLocated(
                                    captchaImage
                            )
                    );

            System.out.println(
                    "CAPTCHA image is visible."
            );

            // --------------------------------------------------
            // Capture CAPTCHA screenshot
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
            // Tesseract
            // --------------------------------------------------

            Tesseract tesseract =
                    new Tesseract();

            // --------------------------------------------------
            // Locate tessdata
            // --------------------------------------------------

            String tessDataPath =
                    new File(
                            "src/test/resources/tessdata"
                    ).getAbsolutePath();

            File tessDataFolder =
                    new File(
                            tessDataPath
                    );

            System.out.println(
                    "Tessdata path: " +
                    tessDataPath
            );

            if (!tessDataFolder.exists()) {

                throw new IllegalStateException(
                        "Tessdata folder does not exist: " +
                        tessDataPath
                );
            }

            File englishData =
                    new File(
                            tessDataFolder,
                            "eng.traineddata"
                    );

            if (!englishData.exists()) {

                throw new IllegalStateException(
                        "eng.traineddata not found: " +
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
            // Clean OCR
            // --------------------------------------------------

            String expression =
                    cleanCaptchaText(
                            captchaText
                    );

            System.out.println(
                    "Detected expression: " +
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
                    "CAPTCHA answer: " +
                    answer
            );

            // --------------------------------------------------
            // Enter CAPTCHA
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
            // SAVE
            // --------------------------------------------------

            WebElement save =
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
                                    saveButton
                            )
                    );

            save.click();

            System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "SAVE BUTTON CLICKED"
            );

            System.out.println(
                    "========================================"
            );

        } catch (Exception e) {

            System.out.println();
            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "CAPTCHA PROCESSING FAILED"
            );

            System.out.println(
                    "========================================"
            );

            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to read/solve CAPTCHA.",
                    e
            );
        }
    }

    // ==========================================================
    // CLEAN CAPTCHA OCR
    // ==========================================================

    private String cleanCaptchaText(
            String captchaText) {

        if (captchaText == null ||
            captchaText.isBlank()) {

            throw new IllegalStateException(
                    "CAPTCHA OCR returned empty text."
            );
        }

        System.out.println(
                "Original OCR text: [" +
                captchaText +
                "]"
        );

        String text =
                captchaText
                        .toLowerCase()
                        .trim();

        // ------------------------------------------------------
        // If OCR contains "=",
        // only use expression before "=".
        //
        // Example:
        //
        // 20+5=25
        //
        // becomes:
        //
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
        // Remove spaces
        // ------------------------------------------------------

        text =
                text.replaceAll(
                        "\\s+",
                        ""
                );

        // ------------------------------------------------------
        // Common OCR corrections
        // ------------------------------------------------------

        text =
                text
                        .replace("o", "0")
                        .replace("i", "1")
                        .replace("l", "1")
                        .replace("s", "5")
                        .replace("b", "8");

        // ------------------------------------------------------
        // Multiplication symbol
        // ------------------------------------------------------

        text =
                text.replace(
                        "×",
                        "x"
                );

        // ------------------------------------------------------
        // Remove unsupported characters
        // ------------------------------------------------------

        text =
                text.replaceAll(
                        "[^0-9+\\-*/x]",
                        ""
                );

        // ------------------------------------------------------
        // Validate
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
                "Calculating CAPTCHA: " +
                expression
        );

        // ------------------------------------------------------
        // ADDITION
        // ------------------------------------------------------

        if (expression.contains("+")) {

            String[] numbers =
                    expression.split(
                            "\\+"
                    );

            validateTwoNumbers(
                    numbers,
                    expression
            );

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
        // SUBTRACTION
        // ------------------------------------------------------

        if (expression.contains("-")) {

            String[] numbers =
                    expression.split(
                            "-"
                    );

            validateTwoNumbers(
                    numbers,
                    expression
            );

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
        // MULTIPLICATION *
        // ------------------------------------------------------

        if (expression.contains("*")) {

            String[] numbers =
                    expression.split(
                            "\\*"
                    );

            validateTwoNumbers(
                    numbers,
                    expression
            );

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
        // MULTIPLICATION x
        // ------------------------------------------------------

        if (expression.contains("x")) {

            String[] numbers =
                    expression.split(
                            "x"
                    );

            validateTwoNumbers(
                    numbers,
                    expression
            );

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
        // DIVISION
        // ------------------------------------------------------

        if (expression.contains("/")) {

            String[] numbers =
                    expression.split(
                            "/"
                    );

            validateTwoNumbers(
                    numbers,
                    expression
            );

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
                "Unsupported CAPTCHA expression: " +
                expression
        );
    }

    // ==========================================================
    // VALIDATE CAPTCHA NUMBERS
    // ==========================================================

    private void validateTwoNumbers(
            String[] numbers,
            String expression) {

        if (numbers.length != 2) {

            throw new IllegalArgumentException(
                    "Invalid CAPTCHA expression: " +
                    expression
            );
        }

        if (numbers[0].isBlank() ||
            numbers[1].isBlank()) {

            throw new IllegalArgumentException(
                    "Invalid CAPTCHA numbers: " +
                    expression
            );
        }
    }
}
