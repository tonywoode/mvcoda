setlocal
set FFMPEG="..\FFmpeg\ffmpeg-20130418-git-ee94362-win64-static\bin\ffmpeg.exe"

set AVILOC=%*

set OUTPUTLOC="C:\Users\Tony\CODE\MVCODAOutputs\\"


%FFMPEG% -i %AVILOC% -v verbose

pause

