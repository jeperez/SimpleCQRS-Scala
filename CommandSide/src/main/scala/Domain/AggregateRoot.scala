package SimpleCqrsScala.CommandSide.Domain

import java.util.UUID
import SimpleCqrsScala.CommandSide._

import scalaz._

trait Versioned {
	val version: Long
}

trait Identity {
	val id: UUID
}

trait Aggregate[A] {
	val apply: List[Event] => A
	val newState: A => Event => A
}
object Aggregate {

	import InventoryItemOps._
	import OrderOps._
	
	implicit lazy val inventoryItemAggregate = new Aggregate[InventoryItem] {
		lazy val newState: InventoryItem => Event => InventoryItem = InventoryItemOps.newState
		lazy val apply: List[Event] => InventoryItem = InventoryItem.apply
	}

	implicit lazy val orderAggregate = new Aggregate[Order] {
		lazy val newState: Order => Event => Order = OrderOps.newState
		lazy val apply: List[Event] => Order = Order.apply
	}
}

object AggregateRoot {

	import DomainStates._

	def evolve[A : Aggregate](aState: A, history: List[Event]): A = 
		(history foldRight aState) {
			(e, s) => implicitly[Aggregate[A]].newState(s)(e)
		}

	def createFrom[A: Aggregate](history: List[Event]): A = implicitly[Aggregate[A]].apply(history)

	def getNewState[A : Aggregate](esg: A => List[Event]): EvolvableState[A] = for {
		es 	<- State.gets(esg)
		_ 	<- State.modify { s: A => evolve(s, es) }
	} yield es
}