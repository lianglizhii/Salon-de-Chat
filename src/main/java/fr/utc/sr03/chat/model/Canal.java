package fr.utc.sr03.chat.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "canal")
public class Canal {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @Column(name="titre")
    private String titre;
    @Column(name="description")
    private String description;
    @Column(name="horaire")
    private Date horaire;
    @Column(name="duree")
    private int duree;
    @ManyToOne
    @JoinColumn(name="owner")
    private User owner;
    public Canal() {
    }

    public Canal(String titre, String description, Date horaire, int duree, User owner) {
        this.titre = titre;
        this.description = description;
        this.horaire = horaire;
        this.duree = duree;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Date getHoraire() {
        return horaire;
    }

    public void setHoraire(Date horaire) {
        this.horaire = horaire;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}