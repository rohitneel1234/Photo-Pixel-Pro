package com.rohitneel.photopixelpro.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rohitneel.photopixelpro.R
import com.rohitneel.photopixelpro.constant.CommonKeys
import com.rohitneel.photopixelpro.helper.SessionManager
import com.rohitneel.photopixelpro.tutorials.TutorialActivity
import com.rohitneel.photopixelpro.photoeditor.ShowSavedFilePath
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.system.measureTimeMillis


class SettingsActivity : AppCompatActivity() {
    private var mSwitchCompat: SwitchCompat? = null
    private var mSession: SessionManager? = null
    private var constraintLayout: ConstraintLayout? = null
    private var mClSaveOption : ConstraintLayout? = null
    private var mClHelpAndSupport : ConstraintLayout? = null
    private var mSwitchFullScreen: SwitchCompat? = null
    private var mSwitchAppPermission: SwitchCompat? = null
    private val REQUEST_PERMISSION_CODE = 7
    private var mTxtTutorial: TextView? = null
    private var mTxtDark: TextView? = null
    private var mTxtFullScreen: TextView? = null
    private var mTxtSave: TextView? = null
    private var mTxtPermission: TextView? = null
    private var mTxtContactUs: TextView? = null
    private var mTxtAppSettingsTitle: TextView? = null
    private var mTxtSaveTitle: TextView? = null
    private var mTxtPermissionTitle: TextView? = null
    private var mTxtAppHelpTitle: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mSession = SessionManager(applicationContext)
        mSwitchCompat = findViewById<View>(R.id.switchTheme) as SwitchCompat
        mSwitchFullScreen = findViewById<View>(R.id.switchFullScreen) as SwitchCompat
        mSwitchAppPermission = findViewById<View>(R.id.switchAppPermission) as SwitchCompat
        constraintLayout = findViewById<ConstraintLayout>(R.id.clTutorial)
        mClSaveOption = findViewById<ConstraintLayout>(R.id.clSaveOption)
        mClHelpAndSupport = findViewById<ConstraintLayout>(R.id.clHelpAndSupport);
        mTxtTutorial = findViewById<TextView>(R.id.txtTutorial)
        mTxtDark = findViewById<TextView>(R.id.txtDarkMode)
        mTxtFullScreen = findViewById<TextView>(R.id.txtFullScreen)
        mTxtSave = findViewById<TextView>(R.id.txtSaveOption)
        mTxtPermission = findViewById<TextView>(R.id.txtAppPermission)
        mTxtContactUs =  findViewById<TextView>(R.id.txtContactUs)
        mTxtAppSettingsTitle = findViewById(R.id.txtAppSettingsTitle);
        mTxtSaveTitle = findViewById(R.id.txtSaveTitle);
        mTxtPermissionTitle = findViewById(R.id.txtPermissionTitle);
        mTxtAppHelpTitle = findViewById(R.id.txtHelpTitle);

        val actionBar: ActionBar? = supportActionBar

       /* val colorDrawable = ColorDrawable(Color.parseColor("#DC4C25"))
        actionBar!!.setBackgroundDrawable(colorDrawable);*/

        constraintLayout!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, TutorialActivity::class.java)
            intent.putExtra(CommonKeys.mSettingsKey, "settings")
            startActivity(intent)
        })

        mClSaveOption!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, ShowSavedFilePath::class.java))
        })

        mClHelpAndSupport!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ContactUs::class.java))
        })

        if (mSession!!.loadState()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.darkTheme)
            mTxtTutorial!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtDark!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtFullScreen!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtSave!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtPermission!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtContactUs!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtAppSettingsTitle!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtSaveTitle!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtPermissionTitle!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtAppHelpTitle!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.AppTheme)
            mTxtTutorial!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtDark!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtFullScreen!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtSave!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtPermission!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtContactUs!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
        }

        //calling dark theme state to load and save
        loadDarkThemeState()

        if (mSession!!.loadFullScreenState()) {
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //Calling full screen state to load and save
        loadFullScreenState()

        loadAppPermission()

        GlobalScope.launch {
            val time = measureTimeMillis {
                val one = sampleOne()
                val two = sampleTwo()
                println("The answer is ${one + two}")
            }
            println("Completed in $time ms")
        }
        println("EOF")
    }

    private suspend fun sampleOne(): Int {
        println( "sampleOne"+System.currentTimeMillis())
        delay(1000L) // pretend we are doing something useful here
        return 10
    }

    private suspend fun sampleTwo(): Int {
        println( "sampleTwo"+System.currentTimeMillis())
        delay(1000L) // pretend we are doing something useful here, too
        return 10
    }


    private fun loadAppPermission() {
        if (mSession!!.loadAppPermissionState() && checkingPermissionIsEnabledOrNot()) {
            mSwitchAppPermission!!.isChecked = true
        }
        mSwitchAppPermission!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mSession!!.saveAppPermissionState(true)
                requestMultiplePermission()
            }
            else {
                mSession!!.saveAppPermissionState(false)
            }
        }
    }

    private fun requestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), REQUEST_PERMISSION_CODE)
    }

    private fun checkingPermissionIsEnabledOrNot(): Boolean {
        val firstPermissionResult = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        val secondPermissionResult = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return firstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                secondPermissionResult == PackageManager.PERMISSION_GRANTED
    }

    private fun loadFullScreenState() {
        if (mSession!!.loadFullScreenState()) {
            mSwitchFullScreen!!.isChecked = true
        }

        mSwitchFullScreen!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                this.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mSession!!.saveFullScreenState(true)
            } else {
                this.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mSession!!.saveFullScreenState(false)
            }
        }
    }

    private fun loadDarkThemeState() {
        if (mSession!!.loadState()) {
            mSwitchCompat!!.isChecked = true
        }

        mSwitchCompat!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mSession!!.saveState(true)
                recreate()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mSession!!.saveState(false)
            }
        }
    }

}