package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Arrays;
import java.util.HashMap;

import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.R;


/**
 * This is activity that used for days dialog picker
 *  int value: day string.
 * // -1: Weekdays
 * // 0 : Weekend
 * // 1 : Mon
 * // 2 : Tue
 * // 3 : Wed
 * // 4 : Thu
 * // 5 : Fri
 * // 6 : Sat
 * // 7 : Sun
 * **/

public class DayPickerDialog extends AppCompatDialogFragment {
    private CheckBox weekdaysCheck;
    private CheckBox weekendCheck;
    private CheckBox day1Check;
    private CheckBox day2Check;
    private CheckBox day3Check;
    private CheckBox day4Check;
    private CheckBox day5Check;
    private CheckBox day6Check;
    private CheckBox day7Check;
    private DaysPickerDialogListener listener;
    private Object[] checkedDays;


    public DayPickerDialog(Object[] intDaysPicked) {
        if (intDaysPicked != null) {
            checkedDays = intDaysPicked;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.days_picker_dialog,null);
        builder.setView(view)
                .setTitle(getResources().getString(R.string.pick_day_alarm))
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        processValue();
                    }
        });

        weekdaysCheck = view.findViewById(R.id.weekdaysCheck);
        weekendCheck = view.findViewById(R.id.weekendCheck);
        day1Check = view.findViewById(R.id.day1);
        day2Check = view.findViewById(R.id.day2);
        day3Check = view.findViewById(R.id.day3);
        day4Check = view.findViewById(R.id.day4);
        day5Check = view.findViewById(R.id.day5);
        day6Check = view.findViewById(R.id.day6);
        day7Check = view.findViewById(R.id.day7);

        fillValue();

        return builder.create();
    }

    private void fillValue() {
        if (checkedDays != null) {
            if (Arrays.asList(checkedDays).contains(-1)) {
                weekdaysCheck.performClick();
            } else {
                if (Arrays.asList(checkedDays).contains(1)) day1Check.performClick();
                if (Arrays.asList(checkedDays).contains(2)) day2Check.performClick();
                if (Arrays.asList(checkedDays).contains(3)) day3Check.performClick();
                if (Arrays.asList(checkedDays).contains(4)) day4Check.performClick();
                if (Arrays.asList(checkedDays).contains(5)) day5Check.performClick();
            }

            if (Arrays.asList(checkedDays).contains(0)) {
                weekendCheck.performClick();
            } else {
                if (Arrays.asList(checkedDays).contains(6)) day6Check.performClick();
                if (Arrays.asList(checkedDays).contains(7)) day7Check.performClick();
            }
        }
    }


    /**
     * This method will return
     * **/
    private void processValue() {
        HashMap<Integer, String> daysPicked = new HashMap<Integer, String>();
        if (weekdaysCheck.isChecked()) {
            daysPicked.put(-1, getResources().getString(R.string.weekday));
        } else {
            if (day1Check.isChecked()) daysPicked.put(1,getResources().getString(R.string.mon_short));
            if (day2Check.isChecked()) daysPicked.put(2,getResources().getString(R.string.tue_short));
            if (day3Check.isChecked()) daysPicked.put(3,getResources().getString(R.string.wed_short));
            if (day4Check.isChecked()) daysPicked.put(4,getResources().getString(R.string.thu_short));
            if (day5Check.isChecked()) daysPicked.put(5,getResources().getString(R.string.fri_short));
        }

        if (weekendCheck.isChecked()) {
            daysPicked.put(0,getResources().getString(R.string.weekend));
        } else {
            if (day6Check.isChecked()) daysPicked.put(6,getResources().getString(R.string.sat_short));
            if (day7Check.isChecked()) daysPicked.put(7,getResources().getString(R.string.sun_short));
        }
        listener.getDaysPickedValue(daysPicked);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DaysPickerDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DaysPickerDialogListener");
        }
    }

    public interface DaysPickerDialogListener {
         void getDaysPickedValue(HashMap<Integer, String> intOfDays);
    }

    public void checkValueCheckBox(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.weekdaysCheck:
                if (checked) {
                    day1Check.setChecked(true);
                    day2Check.setChecked(true);
                    day3Check.setChecked(true);
                    day4Check.setChecked(true);
                    day5Check.setChecked(true);
                } else {
                    day1Check.setChecked(false);
                    day2Check.setChecked(false);
                    day3Check.setChecked(false);
                    day4Check.setChecked(false);
                    day5Check.setChecked(false);
                }
                break;
            case R.id.weekendCheck:
                if (checked) {
                    day6Check.setChecked(true);
                    day7Check.setChecked(true);
                } else {
                    day6Check.setChecked(false);
                    day7Check.setChecked(false);
                }
                break;
            default:
                if (
                    day1Check.isChecked() &&
                    day2Check.isChecked() &&
                    day3Check.isChecked() &&
                    day4Check.isChecked() &&
                    day5Check.isChecked()
                ) {
                    weekdaysCheck.setChecked(true);
                } else {
                    weekdaysCheck.setChecked(false);
                }
                if (day6Check.isChecked() && day7Check.isChecked()) {
                    weekendCheck.setChecked(true);
                } else {
                    weekendCheck.setChecked(false);
                }
        }
    }
}
