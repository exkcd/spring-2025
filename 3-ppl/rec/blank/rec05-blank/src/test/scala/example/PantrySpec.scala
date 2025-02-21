package example
import org.scalatest.flatspec.AnyFlatSpec

class PantrySpec extends AnyFlatSpec {

  // Anatomy of a test:
  /**
    * <functionality we're testing> should <what we expect to occur> in {
    *   <body of test>
    * }
    */

  "adding a new item" should "add a new item properly" in {
    val base = List(Item("beef", 10))
    val res = Pantry.processCommand("add",base)("carrots")
    assert(res.length == 2)
    assert(res(1).name == "carrots")
  }

  "adding an existing item" should "increment the quantity of the item" in {
    //YOUR SOLUTION HERE
    ???
    //END SOLUTION
  }

  "removing one of several existing items" should "decrement the item quantity" in {
    //YOUR SOLUTION HERE
    ???
    //END SOLUTION
  }

  "removing the last item" should "remove the entry from the list" in {
    //YOUR SOLUTION HERE
    ???
    //END SOLUTION
  }

  "retrieving low stock" should "retrieve items with stock < 3" in {
    val base = List(Item("onion", 2), Item("beef", 6), Item("garlic", 1))
    val res = Pantry.lowStock(base)
    assert(res.length == 2)
    assert(res(0).name == "onion")
    assert(res(1).name == "garlic")
  }

  "restocking" should "update each item to have more stock" in {
    val base = List(Item("onion", 2), Item("beef", 6), Item("garlic", 1))
    val res = Pantry.processCommand("restock",base)("2")
    assert(res(0).quantity == 4)
  }

  // TODO: write your own test for removing a nonexistent item! What should happen?

  "loading JSON" should "load JSON properly" in {
    val base = List(Item("beef", 10))
    val res = Pantry.processCommand("json",base)("test_pantry.json")
    assert(res.length == 3)
    assert(res(0).name == "salsa")
  }

}
