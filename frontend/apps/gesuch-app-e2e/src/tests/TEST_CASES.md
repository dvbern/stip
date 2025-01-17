## 1. Testcase: Happy Path

Ein Gesuch im Happy Path erstellen und bis in den Status "Verfügt" begleiten, inklusive der Interaktionen notwendig durch den Sachbearbeiter.

- Gesuch erstellen
- Einreichen
- Gesuch im SB oeffnen via Url
- Statuswechsel auf In_Bearbeitung
- Gesuchdokumente akzeptieren
- Statuswechsel bis auf Verfuegt
- Auf Tab Verfuegung die Werte kontrollieren, stimmt die Berechnung noch?
- Sind im Protokoll alle eintraege vohanden.

## 2. Testcase Tranchen

Auf einem bestehenden Gesuch wird durch den SB eine Tranche erstellt und wir testen, ob diese korrekt implementiert ist. Der Sachbearbeiter führt Statuswechsel aus und es wird getestet, ob diese richtig erfolgen.

- Testcase gesuch via api setzen
- Gesuch einreichen.
- Gesuch im SB oeffnen via Url
- Statuswechsel auf In_Bearbeitung
- Eine Tranche erstellen. Inkl. werten im Dialog. Dabei das Datum beachten
- Dropdown im SB header oeffnen und verifizieren, dass die Tranche vorhanden ist.
- Zur tranche navigieren

## 3. Testcase Änderungen

Aufbauend auf einem validen Gesuch erfasst der GS einen Änderungsantrag. Wir testen. ob der Sachbearbeiter diese korrekt sieht.

- Testcase gesuch via api setzen
- Gesuch einreichen.
- Gesuch im SB oeffnen via Url
- Statuswechsel auf In_Bearbeitung
- Gesuchdokumente akzeptieren
- Statuswechsel bis auf Verfuegt
- GS erfasst eine Aenderung.
- Dialog auffuellen.
- Aenderung bearbeiten. Wir machen 2 Changes: Eine einfache Prop, eine Array Prop (Mutter oder Vater)
- Aenderung Freigenben.
- GEsuch im SB oeffnen via Url
- Via DD Aenderung oeffnen
- Pruefen ob die Aenderungen richtig angezeigt werden.
- Bei der einfachen Prop erfasst der SB einen Change.
- Nach speichern dieser sollte der GS change nicht mehr erscheinen, sondern der neue Change vom SB.
- Akzeptieren der Aenderung und danach schauen ob eine neue Tranche erstellt wurde.

## 4. Testcase Hppy mit geteilterObhut... nur GS (hier noch weitere cases suchen f. GS, um Tab funktionaliaeten zu Testen)

- Gesuch erstellen
- Einreichen

## 5. Testcase Dokumente Ablehnen..

Es soll die interaktion mit dokumenten getestet werden. Wird beispielsweise beim Ablehenen der GS informiert, erscheint die Benachrichtigung auf dem GS Cockpit.

- Testcase gesuch via api setzen
- Dokumente hochladen
- Einreichen
- Gesuch im SB oeffnen via Url
- Statuswechsel auf In_Bearbeitung
- Gesuchdokumente ablehnen
- Gesuch im GS wieder oeffnen,
- Kontrollieren, ob eine Benachrichtigung vorhanden ist.
- dokumente die abgelehnt wurden wieder hochladen
- Gesuch erneut einreichen
- Gesuch im SB oeffnen via Url
- Statuswechsel auf In_Bearbeitung
- Gesuchdokumente akzeptieren
- Statuswechsel bis auf Verfuegt
- Sind im Protokoll alle eintraege vohanden.

## 6. Testcase geteilte Obhut mit aenderung, aufbauend auf nr 3. und 4.

- Testcase gesuch via api setzen, mit geteilter Obhut
- Gesuch einreichen.
- Gesuch im SB oeffnen via Url
- Statuswechsel auf In_Bearbeitung
- Gesuchdokumente akzeptieren
- Statuswechsel bis auf Verfuegt
- GS erfasst eine Aenderung.
- Dialog auffuellen.
- Aenderung bearbeiten. Wir machen 2 Changes: Eine einfache Prop, eine Array Prop (Mutter oder Vater)
- Aenderung Freigenben.
- GEsuch im SB oeffnen via Url
- Via DD Aenderung oeffnen
- Pruefen ob die Aenderungen richtig angezeigt werden.
- Bei der einfachen Prop erfasst der SB einen Change.
- Nach speichern dieser sollte der GS change nicht mehr erscheinen, sondern der neue Change vom SB.
- Akzeptieren der Aenderung und danach schauen ob eine neue Tranche erstellt wurde.

## 7. Testcase SB Admin Funktionen
