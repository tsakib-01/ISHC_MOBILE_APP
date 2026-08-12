package com.example.domain.usecase

import com.example.domain.model.EligibilityStatus
import com.example.domain.model.StudentProfileDomainModel
import com.example.domain.model.UniversityDomainModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EvaluateEligibilityUseCaseTest {

    private val useCase = EvaluateEligibilityUseCase()

    @Test
    fun testDirectMatchWhenProfileExceedsRequirements() {
        val student = StudentProfileDomainModel(
            sscGpa = 5.0,
            hscGpa = 5.0,
            ieltsOverall = 8.0,
            ieltsReading = 8.0,
            ieltsWriting = 7.5,
            ieltsListening = 8.5,
            ieltsSpeaking = 8.0,
            maxTuitionBudgetUsd = 60000
        )

        val university = UniversityDomainModel(
            id = 1,
            name = "Harvard University",
            country = "USA",
            city = "Cambridge",
            website = "https://harvard.edu",
            applyUrl = "https://harvard.edu/apply",
            minSscGpa = 4.8,
            minHscGpa = 4.8,
            minIelts = 7.5,
            minReading = 7.0,
            minWriting = 7.0,
            minListening = 7.0,
            minSpeaking = 7.0,
            tuitionFeeUsd = 54000,
            intakes = "Fall",
            ranking = 1
        )

        val result = useCase.execute(student, university)

        assertEquals(EligibilityStatus.DIRECT_MATCH, result.status)
        assertTrue(result.matchScorePercent >= 90)
        assertTrue(result.gpaMatch)
        assertTrue(result.ieltsMatch)
        assertTrue(result.subBandsMatch)
        assertTrue(result.budgetMatch)
    }

    @Test
    fun testConditionalMatchWhenMinorGpaOrIeltsGap() {
        val student = StudentProfileDomainModel(
            sscGpa = 4.5,
            hscGpa = 4.5,
            ieltsOverall = 7.0,
            ieltsReading = 7.0,
            ieltsWriting = 6.5,
            ieltsListening = 7.0,
            ieltsSpeaking = 6.5,
            maxTuitionBudgetUsd = 40000
        )

        val university = UniversityDomainModel(
            id = 2,
            name = "University of Oxford",
            country = "UK",
            city = "Oxford",
            website = "https://ox.ac.uk",
            applyUrl = "https://ox.ac.uk/apply",
            minSscGpa = 4.5,
            minHscGpa = 4.7, // Gap of 0.2
            minIelts = 7.5, // Gap of 0.5
            tuitionFeeUsd = 38500,
            intakes = "Autumn",
            ranking = 2
        )

        val result = useCase.execute(student, university)

        assertEquals(EligibilityStatus.CONDITIONAL, result.status)
        assertTrue(result.offersEslPathway)
    }
}
