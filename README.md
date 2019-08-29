# Local Reports - web app
My capstone project for the 2019 LaunchCode LiftOff Program.
In progress.

The following screenshot shows the main page: the map and accompanying list of reports
![Map and reports photo](https://github.com/mstaten/capstone/blob/master/screenshots/map_reports.png)

#### Table of Contents
[About](#about)

[Technologies](#technologies)

[Features](#features)

[Acknowledgments](#acknowledgments)

[Screenshots](#screenshots)


### About
This web app allows users to submit reports of local road issues and concerns, including potholes, missing street signs, flooded streets, etc. Users can assign an urgency level to their reports to indicate the seriousness of the issue. Users can view a map populated with all current reports of local issues so that
they can avoid these issues if needed.

Eventually, administrators will be able to mark reports as open, closed, or spam. Administrators will also be able to direct the reports to the appropriate local
departments.

This app uses the Google Maps API to pinpoint and save locations for each report
of a local issue.

### Technologies
* Java
* Spring Boot, Spring Security
* Thymeleaf
* HTML
* CSS, Bootstrap
* JavaScript
* MySQL

### Features
* Secure user login
* Report an issue with a local road
* View reports submitted by other users
* Integration with the Google Maps API

### Acknowledgments
* LaunchCode LC101 curriculum to get started with the basics of back-end Java
* [Baeldung tutorials for Spring Security guidance](https://github.com/eugenp/tutorials)

### Screenshots
![About photo](https://github.com/mstaten/capstone/blob/master/screenshots/about.png)
#### Screenshot of map and accompanying list of reports, toggled to "view reports" (shown at top of README)
![Map and reports photo](https://github.com/mstaten/capstone/blob/master/screenshots/map_reports.png)
#### Screenshot of map, zoomed in, and toggled to "submit report"
![Map and submit report photo](https://github.com/mstaten/capstone/blob/master/screenshots/map_submitreport.png)
#### Screenshot of paginated & sorted list of reports
![List of reports photo](https://github.com/mstaten/capstone/blob/master/screenshots/list_of_reports.png)
#### Screenshot of login page
![Login photo](https://github.com/mstaten/capstone/blob/master/screenshots/new_login.png)
