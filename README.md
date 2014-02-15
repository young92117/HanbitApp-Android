# HanbitApp-Android

## How to set up a development environment

### Required Software & Configuration

1. [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
2. [Android SDK](http://developer.android.com/sdk/index.html?utm_source=weibolife)
3. [Maven](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) - Maven is a package manager for Java projects.
4. [IntelliJ Community Edition](http://www.jetbrains.com/idea/download/) - IntelliJ is an IDE for Java development.


### Import the project

1. Open IntelliJ
2. On the "Welcome" screen, click "Check out from Version Control" and choose "GitHub".
3. When prompted, enter your GitHub credentials.
4. When prompted, enter this project's github address 
```
https://github.com/SanDiegoHanbitChurch/HanbitApp-Android.git
```
5. For steps after opening the project, see [this presentation](https://docs.google.com/presentation/d/1CDrVr3W5B-jeBLmYCM-50rz_3adjcKSLj-aEXUQPvP4/pub?start=false&loop=false&delayms=3000).
6. In addition to the above steps to import and configure the project, there are two additional steps
  1. Need to add two java libraries (.jar) - [android-rome-feed-reader-1.0.0.jar] (https://code.google.com/p/android-rome-feed-reader/) and jdom-1.1.1-android-fork.jar as the dependency. They are both located under "lib" folder.
  2. Add "DatabaseConfigUtil" Configuration as an Application. This generates [database mapping file](http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_4.html#Config-Optimization) for ORMLite.
