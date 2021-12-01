package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private CardType type;
    private String cardholder;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "card", fetch = FetchType.EAGER, orphanRemoval = true)
    Set<AccountCard> accountCards = new HashSet<>();

    //CONSTRUCTOR
    public Card() { }

    public Card(Client client, CardType type, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate) {
        this.type = type;
        this.client = client;
        this.cardholder = client.getFirstName()+" "+client.getLastName();
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }

    //GETTER
    public Long getId() {return id;}
    public CardType getType() {return type;}
    public String getCardholder() {return cardholder;}
    public String getNumber() {return number;}
    public int getCvv() {return cvv;}
    public LocalDateTime getFromDate() {return fromDate;}
    public LocalDateTime getThruDate() {return thruDate;}
    public Set<AccountCard> getAccountCards() {return accountCards;}

    @JsonIgnore
    public Client getClient() {return client;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setType(CardType type) {this.type = type;}
    public void setCardholder(String cardholder) {this.cardholder = cardholder;}
    public void setNumber(String number) {this.number = number;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public void setFromDate(LocalDateTime fromDate) {this.fromDate = fromDate;}
    public void setThruDate(LocalDateTime thruDate) {this.thruDate = thruDate;}
    public void setClient(Client client) {this.client = client;}

    //METHOD-GET
    @JsonIgnore
    public List<Account> getAccounts() {
        return accountCards.stream().map(accountCard -> accountCard.getAccount()).collect(toList());
    }
}
