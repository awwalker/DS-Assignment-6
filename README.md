# DS-Assignment-6

In this project you will revist your project 1 implementation of a program that used the NYPD motor vehicle collision data. A version of the solution to the original project is provided (it has been slightly modified from the specification of the original project). You should use that implementation as the basis for your project 6 solution.
Please, see project 1 for the details of tasks and implementation requirements.

## Objectives

The goal of this programming project is for you to master (or at least get practice on) the following tasks:
* using/modifying existing
* selecting data structures appropriate for given tasks
* writing Java programs

## The Program Input and Output

You should not change this part of the implementation.
There are three data sets inside this repository that you should be using for this project. The largest of them contains all collision data for the entire NYC from July 1, 2012 till June 30, 2015 (three years total). The medium size data set contains all collision data for the entire NYC from July 1, 2013 till June 30, 2015 (two years total). Finally, the smallest data set contains all collision data for the entire NYC from July 1, 2014 till June 30, 2015 (one year total).
For testing purposes, you may wish to use the smaller data sets.

## Computational Task

The posted program stores all the Collision objects in an ArrayList of ZipCodeList objects. The ZipCodeList object, in turn, stores all Collision objects from a particular zip code in an ArrayList of collisions.
At this point in the semester, you should realize that this is not an optimal way of organizing the data. Your task for this project is to modify how the Collision objects are stored in memory when the program is running in order to improve the time performance of the program. You do not need to provide implementation of any data structures yourself. You should use the implementations provided by the Java API. You should only use the data structures that we discussed in class during this semester. Your task is to decide which of them could improve the time performance of the program and use those.
You may wish to try several different changes. In your final program you should leave only the changes that resulted in a better time performance of the program. All other changes should be described in the written report.

### Program Design

You should only change parts of the design of this program that are related to change in data structures. Overall program design should remain the same.
