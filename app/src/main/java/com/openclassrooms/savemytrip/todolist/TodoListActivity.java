package com.openclassrooms.savemytrip.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.savemytrip.R;
import com.openclassrooms.savemytrip.injection.Injection;
import com.openclassrooms.savemytrip.injection.ViewModelFactory;
import com.openclassrooms.savemytrip.models.Item;
import com.openclassrooms.savemytrip.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class TodoListActivity extends AppCompatActivity implements ItemAdapter.Listener {

    @BindView(R.id.todo_list_activity_recycler_view) RecyclerView mRecyclerView;
    private List<String> tab ;
    private List<Item> mItemList;
    private ItemAdapter mAdapter;
    @BindView(R.id.todo_list_activity_edit_text)  EditText mEditText;
    @BindView(R.id.todo_list_activity_header_profile_text) TextView profileText;
    @BindView(R.id.todo_list_activity_header_profile_image) ImageView profileImage;
    @BindView(R.id.todo_list_activity_spinner) Spinner spinner;
    private int selector=0;
    //@BindView(R.id.activity_todo_list_item_image)  ImageView mImageView;
    RequestManager glide;
    private int i=0;
    private static int USER_ID = 1;
    private ItemViewModel itemViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        //tab[i]= "quelques";

        ButterKnife.bind(this);
        configureSpinner();
        configureRecyclerView();
        this.configureViewModel();

        this.getCurrentUser(USER_ID);
        this.getItems(USER_ID);


    }

    private void configureRecyclerView(){
        System.out.println("-------------------configueReyclerview-----------------"+this.spinner.getSelectedItemPosition());
        this.mItemList = new ArrayList<>();
        this.mAdapter = new ItemAdapter(this);
        this.mRecyclerView.setAdapter(mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel.class);
        this.itemViewModel.init(USER_ID);
    }

    // ---

    private void getCurrentUser(int userId){
        this.itemViewModel.getUser(userId).observe(this, this::updateHeader);
    }

    // ---

    private void getItems(int userId){
        this.itemViewModel.getItems(userId).observe(this, this::updateItemsList);
    }

    private void createItem(){
        Item item = new Item(this.mEditText.getText().toString(), this.spinner.getSelectedItemPosition(), USER_ID);
        this.mEditText.setText("");
        this.itemViewModel.createItem(item);
    }

    private void deleteItem(Item item){
        this.itemViewModel.deleteItem(item.getId());
    }

    private void updateItem(Item item){
        item.setSelected(!item.getSelected());
        this.itemViewModel.updateItem(item);
    }

    private void updateHeader(User user){
        this.profileText.setText(user.getUsername());
        Glide.with(this).load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(this.profileImage);
    }

    private void updateItemsList(List<Item> items){
        this.mAdapter.updateData(items);
    }

    private void configureSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

    }



    // -------------------
    // ACTIONS
    // -------------------

    @OnClick(R.id.todo_list_activity_button_add)
    public void onClickAddButton() { this.createItem(); }

    @Override
    public void onClickDeleteButton(Item item) { this.deleteItem(item); }
    //public void onClickDeleteButton(Item item) { this.deleteItem(this.mAdapter.getItem()); }

    public void appelfonction(){
       //addButton(tab);

       // tab.add(this.mEditText.getText().toString());
        mItemList.add(new Item(this.mEditText.getText().toString(), this.spinner.getSelectedItemPosition(), USER_ID));
        this.mAdapter.updateData(mItemList);

        i++;
        selector = spinner.getSelectedItemPosition();
        System.out.println("-----------------------bouton----------------------"+spinner.getSelectedItemPosition());



        mAdapter.notifyDataSetChanged();

    }



    @Override
    public void onItemClick(Item item) {

    }
}