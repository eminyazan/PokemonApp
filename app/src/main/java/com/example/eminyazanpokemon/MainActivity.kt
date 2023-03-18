package com.example.eminyazanpokemon

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.eminyazanpokemon.databinding.ActivityMainBinding
import com.example.eminyazanpokemon.manager.AppConnectivityManager
import com.example.eminyazanpokemon.service.FloatingWindowApp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appConnectivityManager: AppConnectivityManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialog: AlertDialog

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        binding.fragmentContainerView.visibility = View.VISIBLE
//        supportFragmentManager.commit {
//            setReorderingAllowed(true)
//            add<PokemonListFragment>(R.id.fragment_container_view)
//        }

//        //connection manager
//        appConnectivityManager = AppConnectivityManager(this)
//
//
//        appConnectivityManager.observe(this) { isNetworkAvailable ->
//            isNetworkAvailable?.let { internet ->
//                if (internet) {
//
//                    // kill service
//                    if (isServiceRunning()) {
//                        stopService(Intent(this@MainActivity, FloatingWindowApp::class.java))
//                    }
//
//
//                    if (checkPermission()) {
//                        //show fragment we have permission and internet
//                        binding.fragmentContainerView.visibility = View.VISIBLE
//                        supportFragmentManager.commit {
//                            setReorderingAllowed(true)
//                            add<PokemonListFragment>(R.id.fragment_container_view)
//                        }
//
//                        //close permission button we have permission
//                        binding.btnOverlayPermissionButton.visibility = View.GONE
//
//
//                        //we have internet
//                        binding.btnNoInternetRetry.visibility = View.GONE
//                        binding.tvNoInternet.visibility = View.GONE
//
//
//                    } else {
//                        //we have internet we do not have permission
//                        binding.btnNoInternetRetry.visibility = View.GONE
//                        binding.tvNoInternet.visibility = View.GONE
//
//                        //Show button we do not have permission
//                        binding.btnOverlayPermissionButton.visibility = View.VISIBLE
//                        binding.btnOverlayPermissionButton.setOnClickListener {
//                            requestPermission()
//                        }
//
//                    }
//
//
//                } else {
//                    // No internet
//                    binding.fragmentContainerView.visibility = View.GONE
//                    binding.btnOverlayPermissionButton.visibility = View.GONE
//
//                    binding.btnNoInternetRetry.visibility = View.VISIBLE
//                    binding.tvNoInternet.visibility = View.VISIBLE
//
//                    binding.btnNoInternetRetry.setOnClickListener {
//                        appConnectivityManager.checkValidNetworks()
//                    }
//                }
//
//            }
//        }

    }

    private fun isServiceRunning(): Boolean {
        // is floating window is working
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (FloatingWindowApp::class.java.name == service.service.packageName) {
                return true
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        // checks for permission
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Permission Required!")
        builder.setMessage("Enable Display over apps from settings")
        builder.setPositiveButton("OK") { _, _ ->

            // open settings with package name
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, RESULT_OK)
        }
        dialog = builder.create()
        dialog.show()

    }


    private fun checkPermission(): Boolean {
        // we have permission after M otherwise it is false automatically
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            return false
        }
    }
}