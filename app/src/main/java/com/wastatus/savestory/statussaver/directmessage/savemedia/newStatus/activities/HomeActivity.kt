package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.wastatus.savestory.statussaver.directmessage.savemedia.R
import com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.activities.AsciiCategoryActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.directChat.activities.ChatDirectActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.emoji.activities.TextToEmojiActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.scan.ScanWhatsappActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.setting.SettingActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.stylishFonts.activities.StylishFontsActivity
import com.wastatus.savestory.statussaver.directmessage.savemedia.textRepeater.activities.TextRepeaterActivity

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController
    private lateinit var appBarConfi: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfi) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_settings ->{
                startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
                return true
            }
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionWhatsScan -> startActivity(Intent(this@HomeActivity, ScanWhatsappActivity::class.java))
            R.id.actionDirectChat-> startActivity(Intent(this@HomeActivity, ChatDirectActivity::class.java))
            R.id.actionAscii-> startActivity(Intent(this@HomeActivity, AsciiCategoryActivity::class.java))
            R.id.actionStylishFont-> startActivity(Intent(this@HomeActivity, StylishFontsActivity::class.java))
            R.id.actionTxtRepeater-> startActivity(Intent(this@HomeActivity, TextRepeaterActivity::class.java))
            R.id.actionTxtEmoji-> startActivity(Intent(this@HomeActivity, TextToEmojiActivity::class.java))


        }
        return true
    }
}