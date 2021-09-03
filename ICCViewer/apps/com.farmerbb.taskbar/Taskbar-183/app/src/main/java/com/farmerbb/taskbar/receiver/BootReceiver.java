/* Copyright 2016 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.farmerbb.taskbar.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.farmerbb.taskbar.activity.DummyActivity;
import com.farmerbb.taskbar.service.NotificationService;
import com.farmerbb.taskbar.util.CompatUtils;
import com.farmerbb.taskbar.util.U;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Initialize preferences on BlissOS
            SharedPreferences pref = U.getSharedPreferences(context);
            if(U.isBlissOs(context) && !pref.getBoolean("bliss_os_prefs", false))
                U.initPrefs(context);

            SharedPreferences.Editor editor = pref.edit();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    && !U.hasFreeformSupport(context))
                editor.putBoolean("freeform_hack", false);

            if(pref.getBoolean("start_on_boot", false)) {
                editor.putBoolean("taskbar_active", true);
                editor.putLong("time_of_service_start", System.currentTimeMillis());
                editor.apply();

                boolean startServices = false;

                if(!pref.getBoolean("is_hidden", false)) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && pref.getBoolean("freeform_hack", false)) {
                        Intent intent2 = new Intent(context, DummyActivity.class);
                        intent2.putExtra("start_freeform_hack", true);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent2);
                    }

                    startServices = true;
                }

                Intent notificationIntent = new Intent(context, NotificationService.class);
                notificationIntent.putExtra("start_services", startServices);

                CompatUtils.startForegroundService(context, notificationIntent);
            } else {
                editor.putBoolean("taskbar_active", U.isServiceRunning(context, NotificationService.class));
                editor.apply();
            }
        }
    }
}
