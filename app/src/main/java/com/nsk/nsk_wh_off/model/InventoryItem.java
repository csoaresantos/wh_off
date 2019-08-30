package com.nsk.nsk_wh_off.model;

public class InventoryItem {
    int _id;
    String _name;
    int _quantity;

    public InventoryItem(int id, String name, int quantity) {
        this._id = id;
        this._name = name;
        this._quantity = quantity;
    }

    public InventoryItem(String name, int quantity) {
        this._name = name;
        this._quantity = quantity;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_quantity() {
        return _quantity;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }
}
