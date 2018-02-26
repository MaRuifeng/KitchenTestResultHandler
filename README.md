# Kitchen Test Result Hanlder

Description
---------------

This kitchen test result handler collects Chef recipe test results, publishes them to the test result database and generates an HTML report. It communicates with the [RTC Web Proxy](https://github.com/MaRuifeng/RTCWeb) to store the test results and manage related RTC work items automatically. 

* Chef recipe kitchen test results are stored in JSON format.

* The JSON results are parsed by a JSON reader into an ArrayList.

* The result list is used to file API calls to the customized RTC web proxy to create work items, and store in database. 

* Returned information is processed by an XML writer to write a summary XML file.

* The XML file is processed by an XSLT transformer to generate final HTML report.

* An FTP uploader is used to eventually upload the report and raw results to an HTTP server.

Owners
------
Author: Ruifeng Ma

Organization: IBM

Requirements
------------
* IBM JDK/JRE 1.7.0 or above
* A valid Sendgrid account
* Network connection to a Websphere Application Server where the RTC Web Proxy is installed

Usage
-----
Build up a jar application and run it against below arguments

Raw results directory 

	Windows: C:/kitchen_test/json

    Linux: {USER_HOME}/kitchen_test/json

Raw logss directory 

	Windows: C:/kitchen_test/log

    Linux: {USER_HOME}/kitchen_test/log

Report directory 

	Windows: C:/kitchen_report

    Linux: {USER_HOME}/kitchen_report

Build name

Endpoint server platform

For example (Windows): 

	java -jar KitchenReport.jar C:/kitchen_test/json C:/kitchen_test/log C:/kitchen_report ivt_11.4.20160719-1900.166 windows

Contributing
------------
Contact the owner before contributing.

1. Fork the repository on Github
2. Create a named feature branch (like `add_component_x`)
3. Write your change
4. Write tests for your change (if applicable)
5. Run the tests, ensuring they all pass
6. Submit a Pull Request using Github

License and Authors
-------------------
Authors: ruifengm@sg.ibm.com

