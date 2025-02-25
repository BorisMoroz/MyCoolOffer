package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDetailsResponse(
    val name: String,
    val salary: SalaryDto?,
    val employer: EmployerDto?,
    val area: AreaDto?,
    val address: AddressDto?,
    val experience: ExperienceDto?,
    val employment: EmploymentDto?,
    val schedule: ScheduleDto?,
    val description: String?,
    @SerializedName("key_skills") val keySkills: ArrayList<KeySkillDto?>?
) : Response()
