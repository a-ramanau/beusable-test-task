# Be Usable Test Task

A small API that provides an interface for hotels to enter the numbers of Premium and Economy rooms that are available for the night\
and then tells them immediately how many rooms of each category will be occupied and how much money they will make in total.\
Potential guests are represented by an array of numbers that is their willingness to pay for the night.

## Prerequisites

- Java 21
- Gradle (wrapper)
- Git
- Intellij IDEA (see miscellaneous section)


## Usage

1. **Clone the repository**
```bash
git clone https://github.com/a-ramanau/beusable-test-task.git
cd beusable-test-task
```

2. **Run the tests**
```bash
# on Windows use gradlew.bat
./gradlew clean test
```

3. **Run the application**

```bash
# on Windows use gradlew.bat
./gradlew clean :room-occupancy-manager:bootRun
```

4. **Use the API**

As soon as the application is up and running, the API can be tested with `curl`:
```bash
# replace {premiumRooms} & {economyRooms} placeholders with the positive integer numbers
curl --location --request GET 'localhost:9999/api/v1/occupancy?premiumRooms={premiumRooms}&economyRooms={economyRooms}'
```
or with Swagger UI from the browser at http://localhost:9999/swagger-ui/index.html#/

or with `requests.http` file from Intellij IDEA.

## API

#### Endpoint: `GET /api/v1/occupancy`
#### Query Params:
* premiumRooms - positive integer number of free premium rooms
* economyRooms - positive integer number of free economy rooms

#### Example request:
GET http://localhost:9999/api/v1/occupancy?premiumRooms=3&economyRooms=3
#### Example response:
```json
{
   "premiumUsage": 3,
   "economyUsage": 3,
   "premiumRevenue": 738.0,
   "economyRevenue": 167.99
}
```

## Implementation

There are 2 versions:
- SingleLoopOccupancyService - more "optimal" solution, using mutability and classic single for loop (injected in API by default)
- StreamsOccupancyService - more "functional style" solution, using Java 8+ stream syntax (also covered with the tests)

## Incorrect? data for Test 4
- (input) Free Premium rooms: 7
- (input) Free Economy rooms: 1
- (output) Usage Premium: 7 (EUR 1153)
- (output) Usage Economy: 1 (EUR 45.99)\

Given [23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209] as guests\
And 7 as Free Premium Rooms\
And 1 as Free Economy Rooms\
When occupancy is calculated\
Then Usage Premium should be 374 + 209 + 155 + 115 + 101 + 100 + 99.99 = 1153.99\
And Usage Economy should be 45\
But Usage Premium is 1153\
And Usage Economy is 45.99

## Miscellaneous
- Multi-module Gradle project
- Git hooks to enforce commit message and branch name formats
- Dependabot to automate dependency updates
- Github Actions to enforce pull request title, commit message and branch name formats
- Github Actions to get code coverage report for each pull request and enforce at least 95% line coverage
- 100% line coverage (JaCoCo exclusion filters for OccupancyManagerApplication#main configured)
- Shared configuration for Intellij IDEA to enforce code styles, install and configure necessary plugins
- Custom Checkstyle configuration integrated into Gradle, Intellij IDEA and Github Actions
- Custom PMD configuration integrated into Gradle, Intellij IDEA and Github Actions
- request.http containing API usage example
- Dockerfile
- OpenAPI (Swagger UI)
- Actuator
- Global Lombok configuration
- Basic error handling with exception handling advice
- Input validation with Hibernate validator
- Guests loaded from configurable external json file
- Configurable premium limit in application.yml
