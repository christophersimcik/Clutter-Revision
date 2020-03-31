package com.example.clutterrevision;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    ViewModelActivity viewModelActivity;
    PermissionsHelper permissionsHelper = new PermissionsHelper();
    Observer observerDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JodaTimeAndroid.init(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        viewModelActivity = ViewModelProviders.of(this).get(ViewModelActivity.class);
        viewModelActivity.setFragmentManager(getSupportFragmentManager());
        viewModelActivity.getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                viewModelActivity.dayFragmentManager();
            }
        });
        initObserver();
        // view model activity dates liver data observe
        viewModelActivity.datesLiveData.observe(this,observerDates);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(savedInstanceState != null){
            viewModelActivity.getSavedFragment(savedInstanceState);
        }

        FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);

        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest);
        EmojiCompat.init(config);
        permissionsHelper.checkPermissions(this);
        viewModelActivity.deleteAllEmptyDates();
        System.out.println(" MAIN ACTIVITY ON_CREATE CALLED");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings:
                return true;
            case R.id.calendar_settings:
                FragmentCalendar fragmentCalendar;
                if(fragmentManager.findFragmentByTag("calendar")!= null){
                    fragmentCalendar = (FragmentCalendar) fragmentManager.findFragmentByTag("calendar");
                }else{
                    fragmentCalendar = new FragmentCalendar();
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentCalendar, "calendar")
                        .addToBackStack("calendar")
                        .commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        permissionsHelper.onPermissionRequestResults(requestCode, grantResults);
    }

    @Override
    public void onBackPressed() {
        if(fragmentManager.findFragmentById(R.id.fragment_main) instanceof FragmentList){
            FragmentList fragmentList = (FragmentList) fragmentManager.findFragmentById(R.id.fragment_main);
            if(fragmentList.newList) {
                viewModelActivity.setCurrentDay(viewModelActivity.dayHelper.getDateAsString());
                viewModelActivity.setPosition(viewModelActivity.datesLiveData.getValue().size() - 1);
            }
        }
        if(fragmentManager.getBackStackEntryCount()>1){
            fragmentManager.popBackStackImmediate();
            return;
        }
        System.out.println("task id = " + getTaskId());
       // super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragmentManager.findFragmentById(R.id.fragment_main) != null) {
            String key = fragmentManager.findFragmentById(R.id.fragment_main).getTag();
            outState.putString("key", key);
            fragmentManager.putFragment(outState, key, fragmentManager.findFragmentById(R.id.fragment_main));
        }
    }

    public void initObserver(){
        observerDates = new Observer<List<PojoDay>>() {
            @Override
            public void onChanged(List<PojoDay> pojoDays) {
                System.out.println("*** main_activity = " + pojoDays.size());
                viewModelActivity.inititiateFragments();
                viewModelActivity.setPosition(pojoDays.size()-1);
            }
        };

    }

    }

