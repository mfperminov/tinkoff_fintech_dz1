package xyz.mperminov.tinkoff_perminov_dz1

import android.app.IntentService
import android.content.Intent
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class ContactService : IntentService("contact_service") {
    override fun onHandleIntent(intent: Intent?) {
        val contacts = ArrayList<Contact>()
        val contentResolver = this.contentResolver
        val cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null)
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val cursorInfo = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf<String>(id), null
                    )
                    if (cursorInfo != null)
                        while (cursorInfo.moveToNext()) {
                            val contact = Contact(
                                id,
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                                cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            )
                            contacts.add(contact)
                        }
                    cursorInfo?.close()
                }
            }
            cursor.close()
        }

        val intentToSend = Intent(INTENT_FILTER_ACTION)
        if (contacts.isNotEmpty()) intentToSend.putExtra(INTENT_CONTACTS_KEY, contacts) else intentToSend.putExtra(
            INTENT_ERROR_KEY, getString(R.string.no_contacts_found)
        )
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentToSend)
    }
}