#
#
#  Copyright 2024 CUBRID Corporation
# 
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
# 
#       http://www.apache.org/licenses/LICENSE-2.0
# 
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
# 
#
find_package(Git)

if(EXISTS ${TURBOGRAPH_JDBC_OUTPUT_DIR}/VERSION-DIST AND NOT EXISTS ${TURBOGRAPH_JDBC_SOURCE_DIR}/VERSION)

    # set TURBOGRAPH_JDBC_RELEASE_VERSION from existing VERSION-DIST
    file(STRINGS ${TURBOGRAPH_JDBC_OUTPUT_DIR}/VERSION-DIST TURBOGRAPH_JDBC_RELEASE_VERSION)

else(EXISTS ${TURBOGRAPH_JDBC_OUTPUT_DIR}/VERSION-DIST AND NOT EXISTS ${TURBOGRAPH_JDBC_SOURCE_DIR}/VERSION)

    # generate VERSION-DIST file
    file(STRINGS ${TURBOGRAPH_JDBC_SOURCE_DIR}/VERSION TURBOGRAPH_JDBC_VERSION)

    if(NOT TURBOGRAPH_JDBC_VERSION)
        message(FATAL_ERROR "Could not find VERSION file")
    endif(NOT TURBOGRAPH_JDBC_VERSION)

    # TODO: it should be treated as input argument in the future
    set(TURBOGRAPH_JDBC_START_SERIAL_DATE "2021-03-30")

    if (UNIX)
    execute_process(
        COMMAND ${GIT_EXECUTABLE} rev-list --count --after ${TURBOGRAPH_JDBC_START_SERIAL_DATE} HEAD
        COMMAND awk "{ printf \"%04d\", $1 }"
        OUTPUT_VARIABLE EXTRA_VERSION RESULT_VARIABLE git_result
        ERROR_QUIET OUTPUT_STRIP_TRAILING_WHITESPACE
        WORKING_DIRECTORY ${TURBOGRAPH_JDBC_SOURCE_DIR})
    else (UNIX)
    execute_process(
        COMMAND ${GIT_EXECUTABLE} rev-list --count --after ${TURBOGRAPH_JDBC_START_SERIAL_DATE} HEAD
        OUTPUT_VARIABLE EXTRA_VERSION RESULT_VARIABLE git_result
        ERROR_QUIET OUTPUT_STRIP_TRAILING_WHITESPACE
        WORKING_DIRECTORY ${TURBOGRAPH_JDBC_SOURCE_DIR})

    # getting around awk is not supported on windows
    string(CONCAT EXTRA_VERSION "0000" ${EXTRA_VERSION})
    string(REGEX MATCHALL "([0-9][0-9][0-9][0-9]$)" VERSION_MATCHES ${EXTRA_VERSION})
    list(GET VERSION_MATCHES 0 EXTRA_VERSION)
    endif (UNIX)

    if ("${EXTRA_VERSION}" STREQUAL "")
       message(WARNING "Could not get count information from Git. So EXTRA_VERSION is 0000")
       set(EXTRA_VERSION "0000")
    endif ("${EXTRA_VERSION}" STREQUAL "")

    set(TURBOGRAPH_JDBC_RELEASE_VERSION ${TURBOGRAPH_JDBC_VERSION}.${EXTRA_VERSION})

    # write VERSION-DIST and TURBOGRAPH-JDBC-11.2.xxxx (for example) files
    file(WRITE ${TURBOGRAPH_JDBC_OUTPUT_DIR}/VERSION-DIST "${TURBOGRAPH_JDBC_RELEASE_VERSION}")
    
endif(EXISTS ${TURBOGRAPH_JDBC_OUTPUT_DIR}/VERSION-DIST AND NOT EXISTS ${TURBOGRAPH_JDBC_SOURCE_DIR}/VERSION)

if(CMAKE_VERSION VERSION_GREATER "3.12")
    file(TOUCH ${TURBOGRAPH_JDBC_OUTPUT_DIR}/TURBOGRAPH-JDBC-${TURBOGRAPH_JDBC_RELEASE_VERSION})
else(CMAKE_VERSION VERSION_GREATER "3.12")
    file(WRITE ${TURBOGRAPH_JDBC_OUTPUT_DIR}/TURBOGRAPH-JDBC-${TURBOGRAPH_JDBC_RELEASE_VERSION} "")
endif(CMAKE_VERSION VERSION_GREATER "3.12")

configure_file (${TURBOGRAPH_JDBC_SOURCE_DIR}/cmake/build.properties.in ${TURBOGRAPH_JDBC_OUTPUT_DIR}/build.properties)

message ("[INFO] JDBC VERSION = ${TURBOGRAPH_JDBC_RELEASE_VERSION}")
