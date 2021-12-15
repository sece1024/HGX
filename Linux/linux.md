# Linux 基础
## base
`who am i`
`w`
`ll`
`clear`
`history`
`pwd`
- shut down
```
poweroff
halt
shutdown -h now
systemctl poweroff
```
```
date
cal
```
- show system language
`echo ${LANG}`
`locale`
- caculator language
`bc`
- Display Filesystem
`df`

## file
- create numerous files
*use `ll -d test?` to show all testfile.*
```
touch test
touch test{1,2,3,4}
touch test{1..10}
touch test{01..10}
```
## vim
- copy
yy, 5yy, nyy: copy one, five or n line.
p:	plast.
u: undo.

## account manage
`useradd newuser`
`passwd newuser`
`id newuser`
`userdel -r newuser`

## process
- show process
```
ps
pstree
top
```
`ps -l`:	show process about now bash
`ps aux`:	show more information
`pstree -A`:	show as ASCII
`pstree -Aup`:	add PID and user information
`top`:		dynamic show process information

## job control
ctrl + z:	turn job that runing in foreground to background and halt it
jobs [-l]:	list all jobs in foreground
fg %n:		turn the n job to foreground
bg %n:		turn the n job to background

