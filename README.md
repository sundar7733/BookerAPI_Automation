# Restful Booker API Automation Framework
This project automates API testing for the [Restful Booker](https://restful-booker.herokuapp.com/) service using:

- Java
- JUnit
- REST Assured
- CSV
- Extent Reports (custom reporting)
---
For calling the API, we use the [Restful Booker API](https://restful-booker.herokuapp.com/) which is a public API, username and password are required for UPDATE and DELETE operations.
As of present state, Username and password can be found in https://restful-booker.herokuapp.com/apidoc/index.html#api-Auth-CreateToken

**Important**: Do not commit username and password values back to the repository.

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
   Add the following VM options in your IDE or run configuration:
    ```
   username and password for the API under src/test/resources/config.properties:
   username={{username for the API}} 
   password={{password for the API}}
   Username and password can be found in https://restful-booker.herokuapp.com/apidoc/index.html#api-Auth-CreateToken
   ```
   You can run the tests using Maven. Use the following command:
   ```bash
   mvn test
   ```
### Reporting
    The project uses Extent Reports for custom reporting. After running the tests, an HTML report will be generated in the `target/test-results` directory.
**View the reports**
    Open the `BookerAPITestResults.html` file in a web browser to view the test results.

### Run the tests Using IntelliJ IDEA
**For All Tests:**
1. Open the project in IntelliJ IDEA.
2. Add the username and password in the `src/test/resources/config.properties` file as mentioned above.
3. Click maven signature on the right side of the IDE.
4. Expand the `Lifecycle` section.
5. Double-click on `test` to run all tests.
6. Alternatively, you can run all tests by right-clicking on the `tests` package and selecting `Run 'Tests in 'tests''`.

   **For Individual Test Class:**

 Right-click on the test class name and Choose Run tests - Say right click `CreateBookingTests.java` file and select `Run 'CreateBookingTests'`.

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

You can modify these files to add more test coverage. Find the appropriate CSV file you wish to modify and update as appropriate.
You can open the CSV file in a text editor, Excel, or your preferred editing software.
Excel aligns the columns and makes it easy to edit.
Inspect the column headers. You will need to create a row filling the appropriate values.
### CSV Column Headers

The following are column headers in the CSV.

#### Metadata Columns

These columns provide information about the test.
They are considered mandatory, and are used for traceability.

- testname - give the test a useful name. This will appear in reports

### Restful Booker API Payload Columns
- firstname
- lastname
- totalprice
- depositpaid
- bookingdates
  - checkin
  - checkout
- additionalneeds

## Branch/ Merge Strategy

Follow these steps in order to have your updates added to the project.

1. Create a new branch based off the main branch. A good name includes a Jira reference and brief description, e.g. `BTT-123_addBookingValidation` 
   - This will create a new branch in your local repository.
   - Make sure to pull the latest changes from the main branch before creating your new branch.
2. Commit your work. Ensure you follow appropriate conventions and the tests pass.
3. Create a pull request from your branch back to main. Add appropriate reviewers and request a code-review. Your code will be reviewed, and you may be asked to make changes for test quality.
4. Once the pull-request is approved, merge your branch into main. Ensure you delete your branch as you merge to ensure stale branches are not retained.
