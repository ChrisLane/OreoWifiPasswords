package com.gmail.ndraiman.wifipasswords;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ND88 on 09/09/2015.
 */
public class WifiListAdapter extends RecyclerView.Adapter<WifiListAdapter.MyViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<WifiEntry> listWifi = new ArrayList<>();

    public WifiListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

        //TODO Delete once SQLite database is implemented
        listWifi = placeholderData();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_wifi_entry, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        WifiEntry currentEntry = listWifi.get(position);
        holder.wifiTitle.setText(currentEntry.getTitle());
        holder.wifiPassword.setText(currentEntry.getPassword());

    }

    @Override
    public int getItemCount() {
        return listWifi.size();
    }




    public void setWifiList(ArrayList<WifiEntry> listWifi) {
        this.listWifi = listWifi;
        notifyItemRangeChanged(0, listWifi.size());
    }


    /*****************************************/
    /********** View Holder Sub-Class ********/
    /*****************************************/
    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView wifiTitle;
        private TextView wifiPassword;

        public MyViewHolder(View itemView) {
            super(itemView);

            wifiTitle = (TextView) itemView.findViewById(R.id.title_wifi);
            wifiPassword = (TextView) itemView.findViewById(R.id.password_wifi);

        }
    }


    /*****************************************/
    /************ PlaceHolder Data ***********/
    /*****************************************/
    public static List<WifiEntry> placeholderData() {

        List<WifiEntry> data = new ArrayList<>();

        String[] titles = {"Wifi 1", "Wifi 2", "Wifi 3", "Wifi 4", "Wifi 5","Wifi 6","Wifi 7"
                ,"Wifi 8", "Wifi 9","Wifi 10"};

        String[] passwords = {"Pass 1", "Pass 2", "Pass 3", "Pass 4", "Pass 5","Pass 6","Pass 7"
                ,"Pass 8", "Pass 9","Pass 10"};

        for(int i=0; i < 100; i++) {

            WifiEntry current = new WifiEntry();
            current.setTitle(titles[i % titles.length]);
            current.setPassword(passwords[i % passwords.length]);

            data.add(current);
        }

        return data;
    }
}