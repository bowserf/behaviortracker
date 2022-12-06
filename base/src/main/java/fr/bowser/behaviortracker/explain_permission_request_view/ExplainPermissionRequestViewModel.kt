package fr.bowser.behaviortracker.explain_permission_request_view

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes

data class ExplainPermissionRequestViewModel(
    val title: String,
    val message: String,
    @DrawableRes val icon: Int,
    val permissions: List<String>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.createStringArrayList()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeInt(icon)
        parcel.writeStringList(permissions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExplainPermissionRequestViewModel> {
        override fun createFromParcel(parcel: Parcel): ExplainPermissionRequestViewModel {
            return ExplainPermissionRequestViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ExplainPermissionRequestViewModel?> {
            return arrayOfNulls(size)
        }
    }
}
