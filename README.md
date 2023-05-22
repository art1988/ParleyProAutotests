# qa

<b>Requirements:</b>
1. Java version 8;
2. Maven - latest version;
3. Clone repository by command: ```git clone https://github.com/amork/qa.git```
4. Go to ParleyPro Autotests directory
5. Use this command to run Basics suite: ```mvn clean test -Dsurefire.suiteXmlFiles=./src/test/java/suites/Basics.xml```
6. To generate Allure reports, run: ```mvn allure:serve```

<b>Note:</b>  
Look for "ParleyPro Autotests" folder.  
"parley-tests" was written by different person.  
Refer to ParleyPro Autotests/ParleyAutotestsDemo.mp4 for video demonstration of running tests.