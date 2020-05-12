# Product Evaluation
Throughout the development of our product, we made sure to evaluate the decisions which we were making. We made sure to get feedback from our clients so that we knew our design choices were on track. These key meetings coincided with our three submission deadlines:

## Minimum Viable Product
When developing our MVP, we did not have access to the intended users of the app. However, we did still have people who could critique our design and development choices: our clients, our mentor and each other. This evaluation took place as part of our regular in-person meetings which took place either in MVB or in meeting rooms in the Queen’s building. In these meetings we would demonstrate the current state of our app and then ask for and discuss any feedback that both our mentor and/or our clients had. As we were all present during these meetings, we decided that the best way to document the observations made was by directly creating tasks on Jira which corresponded to the new ideas and changes that we had agreed upon. These could then be later assigned as and when they were used.<br>
The key findings from our evaluation at this stage involved the control scheme for our game. We received feedback that the drone needed to move faster and were asked to add a feature to turn the camera from the left slider, which differed from our initial choice to add a new slider for rotations. It was also requested that we add a minimap to the game. Both of these features were added successfully before the next release. There were other aspects of our app which were evaluated here, such as the initial physics engine for movements and the mock buildings which we used for size comparisons, but no issues were made so we didn’t need to make any refinements here.


## Beta Release
Tested again through observation.
People involved: clients, us, other students.
These tests were often performed while we were programming, so we sometimes didn't document as we just implemented the changes directly. (We often worked as a group, or at least with pair programming, so rarely needed to convey this information outside of face-to-face communication)
Here we couldn't test with users because the workshops are infrequent and didn't line up.
Tested outside of the workshop demographic to get a broader sense of feedback. It's gonna go on the play store so anyone can use it.
Advanced physics engine with collisions.
Extra game mechanics, loops.
Menu system extended, achievements and settings

## Final Release
Observation, recorded feedback from users. Asked for feedback to be based on two separate parts: the visuals and the gameplay.
This was documented (see sample material) and we used it to make some improvements. 
Other improvements were not made due to time constraints found as a result of the lockdown, but we will make these changes in the summer. (This might not be needed here, as extra notes contain repetition.)
People involved: us, two close-to-users, wanted more users but covid.
New buildings
More game mechanics, fuel pickups, letter collection, air streams.
Final physics engine with new speeds
Customisation
Levels

## Sample Material
| Critical comment(s) | Possible solutions | Solvable |
|:---------------|:-----------|:----------:|
|"Turning is too sensitive."<br>"It is hard to hover in place."|The slider to control both rotations and height could be made larger, so that more precision can be gained when it is in use. This will allow for slower turning speeds to be used, and should make it easier to find the correct position to hover the drone.|Yes|
|"The loops turn red too fast."<br>"The loops are too far apart, they don't show up on the minimap until you are close."<br>"The timer ran out too fast."|These are level design issues that we can solve by simply changing the level files.|Yes|
|"I keep getting stuck on the building, I want to slide across the walls instead."|This is a much harder issue to solve as it would require changing a large part of how our physics engine calculates collisions.|Not without large changes|
|”There is a lack of interesting UI”<br>”It’s just basic Android”<br>”It is hard to know when sensors are equipped”|Custom UI could be designed to make the menus more interesting|Yes|
|”I’m not told how the sensors affect the gameplay”|We simply need to add a sentence to each sensor to explain what it does to the game.|Yes|
|”The controls are unconventional, they’re not like in other games”|This was an intentional design choice as it was meant to mimic the controls of the drones used in our clients’ workshops.|No|
|”The camera clips into objects”|This would require us implementing camera culling|Yes|
