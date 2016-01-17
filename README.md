# Battlecode 2016

**what_thesis**: _Aaron, Stephen, Ryan, Tamas_


## Master TODO

### For Seeding Submission

- The blitz Archon's scout should get a message to use BlitzTeamStrat
- If we have 3+ archons, one should build a turret turtle
- All archons should look for parts and neutrals within sensor range and get them, regardless of strat

- GTFO code for Archon that is screwed - running even without safe dirs or digging away.
- Need scouts to send "get help" messages so that soldiers in mob fight attack dens.

- Scouts in Ball Move still get shot.


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
