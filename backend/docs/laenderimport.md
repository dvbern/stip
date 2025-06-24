# Länderimport

Wir importieren (einmalig) ein CSV für die Länderliste. Das CSV muss manuell erstellt werden, anhand der Liste der Länder welche vom BKD/ BFS uns gegeben wird. Wichtig sind die folgenden Spalten in der Folgenden Reihenfolge:

| Ländercode BFS | ISO2 | ISO3 | DE Kurzform | FR Kurzform | IT Kurzform | EN Kurzform | Eintrag gültig | EU/EFTA |
|----------------|------|------|-------------|-------------|-------------|-------------|----------------|---------|
| 8100           | CH   | CHE  | Schweiz     | Suisse      | Svizzera    | Switzerland | J              | J       |

Die Namen der Spalten ist egal, wichtig ist aber dass, sie keine Semikolons (`;`) enthalten und nur eine Zeile sind. Am besten enthalten sie auch keine Umlaute.

Zusätzlich wichtig ist, das in den Spalten `Eintrag Gültig` und `EU/EFTA` entweder `J` oder `N` steht, gross/ kleinschreibung muss stimmen.

Am besten eine Kopie vom Excel erstellen, wo dann alle nicht nötigen Spalten gelöscht werden, und die headers umbenennen.