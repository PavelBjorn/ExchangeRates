package com.fedor.pavel.exchangerates.models;


import com.google.gson.annotations.SerializedName;

public abstract class Model {


    protected String name;
    protected long id;

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


}
