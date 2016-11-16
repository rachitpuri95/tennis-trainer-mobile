package com.example.rachitpuri.tennisandroidmobilevfinal;

import android.content.Context;
import android.util.Log;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.PubSub;
import com.google.cloud.pubsub.PubSubOptions;
import com.google.cloud.pubsub.ReceivedMessage;
import com.google.common.io.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class PubSubClient {
    PubSub client = null;
    String PROJECT_ID = "tennis-trainer";

    public PubSubClient(Context context) {
        try {

            InputStream is = context.getResources().openRawResource(R.raw.tennistrainer);
            PubSubOptions options = PubSubOptions.newBuilder()
                    .setProjectId(PROJECT_ID)
                    .setCredentials(ServiceAccountCredentials.fromStream(
                            (is))).build();

            client = options.getService();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void pullAsync(String subscriptionName) throws ExecutionException, InterruptedException {
        Iterator<ReceivedMessage> messages = client.pull("mobile-app", 100);
        // Ack deadline is renewed until the message is consumed
        while (messages.hasNext()) {
            ReceivedMessage message = messages.next();
            Log.d(Constants.TAG, message.getPayloadAsString());
            // do something with message and ack/nack it
//	      message.ack(); // or message.nack()
        }
        Log.d(Constants.TAG, "Retrieved messages from the Pub/sub channel");
    }
}
