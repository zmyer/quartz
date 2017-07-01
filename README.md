## Setup `mvnw` wrapper

The quartz-2.2.x branch requires `apache-maven-3.2.5` and JDK6 to compile, but the `mvnw` can only be setup 
using JDK7 or higher. So either you have to download `apache-maven-3.2.5` and JDK6 manually, or use JDK7 
or higher to setup `mvnw` like this:

	mvn -N io.takari:maven:wrapper -Dmaven=3.2.5

After this, then you can use `mvnw` wrapper.


## Builld status of 2.2.x branch: 
![2.2.x branch build status](https://travis-ci.org/quartz-scheduler/quartz.svg?branch=quartz-2.2.x "2.2.x branch build status")


## Build instructions:

### To compile:
```
  %> ./mvnw install -DskipTests
```

Note:  the final Quartz jar is found under quartz/target 

### To build Quartz distribution kit:
```
  %> cd distribution
  %> ./mvnw package
```

