package dev.notify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Member extends AppCompatActivity {
    private static final String TAG = "Member";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private List<Item> itemList = new ArrayList<>();

    private FloatingActionButton addFAB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        //Initial FAB
        addFAB = (FloatingActionButton) findViewById(R.id.addItemFAB);
        
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // specify an adapter (see also next example)
        mAdapter = new ItemAdapter(Member.this, itemList);
        mRecyclerView.setAdapter(mAdapter);

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddItem.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.member_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                SharedPreferences sharedPreferences = getSharedPreferences("notify", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
            case R.id.history:
                startActivity(new Intent(getApplicationContext(), History.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {

    }
}
