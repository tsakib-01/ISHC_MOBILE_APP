package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class IshcRepository(private val dao: IshcDao) {

    val user: Flow<UserEntity?> = dao.getUser()
    val studentProfile: Flow<StudentProfileEntity?> = dao.getStudentProfile()
    val allUniversities: Flow<List<UniversityEntity>> = dao.getAllUniversities()
    val favoriteUniversities: Flow<List<UniversityEntity>> = dao.getFavoriteUniversities()
    val allDocuments: Flow<List<DocumentEntity>> = dao.getAllDocuments()
    val allApplications: Flow<List<ApplicationEntity>> = dao.getAllApplications()
    val allBookings: Flow<List<BookingEntity>> = dao.getAllBookings()
    val allSeminars: Flow<List<SeminarEntity>> = dao.getAllSeminars()
    val allExperts: Flow<List<ExpertEntity>> = dao.getAllExperts()

    suspend fun seedInitialDataIfEmpty() {
        // Seed Profile
        val currentProfile = dao.getStudentProfile().first()
        if (currentProfile == null) {
            dao.insertUser(
                UserEntity(
                    id = "current_user",
                    name = "Alex Rivera",
                    email = "alex.rivera@student.ishc.org",
                    role = "STUDENT"
                )
            )
            dao.insertStudentProfile(
                StudentProfileEntity(
                    id = 1,
                    sscGpa = 5.0,
                    hscGpa = 4.8,
                    ieltsOverall = 7.0,
                    ieltsReading = 7.5,
                    ieltsWriting = 6.5,
                    ieltsListening = 7.5,
                    ieltsSpeaking = 7.0,
                    preferredCountries = "USA, UK, Canada, Germany",
                    journeyStage = 2
                )
            )
        }

        // Seed Universities
        val currentUnis = dao.getAllUniversities().first()
        if (currentUnis.isEmpty()) {
            val defaultUniversities = listOf(
                UniversityEntity(
                    name = "Harvard University",
                    country = "USA",
                    city = "Cambridge, MA",
                    website = "https://www.harvard.edu",
                    applyUrl = "https://college.harvard.edu/admissions/apply",
                    minSscGpa = 4.8,
                    minHscGpa = 4.8,
                    minIelts = 7.5,
                    tuitionFeeUsd = 54000,
                    intakes = "Fall (Aug/Sep)",
                    ranking = 1,
                    isFavorite = true,
                    description = "Top Ivy League institution offering full need-based financial aid for international students."
                ),
                UniversityEntity(
                    name = "University of Oxford",
                    country = "UK",
                    city = "Oxford, England",
                    website = "https://www.ox.ac.uk",
                    applyUrl = "https://www.ucas.com/undergraduate/applying-university",
                    minSscGpa = 4.5,
                    minHscGpa = 4.7,
                    minIelts = 7.5,
                    tuitionFeeUsd = 38500,
                    intakes = "Autumn (Oct)",
                    ranking = 2,
                    isFavorite = true,
                    description = "Oldest university in the English-speaking world with world-renowned tutorial education system."
                ),
                UniversityEntity(
                    name = "University of Toronto",
                    country = "Canada",
                    city = "Toronto, ON",
                    website = "https://www.utoronto.ca",
                    applyUrl = "https://future.utoronto.ca/apply",
                    minSscGpa = 4.0,
                    minHscGpa = 4.2,
                    minIelts = 6.5,
                    tuitionFeeUsd = 32000,
                    intakes = "Fall (Sep), Winter (Jan)",
                    ranking = 18,
                    isFavorite = false,
                    description = "Canada's leading research university with generous Lester B. Pearson International Scholarships."
                ),
                UniversityEntity(
                    name = "Technical University of Munich (TUM)",
                    country = "Germany",
                    city = "Munich, Bavaria",
                    website = "https://www.tum.de/en",
                    applyUrl = "https://www.tum.de/en/studies/application",
                    minSscGpa = 3.8,
                    minHscGpa = 4.0,
                    minIelts = 6.5,
                    tuitionFeeUsd = 3000,
                    intakes = "Winter (Oct), Summer (Apr)",
                    ranking = 28,
                    isFavorite = true,
                    description = "Premier European STEM institute offering low-tuition high-impact engineering & CS degrees."
                ),
                UniversityEntity(
                    name = "University of Melbourne",
                    country = "Australia",
                    city = "Melbourne, Victoria",
                    website = "https://www.unimelb.edu.au",
                    applyUrl = "https://study.unimelb.edu.au/how-to-apply",
                    minSscGpa = 4.0,
                    minHscGpa = 4.0,
                    minIelts = 6.5,
                    tuitionFeeUsd = 29500,
                    intakes = "Semester 1 (Feb), Semester 2 (Jul)",
                    ranking = 14,
                    isFavorite = false,
                    description = "Australia's #1 ranked university offering strong post-study work visa rights."
                ),
                UniversityEntity(
                    name = "University of Tokyo",
                    country = "Japan",
                    city = "Tokyo",
                    website = "https://www.u-tokyo.ac.jp/en",
                    applyUrl = "https://www.u-tokyo.ac.jp/en/prospective-students/admissions.html",
                    minSscGpa = 4.0,
                    minHscGpa = 4.0,
                    minIelts = 6.5,
                    tuitionFeeUsd = 6200,
                    intakes = "Autumn (Sep), Spring (Apr)",
                    ranking = 29,
                    isFavorite = false,
                    description = "Leading Asian university with PEAK English-medium undergraduate programs."
                ),
                UniversityEntity(
                    name = "Imperial College London",
                    country = "UK",
                    city = "London",
                    website = "https://www.imperial.ac.uk",
                    applyUrl = "https://www.imperial.ac.uk/study/apply/",
                    minSscGpa = 4.5,
                    minHscGpa = 4.6,
                    minIelts = 7.0,
                    tuitionFeeUsd = 42000,
                    intakes = "Autumn (Oct)",
                    ranking = 6,
                    isFavorite = false,
                    description = "Focused exclusively on science, engineering, medicine and business in central London."
                ),
                UniversityEntity(
                    name = "Arizona State University",
                    country = "USA",
                    city = "Tempe, AZ",
                    website = "https://www.asu.edu",
                    applyUrl = "https://admission.asu.edu/international",
                    minSscGpa = 3.2,
                    minHscGpa = 3.5,
                    minIelts = 6.0,
                    tuitionFeeUsd = 28000,
                    intakes = "Fall (Aug), Spring (Jan), Summer (May)",
                    ranking = 120,
                    isFavorite = false,
                    description = "#1 in US for innovation, offering automatic merit-based scholarships up to $14,500/yr."
                )
            )
            dao.insertUniversities(defaultUniversities)
        }

        // Seed Documents
        val currentDocs = dao.getAllDocuments().first()
        if (currentDocs.isEmpty()) {
            val defaultDocs = listOf(
                DocumentEntity(
                    name = "High School Academic Transcript (HSC)",
                    category = "TRANSCRIPT",
                    fileName = "hsc_transcript_official.pdf",
                    fileSizeMb = 2.4,
                    dateAdded = "2026-08-01",
                    status = "VERIFIED"
                ),
                DocumentEntity(
                    name = "IELTS Official Test Report Form (TRF)",
                    category = "IELTS_CERT",
                    fileName = "ielts_academic_trf_7.0.pdf",
                    fileSizeMb = 1.1,
                    dateAdded = "2026-08-02",
                    status = "VERIFIED"
                ),
                DocumentEntity(
                    name = "Statement of Purpose - Computer Science",
                    category = "SOP",
                    fileName = "sop_cs_undergrad_v2.docx",
                    fileSizeMb = 0.5,
                    dateAdded = "2026-08-05",
                    status = "PENDING_REVIEW"
                ),
                DocumentEntity(
                    name = "International Passport Bio Page",
                    category = "PASSPORT",
                    fileName = "passport_scan_valid2030.pdf",
                    fileSizeMb = 3.2,
                    dateAdded = "2026-07-28",
                    status = "VERIFIED"
                )
            )
            defaultDocs.forEach { dao.insertDocument(it) }
        }

        // Seed Applications
        val currentApps = dao.getAllApplications().first()
        if (currentApps.isEmpty()) {
            val defaultApps = listOf(
                ApplicationEntity(
                    universityName = "University of Toronto",
                    programmeName = "B.Sc. Computer Science",
                    country = "Canada",
                    intake = "Fall 2026",
                    status = "SUBMITTED",
                    appliedDate = "2026-07-15",
                    deadline = "2026-11-15",
                    notes = "Application ID #UT-99201. Paid $180 CAD fee. Pending transcript verification."
                ),
                ApplicationEntity(
                    universityName = "Technical University of Munich",
                    programmeName = "B.Sc. Information Engineering",
                    country = "Germany",
                    intake = "Winter 2026",
                    status = "UNDER_REVIEW",
                    appliedDate = "2026-07-20",
                    deadline = "2026-09-01",
                    notes = "Uni-assist VPD document submitted. Certificate in preliminary review."
                ),
                ApplicationEntity(
                    universityName = "Arizona State University",
                    programmeName = "B.S. Software Engineering",
                    country = "USA",
                    intake = "Spring 2027",
                    status = "ACCEPTED",
                    appliedDate = "2026-06-10",
                    deadline = "2026-10-01",
                    notes = "Admitted with $10,000/yr New American University Scholarship! I-20 pending."
                )
            )
            defaultApps.forEach { dao.insertApplication(it) }
        }

        // Seed Experts
        val currentExperts = dao.getAllExperts().first()
        if (currentExperts.isEmpty()) {
            val defaultExperts = listOf(
                ExpertEntity(
                    name = "Dr. Elena Rostova",
                    title = "Senior US & Ivy League Admissions Specialist",
                    countrySpecialization = "USA, Canada",
                    rating = 4.9,
                    totalConsultations = 340,
                    bio = "Former Harvard admissions officer with 12+ years experience helping international students secure full-ride scholarships.",
                    availability = "Mon, Wed, Fri (2:00 PM - 8:00 PM EST)"
                ),
                ExpertEntity(
                    name = "Marcus Vance, M.Ed.",
                    title = "UK Chevening & Visa Counselor",
                    countrySpecialization = "UK, Ireland",
                    rating = 4.8,
                    totalConsultations = 280,
                    bio = "British Council certified agent specializing in UCAS, STEM scholarships, and CAS/Tier 4 visa preparation.",
                    availability = "Tue, Thu, Sat (10:00 AM - 4:00 PM GMT)"
                ),
                ExpertEntity(
                    name = "Sophia Chen",
                    title = "DAAD Germany & EU Tuition-Free Advisor",
                    countrySpecialization = "Germany, Netherlands",
                    rating = 4.95,
                    totalConsultations = 410,
                    bio = "TUM Alumna guiding students through Uni-assist VPD, Blocked Accounts, and APS certificate processing.",
                    availability = "Mon-Fri (1:00 PM - 7:00 PM CET)"
                )
            )
            dao.insertExperts(defaultExperts)
        }

        // Seed Bookings / Meetings
        val currentBookings = dao.getAllBookings().first()
        if (currentBookings.isEmpty()) {
            val defaultBookings = listOf(
                BookingEntity(
                    expertName = "Dr. Elena Rostova",
                    expertTitle = "Senior US Admissions Specialist",
                    scheduledAt = "2026-08-15 15:00 EST",
                    meetingUrl = "https://ishc.daily.co/us-consultation-room-92",
                    status = "CONFIRMED",
                    topic = "SOP Review & US F-1 Visa Interview Strategy",
                    isWebRtcReady = true
                ),
                BookingEntity(
                    expertName = "Sophia Chen",
                    expertTitle = "Germany DAAD & Uni-assist Advisor",
                    scheduledAt = "2026-08-22 11:00 CET",
                    meetingUrl = "https://ishc.daily.co/germany-vpd-prep",
                    status = "PENDING",
                    topic = "Blocked Account Setup & Uni-assist Document Attestation",
                    isWebRtcReady = true
                )
            )
            defaultBookings.forEach { dao.insertBooking(it) }
        }

        // Seed Seminars
        val currentSeminars = dao.getAllSeminars().first()
        if (currentSeminars.isEmpty()) {
            val defaultSeminars = listOf(
                SeminarEntity(
                    title = "Unlocking USA $50,000 Financial Aid & Merit Scholarships",
                    speaker = "Dr. Elena Rostova",
                    date = "Aug 18, 2026",
                    time = "19:00 EST",
                    topic = "CSS Profile, FAFSA for international students, and athletic/merit grants",
                    isRegistered = true,
                    bannerColorHex = "#0F172A"
                ),
                SeminarEntity(
                    title = "How to Get Admitted to Germany Tuition-Free Universities",
                    speaker = "Sophia Chen",
                    date = "Aug 24, 2026",
                    time = "16:00 CET",
                    topic = "Uni-assist step-by-step, English taught programs, and APS guidelines",
                    isRegistered = false,
                    bannerColorHex = "#1E1B4B"
                ),
                SeminarEntity(
                    title = "IELTS 8.0 Masterclass: Writing Task 2 & Speaking Band Boost",
                    speaker = "Marcus Vance, M.Ed.",
                    date = "Aug 29, 2026",
                    time = "14:00 GMT",
                    topic = "Cohesion & Coherence tactics, vocabulary collocations, and live mock speaking feedback",
                    isRegistered = false,
                    bannerColorHex = "#064E3B"
                )
            )
            dao.insertSeminars(defaultSeminars)
        }
    }

    suspend fun updateStudentProfile(profile: StudentProfileEntity) = dao.insertStudentProfile(profile)
    suspend fun toggleFavorite(id: Int, isFav: Boolean) = dao.toggleFavorite(id, isFav)
    suspend fun addUniversity(university: UniversityEntity) = dao.insertUniversities(listOf(university))
    suspend fun addDocument(document: DocumentEntity) = dao.insertDocument(document)
    suspend fun deleteDocument(id: Int) = dao.deleteDocument(id)
    suspend fun addApplication(app: ApplicationEntity) = dao.insertApplication(app)
    suspend fun updateApplication(app: ApplicationEntity) = dao.updateApplication(app)
    suspend fun deleteApplication(id: Int) = dao.deleteApplication(id)
    suspend fun addBooking(booking: BookingEntity) = dao.insertBooking(booking)
    suspend fun toggleSeminarRegistration(id: Int, isReg: Boolean) = dao.toggleSeminarRegistration(id, isReg)
}
