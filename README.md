# Tennis Courts - Challenge

# Introduction 
You have been tasked to fix some misimplementations of a backend API for a simple reservation platform for tennis players. Using this platform, users can book reservations on a variety of tennis courts that are registered on it. 

This code is already developed in https://github.com/ermathias/tennis-courts-v1 - Clone it, open a feature branch.

The actual code has the below user stories already implemented. You should complete at least one of the **bold** ones to be able to attend the interview. **In case you are comfortable to complete other ones it will count in your evaluation.**

**1. As a User I want to be able to book a reservation for one or more tennis court at a given date schedule**

2. As a User I want to be able to see what time slots are free 
3. As a User I want to be able to cancel a reservation 
4. As a User I want to be able to reschedule a reservation 

**5. As a Tennis Court Admin, I want to be able to Create/Update/Delete/Find by id/Find by name/List all the guests**

6. As a Tennis Court Admin, I want to be able to create schedule slots for a given tennis court

**7. As a Tennis Court Admin, I want to charge a reservation deposit of $10 to the user, charged per court, which is refunded upon completion of their match, so that Users don’t abuse my schedule**

8. As a Tennis Court Admin, I want to refund the reservation deposit if the user has cancelled or rescheduled their reservation more than 24 hours in advance 

**9. As a Tennis Court Admin, I want to keep 25% of the reservation fee if the User cancels or reschedules between 12:00 and 23:59 hours in advance, 50% between 2:00 and 11:59 in advance, and 75% between 0:01 and 2:00 in advance**

10. As a Tennis Court Admin, I want to keep 100% of the reservation deposit if the User does not show up for their reservation
11. As a Tennis Court Admin, I want to be able to see a history of my past reservations so that I can use the information to improve the management of my establishment 

# Technical requirements
 
 **- Implement all the missing Restful swagger statements and the API paths to the controllers**
 
 **- Check why rescheduleReservation in the class ReservationService.java is not working correctly and fix it**
 
# Assumptions 
●	No authentication/authorization system is required 

●	Guests will always play for 1 hour - no more, and no less 
 
# Constraints 
1. Ensure the code is well tested
 
# Deliverables and Closing Note 
For delivering your code you have to do the **FORK** of the @ermathias(ermathias/tennis-courts-v1) repository, then when you finish the implementation please submit a Pull Request to this **FORK** which will be placed in your github account.

Good Luck and happy coding!
