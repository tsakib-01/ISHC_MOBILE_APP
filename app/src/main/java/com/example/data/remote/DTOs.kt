package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "email") val email: String,
    @Json(name = "role") val role: String,
    @Json(name = "avatar_url") val avatarUrl: String? = null,
    @Json(name = "google_id") val googleId: String? = null,
    @Json(name = "token") val token: String? = null
)

@JsonClass(generateAdapter = true)
data class StudentProfileDto(
    @Json(name = "id") val id: Int = 1,
    @Json(name = "user_id") val userId: String = "",
    @Json(name = "ssc_gpa") val sscGpa: Double = 5.0,
    @Json(name = "hsc_gpa") val hscGpa: Double = 4.8,
    @Json(name = "ielts_overall") val ieltsOverall: Double = 7.0,
    @Json(name = "ielts_reading") val ieltsReading: Double = 7.5,
    @Json(name = "ielts_writing") val ieltsWriting: Double = 6.5,
    @Json(name = "ielts_listening") val ieltsListening: Double = 7.5,
    @Json(name = "ielts_speaking") val ieltsSpeaking: Double = 7.0,
    @Json(name = "preferred_countries") val preferredCountries: String = "USA, UK, Canada, Germany",
    @Json(name = "max_budget") val maxBudgetUsd: Int = 50000,
    @Json(name = "journey_stage") val journeyStage: Int = 2
)

@JsonClass(generateAdapter = true)
data class UniversityDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") val name: String,
    @Json(name = "country") val country: String,
    @Json(name = "city") val city: String,
    @Json(name = "website") val website: String,
    @Json(name = "apply_url") val applyUrl: String,
    @Json(name = "min_ssc_gpa") val minSscGpa: Double,
    @Json(name = "min_hsc_gpa") val minHscGpa: Double,
    @Json(name = "min_ielts") val minIelts: Double,
    @Json(name = "tuition_fee_usd") val tuitionFeeUsd: Int,
    @Json(name = "intakes") val intakes: String,
    @Json(name = "ranking") val ranking: Int,
    @Json(name = "is_favorite") val isFavorite: Boolean = false,
    @Json(name = "description") val description: String = "",
    @Json(name = "popular_programmes") val popularProgrammes: String = "Computer Science"
)

@JsonClass(generateAdapter = true)
data class ApplicationDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "university_name") val universityName: String,
    @Json(name = "programme_name") val programmeName: String,
    @Json(name = "country") val country: String,
    @Json(name = "intake") val intake: String,
    @Json(name = "status") val status: String,
    @Json(name = "applied_date") val appliedDate: String,
    @Json(name = "deadline") val deadline: String,
    @Json(name = "notes") val notes: String = "",
    @Json(name = "offer_letter_url") val offerLetterUrl: String? = null,
    @Json(name = "reviewer_notes") val reviewerNotes: String? = null
)

@JsonClass(generateAdapter = true)
data class DocumentDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") val name: String,
    @Json(name = "category") val category: String,
    @Json(name = "file_name") val fileName: String,
    @Json(name = "file_size_mb") val fileSizeMb: Double,
    @Json(name = "date_added") val dateAdded: String,
    @Json(name = "status") val status: String = "VERIFIED",
    @Json(name = "file_url") val fileUrl: String? = null,
    @Json(name = "review_notes") val reviewNotes: String? = null
)

@JsonClass(generateAdapter = true)
data class BookingDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "expert_name") val expertName: String,
    @Json(name = "expert_title") val expertTitle: String,
    @Json(name = "scheduled_at") val scheduledAt: String,
    @Json(name = "meeting_url") val meetingUrl: String,
    @Json(name = "status") val status: String,
    @Json(name = "topic") val topic: String,
    @Json(name = "room_id") val roomId: String? = null
)

@JsonClass(generateAdapter = true)
data class SeminarDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "title") val title: String,
    @Json(name = "speaker") val speaker: String,
    @Json(name = "date") val date: String,
    @Json(name = "time") val time: String,
    @Json(name = "topic") val topic: String,
    @Json(name = "is_registered") val isRegistered: Boolean = false,
    @Json(name = "banner_color_hex") val bannerColorHex: String = "#1E293B"
)

@JsonClass(generateAdapter = true)
data class ExpertDto(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") val name: String,
    @Json(name = "title") val title: String,
    @Json(name = "country_specialization") val countrySpecialization: String,
    @Json(name = "rating") val rating: Double,
    @Json(name = "total_consultations") val totalConsultations: Int,
    @Json(name = "bio") val bio: String,
    @Json(name = "availability") val availability: String
)

@JsonClass(generateAdapter = true)
data class AdminStatsDto(
    @Json(name = "total_students") val totalStudents: Int,
    @Json(name = "total_applications") val totalApplications: Int,
    @Json(name = "total_verified_docs") val totalVerifiedDocs: Int,
    @Json(name = "active_consultations") val activeConsultations: Int,
    @Json(name = "acceptance_rate_percent") val acceptanceRatePercent: Int
)

@JsonClass(generateAdapter = true)
data class AuthRequestDto(
    @Json(name = "id_token") val idToken: String? = null,
    @Json(name = "email") val email: String? = null,
    @Json(name = "password") val password: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "role") val role: String? = null
)
