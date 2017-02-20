# IssueFinder
[![Build Status](https://travis-ci.org/vdbaan/IssueFinder.svg?branch=master)](https://travis-ci.org/vdbaan/IssueFinder)
[![License](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/vdbaan/IssueFinder/blob/master/LICENSE)

This tool has been created out of frustration that NessieViewer is/was not available for *nix systems. 
Some of the functionality has been added as a result of various requests. 
The result is an easy tool that can read various formats and show the issues in a table format. This allows you to
sort specific columns. When an issue is selected, more information will be visible in the description pane.

![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/if-main.png)

You can add reports either through the commandline as arguments, or through the file menu by opening them.
The 'New' menu option resets the application.

![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/if-menu.png)

This tool has been created in Groovy as a learning exercise.  


#### Filters
![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/if-filters.png)

The filter section allows you to filter on specific values. Filters can be joined by a comma. e.g. When you set 
the following filter "80,443" for ports you will get a list where either 80 or 443 is in the ports 
(this will match issues on port 80,8080,8081,443,8443,4443; you get the point).
Searching in the description is technically searching a string anywhere in the issue.

All the filters are case sensitive.
#### Filtering from table
![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/if-table.png)

You can right click on a table entry which allows you to:
- Filter on IP
- Filter on Port
- Filter on Service
- Filter on Plugin

These will fill the specific selected filter.

#### IP overview
In the right bottom corner there is an indication of the amount of unique IPs that are visible in the table. 

![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/if-ips.png)

Right-clicking on that allows you to copy the following to the clipboard:
- Unique IPs
- Unique IP/Port

![](https://raw.githubusercontent.com/vdbaan/IssueFinder/master/wiki/images/if-ports.png)


## About
IssueFinder takes some ideas for the definition of an issue from [IVIL xml](https://github.com/seccubus/ivil). 
Currently it is able to parse the following formats:
  - Nessus
  - Nikto
  - NMap
  - Netsparker
  - TestSSL (json format)

## Changelog
1.0.1 - Added Autocompletion to scanner, plugin and risk filter on request of @anantshri. Used code from [Scott Robinson](http://stackabuse.com/example-adding-autocomplete-to-jtextfield/)

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
