-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

insert into user_information (id, email, password, role)
values (1, 'admin@gmail.com', '$2a$10$w6GK9iJHHRCOBul54nCcu.tQ4AZ8Aajeo2AQoO6ZNOrEM8UTY8hje', 'admin'),
       (2, 'rcyangg@gmail.com', '$2a$10$eJtdW.cqqM.KUcKHfreebuwS74a4Q.p9vMtCfKKhTQxZC1whTAwuS', 'user');
alter sequence user_information_seq restart with 3;

-- insert into task (id, user_id, name, completed, create_date)
-- values (1, 2, 'task-1', false, '2024-09-30T00:00:00');
-- insert into task (id, user_id, name, completed, create_date)
-- values (2, 2, 'task-2', true, '2024-09-30T00:00:01');
-- alter sequence task_seq restart with 3;

insert into task (id, name, completed, create_date, email)
values (1, 'task-1', false, '2024-09-30T00:00:00', 'rcyangg@gmail.com');
insert into task (id, name, completed, create_date, email)
values (2, 'task-2', true, '2024-09-30T00:00:01', 'some@gmail.com');
alter sequence task_seq restart with 3;