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
Target users feedback: Overall the target users seemed to think such an application would be useful for keeping track of all different course information throughout school. They provided a few ideas:

It would be nice to have the ability to add grades for each component of the courses.   
App should be user friendly and easy to understand, so students don't waste a lot of time
learning how to use this application on top of all the other work they have.   
A To Do list for each course would come in handy

Keeping the scope of the project in mind some we implemented some of the suggestions as is outlined in the list below

-Requirements
The following features were completed as part of this project:
-Signup      
-Login/logout  
-Add a term  
-Search for a term  
-Add a course  
-Search for a course  
-Display details for the course  
-Homepage view where user can search for all previous terms    added by them   
-Note taking functionality for each course         
-To Do list functionality for each course


## Design
The design pattern we tried to adapt was Model-View-ViewModel, this was because after doing some 
research we noticed that this is the most popular desig pattern used for Android Applications. Additionally, none of our team members had 
used this before so we decided it would be a good idea to learn more about it. We decided to use firebase for authentication and firestore 
for storing our data. This turned out to be a good decision as firebase handled authentication for us in a very nice manner. More over, 
Firestore is a NoSQL document based database. Since none of us had used it before, it was initially hard to grasp how documents/collections are
structured but towardds the end of the course we all became proficient in writing different complex queries using firestore. One technical decision we made during the initial stages was to add all the navigation at the end. This decision did not work in our favor
as towards the end we had to make a lot of changes for the navigation to work as we wanted it to. 

**Webservice Design**

A Ktor based webservice was created and used to work as a middle-layer in our tiered architecture system. When a user needs to fetch or send information to be stored in our Firestore we can model the request like this:

1. User triggers a GET/POST request (Client Side Interaction) to http://localhost:8080/ (local host)
2. Ktor webservice recieves this request and uses the parameters provided by the client to query information from the Firestore.
3. Firestore sends a response to the server and it processes the response converting it to JSON.  
4. Ktor webservice completes the API promise and returns a JSON response.
5. Client side processes the JSON and returns the information through the frontend components for the user to see.


**Deployment**

The webservice is deployed by utilizing Docker and containerizing the application, creating a Docker image based on the JAR file used to build the Ktor server.

Once the Docker container is running, it listens at port 8080, when the client side want to make a API request, it calls it from the users localhost and provides the specific request such as 
`get("/users/{userId}/terms")` 

**Basic Diagram:**
```
+----------------+     +-------------------+     +------------------+
| Android App UI |     | Docker Container  |     | Firestore        |
| (Client Layer) |     | (Ktor Web Service)|     | (Database Layer) |
+----------------+     +-------------------+     +------------------+
        |                       |                          |
        |--- API Request -----> |                          |
        |                       |                          |
        |                       |-- Process Request -----> |
        |                       |                          |
        |                       | <--- Query Data -------- |
        |                       |                          |
        |<-- JSON Response ---- |                          |
        |                       |                          |
        |                       |                          |

```

## Discussion
Since our team had minimal exposure to kotlin & compose before starting the project we frequently referred to the Android Developer 
documentation to understand best practices and learn more. This was also the first time we worked on a project of this nature from start to
end therefore there were a lot of learnings. Weekly meetings, and meeting minutes really helped our team stay organized and focused. Moreover,
pair programming helped us learn new coding techniques from each other. Pair programming also helped us catch flaws earlier in the 
process which were then addressed accordingly.          
A few things we would do differently when creating a project of this nature would be:           
-Complete more readings and watch more tutorials before starting to write code. This will ensure our code is more clean and we are
adapting best practices from the start.       
-Add navigation as soon as a new screen is introduced.    
-Set up more time for pair programming as it is really helpful, especially when everyone is learning.     
-Make sure tasks assigned don't overlap during a sprint so two people are not modifying the same file for the same purpose, this results
in a waste of overall group time and effort.     
-Add more tests

## Installation Details

#### Introduction
This guide provides detailed steps for installing and running the Course Journal application on both an Android client and a backend service. It consists of either building or using our APK to run the application on the Android Studio Emulator, and then setting up the Ktor webservice by using or building a JAR file and finally running and building the docker image. 

#### Prerequisites
- Android Studio
- Docker Desktop
- Java Development Kit (JDK) 11 or higher

### Given Files

1) APK file (Link a download link the the APK **TODO** until final merge)
2) JAR file  (Link a download link the the JAR **TODO** until final merge)

### Step 1. Running the Android App (Client Side)

In these steps you have the option to bundle the APK yourself by cloning the repo and creating a project in Android studio. The quicker alternative, Option A is downloading the given APK file directly, and use it within a emulator. Either choice will work fine.

**Option A:** Download the given APK directly:

1. Download the APK file as provided.
2. Open Android Studio and under `Device Manager` create/run a mobile device (Testing was done on the Pixel 3a)
3. Once the device boots and is turned on, drag and drop the downloaded APK file directly into the emulator screen.
4. The app should begin to download and appear with a default green Android icon. You can now open it and begin.

**Option B:** Build the APK on Android Studio:

1. Clone the repository using Git: git clone <repository-url>
2. Open Android Studio and select 'Open an existing project', then navigate to the cloned repository directory.
3. Build the APK by going to `Build > Build Bundle(s) / APK(s) > Build APK(s)` and click on 'locate' to find the APK in the debug folder.
4. Use the 'Device Explorer' in Android Studio to navigate to `sdcard > Download` and drag the APK there.
5. On the virtual or physical device, navigate to `Files > Downloads`, tap on the APK file to install it, and then open the app.

**Troubleshooting**:

- Make sure you have JDK 11+ installed on your device, you can download it from the [Oracle Website](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)

- If you are having trouble building the APK, you can download the one given here and follow **Option A**.
- Make sure Android Studio is up to date, and if troubles persist restart the application and create a new project.

### Step 2. Setting Up the Ktor Webservice

Once we have the app running, we need to host our webservice locally to be able to send API requests and recieve information from the applications Firestore and UW API instances. 


**Setting up the JAR file**

**Setting up the JAR file**

The JAR file required to build this service, it is already uploaded to the repository and it is not required to build again. It will be in the `build/libs` folder in `ktor-346/`

If you run into issues with using the JAR folder, delete the existing one and in ktor-346/ run: `./gradlew shadowJar` this will rebuild the JAR and build/libs folders.


**Running the Docker Container and deploying the webservice**

1. Make sure Docker Desktop is installed and running.
2. Make sure you are in the Ktor directory:
    `mns346-3/ktor-346`

3. Run the following commands to build your Docker image and run your container.

```
docker build -t ktor-app .
docker run -d -p 8080:8080 --name my-ktor-container ktor-app
```

4. Once the docker container is succesfully running you can test  the microservice is running by going to http://localhost:8080/


**Troubleshooting**:

- To check if docker is installed properly you can run `docker --version` to see if it is installed.

- To check if any containers are running you can run `docker ps`

- Ensure you have JDK 11+ installed on your computer, you can see what version(s) you have by using `java --version`

- To check the error logs for the container you can use `docker logs my-ktor-container`
- If you are having trouble with downloading the JAR or using the downloaded file, you can delete it, and use the command given to build your own JAR file.

## Screenshots/Videos

This is a google drive to the [screenshots](https://docs.google.com/document/d/1Cxab_93FqLL2An8Cf8DXTpiw1gUp9kG0qFYQiSkv9Zs/edit)


## Project Documents
[Rough wireframe diagram](https://git.uwaterloo.ca/m72ahmad/mns346/-/wikis/Rough-high-level-wireframe-diagram)

## Software Releases


