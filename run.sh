#!/bin/bash

# for from 1 to 100
for i in {1..100}
do
    # run the program with the input file ./tests/in/input$i.txt
    # and save the output to ./tests/user/user$i.txt
    # and compare the output with the expected output ./tests/out/output$i.txt
    java ./src/Main.java < ./tests/in/input$i.txt > ./tests/user/user$i.txt
    # diff and ignore spaces and new lines
    diff -w -B ./tests/user/user$i.txt ./tests/out/output$i.txt
    # if the output is different, print the error message
    if [ $? -ne 0 ]
    then
        echo "Test $i failed"
    else
        echo "Test $i passed"
    fi
done

