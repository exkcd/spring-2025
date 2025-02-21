# Week 5 Recitation, Spring 2025

## Announcements

### Goals

* Get comfortable using VSCode
* Implement and run tests in VSCode
* Review Higher-Order Functions

### Quick reference for sbt

- build.sbt - project definitions, dependencies
- `sbt run` - builds and runs the program
- `sbt test` - builds program, runs tests
- `sbt compile` - build the program

## Pantry

Our goal for this recitation is to build a tracker for pantry management. We want to be able to add things, remove them, and get a list of our current stock. 

We've already implemented a text-based interface so you don't have to worry about handling input from the user: just implementing and testing the methods. 

## Warm-up: List

Take a look at the functions and classes in the file, and see if you have any questions (where is Item defined? Why will our program do anything at runtime?). Some parts of the input handling use things you haven't seen yet, don't worry about those for now. 

Implement the body of the function `list_ingredients(baseStock: List[Item])` to print out the given parameter as pairs of the form:
`<name>: <quantity>`

Try running the program. You can run your program in VSCode's built-in terminal by navigating to the folder `week5_student` and entering `sbt run`. Does it print the contents of your pantry? You can end the program by entering `quit` when it asks for a command. 

## Exercise 1: Add Something

### Part A: Implementation

Implement the function `add(name: String, baseStock: List[Item])`. It should take a string to add to your pantry and a stock list to add it to, and return the augmented list. There are two cases:
* if `name` is already in our list, we want to increment the quantity by 1
* if `name` is not in our list, add a new element to the end of the list with quantity 1

(Hint: use match and recurse on the tail if you didn't find the object you were looking for)

Try running your program. Can you add things? Does it work the way you intended? 

### Part B: Testing 

Let's automate checking to see that we added things correctly! In the project folder, src/test/scala/example has a file `PantrySpec.scala`. There is already a test for `adding a new item`. 

Write a test for `adding an existing item`, which should increment the quantity of that item in the list but shouldn't add a new item to the list. 

## Exercise 2: Removal

### Part A: Implementation

We also want to be able to remove things from our pantry. Implement the function `remove(name: String, baseStock: List[Item])`, which should take a string for the name of the item to remove and return the baseStock list with the quantity of that item decremented by 1. A few cases:
* If the item isn't in the pantry, do nothing (you can print a message to the user if you want)
* If the item is in the pantry with a quantity > 1, decrement the quantity
* if you're removing the last remaining item from the pantry (quantity == 1), remove the entry from the list

(Hint: start from the recursive structure for `add`)

### Part B: Testing

Write some tests in your `PantrySpec.scala` file to check that you're removing things correctly:
* removing one of several existing items
* removing the last item
* removing a nonexistent item

## Example: Adding, using, and testing a library using SBT

When j is entered as a command, we want to allow the user to provide a path to a JSON file containing an inventory to load and use as our current pantry stock. 

To do so, we could make our own JSON parser, but this is a common format, and we want to reuse code rather than reinventing the wheel and having to test our parser too.

For this, we'll use the `circe` library in Scala (v. 0.14.1). [Link](https://circe.github.io/circe/)

To add a library, we modify build.sbt, and add it as a library dependency (see build.sbt)

We then import the necessary packages (see Pantry.scala), and can use the library.


## Exercise 3: Higher-Order Functions
Some actions we want may affect every item in our list, or at least multiple. To do this, we'll use higher-order functions. 
Recap: A higher-order function is one that takes a function as input or returns one as output.

For this exercise, the tests are given for you, so we can focus on the implementation itself.

### Part A: Finding Items with low stock

It would be nice to know which items our pantry is running low on, so we can restock those items. We want to implement `lowStock(baseStock: List[Item])`, which should take our stock list, and return a list which includes only the ones with less than 3 stock remaining.

(Hint: Look at the specification of List.filter)

### Part B: Adding stock to every item

When we go to restock our pantry, we may want to buy some amount of every item, and add them to our pantry all at once. 
To do this, we'll implement `increaseStock(stock:List[Item], amount: Int)`, which should add amount to each item in stock.

(Hint: Look at the specification of List.map)
