package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Directory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String accountHolder;
    private String accountNumber;
    private String email;
    private String observation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    //CONSTRUCTOR
    public Directory() { }

    public Directory(Client client, String accountHolder, String accountNumber, String email, String observation) {
        this.client = client;
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.email = email;
        this.observation = observation;
    }

    //GETTER
    public Long getId() {return id;}
    public String getAccountHolder() {return accountHolder;}
    public String getAccountNumber() {return accountNumber;}
    public String getEmail() {return email;}
    public String getObservation() {return observation;}

    @JsonIgnore
    public Client getClient() {return client;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setAccountHolder(String accountHolder) {this.accountHolder = accountHolder;}
    public void setAccountNumber(String accountNumber) {this.accountNumber = accountNumber;}
    public void setEmail(String email) {this.email = email;}
    public void setObservation(String observation) {this.observation = observation;}
    public void setClient(Client client) {this.client = client;}
}
