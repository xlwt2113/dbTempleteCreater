echo off
set CLASSPATH="%CLASSPATH%;..\conf"
set TMP_PATH = ""
setlocal enabledelayedexpansion
for /R ..\lib %%i in (*.jar) do set "TMP_PATH=!TMP_PATH!;%%i"
set CLASSPATH=%CLASSPATH%;%TMP_PATH%
start javaw -classpath %CLASSPATH% com.ssj.jdbcfront.frame.Frame ../
