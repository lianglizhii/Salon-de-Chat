package fr.utc.sr03.chat.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) // strategy=GenerationType.IDENTITY => obligatoire pour auto increment mysql
    private long id;

    @Column(name = "firstname")
    @Size(min = 2)
    @NotEmpty(message = "firstname obligatoire")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;


    @Column(name ="mail")

    private String mail;
    @Column(name ="password")
    private String password;
    @Column(name ="admin")
    private boolean admin;


    @Column(name ="enabled")
    private boolean enabled;
    public User(){}

    public User(long id, String firstName, String lastName, String mail, String password, boolean admin, boolean enabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
        this.admin = admin;
        this.enabled = enabled;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
