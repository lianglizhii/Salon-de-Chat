
package fr.utc.sr03.chat.dao;

import fr.utc.sr03.chat.model.Canal;
import fr.utc.sr03.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CanalRepository extends JpaRepository<Canal,Long>{
    // Requete generee automatiquement par Spring
    List<Canal> findByowner(@Param("Owner") User Owner);
}
