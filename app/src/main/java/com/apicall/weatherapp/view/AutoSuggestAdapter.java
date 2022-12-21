package com.apicall.weatherapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apicall.weatherapp.R;
import com.apicall.weatherapp.model.CityAutocom;

import java.util.ArrayList;
import java.util.List;

public class AutoSuggestAdapter extends ArrayAdapter<CityAutocom> implements Filterable {
    private List<CityAutocom> mlistData;
    private Context context;

    public AutoSuggestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.mlistData = new ArrayList<>();
        this.context = context;
    }

    public void setData(List<CityAutocom> list) {
        mlistData.clear();
        mlistData.addAll(list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.single_autosuggest_item,parent,false);

        CityAutocom cityAutocom = mlistData.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.single_auto_item);

        String cityInfo = mlistData.get(position).getName() + ", " + mlistData.get(position).getRegion() + ", "+ mlistData.get(position).getCountry();
        name.setText(cityInfo);

        return listItem;
    }

    @Override
    public int getCount() {
        return mlistData.size();
    }

    @Nullable
    @Override
    public CityAutocom getItem(int position) {
        return mlistData.get(position);
    }

    public CityAutocom getObject(int position) {
        return mlistData.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = mlistData;
                    filterResults.count = mlistData.size();
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return dataFilter;
    }
}
