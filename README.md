##My Personal Budget Planner

  

**What will the application do?**
    
- This personal budget planner help the user manage their money
    by keeping track of the user's spending habits. This application
    features a settable budget for the month and indicators on whether 
    spending habits are good/bad on a small calendar.

***Who* will use it?**
    
- Anyone who wants to learn more about their spending habits
    and wants to help save more.


**Why is this project of interest to you?**

- I feel like I don't spend my money very well, and I want to
    have an application that can keep me accountable or at least
    keep track of it.

**User Stories**

- As a user, I want to be able to add a monthly budget, and edit it.
- As a user, I want to be able to add daily transactions and paychecks
- As a user, I want to be able to view my spending habits in a clear way. 
- As a user, I want to be able to see days I exceeded a calculated daily budget
- As a user, I want to be able to see any savings.

**Phase 2 User Stories**

- As a user, I want to be able to save my planner to file
- As a user, I want to be able to be able to load my entire planner from file 

**Phase 3 User Stories**

- As a user, I want to be able to add multiple transactions to my day. I want to see the changes automatically 
reflected on my calendar upon entry. 
- As a user, I want to be able to load and save the state of the application.
- As a user, I want to be able to save my planner within my session from the menu bar at any point in time.

**Phase 4: Task 2**
- Sample console output:

>Tue Mar 29 11:57:46 PDT 2022 <br>
>Id:1 Sample Purchase was processed, adding 100.0 to your credit balance.

>Tue Mar 29 11:57:51 PDT 2022 <br>
Id:2 Sample Deposit was processed, adding 100.0 to your credit balance.

>Tue Mar 29 11:57:59 PDT 2022 <br>
Monthly budget for month 1 was set to 100.0

>Tue Mar 29 11:58:02 PDT 2022 <br>
Monthly budget for month 1 was increased by 100.0

>Tue Mar 29 11:58:05 PDT 2022 <br>
Monthly budget for month 1 was decreased by 100.0

**Phase 4: Task 3** <br>
I would refactor Transaction to become an 
abstract class, with fields GregorianCalendar. This would record the date that the transaction was made. This will 
solve the problem that the Day class currently doesn't know which month it's in, consequently allowing the console 
log to be more detailed later on.

I would also add an additional method getString() as an abstract method.
Credit and Debit will be different classes extending Transaction, and will override the toString() method differently.
This will reduce the amount of code dedicated to dealing with two cases of Transaction.

Another change would be separating the CalendarDrawer class into separate classes for each JFrame. This will reduce the
amount of code in a single class and make it more intuitive to navigate.
