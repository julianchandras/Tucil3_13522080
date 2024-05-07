@echo off
cd bin
del /q *.*
for /d %%i in (*) do rmdir /s /q "%%i"
cd ..\src\wordladder
javac -d ..\..\bin *.java
cd ..\..\bin
java -cp . wordladder.Main