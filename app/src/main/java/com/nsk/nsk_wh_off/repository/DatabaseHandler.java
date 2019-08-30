package com.nsk.nsk_wh_off.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nsk.nsk_wh_off.model.InventoryItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "inventoryManager";
    private static final String TABLE_INVENTORY = "inventory";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_QUANTITY = "quantity";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_INVENTORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_QUANTITY + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);

        // Create tables again
        onCreate(db);
    }

    public void addItem(InventoryItem item) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.get_name());
        values.put(KEY_QUANTITY, item.get_quantity());

        sqLiteDatabase.insert(TABLE_INVENTORY, null, values);
        sqLiteDatabase.close();
    }

    public InventoryItem getInventoryItem(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                TABLE_INVENTORY,
                new String[]{KEY_ID, KEY_NAME, KEY_QUANTITY},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        return new InventoryItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
    }

    public List<InventoryItem> getInventoryItems() {
        List<InventoryItem> inventoryItemList = new ArrayList<InventoryItem>();

        String query = "SELECT * FROM " + TABLE_INVENTORY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                InventoryItem item = new InventoryItem(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Integer.parseInt(cursor.getString(2)));
                inventoryItemList.add(item);
            } while (cursor.moveToNext());
        }

        return inventoryItemList;
    }

    public void deleteInventoryItem(InventoryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INVENTORY, KEY_ID + " = ?",
                new String[]{String.valueOf(item.get_id())});
        db.close();
    }
}
