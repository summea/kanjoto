
package com.andrewsummers.kanjoto;

import com.andrewsummers.kanjoto.activity.ChooseEmotionActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class KanjotoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MYLOG", "on receive called... starting activity...");
        intent = new Intent(context, ChooseEmotionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("auto_play", true);
        context.startActivity(intent);
    }

}
