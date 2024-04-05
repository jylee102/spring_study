drop table student cascade constraints;

create table STUDENT (
   name varchar2(100) primary key,
   age number,
   java  varchar2(100),
   orcle  varchar2(100)
);

insert into student values('이유나', 20, 'A+', 'C');
insert into student values('김현우', 23, 'B+', 'A');
insert into student values('이지원', 21, 'F', 'A+');

commit;

select * from student;
select name, age, java, orcle from student;