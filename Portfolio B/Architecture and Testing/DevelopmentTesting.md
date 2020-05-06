# Development Testing


Consistent with Test Driven Development, we decided on testing strategies for the various systems we had in our architecture diagram. 
These testing frameworks were decided on:

- **Front End UI** - APK Build test
- **Graphics Rendering** - Visual Testing
- **.CSV Level Handler** - APK Build Testing
- **Physics Engine** - JUnit Testing

#### APK Build Testing
Android studio provided an **APK Build** feature which allowed us to test things like functionality of certain *non-numeric* features, as well as robustness of integrated systems. This would compile the project state and export it as a testable APK which installed onto a connected mobile device (a terminal would display system messages describing the events occuring and processes running during application usage). This made more sense for complex integrated systems like the **.CSV Level Handler** and **Front End UI**. A full **APK Build** was the only feasible way of testing and monitoring these complex interactions via insightful system messages. As well as testing the functionality of the system's interactions, it also allowed us to test the useability factor of the application. This is equally as important to test when creating a UI and is only really feasible through active use of a testing build.

#### Visual Testing
Things like graphic output is difficult to emperically test and requires assets (such as 3D models) to be at the ready in order to test. Earlier on in development, where the 3D assets were not ready, we used visual placeholders in order to ensure the correctness of the renderer during the **APK Build Tests**. Through this, we were able to test that objects created in out graphical engine would be rendered in the correct position, orientation and size.

#### JUnit Testing
Using the JUnit test framework, method specific tests can created in a separate testing environment. Using a plethora of assertions, testing the correctness of methods in both normal and edge cases prove the robustness of a system and its methods. In back-end systems like our **Physics Engine**, which was implemented with methods that worked completely independent of external systems in the application, creating tests was easy and effective as inputs and expected outcomes could be calculated and could easily be instantiated in the testing framework.
<br>
<br>
Below is an example of some **JUnit** test cases used to test the **Physics Engine**:
<br>
<br>

### **Testing Table Sample:**
<br>

|Test              | Testing Condition | Pass/Fail    |
|:-------------    | ----------------- |-------------:|
| @Test<br>forceAppliedTest | `//Set the comparison parameters`<br>`expectedAcc = (240f, 150f, 0f);`<br>`//Set context`<br>`result = forceApplied(initAcc, inputForce, movement.getMass(), movement.frametime);`<br>`assertEquals(expectedAcc, result, delta);`| **Pass** |
| @Test<br>CalcVelTest | `//Set Comparison Parameters`<br>`inputAcc = (240f, 150f,0f)`<br>`expectedVel = (8f, 5f, -6f);`<br>`//Set Context`<br>`result = movement.calcVel(movement.getVel(), inputAcc, movement.frametime);`<br>`assertEquals(expectedVel, result, delta);` | **Pass**|
| @Test<br>CollisionTrueTest| `//Set context`<br>`InitPos = (2543f, 3500f,500f );`<br>`movement.setPos(initPos);`<br>`movement.setMovementSize(droneObject);`<br>`obj = ApartmentsObject(4000f, 660f, 4000f, 1, 1, 1);`<br>`movement.isCollision(movement, obj);`<br>`assertTrue(movement.collided);`| **Pass** |
| @Test<br>UpdateMovement<br>Test |`//Comparison parameters`<br>`expectedAcc = (240f, 150f, 0f);`<br>`expectedVel = (6f, 3f, -4f);`<br>`expectedPos = (0.27f, 0.17f, 0f);`<br>`//Set context`<br>`movement.updateMover(expectedAcc, expectedVel, expectedPos, movement);`<br>`assertEquals(expectedAcc, (movement.getAcc()), delta);`<br>`assertEquals(expectedVel, (movement.getVel()), delta);`<br>`assertEquals(expectedPos, (movement.getPos()), delta);` | **Pass** |


