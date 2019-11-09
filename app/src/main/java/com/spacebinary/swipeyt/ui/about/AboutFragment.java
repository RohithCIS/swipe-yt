package com.spacebinary.swipeyt.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.spacebinary.swipeyt.R;

public class AboutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        final TextView textViewTitle = root.findViewById(R.id.text_about_title);
        final TextView textViewAbout = root.findViewById(R.id.text_about_content);

        textViewTitle.setText("Swipe YT");
        textViewAbout.setText("This app was built because the widely used TubeMate was rumored " +
                "to be malicious and I didn't want to take chances. This app requires no permission " +
                "other than access to storage for downloading videos.");

        return root;
    }
}