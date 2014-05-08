This application manages the Eventing queue used by Max components and allows for cohesive and decoupled integration
by applications and components external to this code base.

The standard JSON messaging format is as follows:

{
   "verb":"Placed",
   "location":{
      "lng":"-111.7895",
      "lat":"40.6193"
   },
   "generator":"MaxMobileApp",
   "actor":{
      "id":555,
      "objectSubtype":"Associate",
      "displayName":"Larry Bird",
      "objectType":"User"
   },
   "language":"en",
   "object":{
      "id":123,
      "properties":null,
      "objectType":"Order"
   },
   "published":"Wed May 07 18:22:17 MDT 2014"
}

==============================
Subjects
==============================

User(id, type)
 *Added (user Added user)
 *Modified (user Modified user, process Modified user)
 *Deleted (user Deleted user)

Order(id)
 *PaymentFailed (process PaymentFailed order)
 *PaymentProcessed (process PaymentProcessed order)
 *Placed (user Placed order)
 *Shipped (warehouse Shipped order)
 *Cancelled (user Cancelled order)

Product(id,SKU)
 *Added (user Added product)
 *Modified (user Modified product) -- (price, SKU, description, etc.)
 *Deleted (user Deleted product)

Market(id of country?)
 *Added (user Added market)  -- (country, date, language, etc.)
 *Modified
 *Deleted

Event(description, dateTimeStart, dateTimeEnd, location)
 *Added (user Added event, automatedProcess Added event)
 *Modified (user Modified event)
 *Deleted {user Deleted event)

Resource(id, url, type)  -- (video, image, audio, web page, note, commission, invoice, etc.)
 *Added (user Added resource, commissionSystem Added resource)
 *Modified (user Modified resource)
 *Deleted (user Deleted resource)

Prospect(id)
 *Contacted (user Contacted prospect)
 *Added (user Added prospect)
 *Modified (user Modified prospect)
 *Deleted (user Deleted prospect)

Task(id)
 *Added (taskEngine Added task, user Added task)
 *Modified (user Modified task)
 *Deleted (user Deleted task)

==============================
Actors
==============================
Warehouse(id)
 *Shipped
 *Added
 *Modified
 *Deleted

Process/Application/Component (name, url) (e.g. Exigo, Commissioning, Merchant, JavaAPI)
 *Added(task)
 *Shipped(order)
 *PaymentProcessed
 *PaymentFailed

User(id, type)
 *Enrolled
 *Moved
 *Advanced
 *LoggedIn
 *LoggedOut
 *Contacted
 *StartedCoaching
 *StoppedCoaching
 *StartedUsing (Resource: video, audio, image, web page, etc.)
 *StoppedUsing (Resource: video, audio, image, web page, etc.)


