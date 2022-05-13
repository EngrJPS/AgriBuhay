package com.AgriBuhayProj.app.SendNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.AgriBuhayProj.app.ProducerPanel.ProducerPreparedOrderView;
import com.AgriBuhayProj.app.ProductPanelBottomNavigation_Producer;
import com.AgriBuhayProj.app.RetailerPanel.PayableOrders;
import com.AgriBuhayProj.app.ProductPanelBottomNavigation_Retailer;
import com.AgriBuhayProj.app.ProductPanelBottomNavigation_Logistics;
import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.SplashScreen;

import java.util.Random;

//SHOW NOTIFICATION
public class ShowNotification {
    //SHOW NOTIFICATION
    public static void ShowNotif(Context context, String title, String message, String page) {
        String CHANNEL_ID = "NOTICE";
        String CHANNEL_NAME = "NOTICE";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //notification chaneel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            //on screen
            channel.enableLights(true);
            //phone vibrate
            channel.enableVibration(true);
            //show notification on lock screen
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            //notification manager
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            //set notification channel
            manager.createNotificationChannel(channel);
        }

        //NOTIFICATION NAVIGATION
        //splashcreen starts
        Intent acIntent = new Intent(context, SplashScreen.class);
        acIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);

        //check page type
        if (page.trim().equalsIgnoreCase("Order")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Producer.class).putExtra("PAGE", "Orderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("Payment")) {
            acIntent = new Intent(context, PayableOrders.class);
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("Home")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Retailer.class).putExtra("PAGE", "Homepage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("Confirm")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Producer.class).putExtra("PAGE", "Confirmpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("Preparing")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Retailer.class).putExtra("PAGE", "Preparingpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("Prepared")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Retailer.class).putExtra("PAGE", "Preparedpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("DeliveryOrder")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Logistics.class).putExtra("PAGE", "DeliveryOrderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("DeliverOrder")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Retailer.class).putExtra("PAGE", "DeliverOrderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("AcceptOrder")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Producer.class).putExtra("PAGE", "AcceptOrderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("RejectOrder")) {
            acIntent = new Intent(context, ProducerPreparedOrderView.class).putExtra("PAGE", "RejectOrderpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("ThankYou")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Retailer.class).putExtra("PAGE", "ThankYoupage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (page.trim().equalsIgnoreCase("Delivered")) {
            acIntent = new Intent(context, ProductPanelBottomNavigation_Producer.class).putExtra("PAGE", "Deliveredpage");
            pendingIntent = PendingIntent.getActivity(context, 0, acIntent, PendingIntent.FLAG_ONE_SHOT);
        }

        //NOTIFICATION DISPLAY
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.ic_baseline_agriculture_24)
                .setColor(ContextCompat.getColor(context,R.color.Red))
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        int random = new Random().nextInt(9999 - 1) + 1;
        notificationManagerCompat.notify(random, nBuilder.build());
    }
}
