@echo off

REM
REM Copyright 2024 CUBRID Corporation
REM

rem build script for MS Windows.
rem
rem Requirements
rem - jdk 1.6 or higher
rem - build tools (ant)

set SHELL_PATH=%~dp0
set OUTPUT_PATH=%SHELL_PATH%\output
set GIT_FILE=C:\Program Files\Git\bin\git.exe
set JAVA_FILE=%JAVA_HOME%\bin\java.exe
set FIND_ANT1=%ANT_HOME%\bin\ant
set FIND_ANT2=%ANT%\bin\ant
set CMAKE_PATH=C:\Program Files\CMake\bin\cmake.exe

if "%*" == "clean" GOTO :CHECK_ENV
if "%*" == "" GOTO :CHECK_ENV
if "%*" == "/help" GOTO :SHOW_USAGE
if "%*" == "/h" GOTO :SHOW_USAGE
if "%*" == "/?" GOTO :SHOW_USAGE
echo "[INFO] Unknown Option [%*]"
GOTO :SHOW_USAGE

:CHECK_ENV
echo Checking for requirements...
call :FINDEXEC git.exe GIT_FILE "%GIT_FILE%"
call :FINDEXEC cmake.exe CMAKE_PATH "%CMAKE_PATH%"
call :FINDEXEC java.exe JAVA_FILE "%JAVA_FILE%"
if "%JAVA_HOME%" == "" (
  echo "[ERROR] set environment variable is required. (JAVA_HOME)"
  GOTO :EOF 
)

call :FINDEXEC ant ANT_PATH "%ANT_PATH%"
if "%ANT_PATH%" == "" (
  call :FINDEXEC ant FIND_ANT1 "%FIND_ANT1%"
  if "%FIND_ANT1%" == "\bin\ant" (
    call :FINDEXEC ant FIND_ANT2 "%FIND_ANT2%"
    if "%FIND_ANT2%" == "\bin\ant" (
      echo "set environment variable is required. (ANT_HOME or ANT)"
      GOTO :EOF 
    ) else (
      set ANT_PATH=%FIND_ANT2%
		)
	) else (
		set ANT_PATH=%FIND_ANT1%
	)
)
GOTO :BUILD

:BUILD
if "%*" == "clean" (
  rmdir /Q /S %OUTPUT_PATH%
  DEL /Q /F ".\*.jar"
) else (
  if NOT EXIST "%OUTPUT_PATH%" (
    mkdir %OUTPUT_PATH%
  )
  
  SET CURRENT_PATH=%CD%
  
  cd %OUTPUT_PATH% >NUL

  "%CMAKE_PATH%" ..
  if ERRORLEVEL 1 (echo FAILED & GOTO :EOF) ELSE echo CMake CONFIGURE SUCCESSFUL

  "%CMAKE_PATH%" --build . --target jdbc_build
  if ERRORLEVEL 1 (echo FAILED & GOTO :EOF) ELSE echo CMake BUILD SUCCESSFUL
  
  cd %CURRENT_PATH% >NUL
)
GOTO :EOF

:FINDEXEC
if EXIST %3 set %2=%~3
if NOT EXIST %3 for %%X in (%1) do set FOUNDINPATH=%%~$PATH:X
if defined FOUNDINPATH set %2=%FOUNDINPATH:"=%
if NOT defined FOUNDINPATH if NOT EXIST %3 echo Executable [%1] is not found & GOTO :EOF
call echo Executable [%1] is found at [%%%2%%]
set FOUNDINPATH=
GOTO :EOF

:SHOW_USAGE
@echo.Usage: %0 [OPTION]
@echo.Build script for TURBOGRAPH JDBC Driver
@echo. OPTIONS
@echo.  clean          Clean (jar and build file)
@echo.  /help /h /?    Display this help message and exit
@echo.
@echo. Examples:
@echo.  %0             # Build JDBC
@echo.  %0 clean       # Clean (jar and build file)
GOTO :EOF
