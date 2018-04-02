package com.develop.awong.musicplayer2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.time.LocalTime;
import java.util.Calendar;

/**
 * Created by cleve on 3/14/2018.
 */

public class CalendarActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    Button b_pick;
    TextView tv_result;
    Button menu;
    public static Calendar c;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;


    private int currentResource; //*******
    private LocalTime intervalStart, intervalEnd; //*******

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        menu = (Button) findViewById(R.id.menuButton);
        b_pick = (Button) findViewById(R.id.b_pick);
        tv_result = (TextView) findViewById(R.id.tv_result);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CalendarActivity", "onClick: User clicks MENU button");
                Intent i = new Intent(CalendarActivity.this, MenuActivity.class);
                CalendarActivity.this.startActivity(i);
            }
        });

        b_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                Log.d("CalendarActivity", "Displaying date: " + day + "/" + month + "/" + year);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CalendarActivity.this,
                        CalendarActivity.this, year, month, day);
                datePickerDialog.show();
                System.out.println();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month;
        dayFinal = dayOfMonth;

        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CalendarActivity.this,
                CalendarActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c = Calendar.getInstance();
        hourFinal = hourOfDay;
        minuteFinal = minute;

        tv_result.setText("year: " + yearFinal + "\n" +
                        "month: " + monthFinal + 1 + "\n" +
                        "day: " + dayFinal + "\n" +
                        "hour: " + hourFinal + "\n" +
                        "minute: " + minuteFinal);

        c.set(yearFinal,monthFinal,dayFinal,hourFinal,minuteFinal);
        MainActivity.cal = c;
        MainActivity.isTimeMock = true;
    }

    public void getCurrentResource(){

    }
}
