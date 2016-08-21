# Change Log
All notable changes to this project will be documented in this file.
This project does its best to adhere to [Semantic Versioning](http://semver.org/).


--------
###[0.1.1](N/A) - 2016-08-21
####Changed
* Added JCollectionBuilders, JCollectionInterface, and JTuples dependencies
* Updated jcollection-util to latest 0.7.x version


--------
###[0.1.0](https://github.com/TeamworkGuy2/JMetaAccess/commit/f036c16b2703f1ba5759cc610936e201a2b1c9bd) - 2016-06-21
####Added
* Initial versioning of existing code, including simple field lookup with wrappers for treating fields like getters/setters, property like views of fields (i.e. getter and setter pair), and other simple reflection utilities
* Added FieldGets methods for looking up single fields by name
* Added PropertyFactory methods for creating SimpleField instances from a class' fields, also includes methods for gather all of the hierarchical fields of a class' parent classes and interfaces
* CompoundField for traversing a deep object graph to retrieve a property and expose it via a simple get()/set() API as if it were a normal object field
* PropertyFromMethods allows a Supplier and Consumer function pair to be treated like a property
* PropertyNamer and PropertyNamingConvertion include utilities for translating and inferring the names of getters/setters for field names
