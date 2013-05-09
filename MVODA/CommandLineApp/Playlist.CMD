setlocal
set FFMPEG="C:\Users\Tony\CODE\Repo\mvoda\MVODA\FFmpeg\ffmpeg-20130418-git-ee94362-win64-static\bin\ffmpeg.exe"

set AVILOC="C:\Users\Tony\Inputs\4kAvi\\"

set OUTPUTLOC="C:\Users\Tony\Outputs\\"

::The files
set FILE1=%AVILOC%"FalloutBoySugar.avi"
set FILE2=%AVILOC%"ColdplayClocks.avi"
set FILE3=%AVILOC%"ColdplayFixYou.avi"
set FILE4=%AVILOC%"JustJackStars.avi"
set FILE5=%AVILOC%"KasabianFire.avi"
set FILE6=%AVILOC%"LadyGagaPaparazzi.avi"
set FILE7=%AVILOC%"LouiseNaked.avi"
set FILE8=%AVILOC%"MelBFeelsSoGood.avi"
set FILE9=%AVILOC%"NickiMinajStarships.avi"
set FILE10=%AVILOC%"PinkSoWhat.avi"




::This works!
::%FFMPEG% -f concat -i mylist.txt -c copy output.avi



::This apparently is the less preferred method according to ffmpeg.org/trac/ffmpeg/wiki/How to concatenate (join, merge) media files

%FFMPEG% -i "concat:%FILE1%|%FILE2%|%FILE3%|%FILE4%|%FILE5%|%FILE6%|%FILE7%|%FILE8%|%FILE9%|%FILE10%" -c copy %OUTPUTLOC%playlist.avi -v verbose

pause

