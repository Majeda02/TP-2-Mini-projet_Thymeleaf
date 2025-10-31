package com.example.Hotel.controller;

import com.example.Hotel.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("tauxOccupation", statsService.getTauxOccupationSimple());
        model.addAttribute("parType", statsService.getRepartitionChambresParType());
        model.addAttribute("parStatut", statsService.getReservationsParStatut());
        model.addAttribute("totalChambres", statsService.getTotalChambres());
        model.addAttribute("totalClients", statsService.getTotalClients());
        model.addAttribute("totalReservations", statsService.getTotalReservations());
        return "stats/index";
    }
}


