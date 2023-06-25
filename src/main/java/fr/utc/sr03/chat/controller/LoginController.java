package fr.utc.sr03.chat.controller;

import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * URL de base endpoint : <a href="http://localhost:8080/login">...</a>
 */
@Controller
@RequestMapping("login")
public class LoginController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpSession session;

    // Affiche la page de connexion
    @GetMapping
    public String getLogin(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Gère la soumission du formulaire de connexion
    @PostMapping
    public String postLogin(@ModelAttribute User user, Model model) {
        // Recherche de l'utilisateur dans la base de données en utilisant l'e-mail et le mot de passe fournis
        User loggedUser = userRepository.findByMailAndPassword(user.getMail(), user.getPassword());
        if (loggedUser != null && loggedUser.isAdmin() && loggedUser.isEnabled()) {
            // Vérifie si l'utilisateur est un administrateur et est activé
            // Stocke l'utilisateur connecté dans la session
            session.setAttribute("loggedInUser", loggedUser);
            return "redirect:/admin/users"; // Redirige vers la page d'administration des utilisateurs
        } else {
            model.addAttribute("invalid", true); // Indique une connexion invalide
            return "login"; // Retourne la page de connexion
        }
    }
}



