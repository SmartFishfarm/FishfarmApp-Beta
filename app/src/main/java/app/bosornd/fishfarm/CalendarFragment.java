package app.bosornd.fishfarm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by jun on 17. 10. 4.
 */

public class CalendarFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    CalendarView calendarView;
    Intent intent;
    String date;
    int Date;
    dbHandler myDb;
    dbHandler2 myDb2;
    dateSelected dateselected;
    static int incrementer;
    static final String TAG="TAG";

    public CalendarFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        myDb = new dbHandler(getActivity());
        myDb2 = new dbHandler2(getActivity());
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        dateselected = new dateSelected();


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()

        {
            @Override
            public void onSelectedDayChange (@NonNull CalendarView calendarView, int i, int i1,
                                             int i2){
                date = i2 + "" + i1 + "" + i;
                Date = Integer.parseInt(date);
                Log.e(TAG, "int date=" + Date + "");
                Log.e(TAG, "string date=" + date + "");
                //firstly check if an entry exists for the current date.
                Cursor res = myDb.getAllData();

                while (res.moveToNext()) {
                    if (res.getInt(0) == Date) {    //only runs if it find the id for this date.
                        StringBuffer buffer = new StringBuffer();

                        //buffer.append("Id :" + res.getString(0) + "\n");
                        buffer.append("일정제목 :" + res.getString(1) + "\n");
                        buffer.append("일정장소 :" + res.getString(2) + "\n");
                        buffer.append("알정내용 :" + res.getString(3) + "\n\n");

                        //when an id is present for event, than there must be an entry for attendees, So search by date(id) for attendees.
                        Cursor cursor = myDb2.getAllData();
                        while (cursor.moveToNext()) {
                            String test = cursor.getInt(0) + "";
                            if (test.contains(date)) {
                                Log.e(TAG, "it reached here");
                                buffer.append("일정참가자 :" + cursor.getString(1) + "\n\n");
                            }
                        }
                        // show message
                        showMessage("일정", buffer.toString());
                        return;
                    }
                }
                //if no entry is found then pass the id as Date and shift to activity_date_selected activity ).
                intent = new Intent(getActivity(), dateSelected.class);
                intent.putExtra("date message", Date);
                intent.putExtra("day message", i2);
                intent.putExtra("month message", i1);
                intent.putExtra("year message", i);
                startActivity(intent);
            }
        });



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        // set three button on the dialog box ( Edit, Delete , Cancel)
        builder.setMessage(Message).setPositiveButton("일정수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intent = new Intent(getActivity(), editClicked.class);
                Cursor cursor = myDb2.getAllData();
                while(cursor.moveToNext()) {
                    String test = cursor.getInt(0) + "";
                    if (test.contains(date)) {
                        int changedDateId = Integer.parseInt(test);
                        myDb2.deleteEvent(changedDateId);
                    }
                }
                intent.putExtra("date message", Date);
                startActivity(intent);
            }
        }).setNeutralButton("일정삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int result  =myDb.deleteEvent(Date);
                Cursor cursor = myDb2.getAllData();
                while(cursor.moveToNext()) {
                    String test = cursor.getInt(0) + "";
                    if (test.contains(date)) {
                        int changedDateId = Integer.parseInt(test);
                        myDb2.deleteEvent(changedDateId);
                    }
                }
                // checking if data is deleted or not.
                if(result == 1 )
                    Toast.makeText(getActivity(),"Data deleted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(),"Failed to delete Data",Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("취소",null);
        //builder.show();
        AlertDialog alert= builder.create();
        alert.show();
    }
}
