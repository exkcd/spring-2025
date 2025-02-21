package example
import io.circe.parser
import scala.io.StdIn._

import io.circe.generic.auto._, io.circe.syntax._

// create a case class to represent items in our pantry
case class Item(name: String,quantity: Int)

// create the object we'll do everything with
object Pantry extends App {

  // give our pantry an initial stock of items
  var baseStock = List(Item("beef",1), Item("carrots", 10))

  // call our function to process input right away
  process_loop()

  // FUNCTIONS

  // TODO: implement printing a list of all the items in stock. Return the list unmodified.
  def list_ingredients(baseStock: List[Item]) : List[Item] = {
    for (item <- baseStock) {
      println(s"\t${item.name}: ${item.quantity}")
    }
    baseStock
  }

  //TODO: write a function increase the number of <name>s in your baseStock by 1 and return the new list
  def add(name: String, baseStock: List[Item]) : List[Item] = {
    // do this recursively, checking if the head is the item we're interested in
    baseStock match {
      case List() => List(Item(name, 1)) // add the item to the end if we reached the end of the list
      case Item(n, q) :: l if (n == name) => Item(name, q + 1) :: l // replace the item entry with an incremented item entry if we found it in the list
      case _ => baseStock.head :: add(name, baseStock.tail) // recurse if we haven't added the item yet
    }
  }

  //TODO: write a function to decrease the number of <names> in your baseStock by 1
  def remove(name: String, baseStock: List[Item]) : List[Item] = {
    // do this recursively, checking if the head is the item we're interested in
    baseStock match {
      case List() => { // we've reached the end of the list without finding our item, it's not here!
        println(s"No $name in the pantry!")
        List()}
      case Item(n, q) :: l if (n == name) => { // we've found an item matching the name
        if (q > 1) {
          Item(name, q - 1) :: l // replace the item entry with an incremented item entry if we found it in the list
        } else {
          l
        }
      }
      case _ => baseStock.head :: remove(name, baseStock.tail) // any other cases, recurse and add the current element back to the front
    }
  }

  //TODO: write a function to return a list of items which are running low (below 3 stock)
  def lowStock(stock: List[Item]): List[Item] = {
    stock.filter(i => i.quantity < 3)
  }

  // TODO: write a function to increase the amount of each item available by an amount (re-stocking)
  def increaseStock(stock:List[Item], amount: Int): List[Item] = {
    stock.map(i => Item(i.name, i.quantity+amount))
  }

  // processCommand handles figuring out what command was given and executes the appropriate command 
  // this function is already written, take a loook but don't worry about modifying it!
  def processCommand(s:String,baseStock: List[Item]) : String => List[Item] = {
    s match {
      // add some cases to handle our inputs
      case "add" => {
        print("what do you want to add? ")
        (name: String) => {
          add(name, baseStock)
        }
      }
      case "remove" => {
        print("what do you want to remove? ")
        (name: String) => {
          remove(name, baseStock)
        }
      }
      case "restock" => print("How much stock do you want to add?")
           (amt: String) => {
             try {
              increaseStock(baseStock,amt.toInt)
             } 
             catch  {
              case _: Throwable => println("Please enter a valid integer!"); baseStock
             }
           }
      case "json" => {
          println("Please enter the name of the file to load(json)") 
          (s2:String) => {
          val data = scala.io.Source.fromFile(s2).mkString
        
          parser.parse(data) match {
          case Left(_) => throw new Exception("Could not parse file")
          case Right(json) => json.as[List[Item]].getOrElse(throw new Exception("Incorrect format"))
          }
        }
      }
      case s => 
        throw new Exception("unrecognized command")
    }
  }

  // function that handles getting input from the user, call this from the object to interact with the program
  // this function is already written, take a loook but don't worry about modifying it!
  def process_loop(): Unit = {
    println("Welcome to Pantry!")
    println("Available Commands:")
    println("list : print list")
    println("add : add a new item or increment an existing one")
    println("remove : remove an existing item")
    println("quit : exit")
    println("low: print items with low stock")
    println("restock: add some amount to all items stocks")
    println("json : give a json file to load stock from")
    print("What do you want to do? ")
    var input = readLine()
    while (input != "quit") {
      if (input == "list") {
        list_ingredients(baseStock)
      } else if (input == "low") {
        list_ingredients(lowStock(baseStock))
      } else {
        try {
          val inter = processCommand(input,baseStock)
          baseStock = inter(readLine())
        } catch {
          case e: Throwable  => {
            println("Encountered Error:")
            println(e)
          }
        }
      }
      print("What do you want to do? ")
      input = readLine()
    }
    println("Goodbye!")
  }
}