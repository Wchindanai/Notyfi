package dev.notify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by dream on 3/25/2017 AD.
 */

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private List<Item> itemList;
    private Context context;

    ItemAdapter(Context context, List<Item> itemList){
        this.context = context;
        this.itemList = itemList;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView data_name, data_amount, data_date, data_member;
        ImageView data_image;


        MyViewHolder(View view) {
            super(view);
            data_image = (ImageView) view.findViewById(R.id.imageItem);
            data_name = (TextView) view.findViewById(R.id.data_name);
            data_amount = (TextView) view.findViewById(R.id.data_amount);
            data_date = (TextView) view.findViewById(R.id.data_created);
            data_member = (TextView) view.findViewById(R.id.data_member);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View View = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new MyViewHolder(View);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.data_name.setText(itemList.get(position).getName());
        holder.data_amount.setText(String.valueOf(itemList.get(position).getAmount()));
        holder.data_date.setText(itemList.get(position).getDate());
        holder.data_member.setText(itemList.get(position).getMember());

        //Encode Image And SetImage
        String stringEncodeImage = itemList.get(position).getImage();
        byte[] decodeImage = Base64.decode(stringEncodeImage, Base64.DEFAULT);
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);

        //Set Image
        holder.data_image.setImageBitmap(bitmapImage);




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
