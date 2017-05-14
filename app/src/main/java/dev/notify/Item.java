package dev.notify;

/**
 * Created by dream on 3/17/2017 AD.
 */

public class Item {

    private String name, expire, member, image, outDate;
    private int amount, id;
    private boolean isOut;


    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name
     * @param amount
     * @param member
     * @param image
     * @param expire
     * @param outDate
     * @param isOut
     */
    public Item(int id, String name, int amount, String member, String image, String expire, String outDate, boolean isOut){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.expire = expire;
        this.member = member;
        this.image = image;
        this.outDate = outDate;
        this.isOut = isOut;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }
}
