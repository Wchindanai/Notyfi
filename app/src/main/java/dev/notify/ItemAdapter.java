package dev.notify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder>{

    private List<Item> itemList;
    private Context context;

    public ItemAdapter(Context context, List<Item> itemList){
        this.context = context;
        this.itemList = itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date, amount, member;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.data_name);
            date = (TextView) view.findViewById(R.id.data_date);
            amount = (TextView) view.findViewById(R.id.data_amount);
            member = (TextView) view.findViewById(R.id.data_member);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_cardview, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.amount.setText(item.getAmount());
        holder.date.setText(item.getDate());
        holder.member.setText(item.getMember());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}