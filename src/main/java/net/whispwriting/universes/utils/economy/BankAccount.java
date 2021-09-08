package net.whispwriting.universes.utils.economy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankAccount {

    private String owner;
    private List<String> members = new ArrayList<>();
    private double balance;

    public BankAccount(String owner){
        this.owner = owner;
    }

    public void addMember(String member){
        members.add(member);
    }

    public double getBalance(){
        return balance;
    }

    public void deposit(double amount){
        balance += amount;
    }

    public boolean withdraw(double amount){
        if (balance - amount >= 0){
            balance -= amount;
            return true;
        }
        return false;
    }

    public String getOwner(){
        return owner;
    }
}
