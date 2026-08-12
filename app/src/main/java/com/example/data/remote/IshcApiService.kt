package com.example.data.remote

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface IshcApiService {

    // Auth
    @POST("api/auth/google")
    suspend fun googleLogin(@Body request: AuthRequestDto): Response<UserDto>

    @POST("api/auth/login")
    suspend fun emailLogin(@Body request: AuthRequestDto): Response<UserDto>

    @POST("api/auth/register")
    suspend fun emailRegister(@Body request: AuthRequestDto): Response<UserDto>

    @GET("api/auth/me")
    suspend fun getCurrentUser(): Response<UserDto>

    // Profile
    @GET("api/profile")
    suspend fun getStudentProfile(): Response<StudentProfileDto>

    @PUT("api/profile")
    suspend fun updateStudentProfile(@Body profile: StudentProfileDto): Response<StudentProfileDto>

    // Universities
    @GET("api/universities")
    suspend fun getUniversities(): Response<List<UniversityDto>>

    @POST("api/universities")
    suspend fun addUniversity(@Body university: UniversityDto): Response<UniversityDto>

    @PUT("api/universities/{id}")
    suspend fun updateUniversity(@Path("id") id: Int, @Body university: UniversityDto): Response<UniversityDto>

    @DELETE("api/universities/{id}")
    suspend fun deleteUniversity(@Path("id") id: Int): Response<Unit>

    // Applications
    @GET("api/applications")
    suspend fun getApplications(): Response<List<ApplicationDto>>

    @POST("api/applications")
    suspend fun createApplication(@Body app: ApplicationDto): Response<ApplicationDto>

    @PATCH("api/applications/{id}/status")
    suspend fun updateApplicationStatus(
        @Path("id") id: Int,
        @Query("status") status: String,
        @Query("notes") notes: String
    ): Response<ApplicationDto>

    @DELETE("api/applications/{id}")
    suspend fun deleteApplication(@Path("id") id: Int): Response<Unit>

    // Documents
    @GET("api/documents")
    suspend fun getDocuments(): Response<List<DocumentDto>>

    @Multipart
    @POST("api/documents/upload")
    suspend fun uploadDocument(
        @Part("name") name: String,
        @Part("category") category: String,
        @Part file: MultipartBody.Part
    ): Response<DocumentDto>

    @DELETE("api/documents/{id}")
    suspend fun deleteDocument(@Path("id") id: Int): Response<Unit>

    @PATCH("api/documents/{id}/verify")
    suspend fun verifyDocument(
        @Path("id") id: Int,
        @Query("status") status: String,
        @Query("notes") notes: String
    ): Response<DocumentDto>

    // Bookings
    @GET("api/bookings")
    suspend fun getBookings(): Response<List<BookingDto>>

    @POST("api/bookings")
    suspend fun createBooking(@Body booking: BookingDto): Response<BookingDto>

    // Experts
    @GET("api/experts")
    suspend fun getExperts(): Response<List<ExpertDto>>

    @POST("api/experts")
    suspend fun addExpert(@Body expert: ExpertDto): Response<ExpertDto>

    // Seminars
    @GET("api/seminars")
    suspend fun getSeminars(): Response<List<SeminarDto>>

    @POST("api/seminars/{id}/register")
    suspend fun toggleSeminarRegistration(
        @Path("id") id: Int,
        @Query("registered") registered: Boolean
    ): Response<SeminarDto>

    // Admin Stats
    @GET("api/admin/stats")
    suspend fun getAdminStats(): Response<AdminStatsDto>
}
