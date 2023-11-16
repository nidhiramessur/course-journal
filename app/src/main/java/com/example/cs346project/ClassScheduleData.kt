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

data class CourseInfoData (
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
