package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Directory;

public class DirectoryDTO {

    private Long id;
    private String accountHolder;
    private String accountNumber;
    private String email;
    private String observation;

    //CONSTRUCTOR
    public DirectoryDTO() { }

    public DirectoryDTO(Directory directory) {
        this.id = directory.getId();
        this.accountHolder = directory.getAccountHolder();
        this.accountNumber = directory.getAccountNumber();
        this.email = directory.getEmail();
        this.observation = directory.getObservation();
    }

    //GETTER
    public Long getId() {return id;}
    public String getAccountHolder() {return accountHolder;}
    public String getAccountNumber() {return accountNumber;}
    public String getEmail() {return email;}
    public String getObservation() {return observation;}

    //SETTER
    public void setId(Long id) {this.id = id;}
    public void setAccountHolder(String accountHolder) {this.accountHolder = accountHolder;}
    public void setAccountNumber(String accountNumber) {this.accountNumber = accountNumber;}
    public void setEmail(String email) {this.email = email;}
    public void setObservation(String observation) {this.observation = observation;}
}
