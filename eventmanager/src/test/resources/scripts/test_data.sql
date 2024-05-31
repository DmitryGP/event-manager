insert into owners(name, surname, email, phone_number)
values  ('A', 'AA', 'aaa@host.org', '+7 (111) 456-6787'),
        ('B', 'BB', 'bbb@host.org', '+7 (111) 123-5642');

insert into participants(name, surname, email, phone_number)
values  ('C', 'CC', 'ccc@host.org', '+7 (100) 654-1111'),
        ('D', 'DD', 'ddd@host.org', '+7 (200) 555-2222'),
        ('E', 'EE', 'eee@host.org', '+7 (300) 641-3333'),
        ('F', 'FF', 'fff@host.org', '+7 (500) 333-7777');

insert into events(name, description, owner_id, address, start_date, start_time, end_date, end_time, max_participants_count)
values  ('event1', 'super event', 1, 'address1', '2024-03-10', '10:00:00', '2024-03-11', '20:00:00', 3),
        ('event2', 'just event', 2, 'address2', '2025-04-10', '10:00:00', '2025-04-12', '20:00:00', 2),
        ('event3', 'event-event', 1, 'address3', '2025-05-10', '10:00:00', '2025-05-11', '20:00:00', 5);

insert into events_participants(events_id, participants_id)
values  (1, 1),
        (1, 2),
        (2, 2),
        (2, 3);
