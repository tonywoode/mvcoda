setlocal
set FFMPEG="C:\Users\Tony\CODE\Repo\mvoda\MVODA\FFmpeg\ffmpeg-20130418-git-ee94362-win64-static\bin\ffmpeg.exe"

set AVILOC="C:\Users\Tony\Inputs\4kAvi\\"

set LOGOLOC="C:\Users\Tony\Inputs\Elements\LOGOS\\"

set OUTPUTLOC="C:\Users\Tony\Outputs\\"

::The files
set FILE1=%AVILOC%FalloutBoySugar.avi
set LOGO=%LOGOLOC%4music.png


%FFMPEG% -i %FILE1% -i %LOGO% -filter_complex 'overlay' -b:v 4000k %OUTPUTLOC%GFXOutput.avi -v verbose

pause

