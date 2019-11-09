package com.spacebinary.swipeyt.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.WebView;
import android.widget.Toast;

import com.spacebinary.swipeyt.adapters.DownloadsListAdapter;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2.Status;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class DownloadService extends Service {

    IBinder mBinder = new LocalBinder();

    public String YTDownloadURL;
    public String YTDownloadName;
    public Fetch fetch;
    public ArrayList<Download> downloadsList = new ArrayList<>();
    public DownloadsListAdapter downloadsListAdapter = new DownloadsListAdapter(this, downloadsList);

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(this)
                .setDownloadConcurrentLimit(8)
                .build();

        fetch = Fetch.Impl.getInstance(fetchConfiguration);
        fetch.addListener(new FetchListener() {
            @Override
            public void onAdded(@NotNull Download download) {
                
            }

            @Override
            public void onQueued(@NotNull Download download, boolean b) {

            }

            @Override
            public void onWaitingNetwork(@NotNull Download download) {

            }

            @Override
            public void onCompleted(@NotNull Download download) {
                Toast.makeText(DownloadService.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NotNull Download download, @NotNull Error error, @Nullable Throwable throwable) {

            }

            @Override
            public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

            }

            @Override
            public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {
                Func<List<Download>> downloads = new Func<List<Download>>() {
                    @Override
                    public void call(@NotNull List<Download> result) {
                        downloadsList = new ArrayList<>();
                        downloadsList.addAll(result);
//                        downloadsListAdapter.notifyDataSetChanged();
                        downloadsListAdapter = new DownloadsListAdapter(DownloadService.this, downloadsList);
                    }
                };
                fetch.getDownloadsWithStatus(Status.DOWNLOADING, downloads);
            }

            @Override
            public void onProgress(@NotNull Download download, long l, long l1) {
                for (int position = 0; position < downloadsList.size(); position++) {
                    if (downloadsList.get(position).getId() == download.getId()) {
                        downloadsList.set(position, download);
                    }
                }
                downloadsListAdapter = new DownloadsListAdapter(DownloadService.this, downloadsList);
            }

            @Override
            public void onPaused(@NotNull Download download) {

            }

            @Override
            public void onResumed(@NotNull Download download) {

            }

            @Override
            public void onCancelled(@NotNull Download download) {

            }

            @Override
            public void onRemoved(@NotNull Download download) {

            }

            @Override
            public void onDeleted(@NotNull Download download) {

            }
        });
        Log.d("FETCH", "Started Fetch Instance");
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public DownloadService getServerInstance() {
            return DownloadService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void extractString(String YTUrl, String YTTitle) {
        new YouTubeExtractor(DownloadService.this) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    int itag = 22;
                    YTDownloadURL = ytFiles.get(itag).getUrl();
                    Log.d("YTSTRING", YTDownloadURL);
                    startDownload(YTTitle);
                }
            }
        }.extract(YTUrl, true, true);
    }

    public void startDownload(String YTTitle) {
        String file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + YTTitle + ".mp4";
        final Request request = new Request(YTDownloadURL, file);

        request.setPriority(Priority.HIGH);
        request.setNetworkType(NetworkType.ALL);



        fetch.enqueue(request, updatedRequest -> {
            Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(this, "Unable to Download!", Toast.LENGTH_SHORT).show();
        });
    }

    public Fetch getFetchInstance() {
        return fetch;
    }

    public DownloadsListAdapter getDownloadsListAdapter() {
        return downloadsListAdapter;
    }
}
