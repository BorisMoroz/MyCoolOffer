package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDetailsDto(
    val name: String,
    val salary: SalaryDto?,
    val employer: EmployerDto?,
    val area: AreaDto?,
    val experience: ExperienceDto?,
    val employment: EmploymentDto?,
    @SerializedName("work_format") val workFormat: WorkFormatDto?,
    val description: String?,
    @SerializedName("key_skills") val keySkills: ArrayList<KeySkillDto?>?
)
