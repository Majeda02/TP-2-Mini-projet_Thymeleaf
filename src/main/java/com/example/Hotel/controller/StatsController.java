package com.example.Hotel.controller;

import com.example.Hotel.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("tauxOccupation", statsService.getTauxOccupationSimple());
        model.addAttribute("revPAR", statsService.getRevPARSimple());
        model.addAttribute("parType", statsService.getRepartitionChambresParType());
        model.addAttribute("parStatut", statsService.getReservationsParStatut());
        return "stats/index";
    }
}


