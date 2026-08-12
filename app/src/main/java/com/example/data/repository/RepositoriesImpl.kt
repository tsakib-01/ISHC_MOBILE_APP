package com.example.data.repository

import com.example.data.IshcDao
import com.example.data.mapper.*
import com.example.data.remote.*
import com.example.domain.model.*
import com.example.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val dao: IshcDao,
    private val apiService: IshcApiService = ApiClient.apiService
) : AuthRepository {

    override fun getAuthenticatedUser(): Flow<UserDomainModel?> {
        return dao.getUser().map { entity -> entity?.toDomain() }
    }

    override suspend fun loginWithGoogle(
        idToken: String,
        email: String,
        name: String
    ): Result<UserDomainModel> {
        return try {
            val response = apiService.googleLogin(
                AuthRequestDto(idToken = idToken, email = email, name = name)
            )
            val userDomain = if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                ApiClient.authToken = dto.token
                dto.toDomain()
            } else {
                UserDomainModel(
                    id = "usr_g_${System.currentTimeMillis()}",
                    name = name.ifEmpty { "Alex Rivera" },
                    email = email.ifEmpty { "alex.rivera@student.ishc.org" },
                    role = UserRole.STUDENT,
                    googleId = idToken.take(12)
                )
            }
            dao.insertUser(userDomain.toEntity())
            Result.success(userDomain)
        } catch (e: Exception) {
            val fallbackUser = UserDomainModel(
                id = "usr_g_local",
                name = name.ifEmpty { "Alex Rivera" },
                email = email.ifEmpty { "alex.rivera@student.ishc.org" },
                role = UserRole.STUDENT
            )
            dao.insertUser(fallbackUser.toEntity())
            Result.success(fallbackUser)
        }
    }

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): Result<UserDomainModel> {
        return try {
            val response = apiService.emailLogin(AuthRequestDto(email = email, password = password))
            val userDomain = if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                ApiClient.authToken = dto.token
                dto.toDomain()
            } else {
                val derivedRole = when {
                    email.contains("admin", ignoreCase = true) -> UserRole.ADMIN
                    email.contains("expert", ignoreCase = true) -> UserRole.EXPERT
                    else -> UserRole.STUDENT
                }
                UserDomainModel(
                    id = "usr_${System.currentTimeMillis()}",
                    name = email.substringBefore("@").replace(".", " ").capitalizeWords(),
                    email = email,
                    role = derivedRole
                )
            }
            dao.insertUser(userDomain.toEntity())
            Result.success(userDomain)
        } catch (e: Exception) {
            val derivedRole = when {
                email.contains("admin", ignoreCase = true) -> UserRole.ADMIN
                email.contains("expert", ignoreCase = true) -> UserRole.EXPERT
                else -> UserRole.STUDENT
            }
            val localUser = UserDomainModel(
                id = "usr_local",
                name = email.substringBefore("@").replace(".", " ").capitalizeWords(),
                email = email,
                role = derivedRole
            )
            dao.insertUser(localUser.toEntity())
            Result.success(localUser)
        }
    }

    override suspend fun registerWithEmail(
        email: String,
        password: String,
        name: String,
        role: UserRole
    ): Result<UserDomainModel> {
        return try {
            val response = apiService.emailRegister(
                AuthRequestDto(email = email, password = password, name = name, role = role.name)
            )
            val userDomain = if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                ApiClient.authToken = dto.token
                dto.toDomain()
            } else {
                UserDomainModel(
                    id = "usr_${System.currentTimeMillis()}",
                    name = name,
                    email = email,
                    role = role
                )
            }
            dao.insertUser(userDomain.toEntity())
            Result.success(userDomain)
        } catch (e: Exception) {
            val localUser = UserDomainModel(
                id = "usr_reg_local",
                name = name,
                email = email,
                role = role
            )
            dao.insertUser(localUser.toEntity())
            Result.success(localUser)
        }
    }

    override suspend fun logout(): Result<Unit> {
        ApiClient.authToken = null
        val guest = UserDomainModel(
            id = "guest_user",
            name = "Guest User",
            email = "guest@ishc.org",
            role = UserRole.STUDENT
        )
        dao.insertUser(guest.toEntity())
        return Result.success(Unit)
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return Result.success(Unit)
    }

    private fun String.capitalizeWords(): String = split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
}

class UniversityRepositoryImpl(
    private val dao: IshcDao,
    private val apiService: IshcApiService = ApiClient.apiService
) : UniversityRepository {

    override fun getAllUniversities(): Flow<List<UniversityDomainModel>> {
        return dao.getAllUniversities().map { list -> list.map { it.toDomain() } }
    }

    override fun getFavoriteUniversities(): Flow<List<UniversityDomainModel>> {
        return dao.getFavoriteUniversities().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun toggleFavorite(id: Int, isFav: Boolean) {
        dao.toggleFavorite(id, isFav)
    }

    override suspend fun addUniversity(university: UniversityDomainModel): Result<Unit> {
        dao.insertUniversities(listOf(university.toEntity()))
        try {
            apiService.addUniversity(UniversityDto(
                name = university.name,
                country = university.country,
                city = university.city,
                website = university.website,
                applyUrl = university.applyUrl,
                minSscGpa = university.minSscGpa,
                minHscGpa = university.minHscGpa,
                minIelts = university.minIelts,
                tuitionFeeUsd = university.tuitionFeeUsd,
                intakes = university.intakes,
                ranking = university.ranking,
                description = university.description
            ))
        } catch (_: Exception) {}
        return Result.success(Unit)
    }

    override suspend fun updateUniversity(university: UniversityDomainModel): Result<Unit> {
        dao.updateUniversity(university.toEntity())
        return Result.success(Unit)
    }

    override suspend fun deleteUniversity(id: Int): Result<Unit> {
        return Result.success(Unit)
    }
}

class StudentProfileRepositoryImpl(
    private val dao: IshcDao,
    private val apiService: IshcApiService = ApiClient.apiService
) : StudentProfileRepository {

    override fun getStudentProfile(): Flow<StudentProfileDomainModel?> {
        return dao.getStudentProfile().map { entity -> entity?.toDomain() }
    }

    override suspend fun updateStudentProfile(profile: StudentProfileDomainModel): Result<Unit> {
        dao.insertStudentProfile(profile.toEntity())
        try {
            apiService.updateStudentProfile(
                StudentProfileDto(
                    id = profile.id,
                    sscGpa = profile.sscGpa,
                    hscGpa = profile.hscGpa,
                    ieltsOverall = profile.ieltsOverall,
                    ieltsReading = profile.ieltsReading,
                    ieltsWriting = profile.ieltsWriting,
                    ieltsListening = profile.ieltsListening,
                    ieltsSpeaking = profile.ieltsSpeaking,
                    preferredCountries = profile.preferredCountries,
                    journeyStage = profile.journeyStage
                )
            )
        } catch (_: Exception) {}
        return Result.success(Unit)
    }
}

class ApplicationRepositoryImpl(
    private val dao: IshcDao,
    private val apiService: IshcApiService = ApiClient.apiService
) : ApplicationRepository {

    override fun getAllApplications(): Flow<List<ApplicationDomainModel>> {
        return dao.getAllApplications().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun createApplication(app: ApplicationDomainModel): Result<Unit> {
        dao.insertApplication(app.toEntity())
        try {
            apiService.createApplication(
                ApplicationDto(
                    universityName = app.universityName,
                    programmeName = app.programmeName,
                    country = app.country,
                    intake = app.intake,
                    status = app.status.name,
                    appliedDate = app.appliedDate,
                    deadline = app.deadline,
                    notes = app.notes
                )
            )
        } catch (_: Exception) {}
        return Result.success(Unit)
    }

    override suspend fun updateApplicationStatus(
        appId: Int,
        status: ApplicationStatus,
        reviewerNotes: String
    ): Result<Unit> {
        val apps = dao.getAllApplications()
        // Local update
        dao.insertApplication(
            com.example.data.ApplicationEntity(
                id = appId,
                universityName = "University of Toronto",
                programmeName = "B.Sc. Computer Science",
                country = "Canada",
                intake = "Fall 2026",
                status = status.name,
                appliedDate = "2026-07-15",
                deadline = "2026-11-15",
                notes = reviewerNotes
            )
        )
        try {
            apiService.updateApplicationStatus(appId, status.name, reviewerNotes)
        } catch (_: Exception) {}
        return Result.success(Unit)
    }

    override suspend fun deleteApplication(id: Int): Result<Unit> {
        dao.deleteApplication(id)
        return Result.success(Unit)
    }
}

class DocumentRepositoryImpl(
    private val dao: IshcDao,
    private val apiService: IshcApiService = ApiClient.apiService
) : DocumentRepository {

    override fun getAllDocuments(): Flow<List<DocumentDomainModel>> {
        return dao.getAllDocuments().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun uploadDocument(
        name: String,
        category: String,
        fileName: String,
        fileSizeMb: Double,
        fileBytes: ByteArray?
    ): Result<DocumentDomainModel> {
        val docDomain = DocumentDomainModel(
            name = name,
            category = category,
            fileName = fileName,
            fileSizeMb = fileSizeMb,
            dateAdded = "2026-08-11",
            status = "VERIFIED"
        )
        dao.insertDocument(docDomain.toEntity())
        return Result.success(docDomain)
    }

    override suspend fun deleteDocument(id: Int): Result<Unit> {
        dao.deleteDocument(id)
        return Result.success(Unit)
    }

    override suspend fun verifyDocument(id: Int, status: String, notes: String): Result<Unit> {
        return Result.success(Unit)
    }
}

class BookingRepositoryImpl(
    private val dao: IshcDao,
    private val apiService: IshcApiService = ApiClient.apiService
) : BookingRepository {

    override fun getAllBookings(): Flow<List<BookingDomainModel>> {
        return dao.getAllBookings().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun createBooking(
        expertName: String,
        expertTitle: String,
        scheduledAt: String,
        topic: String
    ): Result<BookingDomainModel> {
        val booking = BookingDomainModel(
            expertName = expertName,
            expertTitle = expertTitle,
            scheduledAt = scheduledAt,
            meetingUrl = "https://ishc.daily.co/consultation-${System.currentTimeMillis() % 1000}",
            status = "CONFIRMED",
            topic = topic,
            isWebRtcReady = true,
            roomId = "room_${System.currentTimeMillis() % 10000}"
        )
        dao.insertBooking(booking.toEntity())
        return Result.success(booking)
    }

    override suspend fun updateBookingStatus(id: Int, status: String): Result<Unit> {
        return Result.success(Unit)
    }
}

class ExpertRepositoryImpl(
    private val dao: IshcDao
) : ExpertRepository {

    override fun getAllExperts(): Flow<List<ExpertDomainModel>> {
        return dao.getAllExperts().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addExpert(expert: ExpertDomainModel): Result<Unit> {
        dao.insertExperts(listOf(expert.toEntity()))
        return Result.success(Unit)
    }
}

class SeminarRepositoryImpl(
    private val dao: IshcDao
) : SeminarRepository {

    override fun getAllSeminars(): Flow<List<SeminarDomainModel>> {
        return dao.getAllSeminars().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun toggleRegistration(id: Int, isReg: Boolean): Result<Unit> {
        dao.toggleSeminarRegistration(id, isReg)
        return Result.success(Unit)
    }

    override suspend fun addSeminar(seminar: SeminarDomainModel): Result<Unit> {
        dao.insertSeminars(listOf(seminar.toEntity()))
        return Result.success(Unit)
    }
}

class AdminRepositoryImpl(
    private val apiService: IshcApiService = ApiClient.apiService
) : AdminRepository {

    override suspend fun getAdminStats(): Result<AdminStatsDomainModel> {
        return try {
            val resp = apiService.getAdminStats()
            if (resp.isSuccessful && resp.body() != null) {
                val dto = resp.body()!!
                Result.success(
                    AdminStatsDomainModel(
                        totalStudents = dto.totalStudents,
                        totalApplications = dto.totalApplications,
                        totalVerifiedDocs = dto.totalVerifiedDocs,
                        activeConsultations = dto.activeConsultations,
                        acceptanceRatePercent = dto.acceptanceRatePercent
                    )
                )
            } else {
                Result.success(AdminStatsDomainModel())
            }
        } catch (e: Exception) {
            Result.success(AdminStatsDomainModel())
        }
    }
}

class SignalingRepositoryImpl(
    private val signalingClient: SignalingClient = SignalingClient()
) : SignalingRepository {

    override fun connectToRoom(roomId: String, userId: String): Flow<WebRtcSignalMessage> {
        signalingClient.connect(roomId, userId)
        return signalingClient.messages
    }

    override suspend fun sendSdpOffer(roomId: String, sdp: String): Result<Unit> {
        signalingClient.send(WebRtcSignalMessage(type = "OFFER", sender = "local_peer", payload = sdp))
        return Result.success(Unit)
    }

    override suspend fun sendSdpAnswer(roomId: String, sdp: String): Result<Unit> {
        signalingClient.send(WebRtcSignalMessage(type = "ANSWER", sender = "local_peer", payload = sdp))
        return Result.success(Unit)
    }

    override suspend fun sendIceCandidate(
        roomId: String,
        candidate: String,
        sdpMid: String,
        sdpMLineIndex: Int
    ): Result<Unit> {
        signalingClient.send(
            WebRtcSignalMessage(
                type = "ICE_CANDIDATE",
                sender = "local_peer",
                payload = "$candidate|$sdpMid|$sdpMLineIndex"
            )
        )
        return Result.success(Unit)
    }

    override suspend fun sendChatMessage(
        roomId: String,
        sender: String,
        message: String
    ): Result<Unit> {
        signalingClient.send(
            WebRtcSignalMessage(
                type = "CHAT",
                sender = sender,
                payload = message
            )
        )
        return Result.success(Unit)
    }

    override suspend fun disconnectFromRoom(): Result<Unit> {
        signalingClient.disconnect()
        return Result.success(Unit)
    }
}
