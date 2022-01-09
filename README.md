# Oryx Student Information System

### Task 1:

Prepare a really simple Student Information System. In this system students can enroll to different classes. With this system student can:

- list all available classes
- basic search of classes (i.e. by class name)
- student can enroll to a one or many classes (not enough time, other more important stuff)
- student can cancel enrollment to a class (not enough time, other more important stuff)

- authentication (not enough time, added simple basic authentication):
    - each student has a unique hash that is used for authentication
    - each operation checks hash for user authenticity

Based on described features prepare a REST service(s) over which this operations can be done. Since your doing a service you don't need to bother with UI. The correctness of implementation can be shown with unit/integration tests or simple
Postman (www.getpostman.com) calls. Technologies: Spring, Spring Boot, Maven... how data is stored is up to you :)

### Task 2:

In the file (dataset) you can find 4 columns of data (MATCH_ID, MARKET_ID, OUTCOME_ID, SPECIFIERS) separated with pipe. Your task is to put this data into DB as fast as possible, but in ordered manner (asc) for single MATCH_ID starting with first column and so
on. Please add one more column - date_insert, which should signal the timestamp of insertion. If you order data within one MATCH_ID by 'date_insert', data should be ordered as specified. It's important to know that file mentioned below (dataset) is just a
snippet. In real environment this is represented as a steady data stream. Dataset can be found here: file Please provide min(date_insert) and max(date_insert). Please provide source code (and everything else needed) - use Java and relational database of your
choice.

### Task 3:

Q1: Can you describe your latest performance solving issue? What was the problem? How did you fix and how much time did you spent solving it?
There was no problem in Task 2, just had to find correct algorithem to implement this. Problem was with Task 1. Since I never did API server I quickly learned it online: https://spring.io/guides/tutorials/rest/

Q2: How do you manage conflicts in web applications when different user are managing and working on data concurrently?
Would have to Google that.

Q3: Can you describe your idea of design (architecture) on how to make a big scale e-commerce website with at least 100K concurrent users.
Well, you would need a big team, a lot of time and quite some effort. you would need own servers, with huge bandwidth and storage capacity.
