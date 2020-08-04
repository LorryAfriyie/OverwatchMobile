package com.reidius.lawrenceafriyie.overwatchmap.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.reidius.lawrenceafriyie.overwatchmap.R;
import com.reidius.lawrenceafriyie.overwatchmap.models.ReportIncident;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class IncidentViewAdapter extends RecyclerView.Adapter<IncidentViewAdapter.MyviewHolder> implements  Filterable{

    private Context context;
    private List<ReportIncident> incidentFilter;
    private List<ReportIncident> incidentList;

    public void IncidentViewAdapter(Context context, final List<ReportIncident> incidentList) {
        this.context = context;
        if(this.incidentList == null){
            this.incidentList = incidentList;
            this.incidentFilter = incidentList;
            notifyItemChanged(0, incidentFilter.size());

        }
        else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return IncidentViewAdapter.this.incidentList.size();
                }

                @Override
                public int getNewListSize() {
                    return incidentList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return IncidentViewAdapter.this.incidentList.get(oldItemPosition).getDescription() == incidentList.get(newItemPosition).getDescription();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

                    ReportIncident reportIncident = IncidentViewAdapter.this.incidentList.get(oldItemPosition);
                    ReportIncident reportIncident1 = incidentList.get(newItemPosition);

                    return reportIncident.getDescription() == reportIncident1.getDescription();
                }
            });
            this.incidentList = incidentList;
            this.incidentFilter = incidentList;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public IncidentViewAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_incident_layout, parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidentViewAdapter.MyviewHolder holder, int position) {
        holder.description.setText("Type: " + incidentFilter.get(position).getDescription());
        holder.date.setText("Date: " + incidentList.get(position).getDate());
        holder.address.setText("Address: " + incidentList.get(position).getAddress());
        holder.status.setText("Status: " + incidentList.get(position).getStatus());
        holder.comment.setText("Comment: " + incidentList.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        if(incidentList != null){
            return incidentFilter.size();
        }
        else {
            return 0;
        }
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()) {
                    incidentFilter = incidentList;
                } else {
                    List<ReportIncident> filteredList = new ArrayList<>();
                    for (ReportIncident report : incidentList) {
                        if(report.getDescription().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(report);
                        }
                    }
                    incidentFilter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = incidentFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                incidentFilter = (ArrayList<ReportIncident>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView address;
        TextView status;
        TextView date;
        TextView comment;

        public MyviewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.incID);
            status = (TextView) itemView.findViewById(R.id.status);
            date = (TextView) itemView.findViewById(R.id.date);
            address = (TextView) itemView.findViewById(R.id.incident_address);
            comment = (TextView) itemView.findViewById(R.id.comment);
        }
    }
}
