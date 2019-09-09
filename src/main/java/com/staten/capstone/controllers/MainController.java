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
        return "redirect:/localreports";
    }

    @GetMapping(value = "localreports")
    public String displayMapPage(Model model) {

        List<Report> reportList = processSortButton("", "");
        model.addAttribute("reportList", reportList);
        model.addAttribute("locations", locationDao.findAll());
        model.addAttribute(new Report());
        return "map";
    }

    @PostMapping(value = "localreports")
    public String displayMapPageAndSorts(@RequestParam String sort,
                                         @RequestParam String order,
                                         Model model) {

        List<Report> reportList = processSortButton(sort, order);
        model.addAttribute("reportList", reportList);
        model.addAttribute("locations", locationDao.findAll());
        model.addAttribute(new Report());
        return "map";
    }

    @PostMapping(value = "submitreport")
    public String processReportForm(@ModelAttribute @Valid Report report,
                                    Errors errors, Model model,
                                    @RequestParam String newLocation,
                                    Principal principal) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Report");
            return "map";
        }

        Location myLocation = new Location();
        myLocation.setLatLng(newLocation);
        locationDao.save(myLocation);

        User user = userDao.findByUsername(principal.getName());
        report.setUser(user);
        report.setLocation(myLocation);
        reportDao.save(report);

        return "redirect:/report/" + report.getId();
    }



    @GetMapping(value = "report/{id}")
    public String displayOneReport(@PathVariable int id, Model model) {

        Report report = reportDao.findById(id);
        model.addAttribute("report", report);
        model.addAttribute("title", report.getTitle());
        return "reports/viewReport";
    }

    @GetMapping(value = "report/list")
    public String displayReportList(Model model, HttpServletRequest request) {

        Slice<Report> reportsSlice = processSortButton("", "", request);
        model.addAttribute("reportsSlice", reportsSlice);
        model.addAttribute("title", "All Reports");
        return "reports/reportList";
    }

    @GetMapping(value = "report/list/sort")
    public String displayReportList(@RequestParam String sort, @RequestParam String order,
                                    HttpServletRequest request, Model model) {

        Slice<Report> reportsSlice = processSortButton(sort, order, request);
        model.addAttribute("reportsSlice", reportsSlice);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("title", "All Reports");
        return "reports/reportList";
    }

    @GetMapping(value = "report/list/{username}")
    public String displayReportListByUser(@PathVariable String username,
                                          Model model, HttpServletRequest request) {

        User user = userDao.findByUsername(username);
        if (user == null) {
            return "redirect:/report/list";
        }

        int pageNumber = getPageNumber(request);
        Pageable pageable = new PageRequest(pageNumber,3);

        Slice<Report> reportsSlice = reportDao.findAllByUser(user, pageable);
        model.addAttribute("reportsSlice", reportsSlice);
        model.addAttribute("title", "All Reports by " + username);
        model.addAttribute("username", username);
        return "reports/reportListByUser";
    }

    // edit form - link only avail. when viewing own report list or indvdl report by user
    @GetMapping(value = "report/edit/{id}")
    public String displayEditReport(@PathVariable int id, Model model,
                                    Principal principal) {

        User user = userDao.findByUsername(principal.getName());
        Report report = reportDao.findById(id);

        if (report == null) {
            return "redirect:/report/list";
        }

        if (user != report.getUser()) {
            return "redirect:/unauthorized";
        }

        model.addAttribute("title", "Edit Report");
        model.addAttribute("report", report);
        return "reports/editReport";
    }

    @PostMapping(value = "report/edit/{id}")
    public String processEditReport(@PathVariable int id, Model model,
                                    @ModelAttribute @Valid Report report, Errors errors) {

        Report origReport = reportDao.findById(id);

        if (errors.hasErrors()) {
            // only change fields that caused errors, otherwise keep edits
            if (errors.hasFieldErrors("title")) {
                report.setTitle("");
            }
            if (errors.hasFieldErrors("description")) {
                report.setDescription("");
            }

            model.addAttribute("title", "Edit Report");
            return "reports/editReport";
        }

        origReport.setTitle(report.getTitle());
        origReport.setDescription(report.getDescription());
        origReport.setUrgency(report.getUrgency());

        origReport.setLastEdit(ZonedDateTime.now(ZoneId.systemDefault()));

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
    private int getPageNumber(HttpServletRequest request) {

        int pageNumber = 0; // default page number

        String pageParam = request.getParameter("page");

        if (pageParam != null) {
            pageNumber = Integer.parseInt(pageParam) - 1;
        }

        return pageNumber;
    }

    // process sort button values, return List<Report>, no pagination
    private List<Report> processSortButton(String sort, String order) {

        // default values: if user clicked search btn but didn't change value
        if (sort.equals("")) {
            sort = "dateTime";
        }

        if (order.equals("desc")) {
            return reportDao.findAll(new Sort(Sort.Direction.DESC, sort));
        }
        return reportDao.findAll(new Sort(Sort.Direction.ASC, sort));
    }

    // process sort button values, return Slice<Report>
    private Slice<Report> processSortButton(String sort, String order,
                                           HttpServletRequest request) {

        Pageable pageable;
        int pageNumber = getPageNumber(request);

        // default values: if user clicked search btn but didn't change values
        if (sort.equals("")) {
            sort = "dateTime";
        }
        if (order.equals("")) {
            order = "asc";
        }

        if (order.equals("asc")) {
            pageable = new PageRequest(pageNumber, 3, new Sort(Sort.Direction.ASC, sort));
        } else {
            pageable = new PageRequest(pageNumber, 3, new Sort(Sort.Direction.DESC, sort));
        }

        return reportDao.findAll(pageable);
    }

}
