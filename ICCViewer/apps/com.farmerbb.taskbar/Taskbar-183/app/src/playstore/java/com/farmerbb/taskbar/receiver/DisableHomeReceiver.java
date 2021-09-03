/* Copyright 2017 Braden Farmer
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
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.LocalBroadcastManager;

import com.farmerbb.taskbar.activity.HomeActivity;
import com.farmerbb.taskbar.util.U;

public class DisableHomeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences pref = U.getSharedPreferences(context);
        if(pref.getBoolean("skip_disable_home_receiver", false))
            pref.edit().remove("skip_disable_home_receiver").apply();
        else if(!U.isLauncherPermanentlyEnabled(context)) {
            pref.edit().putBoolean("launcher", false).apply();

            ComponentName component = new ComponentName(context, HomeActivity.class);
            context.getPackageManager().setComponentEnabledSetting(component,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);

            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
            lbm.sendBroadcast(new Intent("com.farmerbb.taskbar.KILL_HOME_ACTIVITY"));
            lbm.sendBroadcast(new Intent("com.farmerbb.taskbar.LAUNCHER_PREF_CHANGED"));
        }
    }
}
