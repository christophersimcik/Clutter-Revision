package com.example.clutterrevision;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

public class DialogDelete extends DialogFragment {
    private DeleteDialogListener listener;
    private int position;

    public DialogDelete() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View dialogLayout = layoutInflater.inflate(R.layout.dialog_delete, null);
        builder.setView(dialogLayout);
        dialog = builder.create();
        initViews(dialogLayout);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        listener.onCanceled(getDialog(),position);
        super.onCancel(dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteDialogListener) getTargetFragment();
        } catch (ClassCastException c) {
            throw new ClassCastException(getActivity().toString() + " hasn't implemented interface");
        }
    }

    public interface DeleteDialogListener {
        void onDismiss(Dialog dialog, int position);
        void onCanceled(Dialog dialog, int position);
        void onConfirm(Dialog dialog, int position);
    }

    private void initViews(View view) {
        Button confirm = view.findViewById(R.id.confirm);
        Button dismiss = view.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDismiss(getDialog(),position);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onConfirm(getDialog(),position);
            }
        });
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
