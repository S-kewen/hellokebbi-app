package com.example.hellokeb_bi.entity;


import java.util.Date;

public class RentRecord {
    //context
    private int id;
    private int type;
    private int state;
    private int amount;
    private String title;
    private String msg;
    private Date add_time;

    public RentRecord(int id, int type, int state, int amount, String title, String msg, Date add_time) {
        this.id = id;
        this.type = type;
        this.state = state;
        this.amount = amount;
        this.title = title;
        this.msg = msg;
        this.add_time = add_time;
    }

    @Override
    public String toString() {
        return "RentRecord{" +
                "id=" + id +
                ", type=" + type +
                ", state=" + state +
                ", amount=" + amount +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                ", add_time=" + add_time +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Date add_time) {
        this.add_time = add_time;
    }
}
