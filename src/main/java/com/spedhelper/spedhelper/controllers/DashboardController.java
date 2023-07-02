package com.spedhelper.spedhelper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spedhelper.spedhelper.database.VehicleRepository;

@Controller
public class DashboardController {
    private @Autowired VehicleRepository vRep;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("vehicles", vRep.findAll(PageRequest.of(0, 10, Sort.by("id"))).toList());
        return "dashboard";
    }
}
