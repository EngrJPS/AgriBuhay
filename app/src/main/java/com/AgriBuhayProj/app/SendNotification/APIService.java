package com.AgriBuhayProj.app.SendNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAf_BT3m0:APA91bFG6wOGIkUuQo74p03fRQwK16tP_yxVBJiTVuL4-HFC_Ne2WnGQeBTvRMR82V8arERcErWrNU28S8I4WEeuSfaW7jRBvUdYCoXBjWMBPGjwz3TY6WSk6IuO7J2dkkV7FiW6abL-"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
