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

import net.sourceforge.tess4j.Tesseract;

public class AppointmentPage {

    // ==========================================================
    // DRIVER + WAIT
    // ==========================================================

    private final WebDriver driver;

    private final WebDriverWait wait;

    private static final ZoneId IST =
            ZoneId.of("Asia/Kolkata");

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
    // DATE LOCATORS
    // ==========================================================

    private final By calendarButton =
            By.cssSelector(
                    "img.ui-datepicker-trigger"
            );

    private final By dateField =
            By.id("DateFrom");

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
    // ACTIVITY LOCATOR
    // ==========================================================

    private final By activityDropdown =
            By.id("Activity");

    // ==========================================================
    // SHOW APPOINTMENT
    // ==========================================================

    private final By showAppointmentButton =
            By.id("btnshow");

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public AppointmentPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
    }

    // ==========================================================
    // EMPLOYEE CLASS
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
        // GET CURRENT DATE IN INDIA
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
        // SAFETY: SKIP WEEKENDS
        // ------------------------------------------------------

        if (todayDay == DayOfWeek.SATURDAY ||
            todayDay == DayOfWeek.SUNDAY) {

            throw new IllegalStateException(
                    "Today is " +
                    todayDay +
                    ". Appointment automation skipped."
            );
        }

        // ------------------------------------------------------
        // APPOINTMENT DATE
        // TODAY + 7 DAYS
        // ------------------------------------------------------

        LocalDate appointmentDate =
                today.plusDays(7);

        System.out.println(
                "Appointment date: " +
                appointmentDate.format(DATE_FORMAT)
        );

        // ------------------------------------------------------
        // GET EMPLOYEES FOR TODAY
        // ------------------------------------------------------

        List<Employee> employees =
                getEmployeesForToday(
                        todayDay
                );

        System.out.println(
                "Employees selected:"
        );

        for (Employee employee : employees) {

            System.out.println(
                    " - " +
                    employee.fullName
            );
        }

        // ======================================================
        // STEP 1: OPEN CALENDAR
        // ======================================================

        openCalendar();

        // ======================================================
        // STEP 2: SELECT APPOINTMENT DATE
        // ======================================================

        selectDate(
                appointmentDate
        );

        // ======================================================
        // STEP 3: SELECT BADMINTON COURT
        // ======================================================

        selectBadmintonCourt2();

        // ======================================================
        // STEP 4: SHOW APPOINTMENT
        // ======================================================

        clickShowAppointment();

        // ======================================================
        // STEP 5: SELECT TIME SLOT
        // ======================================================

        clickTimeSlot(
                "06:00 PM To 07:00 PM"
        );

        // ======================================================
        // STEP 6: ADD EMPLOYEES
        // ======================================================

        addPlayers(
                employees
        );

        // ======================================================
        // STEP 7: CAPTCHA + SAVE
        // ======================================================

        solveCaptchaAndSave();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "APPOINTMENT FLOW COMPLETED"
        );

        System.out.println(
                "========================================"
        );
    }

    // ==========================================================
    // GET EMPLOYEES BASED ON DAY
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

        // ------------------------------------------------------
        // WEDNESDAY
        // ------------------------------------------------------

        else if (day == DayOfWeek.WEDNESDAY) {

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
        // THURSDAY / FRIDAY
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
        // SAFETY
        // ------------------------------------------------------

        if (employees.isEmpty()) {

            throw new IllegalStateException(
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

        System.out.println(
                "Selecting date: " +
                targetDate.format(DATE_FORMAT)
        );

        // ------------------------------------------------------
        // NAVIGATE TO REQUIRED MONTH
        // ------------------------------------------------------

        navigateToMonth(
                targetMonth,
                targetYear
        );

        // ------------------------------------------------------
        // DATE XPATH
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

        WebElement date =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.xpath(dateXPath)
                        )
                );

        date.click();

        // ------------------------------------------------------
        // WAIT FOR DATE FIELD
        // ------------------------------------------------------

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

        String selectedDate =
                driver.findElement(
                        dateField
                ).getAttribute("value");

        System.out.println(
                "Selected date: " +
                selectedDate
        );
    }

    // ==========================================================
    // NAVIGATE TO MONTH
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

            // --------------------------------------------------
            // TARGET FOUND
            // --------------------------------------------------

            if (currentMonth == targetMonth &&
                currentYear == targetYear) {

                return;
            }

            // --------------------------------------------------
            // NEXT MONTH
            // --------------------------------------------------

            WebElement nextButton =
                    wait.until(
                            ExpectedConditions.elementToBeClickable(
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
    // MONTH NAME TO NUMBER
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
    // SELECT BADMINTON COURT 2
    // ==========================================================

    private void selectBadmintonCourt2() {

        System.out.println(
                "Selecting Badminton Court 2..."
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
    // TIME SLOT LOCATOR
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
                "Slot class: " +
                className
        );

        // ------------------------------------------------------
        // CHECK BOOKED
        // ------------------------------------------------------

        if (className != null &&
            className.contains("Booked")) {

            throw new IllegalStateException(
                    "Time slot is already BOOKED: " +
                    timeSlot
            );
        }

        // ------------------------------------------------------
        // CLICK SLOT
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

        System.out.println(
                "----------------------------------------"
        );

        System.out.println(
                "Searching employee: " +
                searchText
        );

        System.out.println(
                "Expected employee: " +
                employeeFullName
        );

        // ------------------------------------------------------
        // FIND SEARCH BOX
        // ------------------------------------------------------

        WebElement searchBox =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                employeeSearchField
                        )
                );

        // ------------------------------------------------------
        // CLEAR SEARCH BOX
        // ------------------------------------------------------

        searchBox.clear();

        // ------------------------------------------------------
        // ENTER SEARCH TEXT
        // ------------------------------------------------------

        searchBox.sendKeys(
                searchText
        );

        // ------------------------------------------------------
        // WAIT FOR AUTOCOMPLETE
        // ------------------------------------------------------

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        autocompleteResults
                )
        );

        // ------------------------------------------------------
        // EXACT EMPLOYEE XPATH
        // CASE INSENSITIVE
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

        By employeeLocator =
                By.xpath(
                        employeeXPath
                );

        // ------------------------------------------------------
        // WAIT FOR EMPLOYEE
        // ------------------------------------------------------

        WebElement employee =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                employeeLocator
                        )
                );

        System.out.println(
                "Found employee: " +
                employeeFullName
        );

        // ------------------------------------------------------
        // CLICK EMPLOYEE
        // ------------------------------------------------------

        employee.click();

        System.out.println(
                "Clicked employee: " +
                employeeFullName
        );

        // ------------------------------------------------------
        // WAIT FOR AUTOCOMPLETE TO DISAPPEAR
        // ------------------------------------------------------

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        autocompleteResults
                )
        );

        // ------------------------------------------------------
        // VERIFY SELECTED VALUE
        // ------------------------------------------------------

        String selectedValue =
                driver.findElement(
                        employeeSearchField
                ).getAttribute(
                        "value"
                );

        System.out.println(
                "Search box value: [" +
                selectedValue +
                "]"
        );

        if (selectedValue == null ||
            selectedValue.trim().isEmpty()) {

            throw new IllegalStateException(
                    "Employee selection failed: " +
                    employeeFullName
            );
        }

        // ------------------------------------------------------
        // CLICK ADD
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
        // WAIT FOR SEARCH BOX TO CLEAR
        // ------------------------------------------------------

        wait.until(
                driver -> {

                    String value =
                            driver.findElement(
                                    employeeSearchField
                            ).getAttribute(
                                    "value"
                            );

                    return value == null ||
                           value.trim().isEmpty();
                }
        );

        System.out.println(
                "Successfully added: " +
                employeeFullName
        );
    }

    // ==========================================================
    // CAPTCHA + SAVE
    // ==========================================================

    private void solveCaptchaAndSave() {

        System.out.println(
                "========================================"
        );

        System.out.println(
                "READING CAPTCHA"
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
            // CAPTURE CAPTCHA IMAGE
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

            System.out.println(
                    "CAPTCHA dimensions: " +
                    captchaImageData.getWidth() +
                    "x" +
                    captchaImageData.getHeight()
            );

            // --------------------------------------------------
            // CREATE TESSERACT
            // --------------------------------------------------

            Tesseract tesseract =
                    new Tesseract();

            // --------------------------------------------------
            // TESSDATA PATH
            // --------------------------------------------------

            String tessDataPath =
                    new File(
                            "src/test/resources/tessdata"
                    ).getAbsolutePath();

            System.out.println(
                    "Tessdata path: " +
                    tessDataPath
            );

            File tessDataFolder =
                    new File(
                            tessDataPath
                    );

            if (!tessDataFolder.exists() ||
                !tessDataFolder.isDirectory()) {

                throw new IllegalStateException(
                        "Tessdata folder does not exist: " +
                        tessDataPath
                );
            }

            // --------------------------------------------------
            // CHECK ENG TRAINED DATA
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
            // CONFIGURE TESSERACT
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
            // CLEAN OCR TEXT
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
            // CALCULATE ANSWER
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
            // ENTER CAPTCHA ANSWER
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
            // CLICK SAVE
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
                    "SAVE BUTTON CLICKED"
            );

            System.out.println(
                    "========================================"
            );

        } catch (Exception e) {

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
    // CLEAN CAPTCHA TEXT
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

        // ------------------------------------------------------
        // LOWERCASE
        // ------------------------------------------------------

        String text =
                captchaText
                        .toLowerCase()
                        .trim();

        // ------------------------------------------------------
        // CAPTCHA MAY LOOK LIKE:
        //
        // 20+5=?
        //
        // We only need:
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
        // REMOVE WHITESPACE
        // ------------------------------------------------------

        text =
                text.replaceAll(
                        "\\s+",
                        ""
                );

        // ------------------------------------------------------
        // COMMON OCR CORRECTIONS
        // ------------------------------------------------------

        text = text
                .replace("o", "0")
                .replace("i", "1")
                .replace("l", "1")
                .replace("s", "5")
                .replace("b", "8");

        // ------------------------------------------------------
        // MULTIPLICATION SYMBOL
        // ------------------------------------------------------

        text =
                text.replace(
                        "×",
                        "x"
                );

        // ------------------------------------------------------
        // REMOVE INVALID CHARACTERS
        // ------------------------------------------------------

        text =
                text.replaceAll(
                        "[^0-9+\\-*/x]",
                        ""
                );

        // ------------------------------------------------------
        // VALIDATE
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

        // ======================================================
        // ADDITION
        // Example: 7+8
        // ======================================================

        if (expression.contains("+")) {

            String[] numbers =
                    expression.split(
                            "\\+"
                    );

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

        // ======================================================
        // SUBTRACTION
        // Example: 8-3
        // ======================================================

        if (expression.contains("-")) {

            String[] numbers =
                    expression.split(
                            "-"
                    );

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

        // ======================================================
        // MULTIPLICATION *
        // ======================================================

        if (expression.contains("*")) {

            String[] numbers =
                    expression.split(
                            "\\*"
                    );

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

        // ======================================================
        // MULTIPLICATION x
        // ======================================================

        if (expression.contains("x")) {

            String[] numbers =
                    expression.split(
                            "x"
                    );

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

        // ======================================================
        // DIVISION
        // ======================================================

        if (expression.contains("/")) {

            String[] numbers =
                    expression.split(
                            "/"
                    );

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

        // ======================================================
        // UNKNOWN OPERATOR
        // ======================================================

        throw new IllegalArgumentException(
                "Could not understand CAPTCHA expression: " +
                expression
        );
    }
}
