# Battlecode 2016

**what_thesis**: _Aaron, Stephen, Ryan, Tamas_


## Master TODO

### For Qualifier Submission
- Improve micro and run scrims. Probably get units to clump before attacking and get everyone to front line. Watch scrim on 24th vs trump, mid high diamond, felix
- Debug z-day and surround. Watch scrimmage on 24th vs vvphys and turing_it_up, somewhat testable on scouting vs jene
- Activate nearby neutral archons robustly. Broken on goodies vs jene 11PM Sunday 24th.
- (Fixed?) Archons must run to allies when threatened and away from enemies. Watch channels vs Team Sheep scrimmage on the 24th
- Balance early game builds. Scouts earlier, more vipers.
- Make scouts see dens on very edge of large maps. See goodies.
- (Improved?) Make soldiers search for dens or surround enemy before dens waypointed. Not enough early action on caverns vs jene. We lose ~200 rounds on scouting waiting for a scout
- Shadow lone enemy archons and waypoint rage them. See scrimmage with return win on helloworld on 24th
- Avoid units stuck at rubble for 1000 rounds. See Barricade and presents vs jene. DenReapers does well on presents: Round 4 Losers B DenReapers (#222) vs. Protoss (#237)
- Have a few midgame vipers. They devastate us on swamp vs jene, the_duck adds 2 around round 300 too
- ~~GTFO Archons. See scrimmage on 24th vs vvphys on space, somewhat testable on on swamp, diffusion vs jene ~~
- ~~Hard code early game builds. Scouts out way to late on 1 archon maps like barricade. Build 1 explore scout per archon before shadow scouts.~~

### For Seeding Submission

- soldiers switching back from mob to do ball get lost and sit around.
- add rubble removal and decisions about when to dig vs go around.
- scouts built in turret range should gtfo
- strange error saying scout can't move there bc too much rubble?
- archons get stuck in their balls
- GTFO code for Archon that is screwed - running even without safe dirs or digging away, avoid small rubble
- MapInfo.doScoutSendUpdates doesn't work - it's being hacked through at the moment. Otherwise, it keeps thinking the parts it just saw are new and messaging forever. Couldn't debug it - AK.


### _For Sprint Submission_
- Archons go to and pick up parts
- Units clear rubble in the way of Archon - AK will work on this with soldiers
- Soldiers follow archons and swarm enemies - AK basic stuff is there, need to integrate with micro
- Picked up parts and destroyed zombie dens get removed from map info
- Scouts can do sighting and shadow units, either turrets or archons
- Units flee from zombies away from our base AK - but what base? possibly they should do this when the get shot, which is a bad sign

### _Longer Term_
- Bored turrets pack up and move to more interesting directions
- Scouts can steer big zombies and other units will pick off other zombies
- More intelligent global strategy as it develops



## Coding guidelines

- You should be able to draw a flowchart for every action and attempted action
- Function name/layout should follow the flowchart, refer to this before implementing anything
- Functions that decide between different behaviors/actions should not contain any of the individual actions' code
  - e.g. both tryBFS() and doBug() are in subfunctions, not just tryBFS() then a buncha code
- Main functions with actions return bool
  - Start with "can/is/has" for functions that check but do not modify state
  - Start with "do" for function that always does action, "try" for function that attempts actions
- Any variables outside the function should be referred to with context
  - e.g. Bot.here for static members
  - e.g. this.Target for instance members
- Put non-state/non-behavior functions (checks etc) in Common files in the base bot so everyone can use them
- Use Debug class for debugging output
- MessageBoard: TBD
