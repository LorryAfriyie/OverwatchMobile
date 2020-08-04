package com.reidius.lawrenceafriyie.overwatchmap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident;

import java.util.List;

public class ViewIncidentAdapter extends RecyclerView.Adapter<ViewIncidentAdapter.MyviewHolder> {

    private Context context;
    private List<ReportIncident> incidentList;

    public ViewIncidentAdapter(Context context, List<ReportIncident> incidentList) {
        this.context = context;
        this.incidentList = incidentList;
    }

    @NonNull
    @Override
    public ViewIncidentAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_incidents_list, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewIncidentAdapter.MyviewHolder holder, int position) {
        holder.id.setText(incidentList.get(position).getPersonID());
        holder.description.setText(incidentList.get(position).getDescription());
        holder.date.setText(incidentList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return incidentList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView id;
        TextView description;
        TextView date;

        public MyviewHolder(View itemView) {
            super(itemView);

            id = (TextView)itemView.findViewById(R.id.id);
            description = (TextView)itemView.findViewById(R.id.description);
            date = (TextView)itemView.findViewById(R.id.date);
        }
    }
}
