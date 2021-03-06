package SimpleCqrsScala.CommandSide.Domain

import java.util.UUID

trait Identified {
	val id: UUID
}

sealed trait Sequenced {
	val sequence: Long
}

sealed trait DomainEvent extends Product with Serializable

sealed trait Event extends DomainEvent with Sequenced with Identified with Product with Serializable

object Event {
	
	lazy val hasACorrectId: Identified => Identity => Boolean = 
		event => aggregate => aggregate.id == new UUID(0, 0) || event.id == aggregate.id

	lazy val isInSequence: Sequenced => Versioned => Boolean = 
		event => aggregate => event.sequence == aggregate.expectedNextVersion
}

//	Order
final case class OrderCreated(id: UUID, description: String, sequence: Long) extends Event
final case class InventoryItemAddedToOrder(id: UUID, inventoryItemId: UUID, quantity: Int, sequence: Long) extends Event
final case class InventoryItemRemovedFromOrder(id: UUID, inventoryItemId: UUID, quantity: Int, sequence: Long) extends Event
final case class ShippingAddressAddedToOrder(id: UUID, shippingAddress: String, sequence: Long) extends Event
final case class OrderPayed(id: UUID, sequence: Long) extends Event
final case class OrderSubmitted(id: UUID, sequence: Long) extends Event
final case class OrderFailedToCheckOutItem(id: UUID, inventoryItemId: UUID, quantity: Int, sequence: Long) extends Event
final case class OrderDispatched(id: UUID, sequence: Long) extends Event
final case class OrderVoided(id: UUID, reason: String, sequence: Long) extends Event

//	Inventory Item
final case class InventoryItemCreated(id: UUID, name: String, sequence: Long) extends Event
final case class InventoryItemDeactivated(id: UUID, sequence: Long) extends Event
final case class InventoryItemRenamed(id: UUID, newName: String, sequence: Long) extends Event
final case class ItemsCheckedInToInventory(id: UUID, count: Int, sequence: Long) extends Event
final case class ItemsRemovedFromInventory(id: UUID, count: Int, sequence: Long) extends Event

final case class UnknownHappened(id: UUID, sequence: Long) extends Event

//	Order -> Inventory Item
final case class OrderItemRequested(orderId: UUID, itemId: UUID, quantity: Int) extends DomainEvent
final case class OrderItemOutOfStock(orderId: UUID, itemId: UUID, quantity: Int) extends DomainEvent
final case class OrderItemAvailable(orderId: UUID, itemId: UUID, quantity: Int) extends DomainEvent
