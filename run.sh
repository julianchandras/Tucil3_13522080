# Change directory to 'bin'
cd bin || exit

# Delete all files in 'bin'
rm -f ./*

# Delete all directories in 'bin'
find . -mindepth 1 -type d -exec rm -rf {} +

# Navigate to the 'src/wordladder' directory
cd ../src/wordladder || exit

# Compile all Java files and place the class files in the 'bin' directory
javac -d ../../bin *.java

# Navigate to the 'bin' directory
cd ../../bin || exit

# Execute the Main class from the 'wordladder' package
java -cp . wordladder.Main