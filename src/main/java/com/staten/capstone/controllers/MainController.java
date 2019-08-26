package com.staten.capstone.controllers;

import com.staten.capstone.models.Location;
import com.staten.capstone.models.Report;
import com.staten.capstone.models.User;
import com.staten.capstone.models.data.LocationDao;
import com.staten.capstone.models.data.ReportDao;
import com.staten.capstone.models.data.UserDao;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    @GetMapping(value = "localreports")
    public String displayMapPage(HttpServletRequest request, Model model) {

        Slice<Report> reportsSlice = processSortButton("", "",request);
        model.addAttribute("reportsSlice", reportsSlice);
        model.addAttribute("locations", locationDao.findAll());
        model.addAttribute(new Report());   // for submitting a report on this page
        return "map";
    }

    @PostMapping(value = "submitreport")
    public String processReportForm(@ModelAttribute @Valid Report report,
                                    Errors errors, Model model,
                                    @RequestParam String newLocation,
                                    Principal principal) {

        if (errors.hasErrors()) {
            // display form again with errors
            model.addAttribute("title", "Add Report");
            return "map";
        }

        Location myLocation = new Location();
        myLocation.setLatLng(newLocation);
        locationDao.save(myLocation);

        User user = userDao.findByUsername(principal.getName());
        report.setUser(user);
        report.setLocation(myLocation);
        // if no errors, save report
        reportDao.save(report);

        return "redirect:/report/" + report.getId();
    }

    @PostMapping(value = "localreports")
    public String displayMapPageAndSorts(@RequestParam String sort,
                                         @RequestParam String order,
                                         HttpServletRequest request,
                                         Model model) {

        Slice<Report> reportsSlice = processSortButton(sort, order, request);
        model.addAttribute("reportsSlice", reportsSlice);
        model.addAttribute("locations", locationDao.findAll());
        model.addAttribute(new Report());   // for submitting a report on this page
        return "map";
    }

    @GetMapping(value = "report/{id}")
    public String displayOneReport(@PathVariable int id, Model model) {
        Report report = reportDao.findById(id);
        model.addAttribute("report", report);
        model.addAttribute("title", report.getTitle());
        return "viewReport";
    }

    @GetMapping(value = "report/list")
    public String displayReportList(Model model, HttpServletRequest request) {

        Slice<Report> reportsSlice = processSortButton("", "", request);
        model.addAttribute("reportsSlice", reportsSlice);
        model.addAttribute("title", "All Reports");
        return "/reportList";
    }

    @PostMapping(value = "report/list")
    public String displayReportList(@RequestParam String sort, @RequestParam String order,
                                    HttpServletRequest request, Model model) {

        Slice<Report> reportsSlice = processSortButton(sort, order, request);
        model.addAttribute("reportsSlice", reportsSlice);
        model.addAttribute("title", "All Reports");
        return "/reportList";
    }

    // view reports by specific user
    @GetMapping(value = "report/list/{username}")
    public String displayReportListByUser(@PathVariable String username,
                                          Model model, HttpServletRequest request) {

        // get requested user
        User user = userDao.findByUsername(username);
        if (user == null) {
            return "redirect:/report/list";
        }

        int pageNumber = getPageNumber(request);
        Pageable pageable = new PageRequest(pageNumber,3);

        // get reports by this user
        Slice<Report> reportsSlice = reportDao.findAllByUser(user, pageable);
        model.addAttribute("reportsSlice", reportsSlice);
        model.addAttribute("title", "All Reports by " + username);
        model.addAttribute("username", username);
        return "/reportListByUser";
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

        // find original report to be edited
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

    @GetMapping(value = "about")
    public String displayAboutPage(Model model) {
        model.addAttribute("title", "About Local Reports");
        return "about";
    }

    // UTIL

    // method to get desired page number from the view
    public int getPageNumber(HttpServletRequest request) {

        int pageNumber = 0; // default page number

        // if query exists, use query parameter as pageNumber
        if (request.getQueryString() != null) {
            String queryString = request.getQueryString();
            int equalsIndex = queryString.indexOf("=");
            String pageNumberStr = queryString.substring(equalsIndex + 1); // get number from string
            pageNumber = Integer.parseInt(pageNumberStr) - 1;
        }

        return pageNumber;
    }

    // process sort button values, return Slice<Report>
    public Slice<Report> processSortButton(String sort, String order,
                                           HttpServletRequest request) {
        Pageable pageable;
        int pageNumber = getPageNumber(request);

        // default values: if user clicked search btn but didn't change values
        if (sort.equals("")) {
            sort = "zonedDateTime";
        }
        if (order.equals("")) {
            order = "asc";
        }

        // create pageable object w/ user-selected sort & order values
        if (order.equals("asc")) {
            pageable = new PageRequest(pageNumber, 7, new Sort(Sort.Direction.ASC, sort));
        } else {
            pageable = new PageRequest(pageNumber, 7, new Sort(Sort.Direction.DESC, sort));
        }

        // get all reports, 7 per page
        return reportDao.findAll(pageable);
    }

}
