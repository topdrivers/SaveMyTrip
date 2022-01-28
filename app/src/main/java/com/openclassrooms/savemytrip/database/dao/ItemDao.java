package com.openclassrooms.savemytrip.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.savemytrip.models.Item;

import java.util.List;

@Dao

public interface ItemDao {


    @Query("SELECT * FROM Item WHERE userId = :userId")
    LiveData<List<Item>> getItems(long userId);


    @Query("SELECT * FROM Item WHERE userId = :userId")
    Cursor getItemsWithCursor(long userId);


    @Insert

    long insertItem(Item item);


    @Update

    int updateItem(Item item);


    @Query("DELETE FROM Item WHERE id = :itemId")

    int deleteItem(long itemId);

}