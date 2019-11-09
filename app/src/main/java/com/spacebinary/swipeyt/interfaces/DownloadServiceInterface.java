package com.spacebinary.swipeyt.interfaces;

import android.webkit.WebView;

import com.spacebinary.swipeyt.adapters.DownloadsListAdapter;
import com.tonyodev.fetch2.Fetch;

public interface DownloadServiceInterface {
    void extractString(String YTString, String YTTitle);
    Fetch getFetchInstance();
    DownloadsListAdapter getDownloadsListAdapter();
}
