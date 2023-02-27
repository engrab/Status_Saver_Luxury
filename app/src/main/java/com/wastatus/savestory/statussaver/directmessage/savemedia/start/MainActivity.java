package com.wastatus.savestory.statussaver.directmessage.savemedia.start;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.wastatus.savestory.statussaver.directmessage.savemedia.R;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.fragments.SavedStatusFragment;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.fragments.WBStatusFragment;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.fragments.WAStatusFragment;
import com.wastatus.savestory.statussaver.directmessage.savemedia.Status.utlis.Utils;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ads.AdmobAdsManager;
import com.wastatus.savestory.statussaver.directmessage.savemedia.ascii.activities.AsciiCategoryActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.databinding.ActivityMainBinding;
import com.wastatus.savestory.statussaver.directmessage.savemedia.directChat.activities.ChatDirectActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.emoji.activities.TextToEmojiActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.scan.ScanWhatsappActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.setting.SettingActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.stylishFonts.activities.StylishFontsActivity;
import com.wastatus.savestory.statussaver.directmessage.savemedia.textRepeater.activities.TextRepeaterActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public static final int READ_WRITE_PERMISSION_CODE = 21;
    ViewPagerAdapter viewPagerAdapter;
    AdView adView;
    String[] permissionsList = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //    private MyFileObserver fileObserver;
    File file;
    private ActivityMainBinding binding;

    public static boolean checkPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        if (Utils.appInstalledOrNot(this, "com.whatsapp.w4b")) {
//
//            String filePath = getWhatsappBusinessFolder();
//            fileObserver = new MyFileObserver(filePath, getApplicationContext());
//            fileObserver.startWatching();
//        }
//        if (Utils.appInstalledOrNot(this, "com.whatsapp")) {
//            String filePath = getWhatsappFolder();
//            fileObserver = new MyFileObserver(filePath, getApplicationContext());
//            fileObserver.startWatching();
//        }
        FirebaseApp.initializeApp(this);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        viewpagerAndBottomNav();

        if (savedInstanceState == null) {
            binding.appBarMain.bottomNavigation.setSelectedItemId(R.id.actionWhatsapp);
            binding.navView.setCheckedItem(R.id.actionWhatsapp);

        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        binding.navView.setNavigationItemSelectedListener(this);


        if (AdmobAdsManager.isAdmob) {
            AdmobAdsManager.loadInterstitial(MainActivity.this);

            adView = AdmobAdsManager.banner(this, binding.appBarMain.llAds);
        }

        if (!checkPermissions(this, permissionsList)) {
            ActivityCompat.requestPermissions(this, permissionsList, READ_WRITE_PERMISSION_CODE);
        }else {
            Utils.loadMedia();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void viewpagerAndBottomNav() {
        binding.appBarMain.bottomNavigation.setOnNavigationItemSelectedListener(this);
        binding.navView.setNavigationItemSelectedListener(MainActivity.this);
        binding.appBarMain.viewPager.setOffscreenPageLimit(3);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.appBarMain.viewPager.setAdapter(viewPagerAdapter);

        binding.appBarMain.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.appBarMain.bottomNavigation.getMenu().findItem(R.id.actionWhatsapp).setChecked(true);

                        binding.navView.getMenu().findItem(R.id.actionWhatsapp).setChecked(true);
                        binding.navView.getMenu().findItem(R.id.actionWhatsappB).setChecked(false);
                        binding.navView.getMenu().findItem(R.id.actionSaved).setChecked(false);

                        break;
                    case 1:
                        binding.appBarMain.bottomNavigation.getMenu().findItem(R.id.actionWhatsappB).setChecked(true);

                        binding.navView.getMenu().findItem(R.id.actionWhatsappB).setChecked(true);
                        binding.navView.getMenu().findItem(R.id.actionWhatsapp).setChecked(false);
                        binding.navView.getMenu().findItem(R.id.actionSaved).setChecked(false);
                        break;

                    case 2:
                        binding.appBarMain.bottomNavigation.getMenu().findItem(R.id.actionSaved).setChecked(true);

                        binding.navView.getMenu().findItem(R.id.actionSaved).setChecked(true);
                        binding.navView.getMenu().findItem(R.id.actionWhatsappB).setChecked(false);
                        binding.navView.getMenu().findItem(R.id.actionWhatsapp).setChecked(false);
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_WRITE_PERMISSION_CODE) {
            if (!checkPermissions(this, permissionsList)) {
                ActivityCompat.requestPermissions(this, permissionsList, READ_WRITE_PERMISSION_CODE);
            }else if (grantResults[0] == RESULT_OK){
                Utils.loadMedia();
            }

        }
    }



    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
//        fileObserver.stopWatching();

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    public String getWhatsappBusinessFolder() {

        if (android.os.Build.VERSION.SDK_INT >= 29) {
            if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp.w4b/WhatsApp Business" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
                return "Android%2Fmedia%2Fcom.whatsapp.w4b%2FWhatsApp Business%2FMedia%2F.Statuses";
            }
        } else {
            return "WhatsApp Business%2FMedia%2F.Statuses";
        }

        return "";

    }

    public String getWhatsappFolder() {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            if (new File(Environment.getExternalStorageDirectory() + File.separator + "Android/media/com.whatsapp/WhatsApp" + File.separator + "Media" + File.separator + ".Statuses").isDirectory()) {
                return "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses";
            }
        } else {
            return "WhatsApp%2FMedia%2F.Statuses";
        }
        return "";
    }

    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers();
        } else {
            exitDialog();

        }

    }


    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setTitle("Exit App");
        builder.setMessage("Are You want to Exit?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });
        builder.setNeutralButton("Rate Us", (dialog, which) -> {
            dialog.dismiss();

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.closeDrawers();
        switch (item.getItemId()) {

            case R.id.actionWhatsapp:

                binding.appBarMain.viewPager.setCurrentItem(0);
                return true;
            case R.id.actionWhatsappB:

                binding.appBarMain.viewPager.setCurrentItem(1);
                return true;
            case R.id.actionSaved:

                binding.appBarMain.viewPager.setCurrentItem(2);
                return true;

            case R.id.actionAscii:

                Intent intent8 = new Intent(MainActivity.this, AsciiCategoryActivity.class);

                if (AdmobAdsManager.isAdmob) {

                    AdmobAdsManager.showInterAd(this, intent8);
                } else {
                    startActivity(intent8);
                }
                return true;


            case R.id.actionWhatsScan:
                Intent intent = new Intent(MainActivity.this, ScanWhatsappActivity.class);

                if (AdmobAdsManager.isAdmob) {

                    AdmobAdsManager.showInterAd(this, intent);
                } else {
                    startActivity(intent);
                }


                return true;

            case R.id.actionDirectChat:
                Intent intent1 = new Intent(MainActivity.this, ChatDirectActivity.class);

                if (AdmobAdsManager.isAdmob) {

                    AdmobAdsManager.showInterAd(this, intent1);
                } else {
                    startActivity(intent1);
                }

                return true;


            case R.id.actionStylishFont:

                Intent intent2 = new Intent(MainActivity.this, StylishFontsActivity.class);

                if (AdmobAdsManager.isAdmob) {

                    AdmobAdsManager.showInterAd(this, intent2);
                } else {
                    startActivity(intent2);
                }

                return true;

            case R.id.actionTxtRepeater:
                Intent intent3 = new Intent(MainActivity.this, TextRepeaterActivity.class);

                if (AdmobAdsManager.isAdmob) {

                    AdmobAdsManager.showInterAd(this, intent3);
                } else {
                    startActivity(intent3);
                }

                return true;

            case R.id.actionTxtEmoji:
                Intent intent4 = new Intent(MainActivity.this, TextToEmojiActivity.class);

                if (AdmobAdsManager.isAdmob) {

                    AdmobAdsManager.showInterAd(this, intent4);
                } else {
                    startActivity(intent4);
                }

                return true;


        }

        return false;

    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new WAStatusFragment();
            } else if (position == 1) {
                fragment = new WBStatusFragment();
            } else if (position == 2) {
                fragment = new SavedStatusFragment();
            }
            assert fragment != null;
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }


}