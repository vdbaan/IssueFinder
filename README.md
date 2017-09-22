# IssueFinder
[![Build Status](https://travis-ci.org/vdbaan/IssueFinder.svg?branch=master)](https://travis-ci.org/vdbaan/IssueFinder)
[![License](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/vdbaan/IssueFinder/blob/master/LICENSE)

This tool has been created out of frustration that NessieViewer is/was not available for *nix systems. 
Some of the functionality has been added as a result of various requests. 
The result is an easy tool that can read various formats and show the issues in a table format. This allows you to
sort specific columns. When an issue is selected, more information will be visible in the description pane.

![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/FX-Issue-Finder.png)

You can add reports either through the commandline as arguments, or through the file menu by opening them.
The 'New' menu option resets the application.

![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/FX-File-Menu.png)

This tool has been created in Groovy as a learning exercise.  


#### Filters
![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/FX-Filter.png)

The filter section allows you to filter on specific values. Filters are like a query language where clause on the data. 
It is possible to filter on the following elements (case insensitive):
- scanner
- ip
- hostname
- port
- plugin
- service
- risk
- exploitable

The filter language understands the following comparing options:

- '==' equals, an element should is the given value
- '!=' not equals, an element is not the given value
- 'like', an element contains the given value

It is possible to operate with queries, a query can be negated (using the !) or two queries can be combined with 
'***or***' (||) or '***and***' (&&) operators.

The use of brackets is encouraged as it will clarify the order in which the queries are processed.

All used filter queries are added to the dropdown list together with the already present examples.
![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/FX-Filter-examples.png)

#### Filtering from table
![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/FX-Table-Menu.png)

You can right click on a table entry which allows you to:
- Filter on IP
- Filter on Port
- Filter on Service
- Filter on Plugin
- Modify a single entry

These will fill the specific selected filter.

#### Status bar
![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/FX-Status.png)
The status bar has three regions. On the left the application will display general information. The other two areas show
the total amount of Findings displayed and the amount of unique IPs displayed. 


Right-clicking on the amount of unique IPs shows you a pupup that allows you to copy the following to the clipboard:
- Unique IPs
- Unique IP/Port

![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/FX-ports.png)


## About
IssueFinder takes some ideas for the definition of an issue from [IVIL xml](https://github.com/seccubus/ivil). 
Currently it is able to parse the following formats:
  - Nessus
  - Nikto
  - NMap
  - Netsparker
  - TestSSL (json format)

## Changelog

1.4.1:
 - Implement Summary sceen

1.4.0:
 - Remade graphical interface into JavaFX
 - Added Filter technique like Burp-Logger++

1.3.0:
 - XML parsers have been optimised to be reused when multiple files are scanned.
 - Nikto parser had lost their issues.
 
1.2.0:
 - Added edit functionality.
 - Added CVSS Base score and exploitable to findings. Display the port in 'gnmap format'. Made more 'groovy'esk changed to the code.
 Started smalling down the jar file by removing unneeded classes.


1.1.0:
 - Added the following parsers:
   - Arachni
   - Burp

Fixed the Nikto parser

1.0.1 - Added Autocompletion to scanner, plugin and risk filter on request of [@anantshri](https://twitter.com/anantshri). Used code from [Scott Robinson](http://stackabuse.com/example-adding-autocomplete-to-jtextfield/)

1.0.0 - Fixed some bugs, made first real release

0.1.1 - Fixed swing threading in regard to filtering

0.1.0 - Initial release

## TODO
1. Implement regular expressions in filters
1. Number sort IP overview
1. Add pasers for the following tools:
   - Burp
   - ZAP
   - Nexpose
   - OpenVAS
   - What ever will be requested
