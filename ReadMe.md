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

### ASCII Key Reference Table

| Key          | ASCII Code | Key         | ASCII Code |
|--------------|------------|-------------|------------|
| **Numbers**  |            | **Letters** |            |
| 0            | 48         | A           | 65         |
| 1            | 49         | B           | 66         |
| 2            | 50         | C           | 67         |
| 3            | 51         | D           | 68         |
| 4            | 52         | E           | 69         |
| 5            | 53         | F           | 70         |
| 6            | 54         | G           | 71         |
| 7            | 55         | H           | 72         |
| 8            | 56         | I           | 73         |
| 9            | 57         | J           | 74         |
|              |            | K           | 75         |
|              |            | L           | 76         |
|              |            | M           | 77         |
|              |            | N           | 78         |
|              |            | O           | 79         |
|              |            | P           | 80         |
|              |            | Q           | 81         |
|              |            | R           | 82         |
|              |            | S           | 83         |
|              |            | T           | 84         |
|              |            | U           | 85         |
|              |            | V           | 86         |
|              |            | W           | 87         |
|              |            | X           | 88         |
|              |            | Y           | 89         |
|              |            | Z           | 90         |
|              |            |             |            |
| **Functions**|            | **Special** |            |
| F1           | 112        | Space       | 32         |
| F2           | 113        | Enter       | 13         |
| F3           | 114        | Esc         | 27         |
| F4           | 115        | Tab         | 9          |
| F5           | 116        | Caps Lock   | 20         |
| F6           | 117        | Shift       | 16         |
| F7           | 118        | Ctrl        | 17         |
| F8           | 119        | Alt         | 18         |
| F9           | 120        | Backspace   | 8          |
| F10          | 121        | Delete      | 46         |
| F11          | 122        | Insert      | 45         |
| F12          | 123        | Home        | 36         |
|              |            | End         | 35         |
|              |            | Page Up     | 33         |
|              |            | Page Down   | 34         |
|              |            | Arrow Up    | 38         |
|              |            | Arrow Down  | 40         |
|              |            | Arrow Left  | 37         |
|              |            | Arrow Right | 39         |

**Notes**:  
- Lowercase letters: a=97, b=98 ... z=122
- Numeric keypad: 0=96, 1=97 ... 9=105
- Case sensitive (A=65 vs a=97)
- Full reference: [ASCII Table](https://www.asciitable.com)
