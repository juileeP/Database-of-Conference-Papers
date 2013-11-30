use jpata;
drop table Author;
drop table Conference_Paper;
drop table Member;
drop table Administrator;
drop table citedBy;
drop table accessedBy;
drop table publishedBy;

create table Author
 (
 authorID numeric(4,0),
 authorName varchar(30),
 institution varchar(60),
 email_addr varchar(40),
 primary key (authorID)
 );
create table Conference_Paper
(
 Title varchar(900),
 category varchar(100),
 conference_name varchar(80),
 abstract varchar(2000),
 publication_date Date,
 authorID numeric(4,0) not null,
 authorName varchar(20),
 locationOfConf varchar(40),
 citation_count numeric(4,0),
 Primary key (Title),
 Foreign key (authorID) references Author
 );
 create table Member
 (
 memberID numeric(4,0),
 name varchar(40),
 institution varchar(60),
 email_addr varchar(40),
 category_viewed varchar(400),
 primary key (memberID)
 );
 create table Administrator
 (
 adminID numeric(5,0),
 name varchar(40),
 email_addr varchar(40),
 primary key (adminID)
 );
 create table citedBy
 (
 memberID numeric(4,0) not null,
 Title varchar(900),
 primary key (memberID),
 foreign key (memberID) references Member on delete cascade,
 foreign key (Title) references Conference_Paper on delete cascade
 );
 create table accessedBy
 (
 memberID numeric(4,0) not null,
 category varchar(100),
 primary key (memberID),
 foreign key (memberID) references member on delete cascade
 );
 create table publishedBy
 (
 authorID numeric(4,0) not null,
 Title varchar(900),
 Primary key (authorID),
 foreign key (authorID) references Author on delete cascade,
 foreign key (Title) references Conference_Paper on delete cascade
 );

