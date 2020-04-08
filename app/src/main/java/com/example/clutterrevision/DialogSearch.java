package com.example.clutterrevision;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.emoji.widget.EmojiEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class DialogSearch extends Dialog implements View.OnClickListener {
    EmojiEditText emojiEditText;
    LinearLayout linearLayout;
    Activity activity;
    Button search;
    String defaultTitleText, defaultKeywordText;
    FragmentManager fragmentManager;
    FragmentSearch fragmentSearch;
    Bundle bundle = new Bundle();
    String query;

    public DialogSearch(@NonNull Context context) {
        super(context);
        activity = (Activity) context;
    }

    public DialogSearch(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        activity = (Activity) context;
    }

    protected DialogSearch(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        defaultTitleText = activity.getResources().getString(R.string.search_keywords_text);
        defaultKeywordText = activity.getResources().getString(R.string.search_title_text);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_dialog);
        linearLayout = findViewById(R.id.dialog_search_layout);
        search = findViewById(R.id.dialog_search_button);
        search.setOnClickListener(this);
        emojiEditText = findViewById(R.id.dialog_search_input);
        emojiEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                System.out.println(" b = " + b);
                if(b){
                    if(emojiEditText.getText().toString().equals(defaultKeywordText) || emojiEditText.getText().toString().equals(defaultTitleText)  ){
                        emojiEditText.setText("");
                    }
                }
            }
        });
        formatViews();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        search.setBackground(activity.getResources().getDrawable(R.drawable.button_inactive,null));
        search.setTextColor(activity.getResources().getColor(R.color.light_gray,null));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                search.setBackground(activity.getResources().getDrawable(R.drawable.button_active,null));
                search.setTextColor(activity.getResources().getColor(R.color.default_text,null));
                query = emojiEditText.getText().toString();
                bundle.putString(Constants.SEARCH_QUERY,query);
                if(fragmentIsSearch()){
                    System.out.println("*** fragment is search");
                    fragmentManager.beginTransaction()
                            .detach(fragmentSearch)
                            .attach(fragmentSearch)
                            .addToBackStack("search")
                            .commit();

                }else {
                    System.out.println("*** fragment is not search");
                    launchFragment();
                }
                emojiEditText.setText("");
                emojiEditText.clearFocus();
                dismiss();

            }
        },250);
    }


    private Point getDimensions(){
        Point dimensions = new Point();
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dimensions.x = metrics.widthPixels;
        dimensions.y = metrics.heightPixels;
        return dimensions;
    }

    private void formatViews(){

        Point dimensions = getDimensions();
        int widthLayout = (int)(dimensions.x * .90);
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.width = widthLayout;
        linearLayout.setLayoutParams(layoutParams);
    }

    public String getInput(){
        return emojiEditText.getText().toString();
    }

    public void setText(String string){
        emojiEditText.setText(string);
    }

    public void initFragment(FragmentManager fragmentManager, int type) {
        this.fragmentManager = fragmentManager;
        Fragment fragment = fragmentManager.findFragmentByTag("search");
        if (fragment instanceof FragmentSearch) {
            fragmentSearch = (FragmentSearch) fragment;
        } else {
            fragmentSearch = new FragmentSearch();
        }
        switch (type) {
            case Constants.SEARCH_ALL:
                bundle.putInt(Constants.SEARCH_TYPE, Constants.SEARCH_ALL);
                break;
            case Constants.SEARCH_TITLE:
                bundle.putInt(Constants.SEARCH_TYPE, Constants.SEARCH_TITLE);
                break;
            case Constants.SEARCH_KEYWORD:
                bundle.putInt(Constants.SEARCH_TYPE, Constants.SEARCH_KEYWORD);
                break;
        }
        fragmentSearch.setArguments(bundle);
    }

    public void launchFragment() {
        fragmentManager.beginTransaction().replace(R.id.fragment_main, fragmentSearch, "search").addToBackStack("search").commit();
    }

    private Boolean fragmentIsSearch() {
        if (fragmentManager.findFragmentById(R.id.fragment_main) instanceof FragmentSearch) {
            return true;
        } else {
            return false;
        }
    }

}
