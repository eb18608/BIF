# Architecture


After deciding on the project requirements, we went about deciding which ones played a significant feature in deciding the architecture of our final product. We grouped them into 3 main groups: technical contraints, project constraints and quality attributes. We settled on these requirements as our **Architectural Drivers:** 
- Technical Constraints (1.x)
  - (1.1) Mobile application
  - (1.2) Offline functionality
  - (1.3) 3D graphics 
- Project Constraints (2.x)
  - (2.1) Java based project
  - (2.1) Due 1st May 2020
- Quality Attributes (3.x)
  - (3.1) High poly 3D models (Lisence Free)
  - (3.2) Smooth performance (Across multiple devices)
    - (3.2.1) Constant high framerate
    - (3.2.2) Clear UI
  - (3.3) Concrete fucntionality testing
  - (3.4) Concrete system testing

In order to fulfill these architectural drivers, we employed the following programs:
  - Android Studio
  - Processing 3
  - Maya

Key libraries to the architecture of the project include:
  - PApplet
  - Apache Commons .CSV library
  - JUnit

### Software
#### Android Studio:

Android Studio fulfilled a large portion of the architectural drivers as a Java based (2.1), mobile application (1.1) development and distribution platform (2.4). Android Studio also provide a plethora of mobile application support (1.1) such as touchscreen functionality libraries. It also provides a plethora of UI tools which allow us to create and operate the separated **MainActivity** front end systems (which include systems such as our main menu, level select, settings menu)(3.2.2). As well UI tools and libraries, it provides seemless support of both modular (JUnit)(3.3) and system testing (APK build testing)(3.4).

#### Processing 3

Due to the Java based constraints, we have chosen to use Processing 3 as our 3D graphical engine. Fortunately, it was very easily integrated into a larger Android application environment, making it ideal for mobile application development (1.1). Moreover, known for its efficiency, we thought it would be quite good in keeping graphical rendering overheads to a minimum, especially key when designing software to run on smaller, less powerful mobile processors (3.2.1). Processing also provided seamless support for universal 3D model file types (3.1). 

#### Maya

Due to the experience of some of our team members, we decided on using a 3D modelling software to create our very own 3D models (3.1). This eliminated the problem of procuring license-free models. Maya is a software with quite a lot of learning support which made the learning curve a lot easier for the teammembers in charge of developing models to develop high 3D assets within the time frame of the project (2.2). Moreover, Maya's exporting system included a file type which was fully supported by processing which made translating those models into our graphics engine easy. Maya also created low impact model files which would help a lot when it comes to rendering performance in tandem with Processing 3.

### Libraries

#### PApplet

The PApplet library provides the Processing.core (3.1, 3.2) fucntionality to java based projects. Importing this allowed us access to all of processing's functionality as a graphics engine in our Android Studio environment (1.1) resulting in the graphical engine backend of our game (which we dubbed **GameSketch**).

#### Apache Commons .CSV

We planned to utilise the universality of .CSV files as a means of efficient level creation and asset arrangements in our game (2.1). This library provided us with the necessary .CSV parsers we would need.

#### JUnit Testing

We used the JUnit Testing library to create tests in order to uphold the Test Driven Development approach we wanted to take. It allowed us to create class specific tests which would ensure the integrity of the methods we were creating and using (3.3).


<!DOCTYPE html>
<html>
<head>
<title>Architecture_Diagram</title>
<meta charset="utf-8"/>
</head>
<body><div class="mxgraph" style="max-width:100%;border:1px solid transparent;" data-mxgraph="{&quot;highlight&quot;:&quot;#FFFFFF&quot;,&quot;nav&quot;:true,&quot;resize&quot;:true,&quot;toolbar&quot;:&quot;zoom layers lightbox&quot;,&quot;edit&quot;:&quot;_blank&quot;,&quot;xml&quot;:&quot;&lt;mxfile host=\&quot;app.diagrams.net\&quot; modified=\&quot;2020-05-01T11:39:40.427Z\&quot; agent=\&quot;5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36\&quot; etag=\&quot;ph6EwXfDckU9NWmcir84\&quot; version=\&quot;13.0.4\&quot; type=\&quot;onedrive\&quot;&gt;&lt;diagram id=\&quot;6a731a19-8d31-9384-78a2-239565b7b9f0\&quot; name=\&quot;Page-1\&quot;&gt;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&lt;/diagram&gt;&lt;/mxfile&gt;&quot;}"></div>
<script type="text/javascript" src="https://app.diagrams.net/js/viewer.min.js"></script>
</body>
</html>

    Figure 1: Architecture Diagram

Figure 1 shows the different front and back end systems we will be creating. Our front-end systems will include the **Main Menu** which provides links to the **Settings, Achievements, Level Select** and **Customsations** systems. All will utilise the touchscreen navigation libraries present in Android Studios to enable user interaction. 

Interactions in **Level Select** and **Customisations** will utilise the various CSV parsers we implemented (using Apache .CSV) in order to determin the state of the loaded game; which in turn will interact with the **3D Models** and **Graphics Engine** to display the playable level. 

**User Controls** is in its own box as it is a unique user interaction, only working in parallel with game rendering. An Android UI is overlayed on top of the game render to register user controls, passing into the **Physics Engine** to be processed in the back-end. 

The **Physics Engine** uses the inputs from the **User Control** overlay in order to calculate the state of the game every frame through a series of mathematical functions which calculate things like position, acceleration and the presence of collisions/ interactions. Due to the independent operation of these functions, the **Physics Engine** was the only module which could be tested via **Unit Tests** as all other aspects were intrinsicly tied to external systems like the **Graphics Engine**/ **3D Models** making them difficult to test in isolation. 

The rest of the systems developed were tested in both functionality and integration via **APK Build Testing** due to their dependencies. **APK Build Testing** allowed us to create and test builds of the game, using system displays to test interactions between systems and to also test gameplay feel (a testing criteria which is quite important due to the nature of our product). 
