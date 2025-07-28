Keyboard Detector Command Usage Guide
=============================================================================
1. /keyboarddetector iskeydown
Function: Detects whether a single key is pressed.
Usage: /keyboarddetector iskeydown <PlayerID> <KeyAscii>
Example: /keyboarddetector iskeydown @p 49

Detects if the player presses the '1' key.

When placed in a repeating command block:

Returns true and outputs a redstone signal while '1' is held.

Returns false with no redstone output when released.

=============================================================================
2. /keyboarddetector matchgroup
Function: Detects simultaneous multi-key presses (keys are processed without mutual interference).
Usage: /keyboarddetector matchgroup <PlayerID> <KeyAscii1>,<KeyAscii2>,... <keepStatic>
Example: /keyboarddetector matchgroup @p 49,50,51 false

Detects if the player presses '1', '2', and '3' concurrently.

When placed in a repeating command block:

Returns true/redstone signal only when all specified keys are pressed simultaneously.

If <keepStatic> is set to true:

Maintains true/redstone signal after keys are released until manually reset.

=============================================================================
3. /keyboarddetector flush
Function: Clears all stored key-press records (used when <keepStatic> is true).
Example:

After executing /keyboarddetector matchgroup @p 49,50,51 true in a loop:

Pressing/releasing '1','2','3' maintains a persistent signal.

Executing /keyboarddetector flush will:

Immediately terminate the redstone signal.

Reset detection until the next valid simultaneous key press.
