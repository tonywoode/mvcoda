setlocal
set FFMPEG="..\FFmpeg\ffmpeg-20130418-git-ee94362-win64-static\bin\ffmpeg.exe"

set AVILOC="C:\Users\Tony\CODE\MVCODAInputs\Love\\"

set OUTPUTLOC="C:\Users\Tony\CODE\MVCODAOutputs\\"

set LOGOLOC="C:\Users\Tony\CODE\Repo\mvcoda\MVCODA\Theme\Pop\Logo\\"

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

::The Logo
set LOGO=%LOGOLOC%4music.png


%FFMPEG% -i "concat:%FILE1%|%FILE2%|%FILE3%|%FILE4%|%FILE5%|%FILE6%|%FILE7%|%FILE8%|%FILE9%|%FILE10%" -i %LOGO% -filter_complex 'overlay' -b:v 4000k %OUTPUTLOC%GFXPlaylist.avi -v verbose

pause

