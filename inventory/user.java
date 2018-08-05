package inventory;

/**
 * Created by Ketter Collins on 2/19/2017.
 */
public class user
{
    public int getInvID() {
        return invID;
    }

    public void setInvID(int invID) {
        this.invID = invID;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public user(int invID, String item, String description, int quantity, int sellingPrice) {
        this.invID = invID;
        this.item = item;
        this.description = description;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
    }

    private int invID;
    private String item;
    private String description;
    private int quantity;
    private int sellingPrice;




}
