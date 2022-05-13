package com.AgriBuhayProj.app.SendNotification;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//NOTIFICATION SERVICE
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String title, message, typepage;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //NOTIFICATION FORMAT
        title = remoteMessage.getData().get("Title");
        message = remoteMessage.getData().get("Message");
        typepage = remoteMessage.getData().get("Typepage");

        //SHOW NOTIFICATION
        ShowNotification.ShowNotif(getApplicationContext(),title,message,typepage);
    }
}
