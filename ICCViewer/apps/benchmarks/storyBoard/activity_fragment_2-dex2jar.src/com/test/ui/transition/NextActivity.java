package com.test.ui.transition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class NextActivity extends AppCompatActivity
{
  private void go2Activity(Context paramContext, Class<AActivity> paramClass)
  {
    startActivity(new Intent(paramContext, paramClass));
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2131361820);
    setSupportActionBar((Toolbar)findViewById(2131230893));
    go2Activity(getApplicationContext(), AActivity.class);
  }
}

/* Location:           D:\SoftwareData\AcademicTool\安卓APK分析\jd-gui-0.3.5.windows\jars\activity_fragment_2-dex2jar.jar
 * Qualified Name:     com.test.ui.transition.NextActivity
 * JD-Core Version:    0.6.2
 */