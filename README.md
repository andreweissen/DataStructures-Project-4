### Data Structures and Analysis - Project 4 ###

#### About ####

This programming project involves writing a program that mimics the Java command line compiler. It builds a directed graph from a list of class names inputted from a text file, and using a topological sort, lists the classes in the order in which they must be recompiled based upon their relationships with one another.

Note: There is an outstanding issue regarding false positives being thrown for nonexistent cycles in the directed graph. The author has plans to patch this.