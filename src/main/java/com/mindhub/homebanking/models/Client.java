package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String firstName;
    private String lastName;
    private  String username;
    private String email;
    private String password;
    private String profile;
    private ClientRole role;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, orphanRemoval = true)
    Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Card> cards = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, orphanRemoval = true)
    Set<CreditCard> creditCards = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, orphanRemoval = true)
    Set<Directory> directories = new HashSet<>();

    //CONSTRUCTOR
    public Client() { }

    public Client(String firstName, String lastName, String username, String email, String password, String profile, ClientRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile = profile;
        this.role = role;
    }

    //GETTER
    public Long getId() {return id;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public String getProfile() {return profile;}
    public ClientRole getRole() {return role;}
    public Set<Account> getAccounts() {return accounts;}
    public Set<ClientLoan> getClientLoans() {return clientLoans;}
    public Set<Card> getCards() {return cards;}
    public Set<CreditCard> getCreditCards() {return creditCards;}
    public Set<Directory> getDirectories() {return directories;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setUsername(String username) {this.username = username;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setProfile(String profile) {this.profile = profile;}
    public void setRole(ClientRole role) {this.role = role;}

    //METHOD-GET
    @JsonIgnore
    public List<Loan> getLoans() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getLoan()).collect(toList());
    }
}
