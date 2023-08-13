insert into team (name, lead_id, deleted)
values ('Flexies', null, false);

insert into team (name, lead_id, deleted)
values ('Partizans', null, false);

insert into team (name, lead_id, deleted)
values ('NovaS Team', null, false);

insert into team (name, lead_id, deleted)
values ('F1 Ferrari', null, false);

insert into project (client, description, start_date, end_date, status, title, team_id, deleted)
values ('Mathias Lessort', 'Best small project description', '2023-01-14', '2023-09-14', 1, 'Flexy App', 1, false);

insert into project (client, description, start_date, end_date, status, title, team_id, deleted)
values ('Aleksa Avramovic', 'Cute little description :)', '2023-09-01', '2024-01-15', 0, 'Partizan App', 2, false);

insert into project (client, description, start_date, end_date, status, title, team_id, deleted)
values ('Ivan Ivanovic', 'Description of project', '2023-11-23', '2024-09-01', 0, 'Vece sa Ivanom', 3, false);

insert into project (client, description, start_date, end_date, status, title, team_id, deleted)
values ('Charles Leclerc', 'Ferrari project for F1', '2023-08-21', '2024-08-21', 1, 'Ferrari SF23 F1', 4, false);

insert into users (address, email, last_name, name)
values ('Vuka Kadardzica 1, Loznica', 'jovanjovic@teamup.com', 'Jovic', 'Jovan');

insert into users (address, email, last_name, name)
values ('Pasiceva 2, Novi Sad', 'milutintesla@teamup.com', 'Tesla', 'Milutin');

insert into users (address, email, last_name, name)
values ('Dunavska 23, Novi Sad', 'ivanjosic@teamup.com', 'Josic', 'Ivan');

insert into users (address, email, last_name, name)
values ('Jevrejska 23, Novi Sad', 'stefanmarkovic@teamup.com', 'Markovic', 'Stefan');

insert into users (address, email, last_name, name)
values ('Bulvear oslobodjenja 1, Novi Sad', 'markostanic@teamup.com', 'Stanic', 'Marko');

insert into employee (position, seniority, team_lead, id, team_id)
values ('React Developer', 0, true, 1, 1);

insert into employee (position, seniority, team_lead, id, team_id)
values ('Java Backend Developer', 1, true, 2, 2);

insert into employee (position, seniority, team_lead, id, team_id)
values ('Backend Developer', 2, true, 3, 3);

insert into employee (position, seniority, team_lead, id, team_id)
values ('.NET Backend Developer', 1, true, 4, 4);

insert into employee (position, seniority, team_lead, id, team_id)
values ('Angular Developer', 1, false, 5, null);

insert into employee_past_projects
values (1, 1);

insert into employee_past_projects
values (2, 2);

insert into employee_past_projects
values (3, 3);

insert into employee_past_projects
values (4, 4);

update team
set lead_id = 1
where id = 1;

update team
set lead_id = 2
where id = 2;

update team
set lead_id = 3
where id = 3;

update team
set lead_id = 4
where id = 4;