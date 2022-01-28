package com.openclassrooms.savemytrip.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.openclassrooms.savemytrip.R;
import com.openclassrooms.savemytrip.databinding.ActivityTodoListItemBinding;
import com.openclassrooms.savemytrip.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    // CALLBACK

    public interface Listener {

        void onClickDeleteButton(Item item);

        void onItemClick(Item item);

    }

    private final Listener callback;


    // FOR DATA

    private List<Item> items;


    // CONSTRUCTOR

    public ItemAdapter(Listener callback) {

        this.items = new ArrayList<>();

        this.callback = callback;

    }


    @Override

    @NonNull

    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        return new ItemViewHolder(ActivityTodoListItemBinding.inflate(inflater, parent, false));

    }


    @Override

    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {

        viewHolder.updateWithItem(this.items.get(position), this.callback);

    }


    @Override

    public int getItemCount() {

        return this.items.size();

    }

    public Item getItem(int position){
        return this.items.get(position);
    }


    public void updateData(List<Item> items){

        this.items = items;

        this.notifyDataSetChanged();

    }
}
