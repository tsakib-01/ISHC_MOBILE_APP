package com.example.data.mapper

import com.example.data.*
import com.example.data.remote.*
import com.example.domain.model.*

// User Mapper
fun UserEntity.toDomain(): UserDomainModel {
    val roleEnum = try {
        UserRole.valueOf(role)
    } catch (e: Exception) {
        UserRole.STUDENT
    }
    return UserDomainModel(
        id = id,
        name = name,
        email = email,
        role = roleEnum,
        avatarUrl = avatarUrl,
        googleId = googleId,
        createdAt = createdAt
    )
}

fun UserDomainModel.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        role = role.name,
        avatarUrl = avatarUrl,
        googleId = googleId,
        createdAt = createdAt
    )
}

fun UserDto.toDomain(): UserDomainModel {
    val roleEnum = try {
        UserRole.valueOf(role)
    } catch (e: Exception) {
        UserRole.STUDENT
    }
    return UserDomainModel(
        id = id,
        name = name,
        email = email,
        role = roleEnum,
        avatarUrl = avatarUrl ?: "",
        googleId = googleId ?: ""
    )
}

// Student Profile Mapper
fun StudentProfileEntity.toDomain(): StudentProfileDomainModel {
    return StudentProfileDomainModel(
        id = id,
        userId = userId,
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
}

fun StudentProfileDomainModel.toEntity(): StudentProfileEntity {
    return StudentProfileEntity(
        id = id,
        userId = userId,
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
}

// University Mapper
fun UniversityEntity.toDomain(): UniversityDomainModel {
    return UniversityDomainModel(
        id = id,
        name = name,
        country = country,
        city = city,
        website = website,
        applyUrl = applyUrl,
        minSscGpa = minSscGpa,
        minHscGpa = minHscGpa,
        minIelts = minIelts,
        tuitionFeeUsd = tuitionFeeUsd,
        intakes = intakes,
        ranking = ranking,
        isFavorite = isFavorite,
        description = description,
        popularProgrammes = popularProgrammes
    )
}

fun UniversityDomainModel.toEntity(): UniversityEntity {
    return UniversityEntity(
        id = id,
        name = name,
        country = country,
        city = city,
        website = website,
        applyUrl = applyUrl,
        minSscGpa = minSscGpa,
        minHscGpa = minHscGpa,
        minIelts = minIelts,
        tuitionFeeUsd = tuitionFeeUsd,
        intakes = intakes,
        ranking = ranking,
        isFavorite = isFavorite,
        description = description,
        popularProgrammes = popularProgrammes
    )
}

fun UniversityDto.toDomain(): UniversityDomainModel {
    return UniversityDomainModel(
        id = id,
        name = name,
        country = country,
        city = city,
        website = website,
        applyUrl = applyUrl,
        minSscGpa = minSscGpa,
        minHscGpa = minHscGpa,
        minIelts = minIelts,
        tuitionFeeUsd = tuitionFeeUsd,
        intakes = intakes,
        ranking = ranking,
        isFavorite = isFavorite,
        description = description,
        popularProgrammes = popularProgrammes
    )
}

// Application Mapper
fun ApplicationEntity.toDomain(): ApplicationDomainModel {
    val statusEnum = try {
        ApplicationStatus.valueOf(status)
    } catch (e: Exception) {
        ApplicationStatus.SUBMITTED
    }
    return ApplicationDomainModel(
        id = id,
        universityName = universityName,
        programmeName = programmeName,
        country = country,
        intake = intake,
        status = statusEnum,
        appliedDate = appliedDate,
        deadline = deadline,
        notes = notes
    )
}

fun ApplicationDomainModel.toEntity(): ApplicationEntity {
    return ApplicationEntity(
        id = id,
        universityName = universityName,
        programmeName = programmeName,
        country = country,
        intake = intake,
        status = status.name,
        appliedDate = appliedDate,
        deadline = deadline,
        notes = notes
    )
}

// Document Mapper
fun DocumentEntity.toDomain(): DocumentDomainModel {
    return DocumentDomainModel(
        id = id,
        name = name,
        category = category,
        fileName = fileName,
        fileSizeMb = fileSizeMb,
        dateAdded = dateAdded,
        status = status
    )
}

fun DocumentDomainModel.toEntity(): DocumentEntity {
    return DocumentEntity(
        id = id,
        name = name,
        category = category,
        fileName = fileName,
        fileSizeMb = fileSizeMb,
        dateAdded = dateAdded,
        status = status
    )
}

// Booking Mapper
fun BookingEntity.toDomain(): BookingDomainModel {
    return BookingDomainModel(
        id = id,
        expertName = expertName,
        expertTitle = expertTitle,
        expertAvatar = expertAvatar,
        scheduledAt = scheduledAt,
        meetingUrl = meetingUrl,
        status = status,
        topic = topic,
        isWebRtcReady = isWebRtcReady,
        roomId = "room_${id}_${expertName.take(3)}"
    )
}

fun BookingDomainModel.toEntity(): BookingEntity {
    return BookingEntity(
        id = id,
        expertName = expertName,
        expertTitle = expertTitle,
        expertAvatar = expertAvatar,
        scheduledAt = scheduledAt,
        meetingUrl = meetingUrl,
        status = status,
        topic = topic,
        isWebRtcReady = isWebRtcReady
    )
}

// Seminar Mapper
fun SeminarEntity.toDomain(): SeminarDomainModel {
    return SeminarDomainModel(
        id = id,
        title = title,
        speaker = speaker,
        date = date,
        time = time,
        topic = topic,
        isRegistered = isRegistered,
        bannerColorHex = bannerColorHex
    )
}

fun SeminarDomainModel.toEntity(): SeminarEntity {
    return SeminarEntity(
        id = id,
        title = title,
        speaker = speaker,
        date = date,
        time = time,
        topic = topic,
        isRegistered = isRegistered,
        bannerColorHex = bannerColorHex
    )
}

// Expert Mapper
fun ExpertEntity.toDomain(): ExpertDomainModel {
    return ExpertDomainModel(
        id = id,
        name = name,
        title = title,
        countrySpecialization = countrySpecialization,
        rating = rating,
        totalConsultations = totalConsultations,
        bio = bio,
        availability = availability
    )
}

fun ExpertDomainModel.toEntity(): ExpertEntity {
    return ExpertEntity(
        id = id,
        name = name,
        title = title,
        countrySpecialization = countrySpecialization,
        rating = rating,
        totalConsultations = totalConsultations,
        bio = bio,
        availability = availability
    )
}
