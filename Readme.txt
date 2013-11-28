 A DATABASE OF CONFERENCE PAPERS
 
 Author: Juilee Patankar
 
 
 
 
 1. The database consisted of 5 tables - Conference_Paper, Member, Author, publishedBy and Adninistrator tables.
 2. Conference_Paper tables has fields like Title, category, conference_name, abstract, publication_date, authorID, 
    authorName, locationOfConf.
 3. Author table has fields - authorID, authorName, institution, email_addr.
 4. Member table has fields - memberID, name, institution, email_addr.
 5. Administrator table has fields - adminID, name, email_addr.
 6. publishedBy table has fields - authorID, title.
 
 7. When a user logs in as a member he has the following options :
    - He can search and view the papers based on the title of the papers in the database
    - He can search and view based on conference name 
    - He can also search and view based on the publishers in the database, who have contributed papers
 8. When a user logs in as an author he can do the following operations :
    - An author can add a paper to the database
    - An author can modify the paper he has previously added (the paper is identified based on title of the paper).
      Author can only modify the abstract and the category of the paper, for further modifications, 
      the administrator should be contacted.
 9. For administrator login, there are the following privileges :
    - Display Members list 
    - Add a new member 
    - Delete a member 
    - Display Publishers list
    - Add a publisher 
    - Display Conference Papers list 
    - Add a Conference Paper 
    - Delete a Conference Paper 
    - Display publishedBy table 
    - Modify a member details 
    - Modify an author details 
    - Modify conference paper details

