package com.example.domain.repository

import com.example.domain.model.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getAuthenticatedUser(): Flow<UserDomainModel?>
    suspend fun loginWithGoogle(idToken: String, email: String, name: String): Result<UserDomainModel>
    suspend fun loginWithEmail(email: String, password: String): Result<UserDomainModel>
    suspend fun registerWithEmail(email: String, password: String, name: String, role: UserRole): Result<UserDomainModel>
    suspend fun logout(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
}

interface UniversityRepository {
    fun getAllUniversities(): Flow<List<UniversityDomainModel>>
    fun getFavoriteUniversities(): Flow<List<UniversityDomainModel>>
    suspend fun toggleFavorite(id: Int, isFav: Boolean)
    suspend fun addUniversity(university: UniversityDomainModel): Result<Unit>
    suspend fun updateUniversity(university: UniversityDomainModel): Result<Unit>
    suspend fun deleteUniversity(id: Int): Result<Unit>
}

interface StudentProfileRepository {
    fun getStudentProfile(): Flow<StudentProfileDomainModel?>
    suspend fun updateStudentProfile(profile: StudentProfileDomainModel): Result<Unit>
}

interface ApplicationRepository {
    fun getAllApplications(): Flow<List<ApplicationDomainModel>>
    suspend fun createApplication(app: ApplicationDomainModel): Result<Unit>
    suspend fun updateApplicationStatus(appId: Int, status: ApplicationStatus, reviewerNotes: String): Result<Unit>
    suspend fun deleteApplication(id: Int): Result<Unit>
}

interface DocumentRepository {
    fun getAllDocuments(): Flow<List<DocumentDomainModel>>
    suspend fun uploadDocument(name: String, category: String, fileName: String, fileSizeMb: Double, fileBytes: ByteArray?): Result<DocumentDomainModel>
    suspend fun deleteDocument(id: Int): Result<Unit>
    suspend fun verifyDocument(id: Int, status: String, notes: String): Result<Unit>
}

interface BookingRepository {
    fun getAllBookings(): Flow<List<BookingDomainModel>>
    suspend fun createBooking(expertName: String, expertTitle: String, scheduledAt: String, topic: String): Result<BookingDomainModel>
    suspend fun updateBookingStatus(id: Int, status: String): Result<Unit>
}

interface ExpertRepository {
    fun getAllExperts(): Flow<List<ExpertDomainModel>>
    suspend fun addExpert(expert: ExpertDomainModel): Result<Unit>
}

interface SeminarRepository {
    fun getAllSeminars(): Flow<List<SeminarDomainModel>>
    suspend fun toggleRegistration(id: Int, isReg: Boolean): Result<Unit>
    suspend fun addSeminar(seminar: SeminarDomainModel): Result<Unit>
}

interface AdminRepository {
    suspend fun getAdminStats(): Result<AdminStatsDomainModel>
}

interface SignalingRepository {
    fun connectToRoom(roomId: String, userId: String): Flow<WebRtcSignalMessage>
    suspend fun sendSdpOffer(roomId: String, sdp: String): Result<Unit>
    suspend fun sendSdpAnswer(roomId: String, sdp: String): Result<Unit>
    suspend fun sendIceCandidate(roomId: String, candidate: String, sdpMid: String, sdpMLineIndex: Int): Result<Unit>
    suspend fun sendChatMessage(roomId: String, sender: String, message: String): Result<Unit>
    suspend fun disconnectFromRoom(): Result<Unit>
}

data class WebRtcSignalMessage(
    val type: String, // OFFER, ANSWER, ICE_CANDIDATE, CHAT, PEER_JOINED, PEER_LEFT
    val sender: String,
    val payload: String = "",
    val timestamp: String = ""
)
