Goodreads Parser
================

# Overview

A utility to parse the CSV files that can be exported from goodreads to allow for various statistical generation, data transformation, and possible data enrichment in future versions.

## Background

I love to read, and I love 

# Classes of Note

[BookParsing](src/main/java/noorg/bookparsing/BookParsing.java)

## Parsing

## Reporting

Once your data has been parsed/enriched, you want to do something with it. I've 

### Domain Objects

The core of the reporting are the do


### Services

So far there is one service interface: [ReportService](src/main/java/noorg/bookparsing/report/ReportService.java) with an abstract implementation ([AbstractReportService](src/main/java/noorg/bookparsing/report/impl/AbstractReportService.java)) for shared code.

I've created the following concrete implementations so far:

- [GenreReportService](src/main/java/noorg/bookparsing/report/impl/GenreReportService.java) - Creates a report for each GENRE you've read and outputs a genre-by-genre breakdown  
- [YearlyReportService](src/main/java/noorg/bookparsing/report/impl/YearlyReportService.java)


TODO this Readme will need a lot of help...
