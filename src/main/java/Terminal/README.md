IMPLEMENTATION OF TERMINAL TEXT BUFFER

**Technology:** Java 25

This project contains a memory efficient line terminal backend implementation in java 25

## Key Features
- **Circular Buffer (Ring Buffer):** Optimized O(1) complexity for line scrolling and history management.
- **Bitmask Style Management:** Efficient storage for cell attributes (Bold, Italic, Underline and more) using bit manipulation.
- **Wide Character Support:** Detection and proper wrapping of CJK characters (2-cell width) in `writeInput`.
- **Cascade Insertion:** Support for "ripple-effect" text insertion across the buffer.
- **Testing:** Includes basic I/O ring buffer overflow and production-grade edge case tests.

## Optimazations
The initial thought behind the project was to implement it using java.util.Arrays or java.util.Lists. 
However, that approach proved ineffective with large history (thousands of lines) 
and overwriting operations. Because of that I changed the approach and decided to use 
a Circular Buffer which provides $O(1)$ complexity for line overwriting and history 
management.

For management of text styles and fonts, I introduced a bitmasking mechanism.
Every style can be combined with others using the logical OR (|) operator.
This allows for complex text formatting without dropping performance.

## Coordinate system
The terminal features a Scrollback history that allows the user to access lines no longer visible on the screen. 
Accessing such content is handled via a negative Y-axis mechanism 
(for example y = -1 refers to the first line in history). 
This implementation is UI-friendly and compatible with most front-end solutions.

## In Progress
Due to time constraints wide character functionality is currently limited to the writeInput 
method. Implementing it into the insert method proved difficult, as the "ripple effect" 
(shifting characters) would need to handle 2-cell blocks atomically or as arrays. 
Attempting to force this implementation at the end of the project risked compromising 
the stability of the entire buffer.

Idea for future Insert functionality: To support Wide Characters in insert, the moveRight (shift) logic 
should be refactored to recognize "continuation cells" (\0). When a shift occurs
the system must ensure that a 2-cell character is moved as a single unit and not split 
between the end of one line and the start of the next.


Project is managed by **Maven**

Compile and Build:
mvn clean install

Run test:
mvn test