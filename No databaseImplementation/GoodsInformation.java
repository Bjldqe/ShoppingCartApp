package ShoppingCart;

public class GoodsInformation {
    private String name;
    private double price;
    private int inventory;
    private int id;

    public GoodsInformation() {
    }

    public GoodsInformation(String name, double price, int inventory, int id) {
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void reduceInventory(int number){
        this.inventory=this.inventory-number;
    }
}
