cmake_minimum_required(VERSION 3.10.2)
project("opencode_engine")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Find Java JNI headers
include_directories(
    ${JAVA_INCLUDE_PATH}
    ${JAVA_INCLUDE_PATH_ANDROID}
)

# llama.cpp source (from submodule or downloaded)
set(LAMACPP_DIR ${CMAKE_SOURCE_DIR}/external/llama.cpp)

# llama.cpp headers and sources
set(LAMACPP_HEADERS
    ${LAMACPP_DIR}/include/llama.h
)

set(LAMACPP_SOURCES
    ${LAMACPP_DIR}/src/llama.cpp
    ${LAMACPP_DIR}/src/llama-vocab.cpp
    ${LAMACPP_DIR}/src/llama-impl.cpp
    ${LAMACPP_DIR}/src/llama-hparams.cpp
    ${LAMACPP_DIR}/src/llama-cmd.cpp
    ${LAMACPP_DIR}/src/llama-sampling.cpp
    ${LAMACPP_DIR}/src/llama-mmtk.cpp
    ${LAMACPP_DIR}/src/llama-adapter.cpp
)

# OpenCode JNI bridge source
set(JNI_HEADERS
    ${CMAKE_SOURCE_DIR}/jni/opencode_engine.h
)

set(JNI_SOURCES
    ${CMAKE_SOURCE_DIR}/jni/opencode_engine.cpp
)

# Build the native library
add_library(
    opencode_engine
    SHARED
    ${LAMACPP_HEADERS}
    ${LAMACPP_SOURCES}
    ${JNI_HEADERS}
    ${JNI_SOURCES}
)

# Link libraries
target_link_labs(
    opencode_engine
    log
    jnigraphics
    android
)

# Optimization flags
target_compile_options(opencode_engine PRIVATE
    -O3
    -march=armv8-a
    -mtune=cortex-a76
    -mfpu=neon
    -mfloat-abi=softfp
)

# GPU acceleration (Vulkan) - optional
option(ENABLE_VULKAN "Enable Vulkan GPU acceleration" OFF)
if(ENABLE_VULKAN)
    find_package(Vulkan REQUIRED)
    target_link_libraries(opencode_engine Vulkan::Vulkan)
    target_compile_definitions(opencode_engine PRIVATE LLAMA_VULKAN=1)
endif()

# Multi-threading
target_compile_definitions(opencode_engine PRIVATE
    LLAMA_NATIVE=1
    LLAMA_THREADS=1
)
