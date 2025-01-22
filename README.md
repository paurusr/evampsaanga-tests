# AutoQA Test Framework User Guide

## Table of Contents
- [Getting Started](#getting-started)
    - [Project Initialization](#project-initialization)
    - [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [The @AQAInfo Annotation](#the-aqainfo-annotation)
- [Test Case Structure](#test-case-structure)
- [Logging](#logging)
- [AQA Tasks Library](#aqa-tasks-library)
    - [Module: aqa-cli](#module-aqa-cli)
    - [Module: aqa-selenium](#module-aqa-selenium)
    - [Module: aqa-metasploit](#module-aqa-metasploit)
- [Adding Dependencies](#adding-dependencies)
- [Notes](#notes)
## Getting Started

### Project Initialization
To create a new AutoQA test project, use the Maven archetype provided by AQA. Run the following command in your terminal:

```bash
mvn archetype:generate \
    -DgroupId=<com.package> \
    -DartifactId=<artifact-name> \
    -DarchetypeGroupId=com.aqanetics \
    -DarchetypeArtifactId=aqa-tasks-archetype
```
There are already some examples in the project to help you get started:
1. **Selenium Test Example**
    - Location: `src/test/java/com/evampsaanga/selenium/GoogleTest.java`
    - Demonstrates how to use the selenium-task module for web testing

2. **CLI Test Example**
    - Location: `src/test/java/com/evampsaanga/cli/CLICommandTest.java`
    - Shows how to use the cli-task module for command-line testing

Generated project provides the foundational structure for your tests. It contains two essential classes:

- **BaseTest**: Contains the necessary methods to start a test run.
- **BasePage**: Provides several utility methods to interact with web pages.

It contains `aqa-tools` dependency for `@AqaInfo` annotation support as well.

## The @AQAInfo Annotation
The use of the `@AQAInfo` annotation is not mandatory, but it is highly recommended for enhancing the readability and maintainability of your tests. This annotation allows you to specify metadata about the test case, making it easier to manage and understand.

### Example Usage:
```java
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

## Test Case Structure
AutoQA test cases follow a structured approach divided into three phases:

1. **Setup Phase**: Code that is executed before the test starts, defined using `@BeforeMethod`.
2. **Execution Phase**: The main test logic, defined using `@Test`.
3. **Teardown Phase**: Code that is executed after the test completes, defined using `@AfterMethod`.

### Example Test Structure:
```java
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
```java
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

## AQA Tasks Library
The aqa-tasks project is a multi-module Maven project designed to simplify basic configuration of AQA, automation and testing tasks. It includes three specialized modules:

### Module: aqa-cli
This module facilitates:
- Executing CLI commands programmatically within Java
- Supporting remote (SSH) command execution by providing ssh key
- Capturing and verifying logs generated by these commands

### Module: aqa-selenium
This module provides:
- Base configuration for Selenium WebDriver and Selenoid setups
- A set of utility methods to streamline Selenium tests
- Built-in logging to make debugging and test tracking easier

### Module: aqa-metasploit
This module focuses on:
- Running Metasploit commands in an automated manner
- Simplifying interaction with the `paurusr/runner:msf` container to execute Metasploit scripts

## Adding Dependencies
To use any of the aqa-tasks modules in your project, include them as dependencies in your pom.xml file.

### Adding Individual Modules

**For aqa-cli:**
```xml
<dependency>
    <groupId>com.aqanetics</groupId>
    <artifactId>aqa-cli</artifactId>
    <version>0.9.4</version>
</dependency>
```

**For aqa-selenium:**
```xml
<dependency>
    <groupId>com.aqanetics</groupId>
    <artifactId>aqa-selenium</artifactId>
    <version>0.9.4</version>
</dependency>
```

**For aqa-metasploit:**
```xml
<dependency>
    <groupId>com.aqanetics</groupId>
    <artifactId>aqa-metasploit</artifactId>
    <version>0.9.4</version>
</dependency>
```

### Adding Multiple Modules
If you want to include more than one module, list them as separate dependencies:

```xml
<dependencies>
    <dependency>
        <groupId>com.aqanetics</groupId>
        <artifactId>aqa-cli</artifactId>
        <version>0.9.4</version>
    </dependency>
    <dependency>
        <groupId>com.aqanetics</groupId>
        <artifactId>aqa-selenium</artifactId>
        <version>0.9.4</version>
    </dependency>
    <dependency>
        <groupId>com.aqanetics</groupId>
        <artifactId>aqa-metasploit</artifactId>
        <version>0.9.4</version>
    </dependency>
</dependencies>
```

## Notes
- Ensure you are using the correct version of the modules based on the latest release in Maven Central.
- The aqa-metasploit module requires access to the `paurusr/runner:msf` container. Ensure the container is running and accessible from your test environment.
- The aqa-selenium module assumes Selenium server (Selenoid) is already configured in your environment or AQA is running in Docker.