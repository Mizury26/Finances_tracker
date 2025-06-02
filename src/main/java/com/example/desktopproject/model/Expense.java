package com.example.desktopproject.model;

import java.time.LocalDate;

public class Expense {
    private LocalDate date;
    private Float total;
    private Float housing;
    private Float food;
    private Float goingOut;
    private Float transportation;
    private Float travel;
    private Float tax;
    private Float others;


    public Expense() {
    }

    public Expense(LocalDate periode, Float total, Float logement, Float nourriture,
                   Float sortie, Float voitureTransport, Float voyage,
                   Float impot, Float autres) {
        this.date = periode;
        this.total = total;
        this.housing = logement;
        this.food = nourriture;
        this.goingOut = sortie;
        this.transportation = voitureTransport;
        this.travel = voyage;
        this.tax = impot;
        this.others = autres;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
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
