# Keyboard Detector Command Guide

*Advanced keyboard event detection in Minecraft*

## Command Overview

| Command       | Functionality                     | Key Parameters               |
|---------------|-----------------------------------|------------------------------|
| `iskeydown`   | Single key press detection        | `<KeyAscii>`                 |
| `matchgroup`  | Multi-key combination detection   | `<KeyAsciiList>`, `<keepStatic>` |
| `flush`       | Reset persistent key states       | None                         |

---

## 1. Single Key Detection: `/keyboarddetector iskeydown`

### Functionality
Detects real-time press state of a specific keyboard key

### Command Syntax
````command
/keyboarddetector iskeydown <PlayerID> <KeyAscii>
````

|Parameter |	Description |	Example Values |
|------------|----------------|-----------------|
| `<PlayerID>` |	Target player selector |	@p, @a, @e[type=player]|
| `<KeyAscii>` |	ASCII code of the 11key to detect |	49 (1), 32 (Space), 65 (A)|  
### Usage Example

````command
/keyboarddetector iskeydown @p 49
````  
Detects if target player is pressing the '1' key
#### Command Block Behavior
- In repeating command blocks:  
  - 🔴 Key pressed: Returns true and activates redstone signal  
  - ⚪ Key released: Returns false and deactivates redstone signal  
## 2. Key Combination Detection: /keyboarddetector matchgroup
### Functionality  

Detects simultaneous pressing of multiple keys (independent key processing)

### Command Syntax  
````command
/keyboarddetector matchgroup <PlayerID> <KeyAsciiList> <keepStatic>
````
### Parameters  

|Parameter |	Description |	Example Values |
|------------|----------------|-----------------|
| `<PlayerID>` |	Target player selector |	@p, @a|
| `<KeyAscii>` |	Comma-separated ASCII codes |	49,50,51 (1,2,3)|  
| `<keepStatic>` |	Persistent state mode (true/false) |	true, false|  


### Usage Examples
#### Standard mode:

```command
/keyboarddetector matchgroup @p 49,50,51 false
````
Detects simultaneous press of 1, 2, 3 keys

#### Persistent mode:

````command
/keyboarddetector matchgroup @p 67,86,88 true
````
Detects Ctrl+C+V combination and maintains state

### Command Block Behavior
|Mode |	Behavior |
|------------|--------------------------|
| Standard Mode `(keepStatic=false)` | • Activates signal when all keys are pressed simultaneously <br>• Deactivates immediately when any key is released |
| Persistent Mode `(keepStatic=true)` |	• Locks signal on first successful detection.<br>• Maintains activation after keys are released.<br>• Requires manual flush to reset | 

## 3. State Reset Command: /keyboarddetector flush
### Functionality
Clears persistent key states created by `matchgroup` with `keepStatic=true`

### Usage Scenario
1.  Persistent mode activated:  

````command
/keyboarddetector matchgroup @p 49,50,51 true
````  
2.  After keys are pressed, signal stays active  

3.  Reset detection state:  

````command
/keyboarddetector flush
````

#### Effects
🔻 Immediately terminates all active redstone signals  

♻️ Resets all detection states  

🚦 Requires new key combination press to reactivate  
