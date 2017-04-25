# Project PERA

Timesheet automation application

## Overview

PERA is an application that automates the generation of an employee's timesheet.

The application consists of two parts:

1\. Web Front-end that provides the following functionality:
 - View and update of an employee's profile
 - View and update of an employee's time-in and time-out
 - On-demand generation of an employee's timesheet

2\. Scheduled task for the following:
 - Daily retrieval of the employee's time-in and time-out
 - Semi-monthly generation of the employee's timesheet
 - Email notifying the employee the location of the generated timesheet

## Demo

URL: https://champ-pera.herokuapp.com/

Username: chiem

Password: 1234

## Technologies Used

The web front-end was built using the MEAN stack.

The scheduled tasks were developed using Java 8, Spring 4, Quartz, and Jasper Reports.
