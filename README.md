# Local Reports - web app
My capstone project for the 2019 LaunchCode LiftOff Program.
In progress.

The following screenshot shows the main page: the map and accompanying list of reports
![Map and reports photo](https://github.com/mstaten/capstone/blob/master/screenshots/map.png)

#### Table of Contents
[About](#about)

[Technologies](#technologies)

[Features](#features)

[Acknowledgments](#acknowledgments)

[Screenshots](#screenshots)


### About
This web app allows users to submit reports of local road issues, including potholes,
missing street signs, flooded streets, etc.
Each report is attached to the particular location of its issue.
Users can assign an urgency level to their reports to indicate its seriousness and immediacy.
Users can view a map populated with all current reports of local issues so that
they can avoid these issues if needed.
Users can edit their own reports after submitting.

Eventually, administrators will be able to mark reports as open, closed, or spam.
Administrators will also be able to direct the reports to the appropriate local
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
* Report an issue with a local road, with location attached to the report
* View reports submitted by other users and a map populated with the corresponding locations
* Sort reports by date or urgency, ascending or descending
* Integration with the Google Maps API

### Acknowledgments
* LaunchCode LC101 curriculum to get started with the basics of Java, HTML, and CSS
* [Baeldung tutorials for Spring Security guidance](https://github.com/eugenp/tutorials)

### Screenshots
![About photo](https://github.com/mstaten/capstone/blob/master/screenshots/about.png)
#### Screenshot of map and accompanying list of reports, toggled to "view reports" (shown at top of README)
![Map and reports photo](https://github.com/mstaten/capstone/blob/master/screenshots/map.png)
#### Screenshot of map, zoomed in, and toggled to "submit report"
![Map and submit report photo](https://github.com/mstaten/capstone/blob/master/screenshots/submitting.png)
#### Screenshot of a single report
![Single report photo](https://github.com/mstaten/capstone/blob/master/screenshots/single_report.png)
#### Screenshot of paginated & sorted list of reports
![List of reports photo](https://github.com/mstaten/capstone/blob/master/screenshots/report_list.png)
#### Screenshot of login page
![Login photo](https://github.com/mstaten/capstone/blob/master/screenshots/login.png)
