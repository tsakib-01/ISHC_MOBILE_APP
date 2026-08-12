package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IshcDao {

    // User & Profile
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUser(id: String = "current_user"): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM student_profiles WHERE id = 1 LIMIT 1")
    fun getStudentProfile(): Flow<StudentProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentProfile(profile: StudentProfileEntity)

    // Universities
    @Query("SELECT * FROM universities ORDER BY ranking ASC")
    fun getAllUniversities(): Flow<List<UniversityEntity>>

    @Query("SELECT * FROM universities WHERE isFavorite = 1")
    fun getFavoriteUniversities(): Flow<List<UniversityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUniversities(universities: List<UniversityEntity>)

    @Update
    suspend fun updateUniversity(university: UniversityEntity)

    @Query("UPDATE universities SET isFavorite = :isFav WHERE id = :id")
    suspend fun toggleFavorite(id: Int, isFav: Boolean)

    // Documents
    @Query("SELECT * FROM documents ORDER BY id DESC")
    fun getAllDocuments(): Flow<List<DocumentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentEntity)

    @Query("DELETE FROM documents WHERE id = :id")
    suspend fun deleteDocument(id: Int)

    // Applications
    @Query("SELECT * FROM applications ORDER BY id DESC")
    fun getAllApplications(): Flow<List<ApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: ApplicationEntity)

    @Update
    suspend fun updateApplication(application: ApplicationEntity)

    @Query("DELETE FROM applications WHERE id = :id")
    suspend fun deleteApplication(id: Int)

    // Bookings & Consultations
    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    // Seminars
    @Query("SELECT * FROM seminars ORDER BY id ASC")
    fun getAllSeminars(): Flow<List<SeminarEntity>>

    @Query("UPDATE seminars SET isRegistered = :isReg WHERE id = :id")
    suspend fun toggleSeminarRegistration(id: Int, isReg: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeminars(seminars: List<SeminarEntity>)

    // Experts
    @Query("SELECT * FROM experts ORDER BY rating DESC")
    fun getAllExperts(): Flow<List<ExpertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperts(experts: List<ExpertEntity>)
}
