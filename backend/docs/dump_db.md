# Dump the DB
Using the `pg_dump` util it's as easy as

    pg_dump $STIP_DB_URL --column-inserts -a

Adding one or more `-t table_to_dump` to the command.  
Using the OC cli you can

    oc exec postgres-debug -- pg_dump $STIP_DB_URL

And redirect that to a .sql file.

Following are a few premade commands to dump common collections of tables:

## Dump all Gesuche
    pg_dump $STIP_DB_URL --column-inserts -a -t gesuch -t gesuch_tranche -t gesuch_formular -t person_in_ausbildung -t ausbildung -t familiensituation -t partner -t auszahlung -t einnahmen_kosten -t lebenslauf_item -t geschwister -t eltern -t kind -t steuerdaten