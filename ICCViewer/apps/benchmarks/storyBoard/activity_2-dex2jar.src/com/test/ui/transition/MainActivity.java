package com.test.ui.transition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
  private void goToActivity(Context paramContext, Class<NextActivity> paramClass)
  {
    startActivity(new Intent(paramContext, paramClass));
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2131361824);
    goToActivity(getApplicationContext(), NextActivity.class);
  }
}

/* Location:           D:\SoftwareData\AcademicTool\安卓APK分析\jd-gui-0.3.5.windows\jars\activity_2-dex2jar.jar
 * Qualified Name:     com.test.ui.transition.MainActivity
 * JD-Core Version:    0.6.2
 */