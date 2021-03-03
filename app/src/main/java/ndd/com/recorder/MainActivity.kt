package ndd.com.recorder

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NavigationUI.setupWithNavController(findViewById<BottomNavigationView>(R.id.bottom_navigation),
            Navigation.findNavController(this, R.id.nav_host_fragment_container))
    }

    fun isServiceRunning(): Boolean {
        val  manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if ("ndd.com.recorder.record.RecordService" == service.service.className) {
                return true
            }
        }
        return false
    }
}
