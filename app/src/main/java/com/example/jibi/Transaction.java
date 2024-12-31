package com.example.jibi;

import com.google.firebase.Timestamp;

public class Transaction {
     Timestamp date;
    String type;
     double value;
     String collectionType;

    public Transaction() {
    }

    public Transaction(Timestamp date, String type, double value, String collectionType) {
        this.date = date;
        this.type = type;
        this.value = value;
        this.collectionType = collectionType;
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

    public String getCollectionType() {
        return collectionType;
    }
}
