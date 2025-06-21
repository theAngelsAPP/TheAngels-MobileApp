package co.median.android.a2025_theangels_new.data.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Basic service for handling FCM messages. The actual implementation will be
 * added later.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle incoming push notifications
    }

    @Override
    public void onNewToken(String token) {
        // TODO: Handle FCM token updates
    }
}
