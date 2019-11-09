package com.spacebinary.swipeyt.ui.downloads;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.spacebinary.swipeyt.R;
import com.spacebinary.swipeyt.interfaces.DownloadServiceInterface;
import com.tonyodev.fetch2.Download;

import java.util.ArrayList;

public class DownloadFragment extends Fragment {

    private DownloadServiceInterface downloadServiceInterface;
    private ListView downloadListView;
    private TextView downloadsTextView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            downloadServiceInterface = (DownloadServiceInterface) context;
        } catch (ClassCastException e) {
            // Error, class doesn't implement the interface
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        downloadServiceInterface = null;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_download, container, false);
        Log.d("FRAGMENT WOLOLO", "Created Download Fragment");
        downloadListView = root.findViewById(R.id.download_list);
        downloadsTextView = root.findViewById(R.id.no_downloads);
        if (downloadServiceInterface.getDownloadsListAdapter().getViewTypeCount() < 1) {
            downloadsTextView.setVisibility(View.VISIBLE);
            downloadListView.setVisibility(View.INVISIBLE);
        } else {
            downloadsTextView.setVisibility(View.INVISIBLE);
            downloadListView.setVisibility(View.VISIBLE);
            downloadListView.setAdapter(downloadServiceInterface.getDownloadsListAdapter());
        }
        return root;
    }

}