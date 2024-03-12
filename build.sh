#!/bin/bash
#
#  Copyright 2024 CUBRID Corporation
#

# Build and package script for TURBOGRAPH JDBC Driver
# Requirements 
# - Bash shell
# - JDK 1.6 or higher
# - Build tool - ANT

arg=$@
shell_dir="$( cd "$( dirname "$0" )" && pwd -P )"
output_dir=$shell_dir/output
ant_file=$(which ant)
java_file=$(which java)

function show_usage ()
{
  echo "Usage: $0 [OPTIONS]"
  echo "Build script for TURBOGRAPH JDBC Driver"
  echo " OPTIONS"
  echo "  clean                Clean (jar and build files)"
  echo "  --help | -h | -?     Display this help message and exit"
  echo ""
  echo " EXAMPLES"
  echo "  $0                   # Build JDBC"
  echo "  $0 clean             # Clean (jar and build files)"
  echo ""
}

function check_env ()
{
  # check Git
  echo "Checking for Git"
  which_git=$(which git)
  [ $? -eq 0 ] && echo "[INFO] Git File [$which_git"] || echo "[ERROR] Git not found"

  echo "Checking Environment Variables(JAVA) and build tools(ANT)"

  if [ "x$JAVA_HOME" != "x" -a "x$JAVA_HOME" != "x" ]; then
    echo "JAVA_HOME [$JAVA_HOME]"
    export PATH="$JAVA_HOME/bin:$PATH"
  elif [ "x$java_file" != "x" ]; then
    echo "[INFO] Used Java File ($java_file)"
  else
    echo "[ERROR] Please check JAVA_HOME Or JAVA PATH" 
    exit 0
  fi

  if [ "x$ant_file" = "x" ]; then
    echo "[ERROR] Pleasse check ANT PATH (Build Tools)"
    exit 0
  fi
}

if [ -z $arg ]; then
  check_env
elif [ $arg = "clean" ]; then
  check_env
  echo "[INFO] Clean Build"

  if [ -d $output_dir ]; then
    rm -rf $output_dir
  fi
  rm -fv $shell_dir/*.jar
  exit 0
elif [ $arg = "--help" -o  $arg = "-h" -o $arg = "-?" ]; then
  show_usage
  exit 0
else 
  echo "[ERROR] Unknown Option [$arg]"
  show_usage
  exit 0
fi

if [ ! -d $output_dir ]; then
  mkdir -p $output_dir
fi

cd $output_dir > /dev/null
cmake .. && cmake --build . --target jdbc_build
cd - > /dev/null
