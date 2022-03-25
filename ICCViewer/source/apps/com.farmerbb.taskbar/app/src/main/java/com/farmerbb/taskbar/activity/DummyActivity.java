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

package com.farmerbb.taskbar.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.provider.Settings;
import android.view.View;

import com.farmerbb.taskbar.R;
import com.farmerbb.taskbar.receiver.LockDeviceReceiver;
import com.farmerbb.taskbar.util.ApplicationType;
import com.farmerbb.taskbar.util.FreeformHackHelper;
import com.farmerbb.taskbar.util.U;

public class DummyActivity extends Activity {

    boolean shouldFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new View(this));
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        if(shouldFinish)
            finish();
        else {
            shouldFinish = true;

            if(getIntent().hasExtra("uninstall")) {
                UserManager userManager = (UserManager) getSystemService(USER_SERVICE);

                Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + getIntent().getStringExtra("uninstall")));
                intent.putExtra(Intent.EXTRA_USER, userManager.getUserForSerialNumber(getIntent().getLongExtra("user_id", 0)));

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) { /* Gracefully fail */ }
            } else if(getIntent().hasExtra("device_admin")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(U.wrapContext(this));
                builder.setTitle(R.string.permission_dialog_title)
                        .setMessage(R.string.device_admin_disclosure)
                        .setNegativeButton(R.string.action_cancel, (dialog, which) -> new Handler().post(this::finish))
                        .setPositiveButton(R.string.action_activate, (dialog, which) -> {
                            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(this, LockDeviceReceiver.class));
                            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.device_admin_description));

                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                U.showToast(this, R.string.lock_device_not_supported);

                                finish();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);
            } else if(getIntent().hasExtra("accessibility")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(U.wrapContext(this));
                builder.setTitle(R.string.permission_dialog_title)
                        .setMessage(R.string.enable_accessibility)
                        .setNegativeButton(R.string.action_cancel, (dialog, which) -> new Handler().post(this::finish))
                        .setPositiveButton(R.string.action_activate, (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            try {
                                startActivity(intent, U.getActivityOptionsBundle(ApplicationType.APPLICATION));
                                U.showToastLong(this, R.string.usage_stats_message);
                            } catch (ActivityNotFoundException e) {
                                U.showToast(this, R.string.lock_device_not_supported);

                                finish();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);
            } else if(getIntent().hasExtra("start_freeform_hack")) {
                SharedPreferences pref = U.getSharedPreferences(this);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        && pref.getBoolean("freeform_hack", false)
                        && isInMultiWindowMode()
                        && !FreeformHackHelper.getInstance().isFreeformHackActive()) {
                    U.startFreeformHack(this, false, false);
                }

                finish();
            } else if(getIntent().hasExtra("show_permission_dialog"))
                U.showPermissionDialog(U.wrapContext(this), null, this::finish);
            else if(getIntent().hasExtra("show_recent_apps_dialog"))
                U.showRecentAppsDialog(U.wrapContext(this), null, this::finish);
            else
                finish();
        }
    }
}