package xyz.mperminov.tinkoff_perminov_dz1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 616
    private val BUTTON_VISIILITY = "button_visibility"
    private var viewAdapter: RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>? = null
    private var viewManager: LinearLayoutManager? = null
    private var contacts: ArrayList<Contact>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            startSecondActivity()
        }
    }

    private fun startSecondActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE ->
                when (resultCode) {
                    Activity.RESULT_OK -> proceedWithResults(data)
                    Activity.RESULT_CANCELED -> showError(data?.getStringExtra(INTENT_ERROR_KEY))
                }
        }
    }

    private fun showError(errorMessage: String?) {
        Toast.makeText(this, errorMessage ?: getString(R.string.unknown_error), Toast.LENGTH_LONG).show()
    }

    private fun proceedWithResults(data: Intent?) {
        contacts = data?.getParcelableArrayListExtra<Contact>(INTENT_CONTACTS_KEY)
        if (!contacts.isNullOrEmpty()) {
            btn.visibility = View.GONE
            initRecyclerView(contacts!!)
        }
    }

    private fun initRecyclerView(contacts: ArrayList<Contact>) {
        viewManager = LinearLayoutManager(this)
        viewAdapter = ContactsAdapter(contacts.distinctBy { contact -> contact.id })

        findViewById<RecyclerView>(R.id.rv).apply {
            visibility = View.VISIBLE
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelableArrayList(INTENT_CONTACTS_KEY, contacts)
        outState?.putInt(BUTTON_VISIILITY, btn.visibility)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        contacts = savedInstanceState?.getParcelableArrayList(INTENT_CONTACTS_KEY)
        if (!contacts.isNullOrEmpty()) initRecyclerView(contacts!!)
        btn.visibility = savedInstanceState?.getInt(BUTTON_VISIILITY) ?: View.VISIBLE
        super.onRestoreInstanceState(savedInstanceState)
    }
}

