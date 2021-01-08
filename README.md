SCHEDULE-MATCHING ANDROID APPLICATION
using Java, Firebase, and Android Studio

Team Members:  
              Jasur Shukurov - Master/Project Manager  
              Giuseppe D'Ambrosio - Frontend Developer  
              Matt Vang - Backend Developer  
              Kenny Martinez - Database Developer  

This is next Billion Dollar Project/Start- UP 


## Table of Contents
- [Overview] (https://github.com/jasur-2902/SoftwareEngineeringProject#overview)
- [Requirements] (https://github.com/jasur-2902/SoftwareEngineeringProject#requirements)
- [Design] (https://github.com/jasur-2902/SoftwareEngineeringProject#design)
- [Risk Management Plan] (https://github.com/jasur-2902/SoftwareEngineeringProject#risk-management-plan)
- [Implementation] (https://github.com/jasur-2902/SoftwareEngineeringProject#implementation)
- [Sprint Schedule] (https://github.com/jasur-2902/SoftwareEngineeringProject#sprint-schedule)
- [Software Development Plan] (https://github.com/jasur-2902/SoftwareEngineeringProject#software-development-plan)
- [Coding Style] (https://github.com/jasur-2902/SoftwareEngineeringProject#coding-style)
- [References/Glossery] (https://github.com/jasur-2902/SoftwareEngineeringProject#references-glossery)

## Overview

We wanted to develop an appliation that sustains the ability to schedule free time from one user to another. This idea came up from the point that as college students, scheduling free time between colleague can be a hassle so why not let the app decide to do the scheduling. We want this app to be where you can input your schedule of your daily life, and having to set a priority to certain events. We want our code to be as user-friendly as possible so that the average phone user can use it. We will be using android studio for our application development alongside with google firebase to help set up the database to make this a fully functional social aid application. The documentation below will lay the ground work for our requirements, design, implementation, the sprint schedule, the software development plan, and various other components to make this application a reality.

## Requirements

We want to create an application that users can schedule free time with another user. Our users (students, teachers, managers, etc.) would be able to register an account with a unique username, an email address, a password and to check in with our terms of service before using. Once that is setup, the user will be able to log into their account with their email address and their password. There is also an option for which if in the case if a user does forget their password, there is a forgot password section in which they will recieve an email to reset their password. All forms of authentication was made possible by Google Firebase integration. Once logged in, the user will be able to start setting up their schedule. They can input data into the calendar/event screen and set a time frame for their events. They can also set a priority to their event whether or not to be bother or not. Users can use this to their advantage to create slots for free time when they are available or when they are busy. Sidenote, events can also be altered just in case.The next section that can be look at is the friends section. You can add friends onto the application by searching up their username and sending them a friend request. Once the request has been accepted, you're now on your way to really getting to the app. Next to the friend's username, there is a schedule button, in which you both can schedule free time from one another. Our alogorithms will determine the best time to meet up upon both request prior to execution and give both users potential meetup options. Once a timeslot has been found, you are on your way to getting to meet up with your friend.

## Design

To fully grasp on what we are dealing with, we had to constuct various models to make our application a reality. We started off with a Use-case model of an examplimentary senario of how the application will be used. Below, the model shows two actors and their functions that they get to perform. The general customer is like any ordinary customer and has the abilities to do what it says in the diagram below. The level 2 actor, know as the manager/faculty is what can be obtained if this application was enterprise driven, which at the moment wont be developed until the later in the future. 

<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/Use%20Case.jpg" alt="alt text" width="800">


Alongside with the use-case models, we have our sequence model to visually show how our application will run in terms of functionality. We are given several figures to work with as they hold the sequence to how our application is able to work. The login portal is where our user is able to login to our application as their info is then inputted and then sent to firebase authentication that is implemented within our code to determine if the user will be granted to have access into the account and to proceed. It also shows what happens when the user fails to login and what it outputs. 

<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/loginsequence.jpg" alt="alt text" width="800">

Our registration process is also a fairly similar process to the login process but with additional steps. Users this time have to enter in their email address, username, password and to check off the terms of service to be granted clearance for account creation. Data is then sent to firebase and a new user is born. It will also give our error messages if the user fails to register such as invalid credentials, username being taken already, and that all the fields must be filled. 

<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/registrationsequence.jpg" alt="alt text" width="800">

This next one is the add/edit/delete schedule process in where the user is able to create or edit or delete their schedule. This sequence diagram is the heart of our application and must be followed thoroughly. User clicks on a date, then gets sent to an user interface with fields that need to be filled in. There, the user will have to set their schedule along side a priority to their event; this priority helps the algorithm determine the best scheduling time. Once the user fills out everything and press submit, the info will be sent to firebase and a reference will be made under their branch in the database. As for editing their schedule, firebase will simply retrieve the info and brnig it back to user display in the interface that the user will edit it in. Finally, for deleting an event, there will be a button and the user will have to press it to delete the event; a two step deletion process will be implemented just in case.

<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/editschedulesequence.jpg" alt="alt text" width="800">

Our next one is the add friend function. User will be able to search different unique users through our database and will be able to send a friend request to that said user. Our application alongside with firebase will send the request prompt to the end user. The end user will have the option to either accept or reject their request. Any answer will alter the database. When accepted, the front user will be notified and they will be each others friends. Now as for to match the schedule. Either user will send a request for a meetup/free time together. One user will send the request and that data will be parsed through firebase and our application itself and the end user will determine to accept looking for the free time. Once accepted, our application's algorithm, along side with firebase will help determine the best time to meet up and send the data over to the users. Both users have to agree to a time that best fits their schedule, a list of time slots will be given to them.

<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/addfriendsequence.jpg" alt="alt text" width="800">

As our previous diagrams explained our sequence and user driven approaches to our application, we now focus our attention to the UML class diagrams that help create the code. In here, our class diagram shows various components of our application to what are are planning on following and to implement every frame of. We start off our authentication branch which shows all the classes needed for users to log into the application. This ranges from our login activity, register activity, the start activity, and the forgot password activity. We then focus on our main branch that is the main display for our application. This includes our main activity with a nested class within it called a view pager adapter, a user adapter with a nested class called viewholder, a user fragment for our UI display and functionality, a calendar fragment that is for display and minor functionality and a schedule activity that makes the schedule itself. There are also other activities that are there to make this appliation more welcoming, and we have the profile activity for just that. We also have several classes that make the activities really function thanks to their usefullness. Those classes are the user class, friendlist class, event list class, and the free time algorithm class. Check the figure below for a visual of everything.

<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/RedesignedUMLClassModel.png" alt="alt text" width="auto">

## Risk Management Plan

Before we go on to implementation, it should be noted that there was risk management involved before setting off our project into the social space. After analyzing the project, there are several possible risks that could threaten the project. One of them is the possibility of losing our developers. Due to Covid 19, there is a high chance that one or more of our team members can get Covid 19 infection, and be unable to continue to participate in the development process for an extended period of time. Delay in any part of the project can lead to delays in other parts of the project as all parts are interconnected. Another potential risk is incorrect expectations or possible technical problems. Even though we have been on schedule so far, moving on to Sprint 3, we may have run into some problems. Since we are using leading technologies and platforms that our team is not 100 percent familiar with, there is a chance that something might go wrong. Again, since all parts of the project are interconnected, any issue might cause delay. For these reasons, we have designed some risk management plans using ‘avoiding’, ‘minimization’, and ‘contingency’ techniques. First and foremost, to prevent some of the above-mentioned risks, we have assigned at least 2 people to each part of the project. In other words, at least 2 people are involved in the back-end of the project, at least 2 people in the front-end, and at least 2 people are in the database part. Also, in our schedule for each sprint, we have added extra hours, which might be used to resolve or find solutions to issues that occur during the development process. By doing this, we are avoiding the risk to some extent that if someone in our team will have some medical issues or gets stuck, we will have at least one person who can keep working on that part of the project. Another way to minimize the consequence of risks: we have created dummy data and divided the project that way, so even if one part of the project is delayed, others can still continue development using mock data instead of actual data. So if we have any issues with the database part or the back end part, the frontend can just use mock data instead of actual data and proceed to develop. Also, we have to mention the risk of miscommunication. To avoid the risk of miscommunication, we have several mandatory meetings where we discuss the current status of the project and all issues. Depending on the scale of the issue, we try to eliminate it in the earlystages by using reserved resources (time/ people).


## Implementation

To start this off, we created a new git repository so that we can create a network of communication from code to code. The front end developer (Giuseppe) started off by creating the project files in andriod studio. The front end developer started to utilize the built in feature of andriod studio to flawlessly desgin the front end in terms of user interface and appearance. After time was conducted, various changes were made here and there in terms of what was needed for our application as the team decided what we wanted and want they didn't really need. All work was able to be complete through the agile method of software development. Once a basis was formed to what was needed, the front end developer was the first to push and set off the code basis.

Along side the front end developer, our database manager developer (Kenny) created the database through google firebase. The project was set up via a connection key given to us by firebase to inset into our android studio software for our application to be conected. Any form of schemas and designs for our database was coded in android studio to set up connection to the authentication and the database itself. Our database manager developer also made effort to create a storage reference for any data that deems necessary. The setup for the database, along side to determine how the database schema was going to be like was no easy approach however once a design structure was though up, the database manager developer got to work setting everything up.

Next we move along to our back end developer which in this case was Matt. The back end was a huge task for this application so our scrum master Jasur came to help in terms of creating all the functionality needed. Matt was able to write the code for setting up to find a friend, creating a profile, adding data to your events, etc. This also included the help of our database developer in which any form of communication with firebase was dealt though him. Furthermore, our back end developer was the brain behind creating the algorithm for to set up free time from one user to another. All of this was made possible through trial and error, diagram creation, etc. 

Finally, we get to our project manager/scrum master, Jasur. Our project manager was the lead of our developement, in which he gave order to what was needed to be complete and what was a priority in terms of coding it in. In addition, our project manager was also a scrum master, so he was the mastermind behind creating our schedule of our timeline on monday.com. Every part of our project was labeled here and had to be complete. Our project manager had experience within the field, so in help he also help us understand what we are doing or trying to do. Our project manager was able to help the backend developer, the frontend developer, and the database manager developer. 

## Sprint Schedule


<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/sprint2photo.png" alt="alt text" width="800">


<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/sprint3photo.png" alt="alt text" width="800">


<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/sprint4photo.png" alt="alt text" width="800">




## Software Developement Plan

### Introduction

Purpose: This is the software development plan for the android application Schedule.Me. Within
this section of the document, there will be given a layout of how this application will be
implemented and created. We layout the system requirements that can be derived from the
use-case models along with other models such as UML diagrams and interaction models.

Scope: The Schedule.Me team will be creating/developing a system that allows single/multi
users, alongside any corporation entity, to manage a way to find time between two individuals
based on their schedule of the day. With that being said, this software development plan
document scope focuses on who is assigned with what task to do along with how our system will
meet our requirements specified by our models.

### Roles: 

Project Manager: sets up the task on monday.com to ensure on what we have to do in terms of
assignments; is the main brain of the project in terms of what it is and focuses on the business
aspect of the project. They are also the master of version control on github to ensure that our
code is good to integrate.

Project team: sets up the code environment alongside brainstorming graphical models and plans
to integrate the models into a real product by utilizing software architecture techniques.

### Usability

Social Use: Schedule.Me will be used as an application aid for those wishing to find free time
with their friends. The application will be available through the google play store and only on
android devices(Android 8.0+). To be able to look up friends and add them to your friend list is
part of the first step, then you move forward to requesting free time with them and the
application will match the free time based on both you and your friends.

Enterprise Use: If done correctly, Schedule.Me can be used in an enterprise environment such as
a company or an educational institution. Companies can utilize the application to schedule
meetings with anyone so there is no need to hassle when each and everyone has a free time slot.
Educational institutions can input data into the application for their students and/or faculty and
schedule free time between faculty and students.

Overall Usability: Schedule.Me is an application that strives to be easy to use and user friendly.
We start off this by introducing a friendly UI interface that makes it easy to traverse through the
application. We also have prompts that give instructions on how to use the app while interacting
with it. This supplements the ease of using the app. Along with a friendly UI, there must come
appealing looks to the application itself so the application is bright and colorful in terms of
background and widgets that it utilizes.

### Reliability

Schedule.Me must ensure a good method for delivering free time from one user to another. This
process will be challenging to achieve since this is the selling point of our application. We want
our users to use this app and not worry about the logistics and correctness of the outcome; this
then leads into user satisfaction and from there, that's how our application will build from itself.
To add on to that, we also want an application that uses the best services to be web based,
therefore for our database and server, we use firebase. Firebase is runned by Google and offers
free cloud services along with free hosting for any mobile application. It’s noSQL feature by the
use of JSON files helps us organize what we want for our application. If we were to host our
down server and/or database, we would most likely run into vulnerabilities by eye-appealing
16 hackers who want to access our data. Google Firebase ensures that our data is secure and
encrypted to prevent any fraudulent actions that can take place.

### Efficiency

What we look for in Schedule.Me most importantly is efficiency. This includes runtime when the
user uses the application. We want the user to easily use this application without any drawbacks
from the application being slow so we must have efficient and abundant code so it can not be an
anchor when using it; we don’t plan on this app taking up too much in memory/processing power
when in use. We also want the application to utilize all the resources that we can give it such as
always being online and ease to update information within the application. For our app to always
be online, this is where yet again firebase comes in handy. As for updating information, we want
to not make it a hassle if the user has to update their schedule for a day or for several days

### Maintainability

Schedule.Me should expect to see some updates once it is launched. Updates are usually enacted
based on user feedback or from beta-testers of a prototype of an upcoming version of the app.
When an update is added, there should be improvement to the app rather than more bugs added
to it. Let's say if there exists a bug on a certain OS of android, then it must be fixed as soon as
possible. Prior to setting our new features or updates to the app, there must be test cases created
and passed before an update is published to the app store.

### Portability

Schedule.Me needs to fulfill its way of storing its data and itself across a majority of platforms.
What this means is that users can install the app on any device and don’t have to worry about
how its data is being stored. Let's say that the user wants to migrate devices and wants to keep
their data, in short - they can do that. The power of having google firebase enables up to keep the
data and for users to switch from one device to another if they want to. This includes their
schedule, their friends list, priorities of events in their schedule, their profile picture, their login
credentials, and anything that was left off from the other device which is now migrated onto the
second device.

### Model Plan


<img src="https://github.com/jasur-2902/SoftwareEngineeringProject/blob/master/projectpics/diagramextra.png" alt="alt text" width="800">

The figure above demonstrates the overall software development plan. The frontend, backend,
and database boxes enclosed in the larger box shows how all three implementations will be done
simultaneously.

## Coding Style
Comments: use comments to indicate what each function and intent does. Avoid unncessary comments relative to syntax decisions
Whitespace: increment one tab for every nested operation. Avoid leaving too much blank lines
Naming: Capitalization for class names, camel back for function names, and lowercase with usage of m-dash and under_score for variable names
Possible Error / To do: comment with tags To Do or To Revise so other contributors can take notice

## Reference/Glossery
Android Studio Documentation: https://developer.android.com/docs
Google Firebase Documentation: https://firebase.google.com/docs
