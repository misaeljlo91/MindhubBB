package com.mindhub.homebanking.dtos;

public class PayCreditCardAccountDTO {

    private String number;
    private Long id;
    private double amount;

    //CONSTRUCTOR
    public PayCreditCardAccountDTO() { }

    public PayCreditCardAccountDTO(String number, Long id, double amount) {
        this.number = number;
        this.id = id;
        this.amount = amount;
    }

    //GETTER
    public String getNumber() {return number;}
    public Long getId() {return id;}
    public double getAmount() {return amount;}

    //SETTER
    public void setNumber(String number) {this.number = number;}
    public void setId(Long id) {this.id = id;}
    public void setAmount(double amount) {this.amount = amount;}
}
