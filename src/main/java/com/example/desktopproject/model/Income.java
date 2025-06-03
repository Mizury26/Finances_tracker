package com.example.desktopproject.model;

import java.time.LocalDate;

public class Income {
    private LocalDate date;
    private Float salary;
    private Float helper;
    private Float selfEnterprise;
    private Float passiveIncome;
    private Float other;

    public Income() {
    }

    public Income(LocalDate periode, Float salary, Float aide,
                  Float selfEnterprise, Float passiveIncome, Float other) {
        this.date = periode;
        this.salary = salary;
        this.helper = aide;
        this.selfEnterprise = selfEnterprise;
        this.passiveIncome = passiveIncome;
        this.other = other;
    }

    public Float getTotal() {
        return this.salary + this.helper + this.selfEnterprise + this.passiveIncome + this.other;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public Float getHelper() {
        return helper;
    }

    public void setHelper(Float helper) {
        this.helper = helper;
    }

    public Float getSelfEnterprise() {
        return selfEnterprise;
    }

    public void setSelfEnterprise(Float selfEnterprise) {
        this.selfEnterprise = selfEnterprise;
    }

    public Float getPassiveIncome() {
        return passiveIncome;
    }

    public void setPassiveIncome(Float passiveIncome) {
        this.passiveIncome = passiveIncome;
    }

    public Float getOther() {
        return other;
    }

    public void setOther(Float others) {
        this.other = others;
    }
}
