package xyz.mperminov.tinkoff_perminov_dz1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_viewholder.view.*

class ContactsAdapter(private val contacts: List<Contact>) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    class ContactViewHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ContactViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_viewholder, parent, false)
    )


    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.itemView.name.text = contacts[position].name
        holder.itemView.phone.text = contacts[position].phone

    }

    override fun getItemCount() = contacts.size
}