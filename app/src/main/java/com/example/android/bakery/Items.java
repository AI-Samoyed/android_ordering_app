package com.example.android.bakery;

public class Items {
    private String name ;
    private long Avail_quantity;
    private long Item_id ;
    private long price;

    private Items() {

    }
    private Items( long Item_id, String name, long price, long Avail_quantity){
         this.Item_id = Item_id;
         this.name = name;
         this.price = price ;
         this.Avail_quantity = Avail_quantity ;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getAvail_quantity() {
        return Avail_quantity;
    }

    public void setAvail_quantity(long avail_quantity) {
        Avail_quantity = avail_quantity;
    }

    public long getItem_id() {
        return Item_id;
    }

    public void setItem_id(long item_id) {
        Item_id = item_id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
