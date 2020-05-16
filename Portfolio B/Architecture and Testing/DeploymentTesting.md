# Deployment Testing

### Release Testing Strategy:
|   Phase   | Heuristic  |Test         |Met         |
|:----------|:-----------|:------------|:----------:|
|**Minimum Viable Product (1.x.)**|1.1. Working movement (**Physics Engine**)<br>1.2. Drone Model (**3D Models**)<br>1.3. Control System (**User Controls**)<br>1.4. Menu (**Front-End UI**)<br>1.5. Test Level(**Graphics Engine**) | 1.1. JUnit Tests Pass<br>1.2. Drone model present<br>1.3. Control feedback visible<br>1.4. Menu navigation possible<br>1.5. Level with win condition complete| 1.1.**Yes**<br><br>1.2.**Yes**<br><br>1.3.**Yes**<br><br>1.4.**Yes**<br><br>1.5.**No** |
|**Beta Release (2.x.)**| 2.1. Collision Detection (**Physics Engine**)<br>2.2. 3D Building Models (**3D Models**)<br>2.3. Intuitive Controls (**User Controls**)<br>2.4. Achievements menu (**Front-End UI**)<br>2.5. Test Level (**Level Select**)| 2.1. Collision detected with building <br>2.2. Imported and rendered building.obj<br> 2.3. Controls mimic drone remote <br>2.4. Achievements page generated via .csv file<br>2.5. Level with building and win condition |2.1.**Yes**<br><br>2.2.**No**<br><br>2.3.**Yes**<br><br>2.4.**Yes** <br><br>2.5.**No**|
|**Final Release (3.x.)**|3.1. Sensor Models (**3D Models**)<br>3.2. Customisation Menu (**UI**)<br>3.3. Level Handler (**UI**)<br>3.4. Environment Interactabels (**Physics Engine**)<br>3.5. Playble Levels| 3.1. Models present <br>3.2. Customisation selections playable<br>3.3. Correct levels loaded from .csv file<br>3.4. Correct interactions objects<br>3.5. Levels completeable |3.1.**Yes** <br><br> 3.2.**Yes** <br><br> 3.3.**Yes** <br><br>3.4.**Yes**<br><br>3.5.**No** |

Through our project through APK Build Testing (defined in **Development Testing**), we were very easily able to test high-level heuristics (above). The dev team provided a wealth of heuristic feedback from build testing, but we also conducted external testing.

Releases were tested with the clients through arranged play-test meetings. A meeting with the client would be arranged to test the current stage of the game against the heuristics defined in the release testing strategy table. As the client was already aware of the heuristics set out for the current release (decided in the prior meeting), the client made for the perfect test user.

#### Method:
We took the heuristics decided for the specific release and planned tests which would prove the heuristics met (shown in **Table 2**). During the play test, the client would be given a device with the game installed on it and we observed as they interacted with the application. Through observations and a questionnaire after the playtest we would mark down the results of the heuristic tests. These results would then go onto influence the decision of the next target heurisitcs for the following release.



