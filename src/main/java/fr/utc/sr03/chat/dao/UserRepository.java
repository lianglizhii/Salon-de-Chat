package fr.utc.sr03.chat.dao;

import fr.utc.sr03.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    // Requete generee automatiquement par Spring
    User findByMailAndPassword(@Param("mail") String mail, @Param("password") String password);
    @Query("SELECT u FROM User u WHERE u.enabled= :enabled")
    List<User> findBYenabled(@Param("enabled") boolean enabled);
    // Requete creee manuellement
    @Query("SELECT u FROM User u WHERE LENGTH(u.lastName) >= :lastNameLength")
    List<User> findByLastNameLength(@Param("lastNameLength") int lastNameLength);

    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:searchQuery% " +
            "OR u.lastName LIKE %:searchQuery% " +
            "OR u.mail LIKE %:searchQuery%")
    Page<User> searchUsers(@Param("searchQuery") String searchQuery, Pageable pageable);
    //findByMail
    User findByMail(@Param("mail") String mail);
}
