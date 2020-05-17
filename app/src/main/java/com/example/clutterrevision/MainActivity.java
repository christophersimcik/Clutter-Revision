package com.example.clutterrevision;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.provider.FontRequest;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;
import android.view.MenuItem;


import com.google.android.material.appbar.AppBarLayout;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    ViewModelActivity viewModelActivity;
    PermissionsHelper permissionsHelper = new PermissionsHelper();
    Observer observerDates;
    AppBarLayout appBarLayout;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JodaTimeAndroid.init(this);
        setContentView(R.layout.activity_main);
        viewModelActivity = ViewModelProviders.of(this).get(ViewModelActivity.class);
        viewModelActivity.setFragmentManager(getSupportFragmentManager());
        viewModelActivity.getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(fragmentManager.findFragmentById(R.id.fragment_main) != null){
                    changeToolbarColor(fragmentManager.findFragmentById(R.id.fragment_main),toolbar);
                };
                viewModelActivity.dayFragmentManager();

            }
        });
        initObserver();
        // view model activity dates liver data observe
        viewModelActivity.datesLiveData.observe(this, observerDates);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setOutlineProvider(null);
        setSupportActionBar(toolbar);
        FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);

        EmojiCompat.Config config = new FontRequestEmojiCompatConfig(this, fontRequest);
        EmojiCompat.init(config);
        permissionsHelper.checkPermissions(this);
        viewModelActivity.deleteAllEmptyDates();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.calendar_settings:
                FragmentCalendar fragmentCalendar;
                if (fragmentManager.findFragmentByTag("calendar") != null) {
                    fragmentCalendar = (FragmentCalendar) fragmentManager.findFragmentByTag("calendar");
                } else {
                    fragmentCalendar = new FragmentCalendar();
                }
                fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentCalendar, "calendar")
                        .addToBackStack("calendar")
                        .commit();
                return true;

            case R.id.search_settings:
                allSearch();
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
        String tag = getResources().getString(R.string.note);
        if (fragmentManager.findFragmentById(R.id.fragment_main) instanceof FragmentList) {
            FragmentList fragmentList = (FragmentList) fragmentManager.findFragmentById(R.id.fragment_main);
            if (fragmentList.newList) {
                viewModelActivity.setCurrentDay(DayHelper.getInstance().getDateAsString());
                viewModelActivity.setPosition(viewModelActivity.datesLiveData.getValue().size() - 1);
            }
        }
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStackImmediate();
            return;
        }else{
            if(!(fragmentManager.findFragmentById(R.id.fragment_main) instanceof FragmentNotes )){
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main,new FragmentNotes(),tag)
                        .addToBackStack(tag)
                        .commit();
            }
        }
          return;
        }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragmentManager.findFragmentById(R.id.fragment_main) != null) {
            String key = fragmentManager.findFragmentById(R.id.fragment_main).getTag();
            outState.putString("key", "book");
            fragmentManager.putFragment(outState, key, fragmentManager.findFragmentById(R.id.fragment_main));
        }
    }

    public void initObserver() {
        observerDates = new Observer<List<PojoDay>>() {
            @Override
            public void onChanged(List<PojoDay> pojoDays) {
                viewModelActivity.inititiateFragments();
                viewModelActivity.setPosition(pojoDays.size() - 1);
            }
        };
    }

    private void allSearch() {
        FragmentSearch fragmentSearch;
        Fragment fragment;
        Bundle bundle = new Bundle();
        fragment = fragmentManager.findFragmentByTag("search");
        if (fragment instanceof FragmentSearch) {
            fragmentSearch = (FragmentSearch) fragment;
        } else {
            fragmentSearch = new FragmentSearch();
        }
        bundle.putInt(Constants.SEARCH_TYPE, Constants.SEARCH_ALL);
        fragmentSearch.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_main, fragmentSearch, "search")
                .addToBackStack("search")
                .commit();
    }

    private void changeToolbarColor(Fragment fragment, Toolbar toolbar){
        switch(fragment.getTag()){
            case Constants.note:
                toolbar.setBackgroundColor(getResources().getColor(R.color.new_red,null));
                break;
            case Constants.term:
                toolbar.setBackgroundColor(getResources().getColor(R.color.new_blue,null));
                break;
            case Constants.audio:
                toolbar.setBackgroundColor(getResources().getColor(R.color.new_yellow,null));
                break;
            case Constants.list:
                toolbar.setBackgroundColor(getResources().getColor(R.color.new_green,null));
                break;
            default:
                toolbar.setBackgroundColor(getResources().getColor(R.color.default_text,null));
         }
        }
    }



