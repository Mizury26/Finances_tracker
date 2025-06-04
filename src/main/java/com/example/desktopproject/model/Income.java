package com.example.desktopproject.model;

import java.time.LocalDate;

public class Income extends Monetary {
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

    public String getTotal() {
        return (String.format("%.2f", (this.salary + this.helper + this.selfEnterprise + this.passiveIncome + this.other) * Monetary.rate) + Monetary.unit);
    }

    public Float getStrictTotal() {
        return this.salary + this.helper + this.selfEnterprise + this.passiveIncome + this.other;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSalary() {
        return (String.format("%.2f", salary * Monetary.rate) + Monetary.unit);
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public Float getStrictSalary() {
        return salary;
    }

    public String getHelper() {
        return (String.format("%.2f", helper * Monetary.rate) + Monetary.unit);
    }

    public void setHelper(Float helper) {
        this.helper = helper;
    }

    public Float getStrictHelper() {
        return helper;
    }

    public String getSelfEnterprise() {
        return (String.format("%.2f", selfEnterprise * Monetary.rate) + Monetary.unit);
    }

    public void setSelfEnterprise(Float selfEnterprise) {
        this.selfEnterprise = selfEnterprise;
    }

    public Float getStrictSelfEnterprise() {
        return selfEnterprise;
    }

    public String getPassiveIncome() {
        return (String.format("%.2f", passiveIncome * Monetary.rate) + Monetary.unit);
    }

    public void setPassiveIncome(Float passiveIncome) {
        this.passiveIncome = passiveIncome;
    }

    public Float getStrictPassiveIncome() {
        return passiveIncome;
    }

    public String getOther() {
        return (String.format("%.2f", other * Monetary.rate) + Monetary.unit);
    }

    public void setOther(Float others) {
        this.other = others;
    }

    public Float getStrictOther() {
        return other;
    }
}