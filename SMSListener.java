package com.nocomp.kharcha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by TVGS on 10/05/19.
 */

public class SMSListener extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    String mobile,body;

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        String message = "";
        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");


                    //String format = bundle.getString("format");

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    message = currentMessage.getDisplayMessageBody();
                    mobile=senderNum.replaceAll("\\s","");
                    body=message.replaceAll("\\s","+");


                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + body);


                    // Show Alert

                // end for loop
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Intent smsIntent= new Intent(context,MainActivity.class);
                smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                smsIntent.putExtra("Message",currentMessage.getMessageBody());
                context.startActivity(smsIntent);
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}