

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

