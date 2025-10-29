package com.example.Hotel.controller;

import com.example.Hotel.entities.Chambre;
import com.example.Hotel.service.ChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/chambres")
public class ChambreController {

    @Autowired
    private ChambreService chambreService;

    @GetMapping
    public String listChambres(Model model, 
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) Boolean disponible) {
        List<Chambre> chambres;
        
        if (type != null && !type.isEmpty()) {
            chambres = chambreService.getChambresByType(type);
        } else if (disponible != null) {
            chambres = chambreService.getChambresByDisponibilite(disponible);
        } else {
            chambres = chambreService.getAllChambres();
        }
        
        model.addAttribute("chambres", chambres);
        model.addAttribute("type", type);
        model.addAttribute("disponible", disponible);
        return "chambres/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("chambre", new Chambre());
        return "chambres/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Chambre chambre, RedirectAttributes redirectAttributes) {
        chambre.setId(null); // S'assurer que l'ID est null pour la création
        if (chambre.getDisponible() == null) {
            chambre.setDisponible(true);
        }
        chambreService.saveChambre(chambre);
        redirectAttributes.addFlashAttribute("message", "Chambre créée avec succès!");
        return "redirect:/chambres";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        chambreService.getChambreById(id).ifPresent(chambre -> model.addAttribute("chambre", chambre));
        return "chambres/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Chambre chambre, RedirectAttributes redirectAttributes) {
        try {
            if (chambre.getDisponible() == null) {
                chambre.setDisponible(true);
            }
            chambre.setId(id);
            chambreService.saveChambre(chambre);
            redirectAttributes.addFlashAttribute("message", "Chambre mise à jour avec succès!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour de la chambre");
        }
        return "redirect:/chambres";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        chambreService.deleteChambre(id);
        redirectAttributes.addFlashAttribute("message", "Chambre supprimée avec succès!");
        return "redirect:/chambres";
    }
}
