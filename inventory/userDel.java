package inventory;

/**
 * Created by Ketter Collins on 2/19/2017.
 */
public class userDel
{
    public int getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(int deliveryID) {
        this.deliveryID = deliveryID;
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public userDel(int deliveryID, String item, int quantity, String date, String supplier) {
        this.deliveryID = deliveryID;
        this.item = item;
        this.quantity = quantity;
        this.date = date;
        this.supplier = supplier;
    }

    private int deliveryID;
    private String item;
    private int quantity;
    private String date;
    private String supplier;




}
