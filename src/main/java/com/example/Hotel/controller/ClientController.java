package com.example.Hotel.controller;

import com.example.Hotel.entities.Client;
import com.example.Hotel.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public String listClients(Model model) {
        List<Client> clients = clientService.getAllClients();
        model.addAttribute("clients", clients);
        return "clients/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Client client, RedirectAttributes redirectAttributes) {
        client.setId(null); // S'assurer que l'ID est null pour la création
        clientService.saveClient(client);
        redirectAttributes.addFlashAttribute("message", "Client créé avec succès!");
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        clientService.getClientById(id).ifPresent(client -> model.addAttribute("client", client));
        return "clients/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Client client, RedirectAttributes redirectAttributes) {
        client.setId(id);
        clientService.saveClient(client);
        redirectAttributes.addFlashAttribute("message", "Client mis à jour avec succès!");
        return "redirect:/clients";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(id);
        redirectAttributes.addFlashAttribute("message", "Client supprimé avec succès!");
        return "redirect:/clients";
    }
}
