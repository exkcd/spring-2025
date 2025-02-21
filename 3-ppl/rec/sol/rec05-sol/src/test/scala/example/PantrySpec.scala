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
    val base = List(Item("beef", 1))
    val res = Pantry.processCommand("add",base)("beef")
    assert(res.length == base.length)
    assert(res(0).quantity == 2)
  }

  "removing one of several existing items" should "decrement the item quantity" in {
    val base = List(Item("beef", 2))
    val res = Pantry.processCommand("remove",base)("beef")
    assert(res.length == base.length)
    assert(res(0).quantity == 1)
  }

  "removing the last item" should "remove the entry from the list" in {
    val base = List(Item("beef", 1), Item("carrots", 2))
    val res = Pantry.processCommand("remove",base)("beef")
    assert(res.length == 1)
    assert(res(0).name == "carrots" && res(0).quantity == 2)
  }

  "removing a nonexistent item" should "not modify the list" in {
    val base = List(Item("beef", 1), Item("carrots", 2))
    val res = Pantry.processCommand("remove",base)("lizards")
    assert(res == base)
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

  "loading JSON" should "load JSON properly" in {
    val base = List(Item("beef", 10))
    val res = Pantry.processCommand("json",base)("test_pantry.json")
    assert(res.length == 3)
    assert(res(0).name == "salsa")
  }

}
