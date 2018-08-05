package receivables;

public class User
{
   private int rcID;
   private int saleID;
   private String item;
   private int quantity;
   private int amount;
   private String purchaseDate;
   private String paymentDate;
   private String customer;
   private int amountPaid;
   private String status;
   private int balance;

    public User(int rcID, int saleID, String item, int quantity, int amount, String purchaseDate, String paymentDate, String customer,int amountPaid, String status, int balance) {
        this.rcID = rcID;
        this.saleID = saleID;
        this.item = item;
        this.quantity = quantity;
        this.amount = amount;
        this.purchaseDate = purchaseDate;
        this.paymentDate = paymentDate;
        this.customer = customer;
        this.amountPaid = amountPaid;
        this.status = status;
        this.balance = balance;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getRcID() {
        return rcID;
    }

    public void setRcID(int rcID) {
        this.rcID = rcID;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
