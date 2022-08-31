package kr.co.uxn.agms.android.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import kr.co.uxn.agms.android.MainActivity;
import kr.co.uxn.agms.android.R;


public class AlarmService extends Service {
    private static final String CHANNEL_ID = "channel_02";

    private static final int NOTIFICATION_ID = 210200;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        startForeground(NOTIFICATION_ID, getNotification());
        setAlarm();

        startService(new Intent(this, kr.co.uxn.agms.android.service.BluetoothService.class));

        stopForeground(true);
        stopSelf();

        return START_NOT_STICKY;
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, kr.co.uxn.agms.android.service.BluetoothService.class);
        String title = getString(R.string.app_name);
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);

        Intent activityIntent = new Intent(this, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                activityIntent,
                //0
                PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID )
                .setContentText(title)
                .setContentIntent(activityPendingIntent)
                .setContentTitle(title)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_baseline_bluetooth_24)
                .setTicker(title)
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }
        Notification noti = builder.build();

        removeSoundAndVibration(noti);
        return noti;
    }
    private void removeSoundAndVibration(Notification notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        } else {
            notification.sound = null;
            notification.vibrate = null;
            notification.defaults &= ~Notification.DEFAULT_SOUND;
            notification.defaults &= ~Notification.DEFAULT_VIBRATE;
        }
    }
    private void setAlarm(){
        Intent intent = new Intent(this,AlarmService.class);
        intent.setAction(kr.co.uxn.agms.android.service.BluetoothService.ACTION_CHECK_LIVE_SERVICE);
        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(this, 1, intent,
                        PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_CANCEL_CURRENT);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }
}
