package fr.utc.sr03.chat.model;
import javax.persistence.*;

@Entity
@Table(name = "user_canal")
public class Usercanal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "canal_id")
    private Canal canal;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Usercanal() {
    }
    public Usercanal(Canal canal, User user) {
        this.canal = canal;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Canal getCanal() {
        return canal;
    }

    public void setCanal(Canal canal) {
        this.canal = canal;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
