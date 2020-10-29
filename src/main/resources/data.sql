insert into guest(id, name)
values (null, 'Roger Federer');
insert into guest(id, name)
values (null, 'Rafael Nadal');

insert into tennis_court(id, name)
values (null, 'Roland Garros - Court Philippe-Chatrier');

insert
into schedule
    (id, start_date_time, end_date_time, tennis_court_id)
values (null, '2020-12-20T14:00:00.0', '2020-12-20T15:00:00.0', 1);

insert
into schedule
    (id, start_date_time, end_date_time, tennis_court_id)
values (null, '2020-10-20T20:00:00.0', '2020-10-20T21:00:00.0', 1);

insert
into schedule
    (id, start_date_time, end_date_time, tennis_court_id)
values (null, '2020-11-21T20:00:00.0', '2020-12-21T21:00:00.0', 1);