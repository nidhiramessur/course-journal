package com.example.cs346project
import com.google.gson.annotations.SerializedName
import java.util.*

data class ScheduleData(
    @SerializedName("courseId") val courseId: String,
//    @SerializedName("courseOfferNumber") val courseOfferNumber: Int,
//    @SerializedName("sessionCode") val sessionCode: String,
    @SerializedName("classSection") val classSection: Int,
    @SerializedName("termCode") val termCode: String,
//    @SerializedName("classMeetingNumber") val classMeetingNumber: Int,
//    @SerializedName("scheduleStartDate") val scheduleStartDate: Date,
//    @SerializedName("scheduleEndDate") val scheduleEndDate: Date,
    @SerializedName("classMeetingStartTime") val classMeetingStartTime: Date,
    @SerializedName("classMeetingEndTime") val classMeetingEndTime: Date,
    @SerializedName("classMeetingDayPatternCode") val classMeetingDayPatternCode: String,
    @SerializedName("classMeetingWeekPatternCode") val classMeetingWeekPatternCode: String,
//    @SerializedName("locationName") val locationName: String
)

//data class InstructorData(
//    @SerializedName("courseId") val courseId: String,
//    @SerializedName("courseOfferNumber") val courseOfferNumber: Int,
//    @SerializedName("sessionCode") val sessionCode: String,
//    @SerializedName("classSection") val classSection: Int,
//    @SerializedName("termCode") val termCode: String,
//    @SerializedName("instructorRoleCode") val instructorRoleCode: String,
//    @SerializedName("instructorFirstName") val instructorFirstName: String,
//    @SerializedName("instructorLastName") val instructorLastName: String,
//    @SerializedName("instructorUniqueIdentifier") val instructorUniqueIdentifier: String,
//    @SerializedName("classMeetingNumber") val classMeetingNumber: Int
//)

data class CourseInfoAPIData(
    val courseId: String,
//    @SerializedName("courseOfferNumber") val courseOfferNumber: Int,
//    @SerializedName("sessionCode") val sessionCode: String,
    val classSection: Int,
    val termCode: String,
//    @SerializedName("classNumber") val classNumber: Int,
    val courseComponent: String,
//    @SerializedName("associatedClassCode") val associatedClassCode: Int,
//    @SerializedName("maxEnrollmentCapacity") val maxEnrollmentCapacity: Int,
//    @SerializedName("enrolledStudents") val enrolledStudents: Int,
//    @SerializedName("enrollConsentCode") val enrollConsentCode: String,
//    @SerializedName("enrollConsentDescription") val enrollConsentDescription: String,
//    @SerializedName("dropConsentCode") val dropConsentCode: String,
//    @SerializedName("dropConsentDescription") val dropConsentDescription: String,
//    val scheduleData: List<ScheduleData>
//    @SerializedName("instructorData") val instructorData: List<InstructorData>
)


//data class CourseInfoAPIData (
//    val termName: String,
//    val subjectCode: String,
//    val catalogNumber: String,
//    val title: String,
//    val descriptionAbbreviated: String,
//    val description: String,
//    val gradingBasis: String,
//    val requirementsDescription: String
//)