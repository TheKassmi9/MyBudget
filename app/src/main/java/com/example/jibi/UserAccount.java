package com.example.jibi;

public class UserAccount {
    private double budget;
    private String email;
    private String userName;
    private double goal;


    public UserAccount() {
    }

    public UserAccount(double budget, String email, String userName, double goal) {
        this.budget = budget;
        this.email = email;
        this.userName = userName;
        this.goal=goal;

    }

    public double getBudget() {
        return budget;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

//    public Income getIncome() {
//        return income;
//    }
//
//    public Spend getSpend() {
//        return spend;
//    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }
    //    public void setIncome(Income income) {
//        this.income = income;
//    }
//
//    public void setSpend(Spend spend) {
//        this.spend = spend;
//    }
}
