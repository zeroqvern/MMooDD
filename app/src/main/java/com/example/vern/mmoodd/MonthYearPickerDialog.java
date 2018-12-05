package com.example.vern.mmoodd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import java.util.Calendar;

public class MonthYearPickerDialog extends DialogFragment {

    private static final int MIN_YEAR = 2000;
    private DatePickerDialog.OnDateSetListener listener;

    private int pickedMonth;
    private int pickedYear;

    private int displayMonthValue;
    private int displayYearValue;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Calendar cal = Calendar.getInstance();

        View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
        final NumberPicker monthPicker = dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = dialog.findViewById(R.id.picker_year);

        //set minimum and maximum value of month
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(getDisplayMonthValue());

        //set minimum and maximum value of year
        final int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(year);
        yearPicker.setValue(getDisplayYearValue());


        builder.setView(dialog)
                // Add action buttons
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDateSet(null, yearPicker.getValue(),
                                monthPicker.getValue(), 0);
                        setPickedMonth(monthPicker.getValue());
                        setPickedYear(yearPicker.getValue());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MonthYearPickerDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public int getDisplayMonthValue() { return displayMonthValue; }

    public int getDisplayYearValue() { return displayYearValue; }

    public void setMonthYearValue (int m, int y)
    {
        displayMonthValue = m;
        displayYearValue = y;
    }

    public int getPickedMonth() {
        return pickedMonth;
    }

    public int getPickedYear() {
        return pickedYear;
    }

    public void setPickedMonth(int m) {
        pickedMonth = m;
    }

    public void setPickedYear (int y) {
        pickedYear = y;
    }
}