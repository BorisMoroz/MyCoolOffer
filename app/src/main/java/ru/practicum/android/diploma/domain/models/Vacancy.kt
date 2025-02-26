package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vacancy(
    val vacancyId: String,
    val vacancyName: String,
    val area: String?,
    val employer: String?,
    val logoUrl: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?
) : Parcelable
