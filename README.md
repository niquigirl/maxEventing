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

------------------------------
==============================
Subjects
==============================

User(id, type)
 *Added (user Added user)
 *Modified (user Modified user, process Modified user)
 *Deleted (user Deleted user)

Order(id)
 *Placed (user Placed order)
 *Shipped (warehouse Shipped order)
 *Cancelled (user Cancelled order)

Product(id,SKU)
 *Added (user Added product)
 *Modified (user Modified product) -- (price, SKU, description, etc.)
 *Deleted (user Deleted product)

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

Payment(id, type, amount, currency)
 *PaymentFailed (process PaymentFailed payment(commission, 567, 32.50, USD), user PaymentFailed payment(order, 123, 500, CAD))
 *PaymentProcessed (process PaymentProcessed order)
 *PaymentProcessed (user PaymentProcessed commission, Exigo PaymentProcessed payment)

------------------------------
==============================
Actors
==============================
Warehouse(id)
 *Shipped
 *Added
 *Modified
 *Deleted

Process/Application/Component (name, url) (e.g. Exigo, VO, Merchant, JavaAPI, Coach)
 *Added(task)
 *Shipped(order)
 *PaymentProcessed (Exigo PaymentProcessed order, Exigo PaymentProcessed commission)
 *PaymentFailed (Exigo PaymentFailed order, Exigo PaymentFailed commission)

User(id, type)
 *Enrolled (user Enrolled user(self/other))
 *Moved (user Moved noSubject Target-Left)
 *AdvancedRank (user AdvancedRank noSubject Target-Gold)
 *LoggedIn (user LoggedIn process)
 *LoggedOut (user LoggedOut resource)
 *Contacted (user Contacted user, user Contacted prospect)
 *Started (user Started coaching, user Started video) (Resource: video, audio, image, web page, etc.)
 *Stopped (user Stopped coaching, user Stopped video) (Resource: video, audio, image, web page, etc.)
 *Completed (user Completed video, user Completed task)

------------------------------
==============================
Generators
==============================
Exigo, VO, JavaAPI, Coach