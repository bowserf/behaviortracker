package fr.bowser.behaviortracker.explain_permission_request

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes

data class ExplainPermissionRequestModel(
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

    companion object CREATOR : Parcelable.Creator<ExplainPermissionRequestModel> {
        override fun createFromParcel(parcel: Parcel): ExplainPermissionRequestModel {
            return ExplainPermissionRequestModel(parcel)
        }

        override fun newArray(size: Int): Array<ExplainPermissionRequestModel?> {
            return arrayOfNulls(size)
        }
    }
}
