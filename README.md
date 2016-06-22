JMetaAccess
==============
version: 0.1.0

Java meta-data access utlities (java.lang.reflect.*). 

####propertyAccessor/
create and store field references, tailored to access getters/setters via common naming conventions, such as get_(), set_(), and is_()

####simpleReflect/
wrappers for java.lang.reflect.Method and java.lang.reflect.Field that throw runtime exceptions and provide easy access to primitive fields

####templates/
ANTLR StringTemplate templates for generating the many primitive variations of the methods found in 'simpleReflect/' classes

####templates/generators/
Generator classes for the 'templates/' files

####test/
JUnit tests and examples