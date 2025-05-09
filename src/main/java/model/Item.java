package model;

import lombok.*;

@NoArgsConstructor
@ToString
@Getter @Setter
public class Item {
    private static int idCounter = 1; // Default ID
    private int id;
    private String name;
    private int qty;
    private double price;
    private String desc;

    public Item(String name, int qty, double price, String desc) {
        this.id = idCounter++;
        this.name = name;
        this.qty = qty;
        this.desc = desc;
        this.price = price;
    }

    public static int getNextId(){
        return idCounter;
    }

}
