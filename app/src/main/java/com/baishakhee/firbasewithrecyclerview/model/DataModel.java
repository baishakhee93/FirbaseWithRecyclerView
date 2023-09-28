package com.baishakhee.firbasewithrecyclerview.model;

public class DataModel {
   private String id;
    private String name;
    private int age;
    private String city;

    public DataModel() {
    }

    public DataModel(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

 public String getId() {
            return id;
        }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}