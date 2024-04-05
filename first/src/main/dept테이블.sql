drop table dept cascade constraints;

create table dept (
   deptno varchar2(100) primary key,
   dept varchar2(100),
   loc  varchar2(100)
);

insert into dept values('101', '개발부', '인천');
insert into dept values('102', '홍보부', '서울');
insert into dept values('103', '연구부', '부산');

commit;

select * from dept;
select deptno, dept, loc from dept;