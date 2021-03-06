# HaoVSort

> *Change languages to : English(currently) , [Thai](README.th.md)*
>
> Sorting Algorithms Visualizer Minecraft's plugin
>
> _By Mr. Sittipat Tepsutar_

## [Commands](src/main/java/com/hao/haovsort/commands/Sort.java)

This plugin has TabCompleter in all commands.

### Sort Command
```
/sort <player> <algorithm> <delay> <length> args...
```
### Parameters
|Parameter|Infomation|Require|
|:-:|:-|:-:|
|\<player\>|Player who will see this visualization|Yes|
|\<algorithm\>|Algorithm's name|Yes|
|\<delay\>|Delay in algorithm|Yes|
|\<length\>|Array size|Yes|
|args...|Other parameter for some algorithm|Various

### Example
```
/sort karnhao quick 10 100
```
```
/sort karnhao radix 10 100 4
```

<hr>

### Sort Stop Command
To stop visualizing the sorting algorithm
use this command :
```
/sortstop <player>
```
### Parameters
|Parameter|Infomation|Require|
|:-:|:-|:-:|
|\<player\>|Target player. (command's sender is default)|Opinion|

### Example
```
/sortstop
```
```
/sortstop @s
```

### Custom Sort Command
Want to try something new and different from now on? If yes, try this command!

```
/sortcustom <player> <delay> <length> <type> args... ; <type> args... ; ..........
```
Usually when you use the command [/sort](#sort-command), there has three sorts: random, \<type\> and finish.

### Example
```
/sortcustom @s 10 100 random 1 ; insertion ; finish #00AA00
```
```
/sortcustom @s 10 100 invert ; shell; finish #FF0000 ; random 0.1
```

command `/sortcustom @s 10 100 random 1 ; insertion ; finish #00AA00` will display similar to the command `/sort @s insertion 10 100`

<hr>


## Playing music
There are music algorithm in this plugin. This algorithm also need [NoteblockAPI](https://www.spigotmc.org/resources/noteblockapi.19287/) to read [.nbs](https://opennbs.org/nbs) file.
And you need to put song file in plugins/HaoVSort/songs. (file's type must be [.nbs](https://opennbs.org/nbs)).

```
/sort <player> music <delay> <length> <song> <smooth>
```
### Parameters
|Parameter|Infomation|Require|
|:-:|:-|:-:|
|\<player\>|Player who will see this visualization|Yes|
|\<delay\>|Delay in algorithm (no effect)|Yes|
|\<length\>|Array size|Yes|
|\<song\>|Song's title name or Song's file name when song has no title|Yes|
|\<smooth\>|Determines whether play smooth or not (default is false).|Opinion

### Example
```
/sort @s music 10 100 martha_speak_theme
```

See also: [NoteBlockStudio](https://opennbs.org/)
