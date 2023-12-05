# Course Journal Application

## Goal
The Course Journal application is a comprehensive platform for students to manage and access their course-related information. It helps students organize courses, view lecture details, and maintain notes and to-do lists.

## Team Members (Team mns346)
- Maroosh Ahmad: m72ahmad@uwaterloo.ca
- Nidhi Ramessur: nbramess@uwaterloo.ca
- Sohom Saha: s52saha@uwaterloo.ca

## Table of Contents
- [Meeting Minutes](#meeting-minutes)
- [Requirements](#requirements)
- [Design](#design)
- [Discussion](#discussion)
- [Installation Details](#installation-details)
- [Source Code](#source-code)
- [Screenshots/Videos](#screenshotsvideos)
- [Project Documents](#project-documents)
- [Software Releases](#software-releases)
- [Tools](#tools)

## Meeting Minutes
- [Sprint 1](https://git.uwaterloo.ca/m72ahmad/mns346/-/wikis/Meeting-minutes-Sprint-1)
- [Sprint 2](https://git.uwaterloo.ca/m72ahmad/mns346/-/wikis/Meeting-minutes-Sprint-2)
- [Sprint 3](https://git.uwaterloo.ca/m72ahmad/mns346/-/wikis/Meeting-minutes-Sprint-3)
- [Sprint 4](https://git.uwaterloo.ca/m72ahmad/mns346/-/wikis/Meeting-minutes-Sprint-4)

## Requirements

-Problem: Students often suffer as they don't have an all-in-one application to store all their course related information throughout their academic career. Additionally, having to switch between different applications for different information for courses throughout the term becomes a pain point. Our application will solve this problem by providing an all-in-one platform so students can easily store, access and edit such information.

-Users:
Target users: University of Waterloo students who want a platform to keep all course related information.
Persona: Kai is a 20-year old tech savvy undergraduate student at the University of Waterloo. They aim to stay organized and on top of their coursework while balancing other activities. They are always on the lookout for digital tools that can simplify academic tasks. Kai’s primary goal is to improve time management. However, they struggle to keep track of deadlines and manage course-related information across multiple platforms. An app that combines features such as viewing daily/weekly schedules, having all course information centralized in one place and managing their own information would be a great solution to meet their needs.
Target users feedback: Overall the target users seemed to think such an application would be useful for keeping track of all different course information throughout school. They provided a few ideas, keeping the scope of the project in mind some of the main suggestions are outlined below and were added to the functional requirements list as well:

It would be nice to have the ability to add grades for each component of the courses.
App should be user friendly and easy to understand, so students don't waste a lot of time
learning how to use this application on top of all the other work they have.
A To Do list for each course would come in handy

-Requirements
The following Functional requirements were completed as part of this project:

Signup
Login/logout
Add a term
Search for a term
Add a course
Search for a course
Display details for the course
Homepage view where user can search for all previous terms added by them
Note taking functionality for each course
To Do list functionality for each course


## Design
[Design](#)

## Discussion
[Discussion](#)

## Installation Details
### On Virtual device in Android studio:
1. Clone the project and open it in Android Studio.
2. Build the project by navigating to `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
3. A popup will appear in the bottom right-hand corner; click on 'locate' to find the APK in the debug folder.
4. Navigate to `Device Explorer` in Android Studio, go to `sdcard > Download`, then drag and drop the APK file there.
5. On the virtual device, navigate to `Files > Downloads`. Tap on the APK file to install it, choose 'Update' if prompted, and then 'Open' after installation.

### Course Journal Application Installation Guide
#### Introduction
This guide provides detailed steps for installing and running the Course Journal application on both an Android client and a backend service.

#### Prerequisites
- Android Studio
- Docker Desktop
- Git
- Java Development Kit (JDK) 11 or higher

#### Installing the Android Client
1. Clone the repository using Git: git clone <repository-url>
2. Open Android Studio and select 'Open an existing project', then navigate to the cloned repository directory.
3. Build the APK by going to `Build > Build Bundle(s) / APK(s) > Build APK(s)` and click on 'locate' to find the APK in the debug folder.
4. Use the 'Device Explorer' in Android Studio to navigate to `sdcard > Download` and drag the APK there.
5. On the virtual or physical device, navigate to `Files > Downloads`, tap on the APK file to install it, and then open the app.

#### Setting Up the Backend Service
1. Make sure Docker Desktop is installed and running.
2. Build the Docker container for the backend service with the following commands:
    ```
    cd ktor-346/
    ./gradlew shadowJar
    docker build -t ktor-app .
    docker run -d -p 8080:8080 --name my-ktor-container ktor-app
    ```
3. Once the docker container is succesfully running you can test  the microservice is running by going to http://localhost:8080/


