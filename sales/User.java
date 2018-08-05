package sales;

import javafx.beans.value.ObservableValue;

public class User
{
    private int saleID;
    private String item;
    private int quantity;
    private String date;
    private int amount;
    private String customer;
    private String status;

    public User(int saleID, String item, int quantity, String date, int amount, String customer, String status) {
        this.saleID = saleID;
        this.item = item;
        this.quantity = quantity;
        this.date = date;
        this.amount = amount;
        this.customer = customer;
        this.status = status;
    }

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
