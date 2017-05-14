package dev.notify;

/**
 * Created by dream on 4/29/2017 AD.
 */

public class HistoryModel {
    String name;
    String expire;
    String created;
    String member;
    String outDate;
    int amount;

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public HistoryModel(String name, String expire, String created, String member, int amount, String outDate) {
        this.name = name;
        this.expire = expire;
        this.created = created;
        this.member = member;
        this.amount = amount;
        this.outDate = outDate;
    }


}
