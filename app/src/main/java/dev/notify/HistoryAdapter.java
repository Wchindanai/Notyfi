package dev.notify;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dream on 4/29/2017 AD.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private static final String TAG = "HistoryAdapter";
    List<HistoryModel> listHistory;
    Context context;

    public HistoryAdapter(List<HistoryModel> listHistory, Context context) {
        Log.d(TAG, "HistoryAdapter: "+ listHistory.size());
        this.listHistory = listHistory;
        this.context = context;
    }



    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, int position) {

        String expire_date = null;
        holder.itemView.setId(listHistory.get(position).getId());
        try {
            Date dateFormat = new SimpleDateFormat("EEEE MMM dd yyyy HH:mm:ss Z").parse(listHistory.get(position).getExpire());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat);
            int day = calendar.get(Calendar.DAY_OF_MONTH)+1;
            int month = calendar.get(Calendar.MONTH)+1;
            int year = calendar.get(Calendar.YEAR);
            expire_date = day + "-" + month + "-" + year;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String created_date = null;
        try {
            Date dateFormat = new SimpleDateFormat("EEEE MMM dd yyyy HH:mm:ss Z").parse(listHistory.get(position).getCreated());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH)+1;
            int year = calendar.get(Calendar.YEAR);
            int hours = calendar.get(Calendar.HOUR) + 12;
            int minute = calendar.get(Calendar.MINUTE);
            created_date = day + "-" + month + "-" + year + " " + hours + ":" + minute;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.data_name.setText(listHistory.get(position).getName());
        holder.data_amount.setText(String.valueOf(listHistory.get(position).getAmount()));
        holder.data_expire.setText(expire_date);
        holder.data_create.setText(created_date);
        holder.data_member.setText(listHistory.get(position).getMember());
        holder.data_outDate.setText(listHistory.get(position).getOutDate());

    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView data_name, data_amount, data_create, data_expire, data_member, data_outDate;
        public MyViewHolder(View itemView) {
            super(itemView);
            data_name = (TextView) itemView.findViewById(R.id.item_name);
            data_amount = (TextView) itemView.findViewById(R.id.item_amount);
            data_create = (TextView) itemView.findViewById(R.id.item_created);
            data_expire = (TextView) itemView.findViewById(R.id.item_expire);
            data_member = (TextView) itemView.findViewById(R.id.item_member);
            data_outDate = (TextView) itemView.findViewById(R.id.item_outDate);

        }
    }


}
