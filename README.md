# KnowMyProgress

* Tracking the English words encountered and their frequency can give English learners a sense of accomplishment and increase motivation. 
KnowMyProgress can calculate the number of words read by users and the frequency of repetitions for each word.
* Users can input words by copying and pasting, or by extracting them from screenshots using the ScreenshotOperator.main function.
* All input words will be standardized (e.g. transfer -ing, -ed, -s, etc. into their original form). 
The program will automatically search for the words in the Oxford dictionary online. 
If the word is not found, the program will prompt the user to choose whether to save or discard it.


**: It is a Java Command Line tool intended for personal use, but with potential for wider adoption with modifications in the future. 

# Related Technologies
Database: Oracle Database XE

Data Persistence: JPA, Hibernate, SQL

Main Language: Java

It is also one of my Java exercising tool for me. Therefore, most of Java features are all used inside the program 
(e.g., Interfaces & Factory Patterns for different implementations; Stream for processing word list etc.)

# TODO
* The program is currently using my local Oracle database. For those without an IT background who are interested in using it, I plan to develop a version that uses Excel to store and visualize data.
* To make the program more accessible to others, I plan to add a graphical user interface (maybe using JavaFX or GWT).

