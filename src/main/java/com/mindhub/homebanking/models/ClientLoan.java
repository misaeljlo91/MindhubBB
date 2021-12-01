package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private double amount;
    private Integer payments;
    private double monthlyPayment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    //CONSTRUCTOR
    public ClientLoan() { }

    public ClientLoan(Client client, Loan loan, String name, double amount, Integer payments, double monthlyPayment) {
        this.client = client;
        this.loan = loan;
        this.name = loan.getName();
        this.amount = amount;
        this.payments = payments;
        this.monthlyPayment = monthlyPayment;
    }

    //GETTER
    public Long getId() {return id;}
    public String getName() {return name;}
    public double getAmount() {return amount;}
    public Integer getPayments() {return payments;}
    public double getMonthlyPayment() {return monthlyPayment;}

    @JsonIgnore
    public Client getClient() {return client;}

    @JsonIgnore
    public Loan getLoan() {return loan;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setPayments(Integer payments) {this.payments = payments;}
    public void setMonthlyPayment(double monthlyPayment) {this.monthlyPayment = monthlyPayment;}
    public void setClient(Client client) {this.client = client;}
    public void setLoan(Loan loan) {this.loan = loan;}
}
