package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientRole;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String profile;
    private ClientRole role;
    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO> clientLoans = new HashSet<>();
    private Set<CardDTO> cards = new HashSet<>();
    private Set<CreditCardDTO> creditCards = new HashSet<>();
    private Set<DirectoryDTO> directories = new HashSet<>();

    //CONSTRUCTOR
    public ClientDTO() { }

    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.username = client.getUsername();
        this.email = client.getEmail();
        this.password = client.getPassword();
        this.profile = client.getProfile();
        this.role = client.getRole();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        this.clientLoans = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
        this.creditCards = client.getCreditCards().stream().map(CreditCardDTO::new).collect(Collectors.toSet());
        this.directories = client.getDirectories().stream().map(DirectoryDTO::new).collect(Collectors.toSet());
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
    public Set<AccountDTO> getAccounts() {return accounts;}
    public Set<ClientLoanDTO> getClientLoans() {return clientLoans;}
    public Set<CardDTO> getCards() {return cards;}
    public Set<CreditCardDTO> getCreditCards() {return creditCards;}
    public Set<DirectoryDTO> getDirectories() {return directories;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setUsername(String username) {this.username = username;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setProfile(String profile) {this.profile = profile;}
    public void setRole(ClientRole role) {this.role = role;}
}
