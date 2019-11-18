package io.mochahub.powermeter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.push.Push;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCenter.start(application, BuildConfig.HOCKEY_APP_SECRET,
            Push::class.java,  Analytics::class.java, Crashes::class.java)
        Analytics.trackEvent("Hello World")

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentLayout, MainFragment.newInstance(), "mainFragment")
            .commit()
    }
}
