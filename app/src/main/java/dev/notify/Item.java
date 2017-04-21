package dev.notify;

/**
 * Created by dream on 3/17/2017 AD.
 */

public class Item {
    private String name, expire, member, image;
    private int amount;


    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    /**
     * @param name
     * @param expire
     * @param amount
     * @param member
     * @param image
     * @param expire
     */
    public Item(String name, int amount, String member, String image, String expire){
        this.name = name;
        this.amount = amount;
        this.expire = expire;
        this.member = member;
        this.image = image;
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
}
