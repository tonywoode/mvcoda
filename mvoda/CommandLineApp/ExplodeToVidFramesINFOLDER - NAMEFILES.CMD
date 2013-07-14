setlocal
set FFMPEG="C:\Users\Tony\CODE\Repo\mvoda\MVODA\FFmpeg\ffmpeg-20130418-git-ee94362-win64-static\bin\ffmpeg.exe"

set AVILOC=%*

set OUTPUTLOC=C:\Users\Tony\CODE\MVODAOutputs

set NAME=QRedBlock


mkdir %OUTPUTLOC%\%NAME%\
%FFMPEG% -i %AVILOC% %OUTPUTLOC%\%NAME%\%NAME%%%d.png -v verbose

pause

