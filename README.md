# Domain Model Design Tools

A simple tool to generate Context Map and Domain Model with Java annotation.

## Introduction

As an architect, I found myself wasted a lot of time drawing and updating diagrams.
DSL like plantuml works good enough for simple diagrams however

1. it is difficult to control the layout of the diagram.
   Tricks like hidden link and clear understanding of the ranking of each component would help,
   but when the digram grew large, it's close to impossible to keep track of things.
1. it is difficult to maintain the diagram.
   Upgrading the diagram often quite labor intensive, mostly because it's just a giant file with many components.
   It could be modularized by using include, but that requires hosting those include files somewhere.
   
## Why Java Annotation

Java is supported by most common IDE, refactoring your domain objects could be done easily.
Using annotation also help us separate the code for the data structure from the code describing their relationship. 

## Usage

### Context Map

All context map annotation are done on the package level.

Each package represents a bounded context is annotated with `@BoundedContext("<name>")`.
The relationship between the bounded context is annotated with the name of the pattern used, `@AntiCorruptionLayer`, `@Conformist`, `@CustomerSupplier`, `@OpenHostService`, or `@SharedKernel`.
All of them take a parameter, `value`, as the full package name of the other bounded context.
`@CustomerSupplier` takes an extra parameter indicting if the bounded context is an upstream or downstream relationship.

#### TODO

* difficult color for external bounded context
* add Published Language

### Domain Model

Domain Model are annotated at the class level.
Classes can be annotated as `@AggregatedRoot`, `@DomainService`, `@Entity`and `@ValueObject`.
These annotation are shown on the generated domain model with different color and provide checks 