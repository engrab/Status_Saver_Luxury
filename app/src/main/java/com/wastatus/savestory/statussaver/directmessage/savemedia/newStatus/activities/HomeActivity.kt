package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.documentfile.provider.DocumentFile
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager
import com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.activities.AsciiCategoryActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.MainViewModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.autoNotify.PeriodicBackgroundNotification
import com.wastatus.savestory.statussaver.directmessage.savemedia.directChat.activities.ChatDirectActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.emoji.activities.TextToEmojiActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.viewModels.StatusViewModel
import com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.utlis.SharedPrefs
import com.wastatus.savestory.statussaver.directmessage.savemedia.scan.ScanWhatsappActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.setting.SettingActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.stylishFonts.activities.StylishFontsActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.textRepeater.activities.TextRepeaterActivity
import java.util.concurrent.TimeUnit


class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfi: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navView: NavigationView
    private lateinit var viewModel: StatusViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adView: AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        adView = AdmobAdsManager.banner(this, findViewById(R.id.llAds))

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        observeChanges()
        FirebaseApp.initializeApp(this)

        FirebaseAnalytics.getInstance(this)

        viewModel = ViewModelProvider(this).get(StatusViewModel::class.java)
        loadData()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawerLayout)
        bottomNavView = findViewById(R.id.bottomNavView)
        navView = findViewById(R.id.navView)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfi = AppBarConfiguration(
            setOf(R.id.whatsappFragment, R.id.WABusinessFragment, R.id.savedFragment), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfi)

        bottomNavView.setupWithNavController(navController)
//        navView.setupWithNavController(navController)


        setupClickListener()


    }

    override fun onPause() {
        adView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    private fun setupClickListener() {
        // Set item click listener to perform action on menu item click.
        navView.setNavigationItemSelectedListener { menuItem -> // Toggle the checked state of menuItem.

            when (menuItem.itemId) {
                R.id.actionWhatsScan -> startActivity(
                    Intent(
                        this@HomeActivity,
                        ScanWhatsappActivity::class.java
                    )
                )
                R.id.actionDirectChat -> startActivity(
                    Intent(
                        this@HomeActivity,
                        ChatDirectActivity::class.java
                    )
                )
                R.id.actionAscii -> startActivity(
                    Intent(
                        this@HomeActivity,
                        AsciiCategoryActivity::class.java
                    )
                )
                R.id.actionStylishFont -> startActivity(
                    Intent(
                        this@HomeActivity,
                        StylishFontsActivity::class.java
                    )
                )
                R.id.actionTxtRepeater -> startActivity(
                    Intent(
                        this@HomeActivity,
                        TextRepeaterActivity::class.java
                    )
                )
                R.id.actionTxtEmoji -> startActivity(
                    Intent(
                        this@HomeActivity,
                        TextToEmojiActivity::class.java
                    )
                )

            }

            drawerLayout.closeDrawers()
            true
        }
    }


    private fun observeChanges() {
        if (SharedPrefs.getNotify(this)) {

            mainViewModel.getPeriodWork().observe(this, Observer {
                val periodWork = PeriodicWorkRequest.Builder(
                    PeriodicBackgroundNotification::class.java, it.toLong(), TimeUnit.HOURS
                )
                    .addTag("periodic-pending-notification")
                    .build()

                WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                    "periodic-pending-notification",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    periodWork
                )
            })


        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfi) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
                return true
            }
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }


    fun loadData() {
        if (getForWhatsappBusiness() != null) {
            viewModel.getWhatsappBusinessMedia(getForWhatsappBusiness())
        }
        if (getForWhatsapp() != null) {
            viewModel.getWhatsappMedia(getForWhatsapp())
        }

    }

    private fun getForWhatsappBusiness(): Array<DocumentFile?>? {
        val treeUri = SharedPrefs.getWBTree(this)
        if (treeUri != "") {
            val fromTreeUri = DocumentFile.fromTreeUri(applicationContext, Uri.parse(treeUri))
            return if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory && fromTreeUri.canRead() && fromTreeUri.canWrite()) {
                fromTreeUri.listFiles()
            } else {
                null
            }
        }
        return null

    }

    private fun getForWhatsapp(): Array<DocumentFile?>? {
        val treeUri = SharedPrefs.getWATree(this)
        if (treeUri != "") {
            val fromTreeUri =
                DocumentFile.fromTreeUri(applicationContext, Uri.parse(treeUri))
            return if (fromTreeUri != null && fromTreeUri.exists() && fromTreeUri.isDirectory
                && fromTreeUri.canRead() && fromTreeUri.canWrite()
            ) {
                fromTreeUri.listFiles()
            } else {
                null
            }
        }
        return null
    }
}
