package net.vdbaan.issuefinder.model

class Issue {
//    int id
    String title
    Finding.Severity severity
    String description
    String recommendations
    List<FindingIdentifier> findings

    boolean checkFinding(Finding finding) {
        return findings.any { it.scanner == finding.scanner && it.plugin == finding.plugin }
    }

    Issue() {
        findings = new ArrayList()
    }

    Issue(Map issue) {
        this()
        title = issue['title']
        severity = issue['severity']
        description = issue['description']
        recommendations = issue['recommendations']
        findings = new ArrayList()
        issue['findings'].each {
            findings += new FindingIdentifier(it)
        }
    }
}

class FindingIdentifier {
    String scanner
    String plugin
}