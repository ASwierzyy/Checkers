cmake_minimum_required(VERSION 3.29)
project(ProjectJNI1)

set(CMAKE_CXX_STANDARD 20)
set(JAVA_HOME "/Library/Java/JavaVirtualMachines/jdk-23.jdk/Contents/Home")
include_directories(${JAVA_HOME}/include)
include_directories(${JAVA_HOME}/include/darwin)


add_library(ProjectJNI1 SHARED GameJNI.cpp
        GameLogic.cpp
        GameLogic.h)
