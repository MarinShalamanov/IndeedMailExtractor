# IndeedMailExtractor
Simple tool for employers who want to automatically extract applicants' emails (from their resumes) in Indeed.com

Provided with the link to the first page of recieved applications in Indeed the tool extracts the applicant
name and personal email (from the CV and NOT the ..... @indeedmail.com) in a CSV file.

## Example

```java
String outputFile = "mails.csv";
String url = "URL to the first page of the list of applicants in Indeed";

IndeedExtractor indeedExtractor = new IndeedExtractor();
indeedExtractor.pw = new PrintStream(new File(outputFile));
indeedExtractor.extract(url, 1, 21);
```
