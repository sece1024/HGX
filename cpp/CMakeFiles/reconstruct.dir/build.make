# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.21

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/local/bin/cmake

# The command to remove a file.
RM = /usr/local/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/sece/Hgx/Gits/HGX/cpp

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/sece/Hgx/Gits/HGX/cpp

# Include any dependencies generated for this target.
include CMakeFiles/reconstruct.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/reconstruct.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/reconstruct.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/reconstruct.dir/flags.make

CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.o: CMakeFiles/reconstruct.dir/flags.make
CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.o: tree/binarytree/reconstruct.cpp
CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.o: CMakeFiles/reconstruct.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/home/sece/Hgx/Gits/HGX/cpp/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.o"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.o -MF CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.o.d -o CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.o -c /home/sece/Hgx/Gits/HGX/cpp/tree/binarytree/reconstruct.cpp

CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /home/sece/Hgx/Gits/HGX/cpp/tree/binarytree/reconstruct.cpp > CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.i

CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /home/sece/Hgx/Gits/HGX/cpp/tree/binarytree/reconstruct.cpp -o CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.s

# Object files for target reconstruct
reconstruct_OBJECTS = \
"CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.o"

# External object files for target reconstruct
reconstruct_EXTERNAL_OBJECTS =

reconstruct: CMakeFiles/reconstruct.dir/tree/binarytree/reconstruct.cpp.o
reconstruct: CMakeFiles/reconstruct.dir/build.make
reconstruct: CMakeFiles/reconstruct.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/home/sece/Hgx/Gits/HGX/cpp/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable reconstruct"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/reconstruct.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/reconstruct.dir/build: reconstruct
.PHONY : CMakeFiles/reconstruct.dir/build

CMakeFiles/reconstruct.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/reconstruct.dir/cmake_clean.cmake
.PHONY : CMakeFiles/reconstruct.dir/clean

CMakeFiles/reconstruct.dir/depend:
	cd /home/sece/Hgx/Gits/HGX/cpp && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/sece/Hgx/Gits/HGX/cpp /home/sece/Hgx/Gits/HGX/cpp /home/sece/Hgx/Gits/HGX/cpp /home/sece/Hgx/Gits/HGX/cpp /home/sece/Hgx/Gits/HGX/cpp/CMakeFiles/reconstruct.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : CMakeFiles/reconstruct.dir/depend

