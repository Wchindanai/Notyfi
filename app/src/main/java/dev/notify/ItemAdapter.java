package dev.notify;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by dream on 3/25/2017 AD.
 */

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private String TAG = "itemAdapter";
    private List<Item> itemList;
    private Context context;

    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        Log.d(TAG, "ItemAdapter: " + itemList.size());
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cardview, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.data_name.setText(itemList.get(position).getName());
        holder.data_amount.setText(Integer.toString(itemList.get(position).getAmount()));
        String date = null;
        try {

            Date dateFormat = new SimpleDateFormat("EEEE MMM dd yyyy HH:mm:ss Z").parse(itemList.get(position).getExpire());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat);
            int day = calendar.get(Calendar.DAY_OF_MONTH) +1;
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR);
            date = day + "-" + month + "-" + year;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.data_date.setText(date);
        holder.data_member.setText(itemList.get(position).getMember());

        //Encode Image And SetImage
        String stringEncodeImage = itemList.get(position).getImage();
        byte[] decodeImage = Base64.decode(stringEncodeImage, Base64.DEFAULT);
        Glide.with(context).load(decodeImage).asBitmap().into(holder.data_image);

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EditItem.class).putExtra("id", itemList.get(position).getId()));

            }
        });

        holder.sw.setSwitchPadding(40);
        holder.sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        v.getContext());

                // set title
                alertDialogBuilder.setTitle("Are you bring the item out ?");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click yes to confirm!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                try {
                                    bringItemOut(itemList.get(position).getId());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                holder.sw.setChecked(false);
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }


            @RequiresApi(api = Build.VERSION_CODES.N)
            private void bringItemOut(int id) throws JSONException {
                String url = "https://notify-166704.appspot.com/api/items/" + id;
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar BangkokTime = new GregorianCalendar(TimeZone.getTimeZone("Asia/Bangkok"));
                BangkokTime.set(Calendar.HOUR, 11);
                String date = dateFormat.format(BangkokTime.getTime());
                JSONObject json = new JSONObject();
                json.put("is_out", true);
                json.put("out_date", date);
                RequestBody body = RequestBody.create(JSON, json.toString());
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .patch(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        itemList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, itemList.size());
                    }
                });
            }
        });
        
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        v.getContext());

                // set title
                alertDialogBuilder.setTitle("Are you sure to delete this item");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click yes to delete!")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                deleteItem(itemList.get(position).getId());

                            }

                            private void deleteItem(int id) {
                                String url = "https://notify-166704.appspot.com/api/items/" + id;
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(url)
                                        .delete()
                                        .build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.d(TAG, "onFailure: " + e);
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        context.startActivity(new Intent(context, Member.class));
                                    }
                                });
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });



    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        Button edit_btn, delete_btn;
        TextView data_name, data_amount, data_date, data_member;
        ImageView data_image;
        Switch sw;


        MyViewHolder(View view) {
            super(view);
            data_image = (ImageView) view.findViewById(R.id.imageItem);
            data_name = (TextView) view.findViewById(R.id.data_name);
            data_amount = (TextView) view.findViewById(R.id.data_amount);
            data_date = (TextView) view.findViewById(R.id.data_created);
            data_member = (TextView) view.findViewById(R.id.data_member);
            edit_btn = (Button) view.findViewById(R.id.editBtn);
            delete_btn = (Button) view.findViewById(R.id.delBtn);
            sw = (Switch) view.findViewById(R.id.switch1);
        }
    }
}
