
DROP TABLE IF EXISTS  employee;
CREATE TABLE employee(
id integer primary  key,
name varchar(50),
age smallint default 18,
degree varchar(50),
email varchar(100),
entryDate char(8),
gender char(1)
);