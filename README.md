# Battlecode 2016

**what_thesis**: _Aaron, Stephen, Ryan, Tamas_


## Master TODO

### _Shorter Term_
- Functions to find distance to edge of turtle and move along edge of turtle in a direction
  - Should prevent archons from getting stuck in a corner
- Prioritize turtle build locations that have more adjacent turrets
  - Should help prevent long branches
- Sighting scouts moving to/along edge, idle ones (no enemies or movement) clear rubble
- Free scouts fighting for freedom
- Add Archon scouts: make sure each Archon ALWAYS has an adjacent scout
  - If archon scouts senses turret, backs up to give Archon enough room to retreat
- Create separate turret signal from zombie signal so everyone knows where turrets are
- Archons can remember all seen turrets and know not to build/go there

### _Longer Term_
- Use simplified/not-bug nav to move inside ball
- Micro overhaul to account for different enemy considerations
- Keep track of interesting waypoints (estimated/observed den locations, seen parts, seen enemies)
- Bored turrets pack up and move to more interesting directions



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
