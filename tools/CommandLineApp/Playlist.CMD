setlocal
set FFMPEG="..\FFmpeg\ffmpeg-20130418-git-ee94362-win64-static\bin\ffmpeg.exe"

set AVILOC="C:\Users\Tony\CODE\MVCODAInputs\Love\\"

set OUTPUTLOC="C:\Users\Tony\CODE\MVCODAOutputs\\"

::The files
set FILE1=%AVILOC%"BrunoMarsJustTheWay.avi"
set FILE2=%AVILOC%"FlorenceAndTheMachineLoverToLover.avi"
set FILE3=%AVILOC%"GloriaEstefanAnythingForYou.avi"
set FILE4=%AVILOC%"KateBushWutheringHeights.avi"
set FILE5=%AVILOC%"LeonaLewisHappy.avi"
set FILE6=%AVILOC%"Maroon5SheWillBeLoved.avi"
set FILE7=%AVILOC%"NeyoStay.avi"
set FILE8=%AVILOC%"NickiMinajYourLove.avi"
set FILE9=%AVILOC%"PinkOneLastKiss.avi"
set FILE10=%AVILOC%"RihannaYouDaOne.avi"




::This works!
::%FFMPEG% -f concat -i mylist.txt -c copy output.avi



::This apparently is the less preferred method according to ffmpeg.org/trac/ffmpeg/wiki/How to concatenate (join, merge) media files

%FFMPEG% -i "concat:%FILE1%|%FILE2%|%FILE3%|%FILE4%|%FILE5%|%FILE6%|%FILE7%|%FILE8%|%FILE9%|%FILE10%" -c copy %OUTPUTLOC%playlist.avi -v verbose

pause

