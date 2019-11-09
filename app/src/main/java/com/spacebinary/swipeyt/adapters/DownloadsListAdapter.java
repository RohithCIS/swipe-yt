package com.spacebinary.swipeyt.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.spacebinary.swipeyt.R;
import com.tonyodev.fetch2.Download;

import java.util.ArrayList;

public class DownloadsListAdapter extends BaseAdapter {

    ArrayList<Download> downloadsList;
    Context context;

    public DownloadsListAdapter(Context context, ArrayList<Download> arrayList) {
        this.downloadsList=arrayList;
        this.context=context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return downloadsList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Download dItem = downloadsList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.download_item, null);
            TextView title = convertView.findViewById(R.id.download_eta);
            ProgressBar progressBar = convertView.findViewById(R.id.progressBar);
            title.setText("ETA");
            progressBar.setProgress(dItem.getProgress());
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return downloadsList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }

    public void update(@NonNull final Download download) {
        for (int position = 0; position < downloadsList.size(); position++) {
            final Download downloadData = downloadsList.get(position);
            if (downloadData.getId() == download.getId()) {
                switch (download.getStatus()) {
                    case REMOVED:
                    case DELETED: {
                        downloadsList.remove(position);
                        notifyDataSetChanged();
                        break;
                    }
                    default: {
                        downloadsList.set(position, download);
                        notifyDataSetChanged();
                    }
                }
                return;
            }
        }
    }
}
