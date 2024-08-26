# Dump the DB
Using the `pg_dump` util it's as easy as

    pg_dump $STIP_DB_URL --column-inserts -a

Adding one or more `-t table_to_dump` to the command.  
Using the OC cli you can

    oc exec postgres-debug -- pg_dump $STIP_DB_URL

And redirect that to a .sql file.

Following are a few premade commands to dump common collections of tables:

## Dump all Gesuche
    pg_dump --column-inserts -a -t gesuch -t gesuch_tranche -t gesuch_formular -t person_in_ausbildung -t ausbildung -t familiensituation -t partner -t auszahlung -t einnahmen_kosten -t lebenslauf_item -t geschwister -t eltern -t kind -t steuerdaten -t adresse -t fall -t benutzer -t benutzereinstellungen -t ausbildungsgang -t bildungskategorie -t gesuchsperiode -f gesuch_dump.sql postgresql://stip:stip@localhost:5432/stip
    
Potentially add the following columns as well:
- `ausbildungsgang`
- `bildungskategorie`

## Restoring a dump

Use the generated .sql file and modify it slightly:

- On Table `ausbildung` update the `ausbildungsgang_id` with a `SELECT id FROM ausbildungsgang WHERE ... LIMIT 1` with the correct where clause
- On Table `gesuch` update the `gesuchsperiode_id` field with `(SELECT gesuchsperiode.id FROM gesuchsperiode ORDER BY gesuchsperiode_start DESC LIMIT 1)`
- 
