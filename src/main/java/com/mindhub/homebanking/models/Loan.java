package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String name;
    private double maxAmount;
    private int averageInterest;

    @ElementCollection
    @Column(name = "payment")
    private List<Integer> payments = new ArrayList<>();

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER, orphanRemoval = true)
    Set<ClientLoan> clientLoans = new HashSet<>();

    //CONSTRUCTOR
    public Loan() { }

    public Loan(String name, double maxAmount, int averageInterest, List<Integer> payments) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.averageInterest = averageInterest;
        this.payments = payments;
    }

    //GETTER
    public Long getId() {return id;}
    public String getName() {return name;}
    public double getMaxAmount() {return maxAmount;}
    public int getAverageInterest() {return averageInterest;}
    public List<Integer> getPayments() {return payments;}
    public Set<ClientLoan> getClientLoans() {return clientLoans;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setMaxAmount(double maxAmount) {this.maxAmount = maxAmount;}
    public void setAverageInterest(int averageInterest) {this.averageInterest = averageInterest;}
    public void setPayments(List<Integer> payments) {this.payments = payments;}

    //METHOD-GET
    @JsonIgnore
    public List<Client> getClients() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(toList());
    }
}
