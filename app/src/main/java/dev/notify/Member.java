package dev.notify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.swipe.SwipeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Member extends AppCompatActivity {
    private static final String TAG = "Member";

    private List<Item> itemList;

    private FloatingActionButton addFAB;

    public RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        itemList = new ArrayList<>();

        //Initial FAB
        addFAB = (FloatingActionButton) findViewById(R.id.addItemFAB);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);


        getItemToRecycler();

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddItem.class));
            }
        });
        
    }

    private void getItemToRecycler() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://notify-163706.appspot.com/api/items")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response.body().string());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String itemName = (String) jsonObject.get("name");
                        int id = (int) jsonObject.get("id");
                        int itemAmount = (int) jsonObject.get("amount");
                        String itemMember = (String) jsonObject.get("users_username");
                        String itemImage = (String) jsonObject.get("picture");
                        String itemExpire_date = (String) jsonObject.get("expire_date");
                        Item item = new Item(id ,itemName, itemAmount, itemMember, itemImage, itemExpire_date);
                        itemList.add(item);
                    }

                    Member.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            mRecyclerView.setAdapter(new ItemAdapter(itemList, getApplicationContext()));

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
        switch (item.getItemId()) {
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




}
