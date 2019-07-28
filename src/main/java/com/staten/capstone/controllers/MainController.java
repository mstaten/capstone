package com.staten.capstone.controllers;

import com.staten.capstone.models.Report;
import com.staten.capstone.models.User;
import com.staten.capstone.models.data.ReportDao;
import com.staten.capstone.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ReportDao reportDao;

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

    @PostMapping(value = "submitReport")
    public String processReportForm(@ModelAttribute @Valid Report report,
                                    Errors errors, Model model,
                                    Principal principal) {
        if (errors.hasErrors()) {
            // display form again with errors
            model.addAttribute("title", "Add Report");
            return "report";
        }

        // get current user, save w/report
        User user = userDao.findByUsername(principal.getName());
        report.setUser(user);
        // if no errors, save report
        reportDao.save(report);

        // eventually will want to redirect to map now displaying this user report
        return "redirect:/report/list";
    }

    @GetMapping(value = "report/{id}")
    public String displayReport(Model model, @PathVariable int id) {
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


}
