# Battlecode 2016

**what_thesis**: _Aaron, Stephen, Ryan, Tamas_


## Master TODO

### For Qualifier Submission
- Hard code early game builds. Scouts out way to late on 1 archon maps like barricade.
- Build 1 explore scout per archon before shadow scouts.
- Scouts can't miss dens on edge of map. See goodies, caverns, we lose on goodies because of it
- Make soldiers search for dens or surround even before dens seen. Not enough early action on caverns. We lose ~200 rounds on scouts waiting for a scout.
- Avoid units stuck at rubble for 1000 rounds. See Barricade and presents. DenReapers does well on presents: Round 4 Losers B DenReapers (#222) vs. Protoss (#237)
- Fix surround/zday bug. On scouts surround is never joined by one archon timid, no Zday.
- GTFO for archons. See endgame on swamp, diffusion.
- Add a few midgame vipers. They devastate us on swamp, the_duck adds 2 around round 300 too.

We suck on diffusion. clutch Queen sacrifice by jene after we viper her brood takes us out in the end. GTFO archon would help.

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
