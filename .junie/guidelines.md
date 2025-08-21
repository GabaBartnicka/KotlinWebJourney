# Junie AI Assistant Instructions

## Java Coding Style Preferences

### Local Variable Type Inference (var)
- ALWAYS use `var` instead of explicit types when the type is obvious from the context
- Prefer `var` for all local variable declarations where possible (Java 10+)
- Use `var` for object initialization: `var list = new ArrayList<String>();`
- Use `var` for method calls: `var result = methodReturningString();`
- Use `var` in enhanced for loops: `for (var element : collection)`
- Use `var` with casting: `var text = (String) object;`
- Use `var` with lambda expressions where type is clear: `var predicate = (String s) -> s.isEmpty();`

### When to use explicit types
- Use explicit types only when:
    - The type is not obvious from the right side of assignment
    - Declaring a variable without initialization
    - The inferred type would be too generic (e.g., Object)
    - Working with numeric literals where precision matters

### Preferred Style Examples

GOOD (use this):
```java
var name = \"John Doe\";
var number = 42;
var list = new ArrayList<String>();
var result = calculateResult();
var map = new HashMap<String, Integer>();
var stream = list.stream();
var optional = Optional.of(\"value\");

for (var item : items) {
    var processedItem = processItem(item);
}

try (var reader = Files.newBufferedReader(path)) {
    // process file
}
```

AVOID (don't use this):
```java
String name = \"John Doe\";
int number = 42;
ArrayList<String> list = new ArrayList<String>();
SomeReturnType result = calculateResult();
HashMap<String, Integer> map = new HashMap<String, Integer>();
Stream<String> stream = list.stream();
Optional<String> optional = Optional.of(\"value\");

for (SomeType item : items) {
    ProcessedType processedItem = processItem(item);
}

try (BufferedReader reader = Files.newBufferedReader(path)) {
    // process file
}
```

## Additional Java Style Preferences

### Modern Java Features
- Use text blocks for multiline strings (Java 15+)
- Prefer switch expressions over switch statements
- Use pattern matching where available
- Use records instead of simple data classes (Java 14+)
- Use sealed classes for restricted inheritance (Java 17+)

### Stream API and Functional Programming
- Prefer stream operations over traditional loops
- Use method references where appropriate
- Keep lambda expressions concise and readable

### Code Organization
- Use meaningful variable names even with `var`
- Group related functionality together
- Prefer composition over inheritance
- Use builder patterns for complex object creation

### Exception Handling
- Use try-with-resources for resource management
- Prefer specific exceptions over generic ones
- Don't catch and ignore exceptions without good reason

### Collections and Generics
- Use diamond operator for generic instantiation
- Prefer immutable collections where possible
- Use appropriate collection interfaces (List, Set, Map) rather than implementations

## Language Requirements
- ALL comments MUST be written in English only
- ALL log messages MUST be written in English only
- ALL JavaDoc documentation MUST be written in English only
- ALL string literals visible to users may be in local language, but code-internal strings should be in English
- ALL variable names, method names, and class names MUST use English words


## Code Quality Guidelines
- Write self-documenting code
- Use `var` to reduce redundancy and improve readability
- Maintain consistency throughout the codebase
- Prefer modern Java idioms and language features
- Keep methods small and focused on single responsibility

- ALWAYS use text blocks for multiline strings (Java 15+)
- Use text blocks for strings longer than 80 characters that contain special characters
- Use text blocks for any string that would require escape sequences (\
  , \\\", \\\\)
- Use text blocks for JSON, XML, HTML, SQL queries, and other structured text
- Use text blocks for file paths and URLs that span multiple conceptual parts

### Text Block Examples

GOOD (use text blocks):
```java
var json = \"\"\"
    {
        \"name\": \"John Doe\",
        \"age\": 30,
        \"address\": {
            \"street\": \"123 Main St\",
            \"city\": \"New York\"
        }
    }
    \"\"\";

var sql = \"\"\"
    SELECT u.name, u.email, p.title
    FROM users u
    JOIN posts p ON u.id = p.user_id
    WHERE u.active = true
    ORDER BY p.created_at DESC
    \"\"\";

var html = \"\"\"
    <div class=\"container\">
        <h1>Welcome to our website</h1>
        <p>This is a sample paragraph with \"quotes\" and special characters.</p>
    </div>
    \"\"\";

var longMessage = \"\"\"
    This is a very long error message that explains what went wrong
    and provides detailed information about how to fix the issue.
    It spans multiple lines and is much more readable as a text block.
    \"\"\";
```

AVOID (don't use concatenation or escape sequences):
```java
String json = \"{\
\" +
    \"    \\\"name\\\": \\\"John Doe\\\",\
\" +
    \"    \\\"age\\\": 30,\
\" +
    \"    \\\"address\\\": {\
\" +
    \"        \\\"street\\\": \\\"123 Main St\\\",\
\" +
    \"        \\\"city\\\": \\\"New York\\\"\
\" +
    \"    }\
\" +
    \"}\";

String sql = \"SELECT u.name, u.email, p.title \" +
    \"FROM users u \" +
    \"JOIN posts p ON u.id = p.user_id \" +
    \"WHERE u.active = true \" +
    \"ORDER BY p.created_at DESC\";

String longMessage = \"This is a very long error message that explains what went wrong \" +
    \"and provides detailed information about how to fix the issue. \" +
    \"It spans multiple lines and is much more readable as a text block.\";
```

### Modern Java Features
- Prefer switch expressions over switch statements
- Use pattern matching where available
- Use records instead of simple data classes (Java 14+)
- Use sealed classes for restricted inheritance (Java 17+)`,
  `oldText`: `### Modern Java Features
- Use text blocks for multiline strings (Java 15+)
- Prefer switch expressions over switch statements
- Use pattern matching where available
- Use records instead of simple data classes (Java 14+)
- Use sealed classes for restricted inheritance (Java 17+)`,


#### Java 17 Features
- Use sealed classes for restricted inheritance
- Use pattern matching for instanceof
- Prefer switch expressions over switch statements

#### Java 18 Features
- Use UTF-8 as default charset
- Simple web server for prototyping: `jwebserver`

#### Java 19 Features
- Use virtual threads for high-concurrency applications:
```java
var executor = Executors.newVirtualThreadPerTaskExecutor();
var future = executor.submit(() -> {
    // task code
});
```
- Use structured concurrency for coordinated tasks
- Use pattern matching in switch (preview)

#### Java 20 Features
- Use scoped values instead of ThreadLocal where appropriate
- Enhanced pattern matching with record patterns

#### Java 21 LTS Features
- Use string templates for dynamic string creation (preview):
```java
var name = \"John\";
var age = 30;
var message = STR.\"Hello \\{name}, you are \\{age} years old\";
```
- Use sequenced collections (List, Set, Map with first/last methods)
- Use unnamed patterns and variables with underscore `_`
- Use unnamed classes and instance main methods for simple programs

#### Java 22 Features
- Use foreign function and memory API for native code interaction
- Use stream gatherers for custom intermediate operations
- Use multi-source file programs
- Use statements before super() in constructors

#### Java 23 Features
- Use primitive types in patterns and switch
- Use flexible constructor bodies
- Use module import declarations
- Use ZGC generational mode for better garbage collection

#### Java 24 Features (Early Access)
- Use stream and optional enhancements
- Use improved pattern matching
- Use enhanced switch expressions with better null handling

### Feature Usage Guidelines
```java
// Sealed classes (Java 17+)
public sealed class Shape permits Circle, Rectangle, Triangle {
    // base implementation
}

// Pattern matching with instanceof (Java 16+)
if (obj instanceof String s && s.length() > 5) {
    var processedString = s.toUpperCase();
}

// Enhanced switch with pattern matching (Java 19+)
var result = switch (obj) {
    case String s -> s.length();
    case Integer i -> i;
    case null -> 0;
    default -> -1;
};

// Virtual threads (Java 19+)
Thread.startVirtualThread(() -> {
    // lightweight thread task
});

// Sequenced collections (Java 21+)
var list = new ArrayList<String>();
var first = list.getFirst(); // instead of get(0)
var last = list.getLast();   // instead of get(size()-1)

// String templates (Java 21+ preview)
var json = STR.\"\"\"
    {
        \"name\": \"\\{name}\",
        \"age\": \\{age}
    }
    \"\"\";

// Unnamed variables (Java 21+)
try (var _ = resource.acquire()) {
    // use underscore for unused variables
}

// Record patterns (Java 19+)
record Point(int x, int y) {}
var point = new Point(1, 2);
switch (point) {
    case Point(var x, var y) -> System.out.printf(\"Point at %d, %d%n\", x, y);
}
````,
