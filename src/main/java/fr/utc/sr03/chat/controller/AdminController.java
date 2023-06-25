package fr.utc.sr03.chat.controller;

import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * URL de base endpoint : <a href="http://localhost:8080/admin">...</a><br>
 * ex users : <a href="http://localhost:8080/admin/users">...</a>
 */
@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    // Obtient la liste des utilisateurs et renvoie la vue "user_list"
    @GetMapping("users")
    public  String getUserList(Model model){
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user_list";
    }

    // Désactive un utilisateur en fonction de l'ID fourni et redirige vers la page des utilisateurs
    @PostMapping("disable")
    public String disableUser(@RequestParam Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setEnabled(false);
            userRepository.save(user);
        });
        return "redirect:/admin/users";
    }

    // Active un utilisateur en fonction de l'ID fourni et redirige vers la page des utilisateurs
    @PostMapping("enable")
    public String enableUser(@RequestParam Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setEnabled(true);
            userRepository.save(user);
        });
        return "redirect:/admin/users";
    }

    // Supprime un utilisateur en fonction de l'ID fourni et redirige vers la page des utilisateurs
    @PostMapping("supprimer")
    public String SupprimerUser(@RequestParam Long id){
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // Récupère un utilisateur en fonction de l'ID fourni et renvoie la vue "user_edit" avec les données de l'utilisateur
    @GetMapping("edit")
    public String editUser(@RequestParam Long id, Model model) {
        userRepository.findById(id).ifPresent(user -> model.addAttribute("user", user));
        return "user_edit";
    }

    // Met à jour un utilisateur en fonction des données fournies et renvoie la vue "user_edit" avec les données de l'utilisateur
    @PostMapping("update")
    public String updateUser(@ModelAttribute User user,Model model) {
        User editUser = userRepository.findByMail(user.getMail());
        if (editUser != null && editUser.getId()!=user.getId()) {
            model.addAttribute("errorMessage", "Email already exists");
            return "user_edit";
        } else {
            userRepository.save(user);
            return "redirect:/admin/users";
        }
    }

    // Renvoie la vue "ajoute" avec un nouvel utilisateur vide
    @GetMapping("ajoute")
    public String getUser(Model model) {
        model.addAttribute("user", new User());
        return "ajoute";
    }

    // Renvoie la vue "list_desactive" avec la liste des utilisateurs désactivés
    @GetMapping("list_desactive")
    public String getUserdesactive(Model model) {
        List<User> users = userRepository.findBYenabled(false);
        model.addAttribute("users", users);
        return "user_list";
    }

    // Ajoute un utilisateur en fonction des données fournies et renvoie la vue "ajoute" avec les données de l'utilisateur
    @PostMapping("ajoute")
    public String addUser(@ModelAttribute User user,Model model) {
        User newUser = user;
        User addUser = userRepository.findByMail(user.getMail());
        if (addUser != null) {
            model.addAttribute("errorMessage", "Email already exists");
            return "ajoute";
        } else {
            userRepository.save(newUser);
            List<User> users = userRepository.findAll();
            model.addAttribute("users", users);
            return "redirect:/admin/users";
        }
    }

}
