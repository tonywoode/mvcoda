setlocal
set FFMPEG="..\FFmpeg\ffmpeg-20130418-git-ee94362-win64-static\bin\ffmpeg.exe"

set AVILOC=%*

set OUTPUTLOC="C:\Users\Tony\CODE\MVCODAOutputs\\"


%FFMPEG% -i %AVILOC% -s 720x576 %OUTPUTLOC%OneSmallTWFM%%d.png -v verbose

pause

