package com.example.cs346project

data class ScheduleData(
    val courseId: String,
    val classSection: String,
    val termCode: String,
    val classMeetingStartTime: String,
    val classMeetingEndTime: String,
    val classMeetingDayPatternCode: String,
    val classMeetingWeekPatternCode: String
)

data class ClassScheduleData(
    val courseId: String,
    val classSection: String,
    val termCode: String,
    val courseComponent: String,
    val scheduleData: List<ScheduleData>
)

data class CourseInfoAPIData (
    val courseId: String,
    val termName: String,
    val subjectCode: String,
    val catalogNumber: String,
    val title: String,
    val descriptionAbbreviated: String,
    val description: String,
    val gradingBasis: String,
    val requirementsDescription: String
)

data class CourseInfoDbData (
    val name: String = "",
    val title: String = "",
    val requirements: String = "",
    val lecturedatetime: String = "",
    val lecturelocation: String = "",
    val professorname: String = "",
    val courserating: String = "0",
    val professorrating: String = "0"
)
