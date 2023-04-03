#Courier Tracking Application

This is a Spring Boot application that tracks couriers when they enter the radius of a store and records their entry into the store's circumference. The application also provides a way to query the total distance traveled by a courier.


These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
Prerequisites


Installing

    Clone the repository:

    git clone https://github.com/mervedkr/couriertracking.git

    Import the project into your IDE.

    Run the application by executing the main method in the CourierTrackingApplication class.

Usage

The application exposes two REST endpoints:

    /location: Accepts a POST request with the latitude and longitude of the courier's location. The application checks if the courier is within the radius of any store and records their entry into the store's circumference if they are.

    /courier/{id}/distance: Accepts a GET request with the ID of the courier. Returns the total distance traveled by the courier.

Built With

    Spring Boot - The web framework used
    Java 11 - The programming language used
    Spring Data JPA - The data access framework used

Authors

    MerveDkr - Merve Döker
