package dev.notify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dream on 3/25/2017 AD.
 */

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    private List<Item> itemList;
    private Context mContext;

    public ItemAdapter(Context context, List<Item> itemList){
        this.mContext = context;
        this.itemList = itemList;
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView data_name, data_amount, data_date, data_member;
        CardView cv;

        public MyViewHolder(View view) {
            super(view);
            cv = (CardView) view.findViewById(R.id.cardView);
            data_name = (TextView) view.findViewById(R.id.data_name);
            data_amount = (TextView) view.findViewById(R.id.data_amount);
            data_date = (TextView) view.findViewById(R.id.data_created);
            data_member = (TextView) view.findViewById(R.id.data_member);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View View = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        MyViewHolder viewHolder = new MyViewHolder(View);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.data_name.setText(item.getName());
        holder.data_amount.setText(String.valueOf(item.getAmount()));
        holder.data_date.setText(item.getDate());
        holder.data_member.setText(item.getMember());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
