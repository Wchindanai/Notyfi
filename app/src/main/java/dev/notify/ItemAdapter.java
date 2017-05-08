package dev.notify;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.support.v7.app.AlertDialog;

import com.bumptech.glide.Glide;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
        Log.d(TAG, "ItemAdapter: "+itemList.size());
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
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH)+1;
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
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        Bitmap bitmapImage = BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length, options);

//        Set Image
//        holder.data_image.setImageBitmap(bitmapImage);

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, EditItem.class).putExtra("id",itemList.get(position).getId()));

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
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                deleteItem(itemList.get(position).getId());

                            }

                            private void deleteItem(int id) {
                                String url = "https://notify-166704.appspot.com/api/items/"+id;
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(url)
                                        .delete()
                                        .build();
                                client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.d(TAG, "onFailure: "+ e);
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        context.startActivity(new Intent(context, Member.class));
                                    }
                                });


                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
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

    class MyViewHolder extends RecyclerView.ViewHolder{
        Button edit_btn, delete_btn;
        TextView data_name, data_amount, data_date, data_member;
        ImageView data_image;


        MyViewHolder(View view) {
            super(view);
            data_image = (ImageView) view.findViewById(R.id.imageItem);
            data_name = (TextView) view.findViewById(R.id.data_name);
            data_amount = (TextView) view.findViewById(R.id.data_amount);
            data_date = (TextView) view.findViewById(R.id.data_created);
            data_member = (TextView) view.findViewById(R.id.data_member);
            edit_btn = (Button) view.findViewById(R.id.editBtn);
            delete_btn = (Button) view.findViewById(R.id.delBtn);
        }
    }
}
