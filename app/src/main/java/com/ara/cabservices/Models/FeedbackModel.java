package com.ara.cabservices.Models;

import com.google.gson.GsonBuilder;

import java.sql.Time;
import java.util.Timer;

/**
 * Created by User on 16-Mar-18.
 */

public class FeedbackModel  {

    String  name;
    String contactNo;
    String pickup;
    String drop;
    String pickTime;
    String dropTime;
    double cost;


    public FeedbackModel(String name, String contactNo, String pickup, String drop, String pickTime, String dropTime, double cost) {
        this.name = name;
        this.contactNo = contactNo;
        this.pickup = pickup;
        this.drop = drop;
        this.pickTime = pickTime;
        this.dropTime = dropTime;
        this.cost = cost;
    }

    public FeedbackModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public String getDropTime() {
        return dropTime;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, FeedbackModel.class);
    }
}
