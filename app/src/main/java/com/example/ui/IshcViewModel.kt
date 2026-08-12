package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class IshcScreen(val title: String, val iconName: String) {
    AUTH("Sign In / Portal", "Key"),
    PROGRAMMES("Find Programmes", "Search"),
    FAVORITES("Favorites", "Star"),
    JOURNEY("Journey Progress", "Compass"),
    DOCUMENTS("Document Vault", "Folder"),
    APPLICATIONS("My Applications", "Send"),
    MEETINGS("Scheduled Meetings", "Video"),
    SEMINARS("Online Seminars", "Calendar"),
    EXPERTS("Expert Advisors", "UserCheck"),
    PROFILE("Student Profile", "User"),
    VIDEO_CALL("Live WebRTC Consultation", "VideoCall"),
    ADMIN("Admin Management", "Shield")
}

data class MatchFilterState(
    val sscGpa: Double = 5.0,
    val hscGpa: Double = 4.8,
    val ieltsScore: Double = 7.0,
    val targetCountry: String = "All",
    val maxTuitionUsd: Int = 60000
)

class IshcViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: IshcRepository

    init {
        val db = IshcDatabase.getDatabase(application)
        repository = IshcRepository(db.ishcDao())
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // UI Navigation State
    private val _currentScreen = MutableStateFlow(IshcScreen.PROGRAMMES)
    val currentScreen: StateFlow<IshcScreen> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Active Video Call Room State
    private val _activeVideoCallUrl = MutableStateFlow<String?>(null)
    val activeVideoCallUrl: StateFlow<String?> = _activeVideoCallUrl.asStateFlow()

    private val _activeVideoExpertName = MutableStateFlow<String?>(null)
    val activeVideoExpertName: StateFlow<String?> = _activeVideoExpertName.asStateFlow()

    // Matching Filters State
    private val _matchFilter = MutableStateFlow(MatchFilterState())
    val matchFilter: StateFlow<MatchFilterState> = _matchFilter.asStateFlow()

    // DB Flows
    val user: StateFlow<UserEntity?> = repository.user.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val studentProfile: StateFlow<StudentProfileEntity?> = repository.studentProfile.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val allUniversities: StateFlow<List<UniversityEntity>> = repository.allUniversities.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val favoriteUniversities: StateFlow<List<UniversityEntity>> = repository.favoriteUniversities.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val documents: StateFlow<List<DocumentEntity>> = repository.allDocuments.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val applications: StateFlow<List<ApplicationEntity>> = repository.allApplications.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val bookings: StateFlow<List<BookingEntity>> = repository.allBookings.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val seminars: StateFlow<List<SeminarEntity>> = repository.allSeminars.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val experts: StateFlow<List<ExpertEntity>> = repository.allExperts.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    // Actions
    fun navigateTo(screen: IshcScreen) {
        _currentScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateMatchFilter(
        sscGpa: Double = _matchFilter.value.sscGpa,
        hscGpa: Double = _matchFilter.value.hscGpa,
        ieltsScore: Double = _matchFilter.value.ieltsScore,
        targetCountry: String = _matchFilter.value.targetCountry,
        maxTuitionUsd: Int = _matchFilter.value.maxTuitionUsd
    ) {
        _matchFilter.value = MatchFilterState(
            sscGpa = sscGpa,
            hscGpa = hscGpa,
            ieltsScore = ieltsScore,
            targetCountry = targetCountry,
            maxTuitionUsd = maxTuitionUsd
        )
    }

    fun toggleFavorite(universityId: Int, isFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(universityId, isFav)
        }
    }

    fun updateStudentProfile(
        sscGpa: Double,
        hscGpa: Double,
        ieltsOverall: Double,
        ieltsReading: Double,
        ieltsWriting: Double,
        ieltsListening: Double,
        ieltsSpeaking: Double,
        preferredCountries: String,
        journeyStage: Int
    ) {
        viewModelScope.launch {
            repository.updateStudentProfile(
                StudentProfileEntity(
                    id = 1,
                    sscGpa = sscGpa,
                    hscGpa = hscGpa,
                    ieltsOverall = ieltsOverall,
                    ieltsReading = ieltsReading,
                    ieltsWriting = ieltsWriting,
                    ieltsListening = ieltsListening,
                    ieltsSpeaking = ieltsSpeaking,
                    preferredCountries = preferredCountries,
                    journeyStage = journeyStage
                )
            )
        }
    }

    fun addDocument(name: String, category: String, fileName: String, fileSizeMb: Double) {
        viewModelScope.launch {
            repository.addDocument(
                DocumentEntity(
                    name = name,
                    category = category,
                    fileName = fileName,
                    fileSizeMb = fileSizeMb,
                    dateAdded = "2026-08-11",
                    status = "VERIFIED"
                )
            )
        }
    }

    fun deleteDocument(id: Int) {
        viewModelScope.launch {
            repository.deleteDocument(id)
        }
    }

    fun addApplication(
        universityName: String,
        programmeName: String,
        country: String,
        intake: String,
        deadline: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.addApplication(
                ApplicationEntity(
                    universityName = universityName,
                    programmeName = programmeName,
                    country = country,
                    intake = intake,
                    status = "DRAFT",
                    appliedDate = "2026-08-11",
                    deadline = deadline,
                    notes = notes
                )
            )
        }
    }

    fun updateApplicationStatus(app: ApplicationEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateApplication(app.copy(status = newStatus))
        }
    }

    fun deleteApplication(id: Int) {
        viewModelScope.launch {
            repository.deleteApplication(id)
        }
    }

    fun bookConsultation(
        expertName: String,
        expertTitle: String,
        scheduledAt: String,
        topic: String
    ) {
        viewModelScope.launch {
            val roomId = "ishc-room-" + System.currentTimeMillis().toString().takeLast(5)
            repository.addBooking(
                BookingEntity(
                    expertName = expertName,
                    expertTitle = expertTitle,
                    scheduledAt = scheduledAt,
                    meetingUrl = "https://ishc.daily.co/$roomId",
                    status = "CONFIRMED",
                    topic = topic,
                    isWebRtcReady = true
                )
            )
        }
    }

    fun startVideoCall(meetingUrl: String, expertName: String) {
        _activeVideoCallUrl.value = meetingUrl
        _activeVideoExpertName.value = expertName
        _currentScreen.value = IshcScreen.VIDEO_CALL
    }

    fun endVideoCall() {
        _activeVideoCallUrl.value = null
        _activeVideoExpertName.value = null
        _currentScreen.value = IshcScreen.MEETINGS
    }

    fun toggleSeminarRegistration(id: Int, isReg: Boolean) {
        viewModelScope.launch {
            repository.toggleSeminarRegistration(id, isReg)
        }
    }

    // Admin Stats
    private val _adminStats = MutableStateFlow(com.example.domain.model.AdminStatsDomainModel())
    val adminStats: StateFlow<com.example.domain.model.AdminStatsDomainModel> = _adminStats.asStateFlow()

    fun addUniversity(
        name: String,
        country: String,
        city: String,
        minSscGpa: Double,
        minHscGpa: Double,
        minIelts: Double,
        tuitionFeeUsd: Int
    ) {
        viewModelScope.launch {
            repository.addUniversity(
                UniversityEntity(
                    name = name,
                    country = country,
                    city = city,
                    website = "https://www.${name.lowercase().replace(" ", "")}.edu",
                    applyUrl = "https://www.${name.lowercase().replace(" ", "")}.edu/apply",
                    minSscGpa = minSscGpa,
                    minHscGpa = minHscGpa,
                    minIelts = minIelts,
                    tuitionFeeUsd = tuitionFeeUsd,
                    intakes = "Fall 2026",
                    ranking = (10..150).random(),
                    description = "Official partner institution offering international student scholarships."
                )
            )
        }
    }

    fun verifyDocumentStatus(docId: Int, status: String) {
        viewModelScope.launch {
            repository.addDocument(
                DocumentEntity(
                    id = docId,
                    name = "Verified High School Document",
                    category = "TRANSCRIPT",
                    fileName = "doc_$docId.pdf",
                    fileSizeMb = 1.5,
                    dateAdded = "2026-08-11",
                    status = status
                )
            )
        }
    }
}
