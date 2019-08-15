package com.staten.capstone.controllers;

import com.staten.capstone.models.Location;
import com.staten.capstone.models.Report;
import com.staten.capstone.models.User;
import com.staten.capstone.models.data.LocationDao;
import com.staten.capstone.models.data.ReportDao;
import com.staten.capstone.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ReportDao reportDao;

    @Autowired
    private LocationDao locationDao;

    @GetMapping(value = "")
    public String index(Model model) {
        model.addAttribute("title", "Local Reports");
        return "index";
    }

    @GetMapping(value = "submitReport")
    public String displayReportForm(Model model) {
        model.addAttribute("title", "Add Report");
        model.addAttribute(new Report());
        return "submitReport";
    }

    @PostMapping(value = {"submitReport", "localreports"})
    public String processReportForm(@ModelAttribute @Valid Report report,
                                    Errors errors, @RequestParam int urgency,
                                    @RequestParam String newLocation,
                                    Model model, Principal principal) {
        // is there a reason I don't use urgency

        if (errors.hasErrors()) {
            // display form again with errors
            model.addAttribute("title", "Add Report");
            return "submitReport";
        }

        Location myLocation = new Location();
        myLocation.setLatLng(newLocation);
        locationDao.save(myLocation);

        User user = userDao.findByUsername(principal.getName());
        report.setUser(user);
        report.setLocation(myLocation);    // also set location
        // if no errors, save report
        reportDao.save(report);

        // eventually will want to redirect to map now displaying this user report
        return "redirect:/report/" + report.getId();
    }

    @GetMapping(value = "report/{id}")
    public String displayReport(@PathVariable int id, Model model) {
        Report report = reportDao.findById(id);
        model.addAttribute("title", report.getTitle());
        model.addAttribute("report", report);
        return "viewReport";
    }

    @GetMapping(value = "report/list")
    public String displayReportList(Model model) {
        // view all reports
        model.addAttribute("title", "All Reports");
        model.addAttribute("reportList", reportDao.findAll());
        return "/reportList";
    }

    // view reports by specific user
    @GetMapping(value = "report/list/{username}")
    public String displayReportListByUser(@PathVariable String username,
                                          Model model) {

        // get requested user
        User user = userDao.findByUsername(username);
        if (user == null) {
            // if no such user exists
            return "redirect:/report/list";
        }

        // get reports by this user
        List<Report> reports = new ArrayList<>();
        for (Report report : reportDao.findAll()) {
            if (report.getUser().equals(user)) {
                // only add to list if report's user field matches this user
                reports.add(report);
            }
        }

        model.addAttribute("title", "All Reports by " + username);
        model.addAttribute("reportList", reports);
        return "/reportList";
    }

    // edit form - link only avail. when viewing own report list or indvdl report by user
    @GetMapping(value = "report/edit/{id}")
    public String displayEditReport(@PathVariable int id, Model model,
                                    Principal principal) {

        // get current user and report to edit
        User user = userDao.findByUsername(principal.getName());
        Report report = reportDao.findById(id);

        // check if report exists
        if (report == null) {
            return "redirect:/report/list";
        }

        // check if user is eligible to edit report
        if (user != report.getUser()) {
            return "redirect:/unauthorized";
        }

        // display edit report
        model.addAttribute("title", "Edit Report");
        model.addAttribute("report", report);
        return "editReport";
    }

    @PostMapping(value = "report/edit/{id}")
    public String processEditReport(@PathVariable int id, Model model,
                                    @ModelAttribute @Valid Report report, Errors errors) {

        // find original cheese to be edited
        Report origReport = reportDao.findById(id);

        if (errors.hasErrors()) {
            // only change fields that caused errors, otherwise keep edits
            if (errors.hasFieldErrors("title")) {
                report.setTitle("");
            }
            if (errors.hasFieldErrors("description")) {
                report.setDescription("");
            }
            // display the edit-report-form again
            model.addAttribute("title", "Edit Report");
            return "editReport";
        }

        // edit fields
        origReport.setTitle(report.getTitle());
        origReport.setDescription(report.getDescription());
        origReport.setUrgency(report.getUrgency());

        // update field lastEdit to keep track of when this was edited
        origReport.setLastEdit(ZonedDateTime.now(ZoneId.systemDefault()));

        // save edits
        reportDao.save(origReport);

        return "redirect:/report/" + id;
    }

    @GetMapping(value = "localreports")
    public String displayMapPage(Model model) {
        model.addAttribute("locations", locationDao.findAll());
        model.addAttribute(new Report());
        model.addAttribute("reportList", reportDao.findAll());
        return "map";
    }

    @GetMapping(value = "about")
    public String displayAboutPage(Model model) {
        model.addAttribute("title", "About Local Reports");
        return "about";
    }

}
