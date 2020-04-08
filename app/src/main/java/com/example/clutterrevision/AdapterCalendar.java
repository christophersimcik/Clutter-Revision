package com.example.clutterrevision;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterCalendar extends RecyclerView.Adapter<AdapterCalendar.ViewHolder> implements ObserverDates {
    List<PojoDayOfMonth> data;
    Context context;
    DayHelper dayHelper = DayHelper.getInstance();
    private List<Integer> positions = new ArrayList();
    FragmentManager fragmentManager;
    private ViewModelCalendar viewModelCalendar;
    ViewModelActivity viewModelActivity;

    public AdapterCalendar(Context context, FragmentManager fragmentManager, ViewModelCalendar viewModelCalendar, ViewModelActivity viewModelActivity) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.viewModelActivity = viewModelActivity;
        this.viewModelCalendar = viewModelCalendar;
    }

    @NonNull
    @Override
    public AdapterCalendar.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_calendar_item, parent, false);
        return new AdapterCalendar.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCalendar.ViewHolder holder, int position) {
        PojoDayOfMonth pojoDayOfMonth = data.get(position);
        if (positions.contains(position)) {
            holder.hasNotes(pojoDayOfMonth.getTypesOfNotes());
            holder.dayId = data.get(position).dayAsString;
        }

        if (pojoDayOfMonth != null) {
            holder.textView.setText(String.valueOf(data.get(position).dayOfMonth));
            if (pojoDayOfMonth.dayAsString.equals(dayHelper.getDateAsString())) {
                holder.setIsToday(true);
                holder.border.setBackground(context.getResources().getDrawable(R.drawable.basic_hilite, null));
            }
        }
    }

    @Override
    public void onEmptyDateDeleted() {

    }

    @Override
    public void onTodaysDateChecked(PojoDay pojoDay) {
        if (pojoDay != null && pojoDay.getNumberOfNotes() > 0) {
            int[] types = new int[5];
            types[0] = pojoDay.getNumberOfNoteNotes();
            types[1] = pojoDay.getNumberOfAudioNotes();
            types[2] = pojoDay.getNumberOfReferNotes();
            types[3] = pojoDay.getNumberOfBookNotes();
            types[4] = pojoDay.getNumberOfListNotes();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i) != null && data.get(i).dayAsString.equals(pojoDay.getDay_id())) {
                    positions.add(i);
                    data.get(i).setTypesOfNotes(types);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDateInserted() {

    }

    @Override
    public void onDatesRetrieved(List<PojoDay> pojoDays) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RelativeLayout relativeLayout;
        RelativeLayout border;
        TypeIndicatorView typeIndicatorView;
        String dayId;
        Drawable background = context.getResources().getDrawable(R.drawable.border_light, null);
        private Boolean isToday = false;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.calendar_date_text_view);
            relativeLayout = itemView.findViewById(R.id.calendar_item_container);
            border = itemView.findViewById(R.id.calendar_item_border);
            typeIndicatorView = itemView.findViewById(R.id.calendar_indicator_view);
            int size = findDimensions();
            relativeLayout.getLayoutParams().width = size;
            relativeLayout.getLayoutParams().height = size;
            itemView.setOnTouchListener(new CustomOnTouchListener(this));

        }

        private void clicked() {
            border.setBackground(context.getResources().getDrawable(R.drawable.basic_press, null));
            typeIndicatorView.setIsCircular(true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    border.setBackground(background);
                    if (!isToday) {
                        typeIndicatorView.setIsCircular(false);
                    }
                    goToDate();

                }
            }, 250);
            System.out.println("clicked");
        }

        private void goToDate() {
            if (dayId != null) {
                viewModelActivity.jumpToThisDay(dayId);
                //fragmentManager.popBackStack();
                FragmentNotes fragmentNotes;
                Fragment fragment = fragmentManager.findFragmentByTag("notes");
                if( fragment instanceof FragmentNotes){
                    fragmentNotes = (FragmentNotes) fragment;
                }else{
                    fragmentNotes = new FragmentNotes();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main,fragmentNotes,"notes")
                        .addToBackStack("notes")
                        .commit();
            } else {
                Toast.makeText(context, "No Notes Available", Toast.LENGTH_SHORT).show();
            }

        }


        public void hasNotes(int[] types) {
            typeIndicatorView.setTypes(types[0], types[1], types[2], types[3], types[4]);
        }

        public void setIsToday(Boolean isToday) {
            this.isToday = isToday;
            typeIndicatorView.setIsCircular(isToday);
        }
    }


    public void setData(List<PojoDayOfMonth> daysOfMonth) {
        data = daysOfMonth;
        viewModelCalendar.check();
        notifyDataSetChanged();
    }

    private int findDimensions() {
        Display d = findActivity().getWindowManager().getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int w = p.x;
        int h = p.y;
        int mySize;
        int size = Math.min(w, h);
        mySize = (size / 7) - 20;
        return mySize;
    }

    private Activity findActivity() {
        Context c = context;
        while (c instanceof ContextWrapper) {
            if (c instanceof Activity) {
                return (Activity) c;
            }
            c = ((ContextWrapper) c).getBaseContext();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

     void resetList() {
        positions.clear();
    }

    private class CustomOnTouchListener implements View.OnTouchListener {
        ViewHolder viewHolder;
        Boolean pressed;

        private CustomOnTouchListener(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressed = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!pressed) {
                                clicked();
                            }
                        }
                    }, 125);
                    break;
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
            return true;
        }

        private void clicked() {
            viewHolder.clicked();
        }
    }

}
