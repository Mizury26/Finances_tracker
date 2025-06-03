package com.example.desktopproject.model;

import java.time.LocalDate;

public class Expense extends Monetary {
    private LocalDate date;
    private Float housing;
    private Float food;
    private Float goingOut;
    private Float transportation;
    private Float travel;
    private Float tax;
    private Float others;


    public Expense() {
    }

    public Expense(LocalDate date, Float housing, Float food,
                   Float goingOut, Float transportation, Float travel,
                   Float tax, Float others) {
        this.date = date;
        this.housing = housing;
        this.food = food;
        this.goingOut = goingOut;
        this.transportation = transportation;
        this.travel = travel;
        this.tax = tax;
        this.others = others;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTotal() {
        return (this.others + this.tax + this.transportation + this.goingOut + this.food + this.housing + this.travel) * Monetary.rate + Monetary.unit;
    }

    public Float getStrictTotal() {
        return this.others + this.tax + this.transportation + this.goingOut + this.food + this.housing + this.travel;
    }

    public String getHousing() {
        return housing * Monetary.rate + Monetary.unit;
    }

    public void setHousing(Float housing) {
        this.housing = housing;
    }

    public Float getStrictHousing() {
        return housing;
    }

    public String getFood() {
        return food * Monetary.rate + Monetary.unit;
    }

    public void setFood(Float food) {
        this.food = food;
    }

    public Float getStrictFood() {
        return food;
    }

    public String getGoingOut() {
        return goingOut * Monetary.rate + Monetary.unit;
    }

    public void setGoingOut(Float goingOut) {
        this.goingOut = goingOut;
    }

    public Float getStrictGoingOut() {
        return goingOut;
    }

    public String getTransportation() {
        return transportation * Monetary.rate + Monetary.unit;
    }

    public void setTransportation(Float transportation) {
        this.transportation = transportation;
    }

    public Float getStrictTransportation() {
        return transportation;
    }

    public String getTravel() {
        return travel * Monetary.rate + Monetary.unit;
    }

    public void setTravel(Float travel) {
        this.travel = travel;
    }

    public Float getStrictTravel() {
        return travel;
    }

    public String getTax() {
        return tax * Monetary.rate + Monetary.unit;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public Float getStrictTax() {
        return tax;
    }

    public String getOthers() {
        return others * Monetary.rate + Monetary.unit;
    }

    public void setOthers(Float others) {
        this.others = others;
    }

    public Float getStrictOthers() {
        return others;
    }
}