package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "current_user",
    val name: String = "Alex Rivera",
    val email: String = "alex.rivera@student.ishc.org",
    val role: String = "STUDENT", // STUDENT, EXPERT, ADMIN
    val avatarUrl: String = "",
    val googleId: String = "google_1029384756",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "student_profiles")
data class StudentProfileEntity(
    @PrimaryKey val id: Int = 1,
    val userId: String = "current_user",
    val sscGpa: Double = 5.0,
    val hscGpa: Double = 4.8,
    val ieltsOverall: Double = 7.0,
    val ieltsReading: Double = 7.5,
    val ieltsWriting: Double = 6.5,
    val ieltsListening: Double = 7.5,
    val ieltsSpeaking: Double = 7.0,
    val preferredCountries: String = "USA, UK, Canada, Germany",
    val journeyStage: Int = 2 // 1..6
)

@Entity(tableName = "universities")
data class UniversityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val country: String,
    val city: String,
    val website: String,
    val applyUrl: String,
    val minSscGpa: Double,
    val minHscGpa: Double,
    val minIelts: Double,
    val tuitionFeeUsd: Int,
    val intakes: String,
    val ranking: Int,
    val isFavorite: Boolean = false,
    val description: String = "",
    val popularProgrammes: String = "Computer Science, Data Science, Business Analytics"
)

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String, // TRANSCRIPT, IELTS_CERT, SOP, PASSPORT, LOR
    val fileName: String,
    val fileSizeMb: Double,
    val dateAdded: String,
    val status: String = "VERIFIED" // UPLOADED, VERIFIED, PENDING_REVIEW
)

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val universityName: String,
    val programmeName: String,
    val country: String,
    val intake: String,
    val status: String, // DRAFT, SUBMITTED, UNDER_REVIEW, ACCEPTED, OFFER_ISSUED
    val appliedDate: String,
    val deadline: String,
    val notes: String = ""
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val expertName: String,
    val expertTitle: String,
    val expertAvatar: String = "",
    val scheduledAt: String,
    val meetingUrl: String,
    val status: String, // PENDING, CONFIRMED, COMPLETED, CANCELLED
    val topic: String,
    val isWebRtcReady: Boolean = true
)

@Entity(tableName = "seminars")
data class SeminarEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val speaker: String,
    val date: String,
    val time: String,
    val topic: String,
    val isRegistered: Boolean = false,
    val bannerColorHex: String = "#1E293B"
)

@Entity(tableName = "experts")
data class ExpertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val title: String,
    val countrySpecialization: String,
    val rating: Double,
    val totalConsultations: Int,
    val bio: String,
    val availability: String
)
