package com.example.a17010304.smsapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    Button bt;
    EditText etTo, etContent;
    BroadcastReceiver br = new SMS();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        bt = findViewById(R.id.buttonSend);
        etTo = findViewById(R.id.editTextTo);
        etContent = findViewById(R.id.editTextContent);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(br,filter);
//        String to = etTo.getText().toString().trim();
//        String content = etContent.getText().toString().trim();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SmsManager smsManager = SmsManager.getDefault();
                String numbers[] = etTo.getText().toString().split(",");

                for (String number : numbers ){
                    smsManager.sendTextMessage(number, null, etContent.getText().toString(), null, null);
                }
                Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_LONG).show();
                etTo.setText(null);
                etContent.setText(null);

            }
        });




    }
    protected void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(br);
    }
    private void checkPermission() {
        int permissionSendSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int permissionRecvSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECEIVE_SMS);
        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED &&
                permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            String[] permissionNeeded = new String[]{Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissionNeeded, 1);
        }
    }
}
