# HaoVSort

> *Change languages to : English(currently) , [Thai](README.th.md)*
>
> Sorting Algorithms Visualizer Minecraft's plugin
>
> _By Mr. Sittipat Tepsutar_

## [Commands](src/main/java/com/hao/haovsort/commands/Sort.java)
### Sort Command
```
/sort <player> <algorithm> <delay> <length> args...
```
### Parameters
|Parameter|Infomation|ความจำเป็น|
|:-:|:-|:-:|
|\<player\>|Player who will see this visualization|Required|
|\<algorithm\>|Algorithm's name|Required|
|\<delay\>|Delay in algorithm|Required|
|\<length\>|Array size|Required|
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
เมื่อต้องการให้การแสดง sorting algorithm หยุด ให้ใช้คำสั่งนี้
```
/sortstop <player>
```
### Parameters
|Parameter|Infomation|ความจำเป็น|
|:-:|:-|:-:|
|\<player\>|ผู้เล่นที่จะถูกหยุดเห็นการแสดง|Opinion|

### Example
```
/sortstop
```
```
/sortstop karnhao
```

### Custom Sort Command
อยากลองเห็นอะไรใหม่ๆและแปลกไปจากนี้ใช่หรือไม่ ถ้าใช่ลองใช้คำสั่งนี้เลย!

```
/sortcustom <player> <delay> <length> <type> args... ; <type> args... ; ..........
```
โดยปกติแล้วเมื่อคุณใช้คำสั่ง [/sort](#sort-command) ธรรมดา จะมีการจัดเรียงอยู่ 3 แบบ คือ random, สิ่งที่ถูกใส่เข้ามาใน \<type\> และ finish

### Example
```
/sortcustom karnhao 10 100 random 1 ; insertion ; finish #00AA00
```
```
/sortcustom karnhao 10 100 invert ; shell; finish #FF0000 ; random 0.1
```

คำสั่ง `/sortcustom karnhao 10 100 random 1 ; insertion ; finish #00AA00` นั้นจะแสดงออกคล้ายกับคำสั่ง `/sort karnhao insertion 10 100`

<hr>


## วัตถุประสงค์ในการสร้าง
เนื่องจากผมชอบฟังเสียงตอน algorithm จัดเรียง เพราะมันทำให้ผมรู้สึกผ่อนคลาย และผมก็ได้รับแรงบรรดาลใจจากคลิปแนว Sorting algorithm หลายๆคลิปใน [Youtube](https://www.youtube.com/) อีกด้วย

<br>

## ประวัติความเป็นมา
โปรเจคนี้ถูกพัฒนาเสร็จสิ้นโดยผมตั้งแต่ตอนอายุ 17 ปี และถูกพัฒนาอีกครั้งในช่วงอายุ 18 ปี ซึ่งการพัฒนาโปรเจคนี้จะเป็นไปอย่างช้าๆ เนื่องจากมีการบ้านที่โรงเรียนที่ต้องทำเยอะมาก
