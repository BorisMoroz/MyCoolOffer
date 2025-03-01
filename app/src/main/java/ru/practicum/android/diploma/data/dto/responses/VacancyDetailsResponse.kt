package ru.practicum.android.diploma.data.dto.responses

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.AddressDto
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.EmployerDto
import ru.practicum.android.diploma.data.dto.EmploymentDto
import ru.practicum.android.diploma.data.dto.ExperienceDto
import ru.practicum.android.diploma.data.dto.KeySkillDto
import ru.practicum.android.diploma.data.dto.SalaryDto
import ru.practicum.android.diploma.data.dto.WorkFormatDto

data class VacancyDetailsResponse(
    val id: String,
    val name: String,
    val salary: SalaryDto?,
    val employer: EmployerDto?,
    val area: AreaDto?,
    val address: AddressDto?,
    val experience: ExperienceDto?,
    val employment: EmploymentDto?,
    @SerializedName("work_format") val workFormat: ArrayList<WorkFormatDto?>?,
    val description: String?,
    @SerializedName("key_skills") val keySkills: ArrayList<KeySkillDto?>?
) : Response()
