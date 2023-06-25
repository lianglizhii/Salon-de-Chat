package fr.utc.sr03.chat.controller;

import fr.utc.sr03.chat.dao.CanalRepository;
import fr.utc.sr03.chat.dao.UserRepository;
import fr.utc.sr03.chat.dao.UsercanalRepository;
import fr.utc.sr03.chat.model.Canal;
import fr.utc.sr03.chat.model.User;
import fr.utc.sr03.chat.model.Usercanal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * URL de base endpoint : <a href="http://localhost:8080/canal">...</a><br>
 * ex users : <a href="http://localhost:8080/canal/">...</a>{username}
 */
@RestController
@RequestMapping("api")
//@CrossOrigin(origins = "http://localhost:3000")
public class CanalController {
    @Autowired
    private UsercanalRepository usercanalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CanalRepository canalRepository;
    @Autowired
    private HttpSession session;

    // Connexion d'un utilisateur
    @PostMapping("/login")
    public User getLogin(@RequestBody Map<String, String> loginRequest) {
        String mail = loginRequest.get("mail");
        String password = loginRequest.get("password");
        User loggedUser = userRepository.findByMailAndPassword(mail, password);
        if (loggedUser != null) {
            session.setAttribute("loggedInUser", loggedUser);
            return loggedUser;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Utilisateur invalide"
            );
        }
    }

    // Récupère la liste des canaux auxquels l'utilisateur a été invité
    @GetMapping("/rooms/invitation")
    public List<Canal> getCanalInvitation(@RequestParam("user_Id") int user_id) {
        long userID = user_id;
        User user = userRepository.findById(userID).get();
        List<Usercanal> usercanals = usercanalRepository.findByuser(user);
        List<Canal> canals = new ArrayList<>();
        for (Usercanal usercanal : usercanals) {
            canals.add(usercanal.getCanal());
        }
        return canals;
    }

    // Récupère la liste des canaux créés par l'utilisateur
    @GetMapping("/rooms/owner")
    public List<Canal> getCanalOwner(@RequestParam("user_Id") int user_id) {
        long userID = user_id;
        User user = userRepository.findById(userID).get();
        return canalRepository.findByowner(user);
    }

    // Récupère la liste des membres d'un canal donné
    @GetMapping("/rooms/owner/{canal_id}")
    public List<User> getCanalMember(@PathVariable("canal_id") int canal_id) {
        long canalID = canal_id;
        Canal canal = canalRepository.findById(canalID).get();
        List<Usercanal> usercanals = usercanalRepository.findBycanal(canal);
        List<User> users = new ArrayList<>();
        users.add(canal.getOwner());
        for (Usercanal usercanal : usercanals) {
            users.add(usercanal.getUser());
        }
        return users;
    }

    // Modifie les informations d'un canal existant
    @PutMapping("/rooms/owners/{canal_id}")
    public Canal editCanal(@PathVariable("canal_id") int canal_id, @RequestBody Map<String, Object> requestBody) {
        long canalID = canal_id;
        Canal canal = canalRepository.findById(canalID).get();
        canal.setTitre((String) requestBody.get("titre"));
        canal.setDescription((String) requestBody.get("description"));
        canal.setDuree(Integer.parseInt((String) requestBody.get("duree")));
        canalRepository.save(canal);
        return canal;
    }

    // Quitte un canal donné en tant qu'utilisateur
    @DeleteMapping("/rooms/inviter/{canal_id}")
    public void quitterCanal(@PathVariable("canal_id") int canal_id, @RequestParam("user") int user_id) {
        long canalID = canal_id;
        long userID = user_id;
        User user = userRepository.findById(userID).get();
        Usercanal usercanal = usercanalRepository.findByuserAndCanal(user, canalRepository.findById(canalID).get());
        usercanalRepository.delete(usercanal);
    }

    // Supprime un canal donné en tant que propriétaire
    @DeleteMapping("/rooms/owner/{canal_id}")
    public boolean supprimerCanal(@PathVariable("canal_id") int canal_id) {
        try {
            long canalID = canal_id;
            Canal canal = canalRepository.findById(canalID).get();
            canalRepository.delete(canal);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Planifie la création d'un canal
    @PostMapping("/rooms/planifier")
    public Canal planifierCanal(@RequestBody Map<String, Object> requestBody) {
        int user_id = (int) requestBody.get("user_id");

        String canal_name = (String) requestBody.get("canal_name");
        String canal_description = (String) requestBody.get("canal_description");
        String canal_date = (String) requestBody.get("canal_date");
        int canal_time = (int) requestBody.get("canal_time");

        long userID = user_id;
        String formatPattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(formatPattern);
        User user = userRepository.findById(userID).get();
        try {
            Date date = formatter.parse(canal_date);
            Canal canal = new Canal(canal_name, canal_description, date, canal_time, user);
            canalRepository.save(canal);
            return canal;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // Récupère la liste des utilisateurs qui ne sont pas membres du canal donné
    @GetMapping("/rooms/inviter/{canal_id}")
    public List<User> getCanalInviter(@PathVariable("canal_id") int canal_id) {
        long canalID = canal_id;
        Canal canal = canalRepository.findById(canalID).get();
        List<Usercanal> usercanals = usercanalRepository.findBycanal(canal);
        List<User> users = new ArrayList<>();
        User owner = canal.getOwner();
        users.add(owner);
        for (Usercanal usercanal : usercanals) {
            users.add(usercanal.getUser());
        }
        List<User> allusers = userRepository.findAll();
        allusers.removeAll(users);
        return allusers;
    }

    // Invite de nouveaux utilisateurs dans un canal donné
    @PostMapping("/rooms/inviter/{canal_id}")
    public void inviterUser(@PathVariable("canal_id") int canal_id, @RequestBody Map<String, Object> requestBody) {
        List<Integer> users_id = (List<Integer>) requestBody.get("users_id");
        List<User> users = new ArrayList<>();
        for (int user_id : users_id) {
            long userID = user_id;
            User user = userRepository.findById(userID).get();
            users.add(user);
        }
        long canalID = canal_id;
        Canal canal = canalRepository.findById(canalID).get();
        for (User user : users) {
            Usercanal usercanal = new Usercanal(canal, user);
            usercanalRepository.save(usercanal);
        }
    }
}
