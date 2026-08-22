package com.rohitneel.photopixelpro.activities

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.rohitneel.photopixelpro.BuildConfig
import com.rohitneel.photopixelpro.R
import com.rohitneel.photopixelpro.curvedbottomnavigation.CbnMenuItem
import com.rohitneel.photopixelpro.curvedbottomnavigation.CurvedBottomNavigationView
import com.rohitneel.photopixelpro.gallery.PhotoEditorGallery
import com.rohitneel.photopixelpro.helper.SessionManager
import com.rohitneel.photopixelpro.photocollage.dialog.RateDialog
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var mSession: SessionManager? = null
    var toolbar: Toolbar? = null
    private var headerTextTitle: TextView? = null
    private var appUpdateManager: AppUpdateManager? = null
    private val RC_APP_UPDATE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            toolbar?.setPadding(0, systemBars.top, 0, 0)
            findViewById<View>(R.id.bottom_nav_view)?.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        val activeIndex = savedInstanceState?.getInt("activeIndex") ?: 0
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager!!.appUpdateInfo.addOnSuccessListener { result ->
            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                try {
                    appUpdateManager?.startUpdateFlowForResult(
                        result,
                        AppUpdateType.IMMEDIATE,
                        this,
                        RC_APP_UPDATE
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            }
        }

        appUpdateManager?.registerListener(installStateUpdatedListener)

        val drawer = findViewById<DrawerLayout>(R.id.main)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        mSession = SessionManager(applicationContext)

        if (mSession!!.loadFullScreenState()) {
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val bottomNavigationView = findViewById<CurvedBottomNavigationView>(R.id.bottom_nav_view)

        if(navigationView.getHeaderView(0)!=null) {
            headerTextTitle = navigationView.getHeaderView(0).findViewById<View>(R.id.txtHeader) as TextView
        }
        navigationView.menu.findItem(R.id.nav_rate_us).setOnMenuItemClickListener {
            showRateUs(this@MainActivity)
            true
        }

        headerTextTitle!!.setOnClickListener {
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
                R.id.nav_home, R.id.bottom_dashboard, R.id.bottom_notifications)
                .setDrawerLayout(drawer)
                .build()
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)

        val menuItems = arrayOf(
            CbnMenuItem(
                R.drawable.ic_outline_home_24,
                R.drawable.avd_home,
                R.id.nav_home,
                "Home"
            ),
            CbnMenuItem(
                R.drawable.outline_filter_frames_24,
                R.drawable.avd_photo_frame,
                R.id.bottom_dashboard,
                "Photo Frame"
            ),
            CbnMenuItem(
                R.drawable.ic_background_remover,
                R.drawable.avd_background_remover,
                R.id.bottom_notifications,
                "BG Eraser"
            )
        )
        bottomNavigationView.setMenuItems(menuItems, activeIndex)
        bottomNavigationView.setupWithNavController(navController)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_gallery -> startActivity(Intent(applicationContext, PhotoEditorGallery::class.java))
                R.id.nav_settings -> startActivity(Intent(applicationContext, SettingsActivity::class.java))
                R.id.nav_exit -> {
                    moveTaskToBack(true)
                    finish()
                    val i = Intent(Intent.ACTION_MAIN)
                    i.addCategory(Intent.CATEGORY_HOME)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)
                }
                R.id.nav_share -> {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Photo Pixel Pro")
                    var shareMessage = "Let me recommend you this application. It is easy and amazing app for creative photo editing. Download & check it out!\n".trimIndent()
                    shareMessage = """
                    ${shareMessage} https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    """.trimIndent()
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                }
                R.id.nav_more_apps -> {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_string))))
                    } catch (e: ActivityNotFoundException) {
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_developer_string))))
                        } catch (e: Throwable) {
                            Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Throwable) {
                        Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            val drawer = findViewById<View>(R.id.main) as DrawerLayout
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@MainActivity.onBackPressed()
            }
        })
    }

    private val installStateUpdatedListener =
        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                showCompletedUpdate()
            }
        }

    override fun onStop() {
        appUpdateManager?.unregisterListener(installStateUpdatedListener)
        super.onStop()
    }

    private fun showCompletedUpdate() {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "Update downloaded. Please restart to complete!",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Restart") {
            appUpdateManager?.completeUpdate()
        }
        snackbar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_APP_UPDATE && resultCode != RESULT_OK) {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        if (mSession!!.loadFullScreenState()) {
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    override fun onBackPressed() {
        val dialogView = layoutInflater.inflate(
            R.layout.dialog_exit_confirmation,
            null
        )
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        dialog.window?.setWindowAnimations(R.style.AnimationsForDialog)
        dialog.setOnShowListener {
            dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            dialog.window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.92).toInt(),
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
        val btnNo = dialogView.findViewById<TextView>(R.id.btnNo)
        val btnYes = dialogView.findViewById<TextView>(R.id.btnYes)
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        btnYes.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }
        dialog.show()
        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.92).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    private fun showRateUs(context: Context?) {
        RateDialog(this, false).show()
    }


}