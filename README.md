# qa

Requirements:
1. Java version 8;
2. Maven - latest version;
3. Clone repository by command: git clone https://github.com/amork/qa.git
4. Go to ParleyPro Autotests directory
5. Use this command to run tests: mvn clean test -Dsurefire.suiteXmlFiles=./src/test/java/suites/Basics.xml
6. To generate Allure reports, run: mvn allure:serve
