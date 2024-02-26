UPDATE gesuch SET gesuch_status = 'IN_BEARBEITUNG_GS' WHERE gesuch_status = 'OFFEN';
UPDATE gesuch SET gesuch_status = 'IN_BEARBEITUNG_GS' WHERE gesuch_status = 'IN_BEARBEITUNG';
UPDATE gesuch SET gesuch_status = 'KOMPLETT_EINGEREICHT' WHERE gesuch_status = 'EINGEREICHT';
UPDATE gesuch SET gesuch_status = 'KOMPLETT_EINGEREICHT' WHERE gesuch_status = 'NICHT_KOMPLETT_EINGEREICHT';
UPDATE gesuch SET gesuch_status = 'KOMPLETT_EINGEREICHT' WHERE gesuch_status = 'NICHT_KOMPLETT_EINGEREICHT_NACHFRIST';
