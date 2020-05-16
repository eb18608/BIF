# Overview
**The client:**
*   The PhD research group at the Bioinspired Flight Lab in the University of Bristol conducting research on the flight behaviour of urban gulls in Bristol
    -	Research includes how gulls utilise thermal air currents to conserve energy, how their behaviour differs from gulls living in a rural area, etc.
    -	The research is used to find ways to maximise the potential of drones and their possible contribution to society
*	The students run a SCEEM Outreach workshop related to the research
    -	The purpose of the workshop is to engage attendees with a possible future in STEM
    -	Attendees are given a problem and asked to solve it using drones, using a budget system to purchase bio-sensors (attachments inspired by natural sensory organs of animals) that may be useful for solving the problem
    -	Attendees then pilot a real drone around a physical obstacle course

**The domain:**
*	The main domain of the product is *education*, though the solution is also intended to entertain users

**The problem:**
*	The client desires a platform similar their workshop to keep attendees engaged with STEM post workshop

**Our solution:**
*	A mobile (Android-based) 3D game intended to replicate the challenges given to the participants of the workshop
*	The challenges will consist of problems in the original workshop, divided into several levels
    -	Certain levels may be timed to simulate time-sensitive problems
*	A customization system will also be available outside of gameplay
    -	Different bio-sensors can be equipped on the drone
    -	Each bio-sensor adds some functionality to either the UI (i.e. minimap) or the drone itself (i.e. the ability to pick up more than one object)
    -	Bio-sensors will also contain educational flavour text about the STEM research that inspired it

# Requirements:
## Core user stories and flow steps:

Out of the user stories above, the ones we will choose to focus on are for the **curious student** and the **clients**.

## The curious student:

*As a curious student, I want the game to be engaging, informative and representative of real-world scenarios. I would like this so that I can further look into the subjects and be more informed._*

Basic flow:
<img src="./Portfolio%20B%20Merged/Assets/studentBasic.png" alt="drawing" width="700" ALIGN="center" HSPACE="15"/>

Alternative play flow:
<img src="./Portfolio%20B%20Merged/Assets/studentAlternative.png" alt="drawing" width="600" ALIGN="center" HSPACE="15"/>

While this would not necessarily help the student achieve their initial goals of looking further into the subject of the workshop, it could still lead to the student gaining an interest in drones and/or further topics involved with the engineering of drones, which would push them towards STEM.

## Clients

_As a client, I would want this game to be engaging, inspiring and somewhat informative. I would like this so that the attendees can remember the workshop fondly and may wish to share their experience with other students (via the game)._

Basic flow:

<img src="./Portfolio%20B%20Merged/Assets/phdBasic.png" alt="drawing" width="600" ALIGN="center" HSPACE="15"/>

An alternative flow would be to inform the attendees during a break in their workshop.

As for exceptional flows, the app will be Android-based, so attendees will be unable to install it on iOS, for example. However, this is not an issue which we can resolve, due to being locked to creating our app for Android by the project requirements.

Our main goal is to fulfil the client’s request of increasing the attendees’ engagement with STEM. As such, we will base our requirements mainly around the user story of the **curious student**, as they are the attendee that is most likely to benefit from such a platform.

## Functional & Non-functional Requirements

### Code Legend

| Code     | Actor(s)                                                           | Purpose of requirement                               |
|----------|--------------------------------------------------------------------|------------------------------------------------------|
| **GAME** | The user of the app (as a player)                                  | Providing an enjoyable gameplay experience           |
| **INFO** | The clients, as well as the user of the app (as a curious student) | Keeping users engaged with STEM                      |
| **APP**  | Google Play Store                                                  | Abiding the constraints set by the Google Play Store |
| **SEC**  | The user's security                                                | Protecting the users' security and privacy           |

### Functional Requirements

| Code         | Description                                                                                                                                                                                                                           | Overridden by | Status        |
|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|---------------|
| **GAME-1**   | The game must be optimized to run at a constant framerate of at least 30 FPS to ensure a smooth gameplay experience, even with 3D assets and the (relatively) limited hardware of a smartphone.                                       | N/A           | Fulfilled     |
| **GAME-2**   | The core game features will function normally offline. This is to ensure that the user is able to use the app at any time, without relying on a working Internet connection.                                                          | N/A           | Fulfilled     |
| **GAME-3**   | The user interface must have a control scheme that is considered easy to learn by at least 7 out of 10 play-testers.                                                                                                                  | N/A           | Untested      |
| **GAME-3.1** | The initial design of the user interface will use the same control scheme as the drones in the workshop, to allow participants to learn the controls quickly.                                                                         | **GAME-3**    | Fulfilled     |
| **GAME-4**   | Collision detection will happen before applying player control during each iteration of the game loop, to ensure that the player does not move when it isn’t supposed to (i.e. if the player is trying to phase through a building).  | N/A           | Fulfilled     |
| **INFO-1**   | The application will include a page with scientifically accurate resources related to the client’s research of bio-inspired flight and sensors, to further the client’s goals of encouraging the user to pursue an education in STEM. | N/A           | Fulfilled     |
| **INFO-1.1** | The main resources related to this research must be accessible offline to allow the user to read through them at any time, at their own pace.                                                                                         | N/A           | Fulfilled     |
| **INFO-2**   | If the game has a loading screen, the application will also include facts related to the client’s research on the loading screen, to allow the user to learn during gameplay without being overly intrusive.                          | N/A           | Not fulfilled |

### Non-functional Requirements

| Code         | Description                                                                                                                                                                                                                                                                                      | Overridden by | Status             |
|--------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|--------------------|
| **APP-0**    | The size of the app’s APK file **must NOT exceed 50MB**, to comply with the Google Play maximum APK file size limit on all devices. (Some versions of Android allow for APK files of up to 100MB, but we want the app to be accessible to as many people as possible)                            | N/A           | Fulfilled          |
| **APP-1**    | The game must be suitable for the PEGI 3 rating, to allow attendees of all ages to benefit from the app.                                                                                                                                                                                         | N/A           | Fulfilled          |
| **GAME-5**   | The different problems presented in the game must mirror the problems in the workshop activities, to supplement the client’s existing workshop.                                                                                                                                                  | N/A           | Fulfilled          |
| **GAME-5.1** | The development team will ask the client for advice if any additional scenarios are to be added to the game, to ensure that a sense of realism is preserved.                                                                                                                                     | N/A           | Not yet applicable |
| **SEC-1**    | If a login system is implemented, user accounts must be secured with a password of min. length 8, containing at least one uppercase letter, one lowercase letter, and a number. This is to ensure a reasonable level of security on the user’s end.                                              | N/A           | Not applicable     |
| **SEC-1.1**  | Any passwords stored this way must be stored in its database in salted hash form, to ensure a reasonable level of server-side security.                                                                                                                                                          | N/A           | Not applicable     |
| **SEC-2**    | The app must NOT allow the user to store sensitive information (i.e. address, health conditions, etc.), as the app is intended to be used by people of all ages, including children as young as 6 years old, who likely do not understand the implications of sharing such data.                 | N/A           | Fulfilled          |
