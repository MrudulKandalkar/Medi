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

public class AppointmentPage1 {

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
    // DATE
    // ==========================================================

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
    // ACTIVITY
    // ==========================================================

    private final By activityDropdown =
            By.id("Activity");

    private final By showAppointmentButton =
            By.id("btnshow");

    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public AppointmentPage1(WebDriver driver) {

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

        System.out.println(
                "========================================"
        );

        System.out.println(
                "STARTING APPOINTMENT PAGE 1"
        );

        System.out.println(
                "========================================"
        );

        // ------------------------------------------------------
        // TODAY
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
        // SATURDAY / SUNDAY
        // ------------------------------------------------------

        if (todayDay == DayOfWeek.SATURDAY ||
            todayDay == DayOfWeek.SUNDAY) {

            System.out.println(
                    "Weekend detected."
            );

            System.out.println(
                    "Appointment automation skipped."
            );

            return;
        }

        // ------------------------------------------------------
        // APPOINTMENT DATE
        // ------------------------------------------------------

        LocalDate appointmentDate =
                today.plusDays(7);

        System.out.println(
                "Appointment date: " +
                appointmentDate.format(DATE_FORMAT)
        );

        // ------------------------------------------------------
        // EMPLOYEES
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
                    employee.fullName
            );
        }

        // ------------------------------------------------------
        // OPEN CALENDAR
        // ------------------------------------------------------

        openCalendar();

        // ------------------------------------------------------
        // SELECT DATE
        // ------------------------------------------------------

        selectDate(
                appointmentDate
        );

        // ------------------------------------------------------
        // SELECT ACTIVITY
        // ------------------------------------------------------

        selectBadmintonCourt1();

        // ------------------------------------------------------
        // SHOW APPOINTMENT
        // ------------------------------------------------------

        clickShowAppointment();

        // ------------------------------------------------------
        // SELECT TIME SLOT
        // ------------------------------------------------------

        clickTimeSlot(
                "05:00 PM To 06:00 PM"
        );

        // ------------------------------------------------------
        // ADD EMPLOYEES
        // ------------------------------------------------------

        addPlayers(
                employees
        );

        // ------------------------------------------------------
        // CAPTCHA
        // ------------------------------------------------------

        solveCaptchaAndSave();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "APPOINTMENT PAGE 1 COMPLETED"
        );

        System.out.println(
                "========================================"
        );
    }

    // ==========================================================
    // EMPLOYEE CONFIGURATION
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

        if (employees.isEmpty()) {

            throw new IllegalStateException(
                    "No employee configuration found for day: "
                    + day
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

        navigateToMonth(
                targetMonth,
                targetYear
        );

        String dateXPath =
                "//div[@id='ui-datepicker-div']"
                + "//td[@data-handler='selectDay']"
                + "[@data-month='"
                + targetMonth
                + "']"
                + "[@data-year='"
                + targetYear
                + "']"
                + "//a[normalize-space(text())='"
                + targetDay
                + "']";

        By dateLocator =
                By.xpath(dateXPath);

        WebElement date =
                wait.until(
                        ExpectedConditions.elementToBeClickable(
                                dateLocator
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
                           !value.trim().isEmpty();
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

            System.out.println(
                    "Calendar currently showing: "
                    + displayedMonth
                    + " "
                    + displayedYear
            );

            if (currentMonth == targetMonth &&
                currentYear == targetYear) {

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
                        "Next month button is disabled. "
                        + "Target date cannot be selected."
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

        String value =
                month.trim().toLowerCase();

        switch (value) {

            case "january":
                return 0;

            case "february":
                return 1;

            case "march":
                return 2;

            case "april":
                return 3;

            case "may":
                return 4;

            case "june":
                return 5;

            case "july":
                return 6;

            case "august":
                return 7;

            case "september":
                return 8;

            case "october":
                return 9;

            case "november":
                return 10;

            case "december":
                return 11;

            default:
                throw new IllegalArgumentException(
                        "Unknown month: " +
                        month
                );
        }
    }

    // ==========================================================
    // SELECT BADMINTON COURT 1
    // ==========================================================

    private void selectBadmintonCourt1() {

        System.out.println(
                "Selecting Badminton Court 1..."
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
                "Badminton Court 1"
        );

        System.out.println(
                "Selected: Badminton Court 1"
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
                "//div[@id='result']"
                + "//a[contains(@class,'shortcut')]"
                + "[.//span[contains("
                + "normalize-space(.), '"
                + timeSlot
                + "')]]";

        return By.xpath(xpath);
    }

    // ==========================================================
    // CLICK TIME SLOT
    // ==========================================================

    private void clickTimeSlot(
            String timeSlot) {

        System.out.println(
                "Looking for time slot: " +
                timeSlot
        );

        By slotLocator =
                getTimeSlot(timeSlot);

        WebElement slot =
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                slotLocator
                        )
                );

        String className =
                slot.getAttribute("class");

        System.out.println(
                "Time slot class: " +
                className
        );

        if (className != null &&
            className.toLowerCase().contains(
                    "booked"
            )) {

            throw new IllegalStateException(
                    "Requested time slot is BOOKED: "
                    + timeSlot
            );
        }

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        slotLocator
                )
        ).click();

        System.out.println(
                "Selected time slot: " +
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
    // SELECT AND ADD EMPLOYEE
    // ==========================================================

    private void selectAndAddEmployee(
            String searchText,
            String employeeFullName) {

        System.out.println(
                "Searching employee: " +
                searchText
        );

        // ------------------------------------------------------
        // SEARCH BOX
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
        // AUTOCOMPLETE
        // ------------------------------------------------------

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        autocompleteResults
                )
        );

        // ------------------------------------------------------
        // EXACT EMPLOYEE
        // ------------------------------------------------------

        String employeeXPath =
                "//ul[contains(@class,'ui-autocomplete')]"
                + "//li[contains(@class,'ui-menu-item')]"
                + "//a["
                + "normalize-space("
                + "translate(text(),"
                + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
                + "'abcdefghijklmnopqrstuvwxyz')"
                + ")='"
                + employeeFullName.toLowerCase()
                + "'"
                + "]";

        By employeeLocator =
                By.xpath(employeeXPath);

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

        employee.click();

        // ------------------------------------------------------
        // WAIT FOR AUTOCOMPLETE TO DISAPPEAR
        // ------------------------------------------------------

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        autocompleteResults
                )
        );

        // ------------------------------------------------------
        // VERIFY SEARCH BOX
        // ------------------------------------------------------

        String selectedValue =
                driver.findElement(
                        employeeSearchField
                ).getAttribute("value");

        System.out.println(
                "Selected employee value: [" +
                selectedValue +
                "]"
        );

        if (selectedValue == null ||
            selectedValue.trim().isEmpty()) {

            throw new IllegalStateException(
                    "Employee selection failed: "
                    + employeeFullName
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
        // WAIT SEARCH BOX CLEAR
        // ------------------------------------------------------

        wait.until(
                driver -> {

                    String value =
                            driver.findElement(
                                    employeeSearchField
                            ).getAttribute("value");

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
    // CAPTCHA
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
            // SCREENSHOT CAPTCHA
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
            // TESSERACT
            // --------------------------------------------------

            Tesseract tesseract =
                    new Tesseract();

            String tessDataPath =
                    new File(
                            "src/test/resources/tessdata"
                    ).getAbsolutePath();

            File tessDataFolder =
                    new File(
                            tessDataPath
                    );

            if (!tessDataFolder.exists()) {

                throw new IllegalStateException(
                        "Tessdata folder does not exist: "
                        + tessDataPath
                );
            }

            File englishData =
                    new File(
                            tessDataFolder,
                            "eng.traineddata"
                    );

            if (!englishData.exists()) {

                throw new IllegalStateException(
                        "eng.traineddata not found: "
                        + englishData.getAbsolutePath()
                );
            }

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
            // CLEAN OCR
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
            // CALCULATE
            // --------------------------------------------------

            int answer =
                    calculateCaptcha(
                            expression
                    );

            System.out.println(
                    "CAPTCHA answer calculated."
            );

            // --------------------------------------------------
            // ENTER ANSWER
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

            System.out.println(
                    "Appointment SAVE clicked."
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
    // CLEAN CAPTCHA
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
                "Original OCR: [" +
                captchaText +
                "]"
        );

        String text =
                captchaText
                        .toLowerCase()
                        .trim();

        // ------------------------------------------------------
        // Take expression before =
        // ------------------------------------------------------

        if (text.contains("=")) {

            text =
                    text.substring(
                            0,
                            text.indexOf("=")
                    );
        }

        // ------------------------------------------------------
        // OCR corrections BEFORE removing characters
        // ------------------------------------------------------

        text =
                text.replace("o", "0")
                    .replace("i", "1")
                    .replace("l", "1")
                    .replace("s", "5")
                    .replace("b", "8");

        // ------------------------------------------------------
        // Multiplication symbols
        // ------------------------------------------------------

        text =
                text.replace("×", "x")
                    .replace("÷", "/");

        // ------------------------------------------------------
        // Remove spaces
        // ------------------------------------------------------

        text =
                text.replaceAll(
                        "\\s+",
                        ""
                );

        // ------------------------------------------------------
        // Keep only supported characters
        // ------------------------------------------------------

        text =
                text.replaceAll(
                        "[^0-9+\\-*/x]",
                        ""
                );

        if (text.isBlank()) {

            throw new IllegalStateException(
                    "Could not extract CAPTCHA expression from OCR: "
                    + captchaText
            );
        }

        System.out.println(
                "Cleaned CAPTCHA: [" +
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
                "Unsupported CAPTCHA expression: "
                + expression
        );
    }

    // ==========================================================
    // VALIDATE CAPTCHA NUMBERS
    // ==========================================================

    private void validateTwoNumbers(
            String[] numbers,
            String expression) {

        if (numbers.length != 2 ||
            numbers[0].isBlank() ||
            numbers[1].isBlank()) {

            throw new IllegalArgumentException(
                    "Invalid CAPTCHA expression: "
                    + expression
            );
        }
    }
}
