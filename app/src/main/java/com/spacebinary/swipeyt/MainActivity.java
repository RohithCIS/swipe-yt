package com.spacebinary.swipeyt;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spacebinary.swipeyt.adapters.DownloadsListAdapter;
import com.spacebinary.swipeyt.interfaces.DownloadServiceInterface;
import com.spacebinary.swipeyt.services.DownloadService;
import com.spacebinary.swipeyt.ui.home.HomeFragment;
import com.tonyodev.fetch2.Fetch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements DownloadServiceInterface, HomeFragment.HomeInterface {

    protected DownloadService downloadService;
    private WebView YTWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        Intent serviceIntent = new Intent(getBaseContext(), DownloadService.class);
        bindService(serviceIntent, downloadServiceConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection downloadServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            downloadService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            DownloadService.LocalBinder mLocalBinder = (DownloadService.LocalBinder)service;
            downloadService = mLocalBinder.getServerInstance();
        }
    };

    @Override
    public void extractString(String YTUrl, String YTTitle) {
        if(isStoragePermissionGranted()) {
            downloadService.extractString(YTUrl, YTTitle);
        }
    }

    @Override
    public Fetch getFetchInstance() {
        return downloadService.getFetchInstance();
    }

    @Override
    public DownloadsListAdapter getDownloadsListAdapter() {
        return downloadService.getDownloadsListAdapter();
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSSIONS","Permission is granted");
                return true;
            } else {

                Log.v("PERMISSSIONS","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERMISSSIONS","Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("PERMISSSIONS","Permission: "+permissions[0]+ "was "+grantResults[0]);
            downloadService.extractString(YTWebView.getUrl(), YTWebView.getTitle());
        }
    }

    public void setWebView(WebView webView) {
        YTWebView = webView;
    }

//    @Override
//    public boolean onKey(View v, int keyCode, KeyEvent event) {
//        if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && YTWebView.canGoBack()) {
//            YTWebView.goBack();
//            return true;
//        }
//        return false;
//    }

}
