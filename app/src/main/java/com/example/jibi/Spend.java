package com.example.jibi;

import com.google.firebase.Timestamp;

public class Spend {
    private Timestamp date;
    private String type;
    private double value;

    public Spend() {
    }

    public Spend(Timestamp date, String type, double value) {
        this.date = date;
        this.type = type;
        this.value = value;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
