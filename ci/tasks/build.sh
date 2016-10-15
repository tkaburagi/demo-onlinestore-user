#!/bin/sh
cd demo-onlinestore-user
./mvnw clean package -DskipTests=true
mv target/*.jar ../build/demo-onlinestore-user.jar