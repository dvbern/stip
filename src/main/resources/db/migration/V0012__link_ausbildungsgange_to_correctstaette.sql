-- In Script V0010 wurden die ausbidlungsgaenge Maturität (dfbe424a-4d83-11ee-be56-0242ac120002) und Eidg. Handelsdiplom / EFZ ('ebc81ade-4d83-11ee-be56-0242ac120002')
-- mit den falschen ausbildungsstaetten verlinkt
update ausbildungsgang set ausbildungsstaette_id = 'b15d0b6a-4d76-11ee-be56-0242ac120002' where id = 'dfbe424a-4d83-11ee-be56-0242ac120002';
update ausbildungsgang set ausbildungsstaette_id = 'c84d0b6a-4d76-11ee-be56-0242ac120002' where id = 'ebc81ade-4d83-11ee-be56-0242ac120002';
