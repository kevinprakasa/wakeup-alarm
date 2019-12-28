package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.ContentProvider.AlarmContentProvider;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Dialog.DayPickerDialog;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Dialog.SnoozableDialog;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room.Alarm;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.ViewModel.AlarmViewModel;

public class CreateAlarmActivity extends AppCompatActivity implements DayPickerDialog.DaysPickerDialogListener, SnoozableDialog.SnoozeableDialogListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    AlarmManager alarmManager;
    DayPickerDialog dayPickerDialog;
    SnoozableDialog snoozableDialog;

    public static final int PICKFILE_RESULT_CODE = 1;
    public static String ALARM_ID = "create_alarm_id";
    public static String ALARM_LABEL = "create_alarm_label";
    public static String ALARM_TIME = "create_alarm_time";
    public static String ALARM_SNOOZE = "create_alarm_snooze";
    public static String ALARM_DAY = "create_alarm_day";

    private Intent intent;

    private AlarmViewModel model;

    private Button submitAlarmButton;

    private TimePicker alarmTimePicker;

    // day picker dialog
    private TextView daysPicker;
    private Object[] intDaysPicked = new Object[] {-1};

    // snoozeable picker dialog
    private TextView snoozeAbleButton;
    private int checkedSnoozeId = R.id.zeroMin;
    private int choosedSnooze = 0;

    //alarm label
    private TextView alarmLabel;
    private String filePath = null;

    // music picker
    private Button musicPicker;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // view model initiation
        model = ViewModelProviders.of(this).get(AlarmViewModel.class);

        alarmTimePicker = findViewById(R.id.alarmTimePicker);
        alarmTimePicker.setIs24HourView(true);
        alarmLabel = findViewById(R.id.alarmLabel);
        daysPicker = findViewById(R.id.daysPicker);
        snoozeAbleButton= findViewById(R.id.snoozeableButton);
        submitAlarmButton = findViewById(R.id.okButtonAlarm);
        musicPicker = findViewById(R.id.musicPicker);

        daysPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDaysDialog();
            }
        });
        snoozeAbleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSnoozeableDialog();
            }
        });
        submitAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitAlarm();
                finish();
            }
        });
        musicPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (shouldShowRequestPermissionRationale(
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            // Explain to the user why we need to read the contacts
                        }

                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                        // app-defined int constant that should be quite unique

                        return;
                    }
                }
                else {
                    Intent chooseFile = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                }

            }
        });

        // check is initiated on intent with alarm passed by
        intent = getIntent();

        if (intent.hasExtra(ALARM_TIME)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(intent.getLongExtra(ALARM_TIME,0));
            //set alarm for
            alarmTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            alarmTimePicker.setMinute(calendar.get(Calendar.MINUTE));
            alarmLabel.setText(intent.getStringExtra(ALARM_LABEL));

            choosedSnooze = intent.getIntExtra(ALARM_SNOOZE,-1);
            snoozeAbleButton.setText(convertFromSnoozeNumToSnoozeString(choosedSnooze));

            intDaysPicked = new Object[] {intent.getIntExtra(ALARM_DAY, -1)};
            daysPicker.setText(convertFromDayNumToDayString((Integer) intDaysPicked[0]));
            // make days not able to be edited again
            daysPicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CreateAlarmActivity.this, getResources().getString(R.string.sorry), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /***
    This method handles when -10...+10 minutes modify button is clicked
    ***/
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onPlusMinusMinuteClicked(View view) {
        int addition;
        switch (view.getId()) {
            case R.id.plusOne:
                addition = 1;
                break;
            case R.id.plusFive:
                addition = 5;
                break;
            case R.id.plusTen:
                addition = 10;
                break;
            case R.id.minusOne:
                addition = -1;
                break;
            case R.id.minusFive:
                addition = -5;
                break;
            case R.id.minusTen:
                addition = -10;
                break;
            default:
                addition = 0;
                break;
        }
        int calculatedMinute = addition(alarmTimePicker.getMinute(),addition);
        if (calculatedMinute > 59) {
            calculatedMinute -= 60;
            alarmTimePicker.setHour(alarmTimePicker.getHour()+1);
        } else if (calculatedMinute < 0) {
            calculatedMinute += 60;
            alarmTimePicker.setHour(alarmTimePicker.getHour()-1);
        }
        alarmTimePicker.setMinute(calculatedMinute);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent chooseFile = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                }
                return;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if ((data != null) && (data.getData() != null)){
                        Uri fileUri = data.getData();
                        filePath = fileUri.toString();
                    }
                }
                break;
        }
    }

    public void onSubmitAlarm() {
        for (int i = 0; i < intDaysPicked.length; i++ ) {
            // if weekday on array, then make alarm for mon till sun
            if ( ((Integer) intDaysPicked[i] )== -1) {
                for (int j = 1; j < 6; j++) { // iterate create an alarm from mon (1) to friday(5)
                    createAlarmByDay(j);
                }
            } else if ((Integer) intDaysPicked[i] == 0) {
                for (int j = 6; j < 8; j++) { // iterate create an alarm from sat (6) to sun (7)
                    createAlarmByDay(j);
                }
            } else {
                createAlarmByDay((Integer) intDaysPicked[i]);
            }
        }
    }

    private String convertFromSnoozeNumToSnoozeString(int snoozeNum) {
        if (snoozeNum == 0) return "OFF";
        return String.format("%d mins", snoozeNum);
    }

    private String convertFromDayNumToDayString(int dayNum) {
        switch (dayNum) {
            case 1:
                return getResources().getString(R.string.mon_short);
            case 2:
                return getResources().getString(R.string.tue_short);
            case 3:
                return getResources().getString(R.string.wed_short);
            case 4:
                return getResources().getString(R.string.thu_short);
            case 5:
                return getResources().getString(R.string.fri_short);
            case 6:
                return getResources().getString(R.string.sat_short);
            case 7:
                return getResources().getString(R.string.sun_short);
            default:
                return "";
        }
    }

    public void createAlarmByDay(int dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, convertFromDefinedDayNumToRealDayNum(dayNum));
        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);

        // Check if the Calendar time is in the past
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7); // it will tell to run to the next week
        }

        String label = String.valueOf(alarmLabel.getText());
        Alarm alarm = new Alarm(calendar.getTimeInMillis(), choosedSnooze, filePath, dayNum, label);
        long alarmId;
        // check if in edit mode alarm
        if (intent.hasExtra(ALARM_ID)) {
            alarmId = intent.getIntExtra(ALARM_ID,-1);
            alarm.setId((int) alarmId);
            model.update(alarm);
        } else {
            try {
                alarmId = model.insert(alarm);
            } catch (ExecutionException e) {
                alarmId = -1;
                e.printStackTrace();
            } catch (InterruptedException e) {
                alarmId = -1;
                e.printStackTrace();
            }
        }

        // create an intent and send along the alarm data
        Intent intent = new Intent(this, AlarmReceiverActivity.class);
        Bundle extras = new Bundle();
        extras.putLong(AlarmReceiverActivity.RECEIVER_ALARM_TIME, alarm.getTime());
        extras.putInt(AlarmReceiverActivity.RECEIVER_ALARM_SNOOZE, alarm.getSnooze());
        extras.putString(AlarmReceiverActivity.RECEIVER_ALARM_LABEL, alarm.getLabel());
        extras.putInt(AlarmReceiverActivity.RECEIVER_ALARM_DAY, alarm.getDay());
        extras.putInt(AlarmReceiverActivity.RECEIVER_ALARM_ID, alarm.getId());
        extras.putString(AlarmReceiverActivity.RECEIVER_ALARM_RINGTONE, alarm.getRingtone());
        intent.putExtras(extras);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                (int)alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am =
                (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                am.INTERVAL_DAY * 7,
                pendingIntent);


        // save to Alarm Content Provider
        ContentValues values = new ContentValues();
        values.put(AlarmContentProvider.LABEL,alarm.getLabel());
        values.put(AlarmContentProvider.SNOOZE,alarm.getSnooze());
        values.put(AlarmContentProvider.TIME, alarm.getTime());
        getContentResolver().insert(AlarmContentProvider.CONTENT_URI, values);
    }

    public void openSnoozeableDialog() {
        snoozableDialog = new SnoozableDialog(checkedSnoozeId);
        snoozableDialog.show(getSupportFragmentManager(), "snoozeable dialog");
    }

    public void openDaysDialog() {
        dayPickerDialog = new DayPickerDialog(intDaysPicked);
        dayPickerDialog.show(getSupportFragmentManager(),"day picker dialog");
    }

    public int convertFromDefinedDayNumToRealDayNum(int definedDayNum) {
        switch (definedDayNum) {
            case 1:
                return Calendar.MONDAY;
            case 2:
                return Calendar.TUESDAY;
            case 3:
                return Calendar.WEDNESDAY;
            case 4:
                return Calendar.THURSDAY;
            case 5:
                return Calendar.FRIDAY;
            case 6:
                return Calendar.SATURDAY;
            case 7:
                return Calendar.SUNDAY;
            default:
                return -1;
        }
    }

    public void onDaysClicked(View v) {
        dayPickerDialog.checkValueCheckBox(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    /**
     * This method will be called with a hashmap filled with
     * {
     *     -1: "Weekdays",
     *     0: "Weekend",
     *     1: "Mon",
     *     2: "Tue",
     *     ..
     *     ..
     * }
     * **/
    public void getDaysPickedValue(HashMap<Integer, String> intOfDays) {
        Object[] sortedIntDays = intOfDays.keySet().toArray();
        Arrays.sort(sortedIntDays);
        intDaysPicked = sortedIntDays.clone();
        String[] daysValue = new String[sortedIntDays.length];
        for (int i = 0 ; i < sortedIntDays.length; i++) {
            daysValue[i] = intOfDays.get(sortedIntDays[i]);
        }
        daysPicker.setText(String.join(", ", daysValue));
    }

    @Override
    public void applySnoozeChoice(int checkedId) {
        String snoozeMinText = "OFF";
        checkedSnoozeId = checkedId;
        switch (checkedId) {
            case R.id.zeroMin:
                snoozeMinText = getResources().getString(R.string.off);
                choosedSnooze = 0;
                break;
            case R.id.fiveMin:
                snoozeMinText = getResources().getString(R.string._5_mins);
                choosedSnooze = 5;
                break;
            case R.id.tenMin:
                snoozeMinText = getResources().getString(R.string._10_mins);
                choosedSnooze = 10;
                break;
            case R.id.fifteenMin:
                snoozeMinText = getResources().getString(R.string._15_mins);
                choosedSnooze = 15;
                break;
            case R.id.twentyMin:
                snoozeMinText = getResources().getString(R.string._20_mins);
                choosedSnooze = 20;
                break;

        }
        snoozeAbleButton.setText(snoozeMinText);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native int addition(int a,int b);
}
