# NXLOG Test task

This project was created to demonstrate usage of rest assured for API testing

## Getting Started

To start project simply create new configuration in format {profile_name}.json and place it into test resources.
Then run 
````
gradle -DtestConfig={profile_name}
````

## Allure reports

To enable Allure reports first install allure command line on local machine using

````
gradle downloadAllure
````

Allure test results are automatically saved in
````
{project.folder}/allure-results
````

To access HTML Report from last test run use command
````
gradle allureServe
````

Which will automatically open browser with displayed results.

## Author

* **Martin Keprta** 

