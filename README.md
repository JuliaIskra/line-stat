## Lines size analyzer in Java code

### How to build
`lein uberjar`

### How to use
To get stat on lines sizes:

`java -jar target\uberjar\line-stat-*-standalone.jar stat path/to/java/project`

Sample output:
```
lines   count
 
 0      54
 1      22
 2      1
 4      106
 5      114
 8      4
 9      65
 10     34
 ```
 
 To get lines longer than specified size:
 
 `java -jar target\uberjar\line-stat-*-standalone.jar lines path/to/java/project 78`
 
 Sample output:
 ```
 
 ================================================================================
 ..\..\Java\calculator\expression\Division.java:
         double denominator = Math.pow(op2.real, 2) + Math.pow(op2.imaginary, 2);
 
 ================================================================================
 ..\..\Java\calculator\expression\Subtraction.java:
     public Subtraction(int nesting, Expression operand1, Expression operand2) {
 ```
 
