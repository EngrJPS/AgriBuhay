package com.AgriBuhayProj.app.SendNotification;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

//USER MESSAGING SERVICE
public class MyFirebaseIdService extends FirebaseMessagingService {

    //NEW TOKEN
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        //get user
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        //generate user token
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if (firebaseUser !=null)
        {
            //generate token
            updateToken(refreshToken);
        }
    }

    //GENERATE USER TOKEN
    private void updateToken(String refreshToken)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //set token value
        Token token1=new Token(refreshToken);
        //save value to token db
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);
    }
}
