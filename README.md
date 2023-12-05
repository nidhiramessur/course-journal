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
[Requirements](#)

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


