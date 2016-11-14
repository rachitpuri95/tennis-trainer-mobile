package com.example.rachitpuri.tennisandroidmobilevfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.cloud.pubsub.Message;
import com.google.cloud.pubsub.PubSub;
import com.google.cloud.pubsub.PubSubOptions;
import com.google.cloud.pubsub.SubscriptionInfo;
import com.google.cloud.pubsub.Subscription;


public class DepthTraining extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_depth_training);
        System.out.println("Sup");

        try (PubSub pubsub = PubSubOptions.getDefaultInstance().getService()) {
            Subscription subscription =
                    pubsub.create(SubscriptionInfo.of("mobile-direction", "mobile-app"));
            PubSub.MessageProcessor callback = new PubSub.MessageProcessor() {
                @Override
                public void process(Message message) throws Exception {
                    System.out.printf("Received message \"%s\"%n", message.getPayloadAsString());
                }
            };
            // Create a message consumer and pull messages (for 60 seconds)
            try (PubSub.MessageConsumer consumer = subscription.pullAsync(callback)) {
                Thread.sleep(60_000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
