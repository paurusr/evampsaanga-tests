# AutoQA Test Framework Guide

## Prerequisites
Before starting to write your first AutoQA test, you need to set up the **AutoQA archetype**. This archetype provides the foundational structure for your tests. Within the archetype, you will find two essential classes:

- **BaseTest**: Contains the necessary methods to start a test run.
- **BasePage**: Provides several utility methods to interact with web pages.

To access the `@AqaInfo` annotation, add the `aqa-tools` dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.aqanetics</groupId>
    <artifactId>aqa-tools</artifactId>
    <version>1.0.6</version>
</dependency>
```

## The `@AQAInfo` Annotation
The use of the `@AQAInfo` annotation is not mandatory, but it is highly recommended for enhancing the readability and maintainability of your tests. This annotation allows you to specify metadata about the test case, making it easier to manage and understand.

### Example Usage:
```
@AQAInfo(
metadata = {"M365", "Feature", "MSOS"},
name = "Sample Test Case",
description = "This is a short description of the test case.",
comment = "A detailed explanation of the test case goes here."
)
@Test
public class ExampleTest {
// Test logic implementation
}
```

### Parameters:
1. **Metadata**: Tags to categorize or specify information about the test (e.g., `M365`, `Feature`, `MSOS`).
2. **Name**: The name of the test case as it appears in AutoQA.
3. **Description**: A concise description of the test case.
4. **Comment**: A longer explanation or additional context for the test case.

---

## Test Case Structure
AutoQA test cases follow a structured approach divided into three phases:

1. **Setup Phase**: Code that is executed before the test starts, defined using `@BeforeMethod`.
2. **Execution Phase**: The main test logic, defined using `@Test`.
3. **Teardown Phase**: Code that is executed after the test completes, defined using `@AfterMethod`.

### Example Test Structure:
```
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class SampleTest {

    @BeforeMethod
    public void setup() {
        // Setup logic before the test
    }

    @Test
    public void executeTest() {
        // Main test execution logic
    }

    @AfterMethod
    public void teardown() {
        // Cleanup logic after the test
    }
}
```

### Best Practices:
- Create a separate test class for each test case.
- Limit each test class to one `@BeforeMethod`, one `@AfterMethod`, and one `@Test` method.
- Write all test logic inside the test class; do not reference test logic outside of it.
- Avoid referencing one `@Test` method from another.

---

## Logging
Adding logs to your test cases helps in tracking the execution flow and debugging issues. Use the `LoggerFactory` object to create and manage log messages.

### Log Levels:
- **Debug**: For detailed debugging information.
- **Info**: For general information about the execution flow.
- **Warn**: For non-critical warnings.
- **Error**: For critical issues or errors.

### Example Logging Code:
```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExample {

    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);

    public void sampleMethod() {
        logger.info("Test execution started.");
        logger.debug("Debugging details here.");
        logger.warn("This is a warning message.");
        logger.error("An error occurred during execution.");
    }
}
```

---

## Conclusion
This guide outlines the foundational steps for writing AutoQA tests. By following the recommended structure, utilizing annotations, and incorporating logging, you can create maintainable and effective test cases.
