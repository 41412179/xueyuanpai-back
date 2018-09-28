@echo off

%~d0
cd %~dp0
title %cd%

set MAVEN_OPTS=%MAVEN_OPTS% -Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m
call D:\software\apache-maven-3.5.4-bin\apache-maven-3.5.4\bin\mvn clean spring-boot:run -Dmaven.test.skip=true -Dspring.profiles.active=xueshiju-dev -U
pause