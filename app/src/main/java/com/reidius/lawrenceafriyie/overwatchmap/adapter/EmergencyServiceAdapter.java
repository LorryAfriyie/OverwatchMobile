package com.reidius.lawrenceafriyie.overwatchmap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.models.EmergencyService;

import java.util.List;

public class EmergencyServiceAdapter extends RecyclerView.Adapter<EmergencyServiceAdapter.MyviewHolder>{

    private Context context;
    private List<EmergencyService> serviceList;

    public EmergencyServiceAdapter(Context context, List<EmergencyService> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public EmergencyServiceAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.emergency_service_list, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyServiceAdapter.MyviewHolder holder, int position) {
        holder.name.setText("Service Name: " + serviceList.get(position).getName());
        holder.description.setText("Description: " + serviceList.get(position).getDescription());
        holder.contact.setText("Contact: " + serviceList.get(position).getContactNo());
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        TextView contact;

        public MyviewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.service_name);
            description = (TextView)itemView.findViewById(R.id.service_description);
            contact = (TextView)itemView.findViewById(R.id.service_contact);
        }
    }
}
