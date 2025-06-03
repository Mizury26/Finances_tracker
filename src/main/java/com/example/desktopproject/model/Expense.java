package com.example.desktopproject.model;

import java.time.LocalDate;

public class Expense {
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

    public Float getTotal() {
        return this.others + this.tax + this.transportation + this.goingOut + this.food + this.housing + this.travel;
    }

    public Float getHousing() {
        return housing;
    }

    public void setHousing(Float housing) {
        this.housing = housing;
    }

    public Float getFood() {
        return food;
    }

    public void setFood(Float food) {
        this.food = food;
    }

    public Float getGoingOut() {
        return goingOut;
    }

    public void setGoingOut(Float goingOut) {
        this.goingOut = goingOut;
    }

    public Float getTransportation() {
        return transportation;
    }

    public void setTransportation(Float transportation) {
        this.transportation = transportation;
    }

    public Float getTravel() {
        return travel;
    }

    public void setTravel(Float travel) {
        this.travel = travel;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public Float getOthers() {
        return others;
    }

    public void setOthers(Float others) {
        this.others = others;
    }
}
