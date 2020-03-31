package com.example.clutterrevision;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDay extends RecyclerView.Adapter<AdapterDay.ViewHolder> {
    private Context context;
    public List<PojoDay> data = new ArrayList();
    private ViewModelDay viewModelDay;
    private FragmentManager fragmentManager;

    public AdapterDay(Context context, ViewModelDay viewModelDay ) {
        this.context = context;
        this.viewModelDay = viewModelDay;
    }

    @NonNull
    @Override
    public AdapterDay.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_date_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDay.ViewHolder holder, int position) {
        PojoDay pojoDay = data.get(position);
        String date = pojoDay.getDayOfWeek() + ", " +
                pojoDay.getMonthOfYear() + " " +
                pojoDay.getDayOfMonth() + " " +
                pojoDay.getYear();
        holder.dateTextView.setText(date);
        setIndicatorColors(holder.backIndicator,holder.frontIndicator,position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        ImageView backIndicator, frontIndicator;

        private ViewHolder(@NonNull final View itemView) {
            super(itemView);
            backIndicator = itemView.findViewById(R.id.back_indicator);
            frontIndicator = itemView.findViewById(R.id.front_indicator);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            dateTextView.setTextSize(24);
        }
    }
        void setData(List<PojoDay> days) {
        if(days != null) {
            final DiffCallbackDay diffCallback = new DiffCallbackDay(this.data, days);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
            this.data.clear();
            this.data.addAll(days);
            diffResult.dispatchUpdatesTo(this);
        }
        }

    public void setIndicatorColors(ImageView back, ImageView front, int position){
        int lightGray = context.getResources().getColor(R.color.light_gray,null);
        int darkGray = context.getResources().getColor(R.color.default_text,null);
        if(position <= 0){
            back.setColorFilter(lightGray);
        }else{
            back.setColorFilter(darkGray);
        }
        if(position >= data.size()-1){
            front.setColorFilter(lightGray);
        }else{
            front.setColorFilter(darkGray);
        }
    }
    }
