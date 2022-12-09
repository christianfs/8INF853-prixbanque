package com.prixbanque.statementservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer user_id;

    private Integer amount;

    public Integer getUser_Id() {
        return user_id;
    }

    public void setUser_Id(Integer user_id) {
        this.user_id = user_id;
    }



    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}