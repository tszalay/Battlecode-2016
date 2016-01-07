# Battlecode 2016

**what_thesis**: _Aaron, Stephen, Ryan, Tamas_


## Master TODO

### _Short Term_
- Archons moving around and building
- Scouts moving and sighting
- Chaos scouts

### _Medium Term_
- Nav overhaul to move in crowded spaces
- Micro overhaul to account for different considerations
- Global strategy/build direction

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
