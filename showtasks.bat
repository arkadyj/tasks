call runcrud.bat
if "%ERRORLEVEL%" == "0" goto runbrowser
echo.
echo Can't run runcrud.bat
goto fail

:runbrowser
start "%PROGRAMFILES(x86)%\opera\launcher.exe" http://localhost:8080/crud/v1/task/getTasks
if "%ERRORLEVEL%" == "0" goto end
echo Can't run browser
goto fail

:fail
echo.
echo Something goes wrong

:end
echo.
echo Job is finish
