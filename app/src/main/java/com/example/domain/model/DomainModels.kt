package com.example.domain.model

enum class UserRole {
    STUDENT,
    EXPERT,
    ADMIN
}

data class UserDomainModel(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val avatarUrl: String = "",
    val googleId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class StudentProfileDomainModel(
    val id: Int = 1,
    val userId: String = "",
    val sscGpa: Double = 5.0,
    val hscGpa: Double = 4.8,
    val ieltsOverall: Double = 7.0,
    val ieltsReading: Double = 7.5,
    val ieltsWriting: Double = 6.5,
    val ieltsListening: Double = 7.5,
    val ieltsSpeaking: Double = 7.0,
    val preferredCountries: String = "USA, UK, Canada, Germany",
    val maxTuitionBudgetUsd: Int = 50000,
    val journeyStage: Int = 2
)

data class UniversityDomainModel(
    val id: Int = 0,
    val name: String,
    val country: String,
    val city: String,
    val website: String,
    val applyUrl: String,
    val minSscGpa: Double,
    val minHscGpa: Double,
    val minIelts: Double,
    val minReading: Double = 6.0,
    val minWriting: Double = 6.0,
    val minListening: Double = 6.0,
    val minSpeaking: Double = 6.0,
    val tuitionFeeUsd: Int,
    val intakes: String,
    val ranking: Int,
    val isFavorite: Boolean = false,
    val description: String = "",
    val popularProgrammes: String = "Computer Science, Data Science, Business Analytics"
)

enum class EligibilityStatus {
    DIRECT_MATCH,
    CONDITIONAL,
    REACH
}

data class EligibilityResultDomainModel(
    val universityId: Int,
    val universityName: String,
    val status: EligibilityStatus,
    val matchScorePercent: Int,
    val gpaMatch: Boolean,
    val ieltsMatch: Boolean,
    val subBandsMatch: Boolean,
    val budgetMatch: Boolean,
    val summaryReason: String,
    val offersEslPathway: Boolean = true
)

enum class ApplicationStatus {
    DRAFT,
    SUBMITTED,
    UNDER_REVIEW,
    OFFER_ISSUED,
    ACCEPTED,
    REJECTED,
    WITHDRAWN
}

data class ApplicationDomainModel(
    val id: Int = 0,
    val universityName: String,
    val programmeName: String,
    val country: String,
    val intake: String,
    val status: ApplicationStatus,
    val appliedDate: String,
    val deadline: String,
    val notes: String = "",
    val offerLetterUrl: String = "",
    val reviewerNotes: String = ""
)

data class DocumentDomainModel(
    val id: Int = 0,
    val name: String,
    val category: String, // TRANSCRIPT, IELTS_CERT, SOP, PASSPORT, LOR
    val fileName: String,
    val fileSizeMb: Double,
    val dateAdded: String,
    val status: String = "VERIFIED", // UPLOADED, VERIFIED, PENDING_REVIEW, REJECTED
    val fileUrl: String = "",
    val reviewNotes: String = ""
)

data class BookingDomainModel(
    val id: Int = 0,
    val expertName: String,
    val expertTitle: String,
    val expertAvatar: String = "",
    val scheduledAt: String,
    val meetingUrl: String,
    val status: String, // PENDING, CONFIRMED, COMPLETED, CANCELLED
    val topic: String,
    val isWebRtcReady: Boolean = true,
    val roomId: String = ""
)

data class SeminarDomainModel(
    val id: Int = 0,
    val title: String,
    val speaker: String,
    val date: String,
    val time: String,
    val topic: String,
    val isRegistered: Boolean = false,
    val bannerColorHex: String = "#1E293B"
)

data class ExpertDomainModel(
    val id: Int = 0,
    val name: String,
    val title: String,
    val countrySpecialization: String,
    val rating: Double,
    val totalConsultations: Int,
    val bio: String,
    val availability: String,
    val isVerified: Boolean = true
)

data class AdminStatsDomainModel(
    val totalStudents: Int = 1250,
    val totalApplications: Int = 342,
    val totalVerifiedDocs: Int = 890,
    val activeConsultations: Int = 48,
    val acceptanceRatePercent: Int = 78
)
