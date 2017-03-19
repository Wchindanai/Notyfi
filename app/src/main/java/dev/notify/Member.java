package dev.notify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Member extends AppCompatActivity {
    private List<Item> itemList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mAdapter = new ItemAdapter(this,prepareData());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        

    }

    private List<Item> prepareData() {
        Item item1 = new Item("Hello", "2017-02-02", 10, "admin");

        Item item2 = new Item("world", "2017-02-02", 10, "admin555");

        List<Item> item = new ArrayList<Item>();
        item.add(item1);
        item.add(item2);

        Log.i("Kuy", "KUU");
        return item;
    }
}
