package dev.notify;

/**
 * Created by dream on 3/17/2017 AD.
 */

public class Item {
    private String name, date, member, image;
    private int amount;



    /**
     * @param name
     * @param date
     * @param amount
     * @param member
     * @param image
     */
    public Item(String name, String date, int amount, String member, String image){
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.member = member;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
