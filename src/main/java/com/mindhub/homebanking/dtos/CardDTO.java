package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CardDTO {

    private Long id;
    private CardType type;
    private String cardholder;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private Set<AccountCardDTO> accountCards = new HashSet<>();

    //CONSTRUCTOR
    public CardDTO() { }

    public CardDTO(Card card) {
        this.id = card.getId();
        this.type = card.getType();
        this.cardholder = card.getCardholder();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.accountCards = card.getAccountCards().stream().map(AccountCardDTO::new).collect(Collectors.toSet());
    }

    //GETTER
    public Long getId() {return id;}
    public CardType getType() {return type;}
    public String getCardholder() {return cardholder;}
    public String getNumber() {return number;}
    public int getCvv() {return cvv;}
    public LocalDateTime getFromDate() {return fromDate;}
    public LocalDateTime getThruDate() {return thruDate;}
    public Set<AccountCardDTO> getAccountCards() {return accountCards;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setType(CardType type) {this.type = type;}
    public void setCardholder(String cardholder) {this.cardholder = cardholder;}
    public void setNumber(String number) {this.number = number;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public void setFromDate(LocalDateTime fromDate) {this.fromDate = fromDate;}
    public void setThruDate(LocalDateTime thruDate) {this.thruDate = thruDate;}
}
