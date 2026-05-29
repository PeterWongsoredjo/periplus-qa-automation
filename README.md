# Periplus E-Commerce QA Automation

![Java](https://img.shields.io/badge/Java-21-007396?style=flat&logo=openjdk&logoColor=white)
![Selenium](https://img.shields.io/badge/Selenium-4.18.1-43B02A?style=flat&logo=selenium&logoColor=white)
![TestNG](https://img.shields.io/badge/TestNG-7.9.0-FF6C37?style=flat)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=flat&logo=apachemaven&logoColor=white)
![WebDriverManager](https://img.shields.io/badge/WebDriverManager-5.7.0-4B8BBE?style=flat)
![POM](https://img.shields.io/badge/Architecture-Page%20Object%20Model-blue?style=flat)

## 1. Project Overview

This repository contains an end-to-end UI automation suite for the **Periplus** online bookstore
(`https://www.periplus.com/`), built as part of a technical assessment (Scenario Option B).

The suite validates the core **Shopping Cart** user journey of the e-commerce platform, covering:

- Adding a single product to the cart.
- Adding multiple unique products.
- Updating item quantities and recalculating subtotals.
- Removing items from the cart.
- Verifying subtotal accuracy against individual item prices.
- Guest-to-member cart retention after login.
- Voucher application handling.

The framework is implemented in Java using Selenium WebDriver and TestNG, and follows the
**Page Object Model (POM)** design pattern to keep test logic, page interactions, and configuration
cleanly separated and maintainable.

## 2. Prerequisites

Before running the suite, ensure the following are installed and available on your machine:

| Requirement      | Version / Notes                                  |
| ---------------- | ------------------------------------------------ |
| Java JDK         | 21 (LTS)                                          |
| IntelliJ IDEA    | Community or Ultimate (recommended IDE)           |
| Google Chrome    | Latest stable release                             |
| Maven            | Bundled with IntelliJ, or installed standalone    |

> Note: The matching ChromeDriver binary is resolved automatically at runtime by **WebDriverManager**,
> so no manual driver download is required.

## 3. Installation & Setup

The framework reads test credentials from a local properties file that is intentionally excluded from
version control. Configure it as follows:

1. **Clone the repository:**

   ```bash
   git clone <repository-url>
   cd periplus-qa-automation/PeriplusQAAutomation
   ```

2. **Navigate to the resources directory:**

   ```bash
   cd src/main/resources/
   ```

3. **Duplicate the example configuration file and rename the copy to `config.properties`:**

   ```bash
   # Windows (PowerShell)
   Copy-Item config.example.properties config.properties

   # macOS / Linux
   cp config.example.properties config.properties
   ```

4. **Open `config.properties` and fill in your valid Periplus test credentials:**

   ```properties
   baseUrl=https://www.periplus.com/
   testEmail=your-registered-email@example.com
   testPassword=your-account-password
   ```

> Important: `config.properties` holds real credentials and must never be committed. Keep only
> `config.example.properties` under version control as the template.

## 4. How to Run the Tests

Two methods are provided so the suite can be executed reliably from either the terminal or the IDE.

### Method A — Terminal (Maven)

From the `PeriplusQAAutomation` project directory, run:

```bash
mvn clean test
```

If a Maven Wrapper is present in the project, you may use it instead (no local Maven installation required):

```bash
# Windows
.\mvnw clean test

# macOS / Linux
./mvnw clean test
```

### Method B — IntelliJ IDEA (GUI)

Run the suite directly inside IntelliJ without touching the terminal:

1. Open the project in IntelliJ IDEA (open the `PeriplusQAAutomation` folder so the `pom.xml` is detected as a Maven project).
2. Open the **Maven** tool window via the icon on the **right sidebar** (or **View → Tool Windows → Maven**).
3. Expand **PeriplusQAAutomation → Lifecycle**.
4. **Double-click `clean`** to clear previous build artifacts.
5. **Double-click `test`** to compile and execute the full test suite.

Alternatively, to run just the cart tests:

- Open `src/test/java/tests/CartTests.java`.
- Click the green **Play** button in the gutter next to the `CartTests` class declaration and choose **Run 'CartTests'**.

## 5. Project Structure

```
PeriplusQAAutomation/
├── pom.xml                              # Maven build configuration and dependencies
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── pages/                   # Page Object classes (one per page/section)
    │   │   │   ├── BasePage.java        # Shared waits and reusable WebDriver helpers
    │   │   │   ├── HomePage.java        # Search, navigation, cart counter, login entry
    │   │   │   ├── LoginPage.java       # Authentication flow
    │   │   │   ├── ProductPage.java     # Product selection and add-to-cart actions
    │   │   │   └── CartPage.java        # Cart actions, subtotal parsing, voucher handling
    │   │   └── utils/
    │   │       └── ConfigReader.java    # Reads credentials/config from config.properties
    │   └── resources/
    │       └── config.example.properties  # Credentials template (copy to config.properties)
    └── test/
        └── java/
            ├── base/
            │   └── BaseTest.java        # WebDriver lifecycle (@BeforeMethod/@AfterMethod)
            └── tests/
                └── CartTests.java       # TestNG cart test cases (TC-CART-001..007)
```

- **`src/main/java/pages`** — Page Object classes that encapsulate locators and interactions for each
  page, keeping selectors out of the test logic.
- **`src/test/java/base`** — Base test infrastructure responsible for browser setup and teardown, so
  every test starts from a clean, isolated session.
- **`src/test/java/tests`** — The TestNG test classes that implement the cart scenarios and assertions.

## 6. Future Scope

The current suite fully automates the **golden path** of the shopping cart experience. The following
edge cases are acknowledged but scoped for future implementation or manual coverage:

- **Session expiry / persistence:** Validating cart behavior when an authenticated session times out.
- **Cross-device synchronization:** Verifying that a cart populated on one device reflects on another
  after login.
- **Voucher / coupon application:** The coupon UI is currently commented out in the live Periplus cart
  DOM. The supporting `CartPage` methods are already in place and the test is marked as skipped until
  the feature is re-enabled on the site.
