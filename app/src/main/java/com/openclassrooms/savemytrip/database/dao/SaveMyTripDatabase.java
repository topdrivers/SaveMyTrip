package com.openclassrooms.savemytrip.database.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.savemytrip.models.Item;
import com.openclassrooms.savemytrip.models.User;

import java.util.concurrent.Executors;

@Database(entities = {Item.class, User.class}, version = 1, exportSchema = false)

public abstract class SaveMyTripDatabase extends RoomDatabase {


    // --- SINGLETON ---

    private static volatile SaveMyTripDatabase INSTANCE;


    // --- DAO ---

    public abstract ItemDao itemDao();


    public abstract UserDao userDao();


    // --- INSTANCE ---

    public static SaveMyTripDatabase getInstance(Context context) {

        if (INSTANCE == null) {

            synchronized (SaveMyTripDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),

                            SaveMyTripDatabase.class, "MyDatabase.db")

                            .addCallback(prepopulateDatabase())

                            .build();

                }

            }

        }

        return INSTANCE;

    }


    private static Callback prepopulateDatabase() {

        return new RoomDatabase.Callback() {

            @Override

            public void onCreate(@NonNull SupportSQLiteDatabase db) {

                super.onCreate(db);

                Executors.newSingleThreadExecutor().execute(() -> INSTANCE.userDao().createUser(new User(1, "Philippe", "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2")));

            }

        };

    }

}