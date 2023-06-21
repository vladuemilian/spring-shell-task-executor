# Introduction
Task executor is a module based CLI tool written with Spring-Shell. It comes with a modules approach that allows
grouping and designing tasks in a modular ways around a specific topic or use case.

# Existing modules
### Transaction module
Transaction module comes up with 3 commands: `csv-read`, `json-writer` and `pretty-writer`. The module processes
a the CSV file and transforms it into the domain specific object called `TransactionDto`, which has the following structure:

```json
{
  "inputFilename": "filename",
  "processedRecords": 0,
  "totalTransactionsAmount": 0,
  "amountsPerDay": {
    "2018-01-01": 0
  }
}
```

This is the main object where each reader and writer is working with. For more information please use the `help <command>` option
on each command.

# Design considerations
The application was designed with extensibility in mind, instead of grouping everything around the `Transaction` logic, 
it allows defining new modules and commands that can be used to process different types of files or entities.

The transaction module with the way the readers and writers work independently, allows to easily extend the application.
Where the writers and readers intersect is through the TransactionContext - where each Transaction thas has been read, 
is being stored in memory for further processing by the writers. This has been achieved using a singleton bean that is
being injected into the readers and writers.

# Future improvements
 - Integration tests are missing 
 - There was room left with the Task.TaskType (READER/WRITER) to define common methods for the readers and writers such
as if a reader is defined, the `output` argument will be automatically set.
 - The application was not designed to support multiple instances processing at the same time. For big datasets, there might
be other solutions that can be used to process the data in parallel.
 - A transition helper command to support running a reader and a writer at the same time, e.g: `perform <reader> ...args <writer> ...args`
which can be achieved by iterating through the readers and writers and register them as a single command.
 - A clean git history with proper commits was not considered for the project.

# Installation

* `mvn clean install`
* `java -jar target/task-executor-1.0.0.jar`
* `help <command>` to see the available commands

### A sample transaction file can be found at the rood of the project: `transaction_data.csv`

# Usage

* `help` or `help csv-read` to print the existing parameters

* `csv-reader --file /Users/sorinvladu/transaction_data.csv --output /Users/sorinvladu/output.json`
The `--output` parameter is optional, if not provided, the output will be printed to the console.

* `pretty-writer --file /Users/sorinvladu/output.json` or simply `pretty-writer`
* `json-writer --file /Users/sorinvladu/output.json` or simply `json-writer`

