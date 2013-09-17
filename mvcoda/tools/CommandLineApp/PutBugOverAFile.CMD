setlocal
set FFMPEG="..\FFmpeg\ffmpeg-20130418-git-ee94362-win64-static\bin\ffmpeg.exe"


set AVILOC="C:\Users\Tony\CODE\MVODAInputs\Love\\"

set OUTPUTLOC="C:\Users\Tony\CODE\MVODAOutputs\\"

set LOGOLOC="C:\Users\Tony\CODE\Repo\mvoda\MVODA\Theme\Pop\Logo\\"


::The files
set FILE1=%AVILOC%BrunoMarsJustTheWay.avi
set LOGO=%LOGOLOC%4music.png


%FFMPEG% -i %FILE1% -i %LOGO% -filter_complex 'overlay' -b:v 4000k %OUTPUTLOC%GFXOutput.avi -v verbose

pause

