package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
    private DatePicker mdatePicker;
    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE =
            "com.bignerdranch.android.criminalintent.date";


    public static DatePickerFragment newinstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);

        DatePickerFragment dpf = new DatePickerFragment();
        dpf.setArguments(args);
        return dpf;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);

        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        mdatePicker = v.findViewById(R.id.dialog_date_picker);

        mdatePicker.init(year,month,day,null);



        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int day = mdatePicker.getDayOfMonth();
                        int month = mdatePicker.getMonth();
                        int year = mdatePicker.getYear();
                        Date date = new GregorianCalendar(year,month,day).getTime();
                        sendResult(Activity.RESULT_OK,date);


                    }
                })
                .create();
    }

    private void sendResult(int resultcode , Date date){
        if(getTargetFragment()==null){
            return ;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultcode,intent);
    }
}
