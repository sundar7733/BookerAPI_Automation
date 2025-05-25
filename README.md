# Restful Booker API Automation Framework
This project automates API testing for the [Restful Booker](https://restful-booker.herokuapp.com/) service using:

- Java
- JUnit
- REST Assured
- CSV
- Extent Reports (custom reporting)
---
For calling the API, we use the [Restful Booker API](https://restful-booker.herokuapp.com/) which is a public API, username and password are required for UPDATE and DELETE operations.
Username and password can be found in https://restful-booker.herokuapp.com/apidoc/index.html#api-Auth-CreateToken

## Project Structure
src
└── test
├── java
│ └── nz.co.sundar.testautomation.restfulbooker
│ ├── base
│ ├── payloads
│ ├── pojo
│ ├── tests
│ └── utils
└── resources
└── csv
   └── create_booking.csv
   └── update_booking.csv
## How to Run the Tests
   ###  Pre-requisites

- Java 11+
- Maven
- IntelliJ or any IDE
- Internet connection (to hit the public RESTful Booker API)
---
### Setup Instructions

1. **Clone the repository**
2. Open a terminal and run the following commands to clone the repository:
    ```bash
    git clone https://github.com/sundar7733/BookerAPI_Automation.git
    cd restfulBookerAPI-automation
    ```
3. **Install dependencies**
    Ensure you have Maven installed. Run the following command to install dependencies:
    ```bash
   mvn clean install
   ```
4. **Run the tests**
   You can run the tests using Maven. Use the following command:
   ```bash
   mvn test
   ```
5. **View the reports**
    After running the tests, you can find the HTML report in the `target/test-results` directory. Open the `BookerAPITestResults.html` file in a web browser to view the test results.

### Types of Tests
| **Test Class**               | **Description**                                                 |
|------------------------------|-----------------------------------------------------------------|
| `CreateBookingTests`         | Creates bookings with data from `create_booking.csv`            |
| `DeleteBookingTests`         | Deletes a created booking                                       |
| `GetAllBookingIdsTests`      | Validates fetching all booking Ids                              |
| `GetSpecificBookingIdsTests` | Validates fetching for a specific booking Id                    |
| `UpdateBookingTests`         | Updates existing bookings using data from `updated_booking.csv` |

### Test Data
Test data is maintained in src/test/resources/csv as CSV files:

create_booking.csv – Data for creating bookings (with testcase name column)

updated_booking.csv – Data for updating bookings (with testcase name column)

You can modify these files to add more test coverage.