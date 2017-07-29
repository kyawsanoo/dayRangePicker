package mm.kso.dayrangepickertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.TimeZone;

import kso.mm.dayrangepicker.DayRangePickerActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText txt_DepartDate, txt_ReturnDate;
    private Button reset;
    private int DATE_REQUEST_CODE = 1;
    String departDate = "";
    String returnDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_DepartDate = (EditText) findViewById(R.id.textView_DepartDate);
        txt_ReturnDate = (EditText) findViewById(R.id.textView_ReturnDate);
        reset = (Button) findViewById(R.id.reset);
        txt_DepartDate.setOnClickListener(this);
        txt_ReturnDate.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.textView_DepartDate:
                goToSelectDateActivity();
                break;
            case R.id.textView_ReturnDate:
                goToSelectDateActivity();
                break;
            case R.id.reset:
                reset();
                break;
        }
    }

    private void reset(){
        if(txt_DepartDate.getText().toString().isEmpty() && txt_ReturnDate.getText().toString().isEmpty()){
            showSnackBar(txt_DepartDate, getResources().getString(R.string.info));
        }else {
            txt_ReturnDate.setText("");
            txt_DepartDate.setText("");
        }
    }
    private void goToSelectDateActivity(){
        if(txt_DepartDate.getText().toString().isEmpty() || txt_ReturnDate.getText().toString().isEmpty() ){
            Intent intent = DayRangePickerActivity.Companion.newIntent(MainActivity.this,
                    TimeZone.getDefault(),
                    //DateTime.now().plusDays(5).getMillis(),
                    null,
                    null

            );
            startActivityForResult(intent, DATE_REQUEST_CODE);

        }else{
            String departDate = txt_DepartDate.getText().toString();
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime departDateTime = format.parseDateTime(departDate);

            String returnDate = txt_ReturnDate.getText().toString();
            DateTimeFormatter dtF = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime returnDateTime = dtF.parseDateTime(returnDate);

            Intent intent = DayRangePickerActivity.Companion.newIntent(MainActivity.this,
                    TimeZone.getDefault(),
                    departDateTime.getMillis(),
                    returnDateTime.getMillis()

            );
            startActivityForResult(intent, DATE_REQUEST_CODE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==RESULT_OK) {
            switch (requestCode) {
                case 1:
                    long start = data.getLongExtra("startTimeInMillis", 0);
                    long end = data.getLongExtra("endTimeInMillis", 0);
                    String timezone = data.getStringExtra("timeZone");
                    Log.e("Time", "Start : " + start + " end : " + end + " time : " + timezone);
                    final DateTime departDt = new DateTime(start);
                    final DateTime returnDt = new DateTime(end);
                    DateTimeFormatter departOut = DateTimeFormat.forPattern("yyyy-MM-dd");
                    Log.e("depart date format ", departOut.print(departDt));
                    DateTimeFormatter returnOut = DateTimeFormat.forPattern("yyyy-MM-dd");
                    Log.e("return date format ", returnOut.print(returnDt));
                    departDate = departOut.print(departDt);
                    returnDate = returnOut.print(returnDt);
                    if(!departDate.equals("")){
                        txt_DepartDate.setText(departDate + "");
                    }
                    if(!returnDate.equals(""))
                        txt_ReturnDate.setText(returnDate);
                    if(TextUtils.isEmpty(txt_DepartDate.getText().toString()) &&
                            TextUtils.isEmpty(txt_ReturnDate.getText().toString())){
                        String message = "Please Select Depar and Return Date";
                        showSnackBar(txt_DepartDate, message);
                    }
                    break;
                default:
                    break;
            }

        }

    }
    private void showSnackBar(View view, String message){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
