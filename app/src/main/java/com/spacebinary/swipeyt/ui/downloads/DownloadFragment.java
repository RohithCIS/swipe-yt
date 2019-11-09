package com.spacebinary.swipeyt.ui.downloads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.spacebinary.swipeyt.R;

public class DownloadFragment extends Fragment {

    private DownloadViewModel downloadViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        downloadViewModel =
                ViewModelProviders.of(this).get(DownloadViewModel.class);
        View root = inflater.inflate(R.layout.fragment_download, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        downloadViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}