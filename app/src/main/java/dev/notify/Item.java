package dev.notify;

/**
 * Created by dream on 3/17/2017 AD.
 */

public class Item {
    private String name, date, member;
    private int amount;

    public Item(){}

    public Item(String name, String date, int amount, String member){
        this.name = name;
        this.date = date;
        this.amount = amount;
        this.member = member;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}
