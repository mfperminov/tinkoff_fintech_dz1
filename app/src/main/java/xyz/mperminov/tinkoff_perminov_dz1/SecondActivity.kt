package xyz.mperminov.tinkoff_perminov_dz1

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_second.*

const val INTENT_CONTACTS_KEY = "contacts"
const val INTENT_ERROR_KEY = "error"
const val INTENT_FILTER_ACTION = "send contacts"


class SecondActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 4353

    private val contactsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            hideLoading()
            if (intent.getStringExtra(INTENT_ERROR_KEY) != null)
                sendError(intent.getStringExtra(INTENT_ERROR_KEY))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            contactsReceiver,
            IntentFilter(INTENT_FILTER_ACTION)
        )
        if (hasNoPermissionsToReadContacts()) {
            requestPermissionsToReadContacts()
        } else {
            showLoading()
            startService(Intent(this, ContactService::class.java))
        }
        super.onResume()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(contactsReceiver)
        super.onPause()
    }

    private fun requestPermissionsToReadContacts() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSIONS_REQUEST_READ_CONTACTS
        )
    }

    private fun hasNoPermissionsToReadContacts(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        )
            != PackageManager.PERMISSION_GRANTED)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showLoading()
                    startService(Intent(this, ContactService::class.java))
                } else {
                    sendError(getString(R.string.no_permissions))
                }
                return
            }
        }
    }

    private fun sendError(errorMessage: String) {
        val intent = Intent()
        intent.putExtra(INTENT_ERROR_KEY, errorMessage)
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    private fun showLoading() {
        pBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        pBar.visibility = View.GONE
    }
}
