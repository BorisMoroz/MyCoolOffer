package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Region(
    val regionId: String,
    val parentId: String?,
    val regionName: String,
) : Parcelable
