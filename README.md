# gcd-service-application
## Problem statement
Develop an enterprise Java application that implements RESTful and SOAP web
services that is secure.
1. The RESTful service will expose two methods:
   1. public String push(int i1, int i2);
     which returns the status of the request to the caller as a String. The two
     parameters will be added to a JMS queue.
   1. public List&lt;Integer&gt; list();
     which returns a list of all the elements ever added to the queue from a
     database in the order added as a JSON structure.
1. The SOAP service will expose the following method as operations:
   1. public int gcd();
      which returns the greatest common divisor* of the two integers at the
      head of the queue. These two elements will subsequently be discarded
      from the queue and the head replaced by the next two in line.
   1. public List&lt;Integer&gt; gcdList();
      which returns a list of all the computed greatest common divisors from a
      database.
   1. public int gcdSum();
      which returns the sum of all computed greatest common divisors from a
      database.

1. The application needs to support access by up to 20 concurrent users and the code
be of production quality. It needs to be deployable to a JBoss (or similar) application
server as an enterprise archive [EAR] and be tolerant of server outage.
1. You are to document your assumptions and make your solution available on GitHub.
1. Greatest Common Divisor (GCD) of two whole numbers is the largest whole number
that&#39;s a divisor (factor) of both of them. For instance, the largest number that divides
into both 20 and 16 is 4.

## Solution
### Requirements
#### Dev/Build Requirements
1. Apache Maven 3.5.2
1. Spring tool suite

#### Runtime requirements
1. ActiveMQ messaging server
1. Tomcat App Server
1. Mysql

### How to build the project
1. Go to the project root of gcd-rest-app and run ‘mvn clean package’. This will produce gcdinput.war in <project-root>/target directory.
1. Go to the project root of gcd-soap-app and run ‘mvn clean package’. This will produce gcdoutput.war in <project-root>/target directory

### How to run the project
1. Mysql Schema creation.
1. Before starting up the application, mysql schema and tables are to be created as follows.
   1. mysql> CREATE DATABASE gcd_schema;
   1. mysql> CREATE TABLE `gcdtable` (
      `firstNumber` int unsigned NOT NULL,
      `secondNumber` int unsigned NOT NULL,
      `id` int NOT NULL AUTO_INCREMENT,
      `value` int NOT NULL DEFAULT '-1',
      PRIMARY KEY (`id`)
      ) ;

1. Start activemq server
1. This app has been built with windows in mind. The logs will be created in c:\logs directory. Create the logs directory in c:\.
1. Deploy gcdinput.war and gcdoutput.war on tomcat


### gcd-rest-app
The 'gcdinput.war' produced upon building this project provides the required rest interface to input and list the numbers. It will be hosted at /gcdinput. Basic authentication is enabled for this application. (username/password : admin/admin123)
1. push(int i1, int i2) (http://<hostname>:<port>/gcdinput/push?firstNumber=i1&secondNumber=i2)
1. list() (http://<hostname>:<port>/gcdinput/list)

### gcd-soap-app
The ‘gcdoutput.war’ provides the services to retrieve computed gcd value(s) through SOAP interfaces. It will be hosted at /gcdoutput.Basic authentication is enabled for this application. (username/password : admin/admin123). This app provides the following services      
1. int gcd() (http://<hostname>:<port>/gcdoutput/ws)
	 Computes the gcd of the first 2 elements in the queue and returns the same.
	 If there aren’t atleast 2 elements in queue, return ‘-1’ instead.
1. List<int> gcdList() (http://<hostname>:<port>/gcdoutput/ws)
	 Returns the list of all valid ‘gcd’ computed by the service so far.
1. int gcdSum() (http://<hostname>:<port>/gcdoutput/ws)
	 Returns the sum of all valid ‘gcd’ computed by the service so far. '0' otherwise


### Sample Input/Output

#### Gcdinput service
1. No credentials provided

   C:\>curl -X POST  "http://localhost:8080/gcdinput/push?firstNumber=12345&secondNumber=54321"
   
   **HTTP Status 401 : Full authentication is required to access this resource**

1. Invalid input - Negative integers

   C:\>curl -X POST  --user admin:admin123 "http://localhost:8080/gcdinput/push?firstNumber=12345&secondNumber=-54321"
   
   **Invalid Input**

1. Valid input 

   C:\>curl -X POST  --user admin:admin123 "http://localhost:8080/gcdinput/push?firstNumber=12345&secondNumber=54321"
   
   **Success**

   
1. Listing service

   C:\>curl --user admin:admin123 "http://localhost:8080/gcdinput/list"
   
   **[12345,54321,125,58,15,375]**
   

#### Gcdoutput service
1. No credential provided

   C:\Curl>curl --header "content-type:text/xml" -d @gcdrequest.xml http://localhost:8080/gcdoutput/ws/
   **HTTP Status 401 : Full authentication is required to access this resource**
   C:\Curl>

1. Compute gcd

   C:\Curl>curl --header "content-type:text/xml" --user admin:admin123 -d @gcdrequest.xml http://localhost:8080/gcdoutput/ws/
   ~~~xml 
      <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><gcdResponse><gcd>3</gcd></gcdResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>


1. Compute gcd - no numbers in queue

   C:\Curl>curl --header "content-type:text/xml" --user admin:admin123 -d @gcdrequest.xml http://localhost:8080/gcdoutput/ws/
   ~~~xml 
   <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><gcdResponse><gcd>-1</gcd></gcdResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>

1. Get gcd list

   C:\Curl>curl --header "content-type:text/xml" --user admin:admin123 -d @gcdlistrequest.xml http://localhost:8080/gcdoutput/ws/
   ~~~xml
   <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><gcdListResponse><gcdList><gcd>3</gcd><gcd>1</gcd><gcd>15</gcd></gcdList></gcdListResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>
   C:\Curl>

1. Get gcd sum

   C:\Curl>curl --header "content-type:text/xml" --user admin:admin123 -d @gcdsumrequest.xml http://localhost:8080/gcdoutput/ws/
   ~~~xml
   <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"><SOAP-ENV:Header/><SOAP-ENV:Body><gcdSumResponse><gcdSum>19</gcdSum></gcdSumResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>
