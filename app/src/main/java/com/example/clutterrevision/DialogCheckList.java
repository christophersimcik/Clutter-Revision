package com.example.clutterrevision;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.emoji.widget.EmojiEditText;
import androidx.fragment.app.DialogFragment;

public class DialogCheckList extends DialogFragment {
    ChecklistDialogListener listener;
    EmojiEditText emojiEditText;
    public DialogCheckList(){}

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState){
      AlertDialog dialog;
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      LayoutInflater layoutInflater = getActivity().getLayoutInflater();
      View dialogLayout = layoutInflater.inflate(R.layout.alert_dialog_checklist, null);
      builder.setView(dialogLayout);
      dialog = builder.create();

      // handle sizes
      initViews(dialogLayout);

      return dialog;
  }

    @Override
  public void onAttach(Context context) {
      super.onAttach(context);
      try {
          listener = (ChecklistDialogListener) getTargetFragment();
      } catch (ClassCastException c) {
          throw new ClassCastException(getActivity().toString() + " hasn't implemented interface");
      }
  }

    public interface ChecklistDialogListener {
        void onSubmit(Dialog dialog);
    }

    private Point getDimensions(){
        Point dimensions = new Point();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        dimensions.x = metrics.widthPixels;
        dimensions.y = metrics.heightPixels;
        return dimensions;
    }

    private void initViews(View view){
        Point dimensions = getDimensions();
        final Button submit;
        LinearLayout linearLayout;
        int widthLayout = (int)(dimensions.x * .90);
        int heightLayout = (int)(dimensions.y *.20);
        linearLayout = view.findViewById(R.id.alert_layout);
        emojiEditText = view.findViewById(R.id.alert_input);
        submit = view.findViewById(R.id.alert_input_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setTextColor(getResources().getColor(R.color.light_gray,null));
                submit.setBackground(getResources().getDrawable(R.drawable.button_inactive,null));
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSubmit(getDialog());
                    }
                },250);

            }
        });
        linearLayout.setLayoutParams( new LinearLayout.LayoutParams(widthLayout,heightLayout));
    }

    public String getInput(){
        return emojiEditText.getText().toString();
    }
}
