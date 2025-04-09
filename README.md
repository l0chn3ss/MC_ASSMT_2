Flight Tracker Application
The Flight Tracker application is an Android project that demonstrates the following features:

Flight Tracking:
A screen to input a flight IATA code to simulate flight tracking. (In this demo, flight details are fetched using a simulated AviationStack API call.)

Average Flight Time Calculation:
A background job uses a hardcoded JSON dataset of flight records. The app parses departure and arrival times using Moshi and the Java Time API, stores the data in a Room database, and computes the average flight duration (accounting for overnight flights).

Modern Android Components:
The project leverages Retrofit, Moshi, Room, WorkManager, Kotlin Coroutines, and Java 8+ time APIs to deliver a modern solution. It also employs background processing (via WorkManager) and local data persistence (via Room).

Project Structure
Activities:

MainActivity:

Contains an input field for entering the flight IATA code.

Provides a Track Flight button to simulate fetching flight details.

Provides an Average Flight Time button to launch the average time screen.

FlightTrackerActivity:

Displays simulated flight details (departure, arrival, location) based on the entered IATA code.

Includes options to stop tracking and go back.

AverageTimeActivity:

Uses WorkManager to schedule a background job that:

Inserts hardcoded flight data (if the database is empty).

Parses the flight times with Moshi.

Calculates each flight’s duration (correcting for flights crossing midnight).

Computes and displays the average flight duration.

Database:

Flight.kt (Entity): Represents individual flight records.

FlightDao.kt (DAO): Contains methods to insert flights and retrieve flight data (including a query for average duration).

FlightDatabase.kt: Provides a singleton Room database instance.

Background Processing:

AverageFlightTimeWorker.kt:
A WorkManager worker that parses the JSON data (using Moshi), inserts records into the database if needed, computes individual flight durations, and calculates the overall average flight time.

Networking and JSON Parsing:
Retrofit and Moshi are set up (for the API call simulation) and are used for parsing the hardcoded JSON within the worker.

Resources:

A background image resource (pinkbg) is used in every screen. Ensure that this file is placed in res/drawable.
