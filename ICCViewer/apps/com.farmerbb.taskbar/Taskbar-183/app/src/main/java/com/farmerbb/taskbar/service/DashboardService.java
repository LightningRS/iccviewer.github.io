/* Based on code by Leonardo Fischer
 * See https://github.com/lgfischer/WidgetHostExample
 *
 * Copyright 2016 Braden Farmer
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

package com.farmerbb.taskbar.service;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.ColorUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farmerbb.taskbar.R;
import com.farmerbb.taskbar.activity.DashboardActivity;
import com.farmerbb.taskbar.activity.dark.DashboardActivityDark;
import com.farmerbb.taskbar.util.DashboardHelper;
import com.farmerbb.taskbar.util.CompatUtils;
import com.farmerbb.taskbar.widget.DashboardCell;
import com.farmerbb.taskbar.util.FreeformHackHelper;
import com.farmerbb.taskbar.util.LauncherHelper;
import com.farmerbb.taskbar.util.U;

import java.util.List;

public class DashboardService extends Service {

    private AppWidgetManager mAppWidgetManager;
    private AppWidgetHost mAppWidgetHost;

    private WindowManager windowManager;
    private LinearLayout layout;

    private SparseArray<DashboardCell> cells = new SparseArray<>();
    private SparseArray<AppWidgetHostView> widgets = new SparseArray<>();

    private final int APPWIDGET_HOST_ID = 123;

    private int columns;
    private int rows;
    private int maxSize;
    private int previouslySelectedCell = -1;

    private View.OnClickListener ocl = view -> toggleDashboard();

    private View.OnClickListener cellOcl = view -> cellClick(view, true);

    private View.OnHoverListener cellOhl = (v, event) -> {
        cellClick(v, false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            v.setPointerIcon(PointerIcon.getSystemIcon(DashboardService.this, PointerIcon.TYPE_DEFAULT));

        return false;
    };

    private View.OnLongClickListener olcl = v -> {
        cellLongClick(v);
        return true;
    };

    private View.OnGenericMotionListener ogml = (view, motionEvent) -> {
        if(motionEvent.getAction() == MotionEvent.ACTION_BUTTON_PRESS
                && motionEvent.getButtonState() == MotionEvent.BUTTON_SECONDARY)
            cellLongClick(view);

        return false;
    };

    private DashboardCell.OnInterceptedLongPressListener listener = this::cellLongClick;

    private BroadcastReceiver toggleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            toggleDashboard();
        }
    };

    private BroadcastReceiver addWidgetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fadeIn();

            if(intent.hasExtra("appWidgetId") && intent.hasExtra("cellId")) {
                int appWidgetId = intent.getExtras().getInt("appWidgetId", -1);
                int cellId = intent.getExtras().getInt("cellId", -1);

                addWidget(appWidgetId, cellId, true);
            }
        }
    };

    private BroadcastReceiver removeWidgetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fadeIn();

            if(intent.hasExtra("cellId")) {
                int cellId = intent.getExtras().getInt("cellId", -1);

                removeWidget(cellId, false);
            }
        }
    };

    private BroadcastReceiver hideReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            hideDashboard();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences pref = U.getSharedPreferences(this);
        if(pref.getBoolean("dashboard", false)) {
            if(pref.getBoolean("taskbar_active", false) || LauncherHelper.getInstance().isOnHomeScreen()) {
                if(U.canDrawOverlays(this))
                    drawDashboard();
                else {
                    pref.edit().putBoolean("taskbar_active", false).apply();

                    stopSelf();
                }
            } else stopSelf();
        } else stopSelf();
    }

    @SuppressLint("RtlHardcoded")
    private void drawDashboard() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                CompatUtils.getOverlayType(),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                PixelFormat.TRANSLUCENT);

        // Initialize views
        layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setVisibility(View.GONE);
        layout.setAlpha(0);

        SharedPreferences pref = U.getSharedPreferences(this);
        int width = pref.getInt("dashboard_width", getApplicationContext().getResources().getInteger(R.integer.dashboard_width));
        int height = pref.getInt("dashboard_height", getApplicationContext().getResources().getInteger(R.integer.dashboard_height));

        boolean isPortrait = getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        boolean isLandscape = getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if(isPortrait) {
            columns = height;
            rows = width;
        }

        if(isLandscape) {
            columns = width;
            rows = height;
        }

        maxSize = columns * rows;

        int backgroundTint = U.getBackgroundTint(this);
        int accentColor = U.getAccentColor(this);
        int accentColorAlt = accentColor;
        accentColorAlt = ColorUtils.setAlphaComponent(accentColorAlt, Color.alpha(accentColorAlt) / 2);

        int cellCount = 0;

        for(int i = 0; i < columns; i++) {
            LinearLayout layout2 = new LinearLayout(this);
            layout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            layout2.setOrientation(LinearLayout.VERTICAL);

            for(int j = 0; j < rows; j++) {
                DashboardCell cellLayout = (DashboardCell) View.inflate(this, R.layout.dashboard, null);
                cellLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                cellLayout.setBackgroundColor(backgroundTint);
                cellLayout.setOnClickListener(cellOcl);
                cellLayout.setOnHoverListener(cellOhl);

                TextView empty = U.findViewById(cellLayout, R.id.empty);
                empty.setBackgroundColor(accentColorAlt);
                empty.setTextColor(accentColor);

                Bundle bundle = new Bundle();
                bundle.putInt("cellId", cellCount);

                cellLayout.setTag(bundle);
                cells.put(cellCount, cellLayout);
                cellCount++;

                layout2.addView(cellLayout);
            }

            layout.addView(layout2);
        }

        mAppWidgetManager = AppWidgetManager.getInstance(this);
        mAppWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
        mAppWidgetHost.startListening();

        for(int i = 0; i < maxSize; i++) {
            int appWidgetId = pref.getInt("dashboard_widget_" + Integer.toString(i), -1);
            if(appWidgetId != -1)
                addWidget(appWidgetId, i, false);
            else if(pref.getBoolean("dashboard_widget_" + Integer.toString(i) + "_placeholder", false))
                addPlaceholder(i);
        }

        mAppWidgetHost.stopListening();

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        
        lbm.unregisterReceiver(toggleReceiver);
        lbm.unregisterReceiver(addWidgetReceiver);
        lbm.unregisterReceiver(removeWidgetReceiver);
        lbm.unregisterReceiver(hideReceiver);

        lbm.registerReceiver(toggleReceiver, new IntentFilter("com.farmerbb.taskbar.TOGGLE_DASHBOARD"));
        lbm.registerReceiver(addWidgetReceiver, new IntentFilter("com.farmerbb.taskbar.ADD_WIDGET_COMPLETED"));
        lbm.registerReceiver(removeWidgetReceiver, new IntentFilter("com.farmerbb.taskbar.REMOVE_WIDGET_COMPLETED"));
        lbm.registerReceiver(hideReceiver, new IntentFilter("com.farmerbb.taskbar.HIDE_DASHBOARD"));

        windowManager.addView(layout, params);

        new Handler().postDelayed(() -> {
            int paddingSize = getResources().getDimensionPixelSize(R.dimen.icon_size);

            switch(U.getTaskbarPosition(DashboardService.this)) {
                case "top_vertical_left":
                case "bottom_vertical_left":
                    layout.setPadding(paddingSize, 0, 0, 0);
                    break;
                case "top_left":
                case "top_right":
                    layout.setPadding(0, paddingSize, 0, 0);
                    break;
                case "top_vertical_right":
                case "bottom_vertical_right":
                    layout.setPadding(0, 0, paddingSize, 0);
                    break;
                case "bottom_left":
                case "bottom_right":
                    layout.setPadding(0, 0, 0, paddingSize);
                    break;
            }
        }, 100);
    }

    private void toggleDashboard() {
        if(layout.getVisibility() == View.GONE)
            showDashboard();
        else
            hideDashboard();
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.N)
    private void showDashboard() {
        if(layout.getVisibility() == View.GONE) {
            layout.setOnClickListener(ocl);
            fadeIn();

            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("com.farmerbb.taskbar.DASHBOARD_APPEARING"));
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("com.farmerbb.taskbar.HIDE_START_MENU"));

            boolean inFreeformMode = FreeformHackHelper.getInstance().isInFreeformWorkspace();

            final SharedPreferences pref = U.getSharedPreferences(this);
            Intent intent = null;

            switch(pref.getString("theme", "light")) {
                case "light":
                    intent = new Intent(this, DashboardActivity.class);
                    break;
                case "dark":
                    intent = new Intent(this, DashboardActivityDark.class);
                    break;
            }

            if(intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            }

            if(inFreeformMode) {
                if(intent != null && U.hasBrokenSetLaunchBoundsApi())
                    intent.putExtra("context_menu_fix", true);

                U.launchAppMaximized(this, intent);
            } else
                startActivity(intent);

            for(int i = 0; i < maxSize; i++) {
                final DashboardCell cellLayout = cells.get(i);
                final AppWidgetHostView hostView = widgets.get(i);

                if(hostView != null) {
                    try {
                        getPackageManager().getApplicationInfo(hostView.getAppWidgetInfo().provider.getPackageName(), 0);
                        hostView.post(() -> {
                            ViewGroup.LayoutParams params = hostView.getLayoutParams();
                            params.width = cellLayout.getWidth();
                            params.height = cellLayout.getHeight();
                            hostView.setLayoutParams(params);
                            hostView.updateAppWidgetSize(null, cellLayout.getWidth(), cellLayout.getHeight(), cellLayout.getWidth(), cellLayout.getHeight());
                        });
                    } catch (PackageManager.NameNotFoundException e) {
                        removeWidget(i, false);
                    } catch (NullPointerException e) {
                        removeWidget(i, true);
                    }
                }
            }

            if(!pref.getBoolean("dashboard_tutorial_shown", false)) {
                U.showToastLong(this, R.string.dashboard_tutorial);
                pref.edit().putBoolean("dashboard_tutorial_shown", true).apply();
            }
        }
    }

    private void hideDashboard() {
        if(layout.getVisibility() == View.VISIBLE) {
            layout.setOnClickListener(null);
            fadeOut(true);

            for(int i = 0; i < maxSize; i++) {
                FrameLayout frameLayout = cells.get(i);
                frameLayout.findViewById(R.id.empty).setVisibility(View.GONE);
            }

            previouslySelectedCell = -1;
        }
    }

    private void fadeIn() {
        mAppWidgetHost.startListening();

        DashboardHelper.getInstance().setDashboardOpen(true);

        layout.setVisibility(View.VISIBLE);
        layout.animate()
                .alpha(1)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setListener(null);
    }

    private void fadeOut(final boolean sendIntent) {
        mAppWidgetHost.stopListening();

        DashboardHelper.getInstance().setDashboardOpen(false);

        layout.animate()
                .alpha(0)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layout.setVisibility(View.GONE);
                        if(sendIntent) LocalBroadcastManager.getInstance(DashboardService.this).sendBroadcast(new Intent("com.farmerbb.taskbar.DASHBOARD_DISAPPEARING"));
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(layout != null) {
            try {
                windowManager.removeView(layout);
            } catch (IllegalArgumentException e) { /* Gracefully fail */ }

            SharedPreferences pref = U.getSharedPreferences(this);
            if(U.canDrawOverlays(this))
                drawDashboard();
            else {
                pref.edit().putBoolean("taskbar_active", false).apply();

                stopSelf();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(layout != null)
            try {
                windowManager.removeView(layout);
            } catch (IllegalArgumentException e) { /* Gracefully fail */ }

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);

        lbm.unregisterReceiver(toggleReceiver);
        lbm.unregisterReceiver(addWidgetReceiver);
        lbm.unregisterReceiver(removeWidgetReceiver);
        lbm.unregisterReceiver(hideReceiver);

        lbm.sendBroadcast(new Intent("com.farmerbb.taskbar.DASHBOARD_DISAPPEARING"));
    }

    private void cellClick(View view, boolean isActualClick) {
        Bundle bundle = (Bundle) view.getTag();
        int cellId = bundle.getInt("cellId");
        int appWidgetId = bundle.getInt("appWidgetId", -1);

        int currentlySelectedCell = appWidgetId == -1 ? cellId : -1;

        SharedPreferences pref = U.getSharedPreferences(this);
        boolean shouldShowPlaceholder = pref.getBoolean("dashboard_widget_" + Integer.toString(cellId) + "_placeholder", false);
        if(isActualClick && ((appWidgetId == -1 && currentlySelectedCell == previouslySelectedCell) || shouldShowPlaceholder)) {
            fadeOut(false);

            FrameLayout frameLayout = cells.get(currentlySelectedCell);
            frameLayout.findViewById(R.id.empty).setVisibility(View.GONE);

            Intent intent = new Intent("com.farmerbb.taskbar.ADD_WIDGET_REQUESTED");
            intent.putExtra("appWidgetId", APPWIDGET_HOST_ID);
            intent.putExtra("cellId", cellId);
            LocalBroadcastManager.getInstance(DashboardService.this).sendBroadcast(intent);

            if(shouldShowPlaceholder) {
                String providerName = pref.getString("dashboard_widget_" + Integer.toString(cellId) + "_provider", "null");
                if(!providerName.equals("null")) {
                    ComponentName componentName = ComponentName.unflattenFromString(providerName);

                    List<AppWidgetProviderInfo> providerInfoList = mAppWidgetManager.getInstalledProvidersForProfile(Process.myUserHandle());
                    for(AppWidgetProviderInfo info : providerInfoList) {
                        if(info.provider.equals(componentName)) {
                            U.showToast(this, getString(R.string.widget_restore_toast, info.loadLabel(getPackageManager())), Toast.LENGTH_SHORT);
                            break;
                        }
                    }
                }
            }

            previouslySelectedCell = -1;
        } else {
            for(int i = 0; i < maxSize; i++) {
                FrameLayout frameLayout = cells.get(i);
                frameLayout.findViewById(R.id.empty).setVisibility(i == currentlySelectedCell && !shouldShowPlaceholder ? View.VISIBLE : View.GONE);
            }

            previouslySelectedCell = currentlySelectedCell;
        }
    }

    private void cellLongClick(View view) {
        fadeOut(false);

        Bundle bundle = (Bundle) view.getTag();
        int cellId = bundle.getInt("cellId");

        Intent intent = new Intent("com.farmerbb.taskbar.REMOVE_WIDGET_REQUESTED");
        intent.putExtra("cellId", cellId);
        LocalBroadcastManager.getInstance(DashboardService.this).sendBroadcast(intent);
    }

    private void addWidget(int appWidgetId, int cellId, boolean shouldSave) {
        AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

        final DashboardCell cellLayout = cells.get(cellId);
        final AppWidgetHostView hostView = mAppWidgetHost.createView(DashboardService.this, appWidgetId, appWidgetInfo);
        hostView.setAppWidget(appWidgetId, appWidgetInfo);

        Bundle bundle = new Bundle();
        bundle.putInt("cellId", cellId);
        hostView.setTag(bundle);

        cellLayout.findViewById(R.id.empty).setVisibility(View.GONE);
        cellLayout.findViewById(R.id.placeholder).setVisibility(View.GONE);
        cellLayout.setOnLongClickListener(olcl);
        cellLayout.setOnGenericMotionListener(ogml);
        cellLayout.setOnInterceptedLongPressListener(listener);

        LinearLayout linearLayout = U.findViewById(cellLayout, R.id.dashboard);
        linearLayout.addView(hostView);

        Bundle bundle2 = (Bundle) cellLayout.getTag();
        bundle2.putInt("appWidgetId", appWidgetId);
        cellLayout.setTag(bundle2);

        widgets.put(cellId, hostView);

        if(shouldSave) {
            SharedPreferences pref = U.getSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("dashboard_widget_" + Integer.toString(cellId), appWidgetId);
            editor.putString("dashboard_widget_" + Integer.toString(cellId) + "_provider", appWidgetInfo.provider.flattenToString());
            editor.remove("dashboard_widget_" + Integer.toString(cellId) + "_placeholder");
            editor.apply();
        }

        new Handler().post(() -> {
            ViewGroup.LayoutParams params = hostView.getLayoutParams();
            params.width = cellLayout.getWidth();
            params.height = cellLayout.getHeight();
            hostView.setLayoutParams(params);
            hostView.updateAppWidgetSize(null, cellLayout.getWidth(), cellLayout.getHeight(), cellLayout.getWidth(), cellLayout.getHeight());
        });
    }

    private void removeWidget(int cellId, boolean tempRemove) {
        widgets.remove(cellId);

        DashboardCell cellLayout = cells.get(cellId);
        Bundle bundle = (Bundle) cellLayout.getTag();

        mAppWidgetHost.deleteAppWidgetId(bundle.getInt("appWidgetId"));
        bundle.remove("appWidgetId");

        LinearLayout linearLayout = U.findViewById(cellLayout, R.id.dashboard);
        linearLayout.removeAllViews();

        cellLayout.setTag(bundle);
        cellLayout.setOnClickListener(cellOcl);
        cellLayout.setOnHoverListener(cellOhl);
        cellLayout.setOnLongClickListener(null);
        cellLayout.setOnGenericMotionListener(null);
        cellLayout.setOnInterceptedLongPressListener(null);

        SharedPreferences pref = U.getSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("dashboard_widget_" + Integer.toString(cellId));

        if(tempRemove) {
            editor.putBoolean("dashboard_widget_" + Integer.toString(cellId) + "_placeholder", true);
            addPlaceholder(cellId);
        } else
            editor.remove("dashboard_widget_" + Integer.toString(cellId) + "_provider");

        editor.apply();
    }

    private void addPlaceholder(int cellId) {
        FrameLayout placeholder = U.findViewById(cells.get(cellId), R.id.placeholder);
        SharedPreferences pref = U.getSharedPreferences(this);
        String providerName = pref.getString("dashboard_widget_" + Integer.toString(cellId) + "_provider", "null");

        if(!providerName.equals("null")) {
            ImageView imageView = U.findViewById(placeholder, R.id.placeholder_image);
            ComponentName componentName = ComponentName.unflattenFromString(providerName);

            List<AppWidgetProviderInfo> providerInfoList = mAppWidgetManager.getInstalledProvidersForProfile(Process.myUserHandle());
            for(AppWidgetProviderInfo info : providerInfoList) {
                if(info.provider.equals(componentName)) {
                    Drawable drawable = info.loadPreviewImage(this, -1);
                    if(drawable == null) drawable = info.loadIcon(this, -1);

                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);

                    imageView.setImageDrawable(drawable);
                    imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
                    break;
                }
            }
        }

        placeholder.setVisibility(View.VISIBLE);
    }
}
