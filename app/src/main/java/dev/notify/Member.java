package dev.notify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Member extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<Item> itemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);


        
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // specify an adapter (see also next example)
        mAdapter = new ItemAdapter(Member.this, itemList);
        mRecyclerView.setAdapter(mAdapter);

        //Authentication App
        if(!auth()){
            startActivity(new Intent(Member.this, Login.class));
        }
        getData();
    }

    private boolean auth() {
        boolean auth;
        auth = getIntent().getExtras().getBoolean("auth", false);
        if(auth){
            return true;
        }
        else{
            return false;
        }
    }

    private void getData() {
        Item item = new Item("Orange", "2016-03-22", 10, "Chindanai");
        itemList.add(item);
        item = new Item("Mango", "2016-03-20", 5, "Admin");
        itemList.add(item);
        item = new Item("PineApple", "2012-05-2", 1, "Member");
        itemList.add(item);
        item = new Item("Banana", "2012-05-2", 1, "Dream");
        itemList.add(item);
        item = new Item("Test", "2012-05-2", 1, "Test");
        itemList.add(item);
        item = new Item("Test", "2012-05-2", 1, "Test");
        itemList.add(item);
//        mAdapter.notifyDataSetChanged();
    }
}
