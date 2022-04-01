#teamidea_krasov
[![Build Status](https://app.travis-ci.com/Krasobas/teamidea_krasov.svg?branch=master)](https://app.travis-ci.com/Krasobas/teamidea_krasov)
[![codecov](https://codecov.io/gh/Krasobas/teamidea_krasov/branch/master/graph/badge.svg?token=Z5PCTG6WW9)](https://codecov.io/gh/Krasobas/teamidea_krasov)

This project represents a solution for TeamIDEA company test tasks.
#### Task:
Develop a program that, based on the service data [openweathermap.org](https://openweathermap.org/) (requires registration, Free plan is enough) will display the following data for your city:
1. Day, with the minimum difference between the "feels like" and actual temperature at night (indicating the difference in degrees Celsius)
2. The maximum duration of daylight hours (calculated as the difference between the time of sunset and sunrise) for the next 5 days (including the current one), indicating the date.
   
#### Solution:
To build and start the solution program `cd` the project directory and use command below:

    mvn install; java -jar ./target/weather.jar
![img](img/img.png)

#### Settings:
All project settings are stored in a file `app.properties` 