package com.example.clutterrevision;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterCalendar extends RecyclerView.Adapter<AdapterCalendar.ViewHolder> {
    List<PojoDayOfMonth> data;
    Context context;
    DayHelper dayHelper = DayHelper.getInstance();

    public AdapterCalendar(Context context){
        this.context = context;
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
        if(pojoDayOfMonth != null) {
            holder.textView.setText(String.valueOf(data.get(position).dayOfMonth));
            if(pojoDayOfMonth.dayAsString.equals(dayHelper.getDateAsString())){
                holder.border.setBackground(context.getResources().getDrawable(R.drawable.basic_hilite,null));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RelativeLayout relativeLayout;
        RelativeLayout border;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.calendar_date_text_view);
            relativeLayout = itemView.findViewById(R.id.calendar_item_container);
            border = itemView.findViewById(R.id.calendar_item_border);
            int size = findDimensions();
            relativeLayout.getLayoutParams().width = size;
            relativeLayout.getLayoutParams().height = size;
        }
    }

    public void setData(List<PojoDayOfMonth> daysOfMonth){
        data = daysOfMonth;
        notifyDataSetChanged();
    }

    private int findDimensions(){
        Display d = findActivity().getWindowManager().getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int w = p.x;
        int h = p.y;
        int mySize;
        int size = Math.min(w,h);
        mySize = (size/7)- 20;
        return mySize;
    }

    private Activity findActivity(){
        Context c = context;
        while(c instanceof ContextWrapper){
            if( c instanceof Activity){
                return (Activity) c;
            }
            c = ((ContextWrapper)c).getBaseContext();
        }
        return null;
    }
}
