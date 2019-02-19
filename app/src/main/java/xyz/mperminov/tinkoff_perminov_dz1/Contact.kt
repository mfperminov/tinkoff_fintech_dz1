package xyz.mperminov.tinkoff_perminov_dz1

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(val id: String, val name: String, val phone: String): Parcelable