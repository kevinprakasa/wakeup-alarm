package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDialogFragment;

import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.R;

public class SnoozableDialog extends AppCompatDialogFragment {
    private RadioGroup rg;
    private SnoozeableDialogListener listener;
    private int initialValueId;
    public SnoozableDialog(int checkedId) {
        initialValueId = checkedId;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view  = inflater.inflate(R.layout.snoozeable_dialog, null);

        builder.setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int checkedId = rg.getCheckedRadioButtonId();
                        listener.applySnoozeChoice(checkedId);
                    }
                });
        rg = view.findViewById(R.id.snoozeRadioGroup);
        view.findViewById(initialValueId).performClick();
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SnoozeableDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement SnoozeableDialogListener");
        }
    }

    public interface SnoozeableDialogListener {
        void applySnoozeChoice(int checkedId);
    }
}
