package com.spacebinary.swipeyt.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spacebinary.swipeyt.R;
import com.spacebinary.swipeyt.interfaces.DownloadServiceInterface;

public class HomeFragment extends Fragment {

    private DownloadServiceInterface downloadServiceInterface;
    private HomeInterface homeInterface;
    private WebView YTWebView;
    private HomeViewModel homeViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            downloadServiceInterface = (DownloadServiceInterface) context;
            homeInterface = (HomeInterface) context;
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
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fab = root.findViewById(R.id.download_fab);
        fab.setOnClickListener((View v) -> {
            homeInterface.setWebView(YTWebView);
            downloadServiceInterface.extractString(YTWebView.getUrl(), YTWebView.getTitle());
        });


        YTWebView = root.findViewById(R.id.YTWebView);
        YTWebView.getSettings().setJavaScriptEnabled(true);
        YTWebView.loadUrl("https://m.youtube.com");


        return root;
    }

    public interface HomeInterface {
        void setWebView(WebView v);
    }

}