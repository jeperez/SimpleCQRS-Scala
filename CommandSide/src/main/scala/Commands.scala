package SimpleCqrsScala.CommandSide

import java.util.UUID

sealed trait Command

//	Order
case class CreateOrder(id: UUID, customerId: UUID, customerName: UUID) extends Command
case class AddInventoryItemToOrder(id: UUID, invetoryItemId: UUID, quantity: Int) extends Command
case class RemoveInventoryItemFromOrder(id: UUID, invetoryItemId: UUID, quantity: Int) extends Command
case class AddShippingAddressToOrder(id: UUID, invetoryItemId: UUID, quantity: Int) extends Command
case class PayForTheOrder(id: UUID, invetoryItemId: UUID, quantity: Int) extends Command
case class SubmitTheOrder(id: UUID) extends Command

//	Inventory Item
case class CreateInventoryItem(id: UUID, name: String) extends Command
case class DeactivateInventoryItem(id: UUID) extends Command
case class RenameInventoryItem(id: UUID, newName: String) extends Command
case class CheckInItemsToInventory(id: UUID, count: Int) extends Command
case class RemoveItemsFromInventory(id: UUID, count: Int) extends Command