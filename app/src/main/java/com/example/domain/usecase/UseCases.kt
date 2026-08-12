package com.example.domain.usecase

import com.example.domain.model.*
import com.example.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.math.min

class EvaluateEligibilityUseCase {

    fun execute(
        student: StudentProfileDomainModel,
        university: UniversityDomainModel
    ): EligibilityResultDomainModel {
        val sscPass = student.sscGpa >= university.minSscGpa
        val hscPass = student.hscGpa >= university.minHscGpa
        val gpaPass = sscPass && hscPass

        val ieltsPass = student.ieltsOverall >= university.minIelts

        val subBandsPass = student.ieltsReading >= university.minReading &&
                student.ieltsWriting >= university.minWriting &&
                student.ieltsListening >= university.minListening &&
                student.ieltsSpeaking >= university.minSpeaking

        val budgetPass = student.maxTuitionBudgetUsd >= university.tuitionFeeUsd

        // Mathematical Weighted Match Calculation (0 - 100%)
        val sscRatio = min(1.0, student.sscGpa / university.minSscGpa)
        val hscRatio = min(1.0, student.hscGpa / university.minHscGpa)
        val gpaScore = ((sscRatio + hscRatio) / 2.0) * 35.0

        val ieltsRatio = min(1.0, student.ieltsOverall / university.minIelts)
        val ieltsScore = ieltsRatio * 35.0

        val subBandAvg = (
                min(1.0, student.ieltsReading / university.minReading) +
                min(1.0, student.ieltsWriting / university.minWriting) +
                min(1.0, student.ieltsListening / university.minListening) +
                min(1.0, student.ieltsSpeaking / university.minSpeaking)
        ) / 4.0
        val subBandScore = subBandAvg * 15.0

        val budgetRatio = if (university.tuitionFeeUsd == 0) 1.0 else min(1.0, student.maxTuitionBudgetUsd.toDouble() / university.tuitionFeeUsd.toDouble())
        val budgetScore = budgetRatio * 15.0

        val totalMatchPercent = (gpaScore + ieltsScore + subBandScore + budgetScore).toInt().coerceIn(10, 100)

        // Status determination
        val gpaGap = (university.minHscGpa - student.hscGpa).coerceAtLeast(0.0)
        val ieltsGap = (university.minIelts - student.ieltsOverall).coerceAtLeast(0.0)

        val status = when {
            gpaPass && ieltsPass && subBandsPass && budgetPass -> EligibilityStatus.DIRECT_MATCH
            gpaGap <= 0.5 && ieltsGap <= 0.5 -> EligibilityStatus.CONDITIONAL
            else -> EligibilityStatus.REACH
        }

        val summaryReason = when (status) {
            EligibilityStatus.DIRECT_MATCH -> "Your academic GPA (${student.hscGpa}), IELTS score (${student.ieltsOverall}), and tuition budget fully satisfy ${university.name}'s admissions criteria."
            EligibilityStatus.CONDITIONAL -> "You satisfy core entry levels with minor gap (GPA gap: ${"%.1f".format(gpaGap)}, IELTS gap: ${"%.1f".format(ieltsGap)}). Eligible for ESL Pre-sessional pathway."
            EligibilityStatus.REACH -> "${university.name}'s competitive entry criteria exceeds your current profile scores. Consider upgrading IELTS or taking pathway foundation credits."
        }

        return EligibilityResultDomainModel(
            universityId = university.id,
            universityName = university.name,
            status = status,
            matchScorePercent = totalMatchPercent,
            gpaMatch = gpaPass,
            ieltsMatch = ieltsPass,
            subBandsMatch = subBandsPass,
            budgetMatch = budgetPass,
            summaryReason = summaryReason,
            offersEslPathway = status == EligibilityStatus.CONDITIONAL || status == EligibilityStatus.DIRECT_MATCH
        )
    }
}

class AuthenticateUserUseCase(
    private val authRepository: AuthRepository
) {
    fun getAuthenticatedUser(): Flow<UserDomainModel?> = authRepository.getAuthenticatedUser()

    suspend fun loginWithGoogle(idToken: String, email: String, name: String): Result<UserDomainModel> {
        return authRepository.loginWithGoogle(idToken, email, name)
    }

    suspend fun loginWithEmail(email: String, password: String): Result<UserDomainModel> {
        return authRepository.loginWithEmail(email, password)
    }

    suspend fun registerWithEmail(email: String, password: String, name: String, role: UserRole): Result<UserDomainModel> {
        // Enforce public registration security rule: role is forced to STUDENT unless approved
        val safeRole = if (role == UserRole.ADMIN) UserRole.STUDENT else role
        return authRepository.registerWithEmail(email, password, name, safeRole)
    }

    suspend fun logout(): Result<Unit> = authRepository.logout()

    suspend fun resetPassword(email: String): Result<Unit> = authRepository.resetPassword(email)
}

class SubmitApplicationUseCase(
    private val applicationRepository: ApplicationRepository
) {
    suspend fun execute(
        universityName: String,
        programmeName: String,
        country: String,
        intake: String,
        deadline: String,
        notes: String
    ): Result<Unit> {
        val app = ApplicationDomainModel(
            universityName = universityName,
            programmeName = programmeName,
            country = country,
            intake = intake,
            status = ApplicationStatus.SUBMITTED,
            appliedDate = "2026-08-11",
            deadline = deadline,
            notes = notes
        )
        return applicationRepository.createApplication(app)
    }
}

class UploadDocumentUseCase(
    private val documentRepository: DocumentRepository
) {
    suspend fun execute(
        name: String,
        category: String,
        fileName: String,
        fileSizeMb: Double,
        fileBytes: ByteArray?
    ): Result<DocumentDomainModel> {
        return documentRepository.uploadDocument(name, category, fileName, fileSizeMb, fileBytes)
    }
}

class BookConsultationUseCase(
    private val bookingRepository: BookingRepository
) {
    suspend fun execute(
        expertName: String,
        expertTitle: String,
        scheduledAt: String,
        topic: String
    ): Result<BookingDomainModel> {
        return bookingRepository.createBooking(expertName, expertTitle, scheduledAt, topic)
    }
}

class AdminManagementUseCase(
    private val universityRepository: UniversityRepository,
    private val applicationRepository: ApplicationRepository,
    private val documentRepository: DocumentRepository,
    private val adminRepository: AdminRepository
) {
    suspend fun getStats(): Result<AdminStatsDomainModel> = adminRepository.getAdminStats()

    suspend fun addUniversity(uni: UniversityDomainModel): Result<Unit> = universityRepository.addUniversity(uni)

    suspend fun updateApplicationStatus(appId: Int, status: ApplicationStatus, notes: String): Result<Unit> {
        return applicationRepository.updateApplicationStatus(appId, status, notes)
    }

    suspend fun verifyDocument(docId: Int, status: String, notes: String): Result<Unit> {
        return documentRepository.verifyDocument(docId, status, notes)
    }
}
