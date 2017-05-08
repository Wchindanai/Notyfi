package dev.notify;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;

/**
 * Created by dream on 5/9/2017 AD.
 */

public class NotificationTimeFragment extends DialogFragment {

    public NotificationTimeFragment() {
    }
    TimePickerDialog.OnTimeSetListener onTimeSet;
    public void setCallBack(TimePickerDialog.OnTimeSetListener time ){
        onTimeSet = time;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create a new instance of TimePickerDialog and return it
        int minute = 0;
        int hour = 0;
        return new TimePickerDialog(getActivity(), onTimeSet, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

}
