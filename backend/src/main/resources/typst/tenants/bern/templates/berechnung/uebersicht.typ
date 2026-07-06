#import "/tenants/bern/constants.typ"
#import "/tenants/bern/components/icons.typ"
#import "/shared/utils/helper.typ": *
#import "/shared/utils/format.typ"
#import "/tenants/bern/components/card.typ": card
#import "/tenants/bern/components/table.typ"

#let render(data, t) = {
  let payload = safe-get(data, "payload")

  heading(level: 1)[#t("berechnung.uebersicht.resultat")]

  v(constants.layout.spacing.small)

  let granted = safe-get(payload, "berechnungStipendium") > 0
  let hasDarlehen = safe-get(payload, "berechnungDarlehen") != none
  let hasKuerzung = safe-get(payload, "anzahlMonateEinreichefrist") > 0
  let hasUnterbruch = safe-get(payload, "anzahlMonateUnterbruch") > 0

  let color = if granted { constants.colors.success } else {
    constants.colors.error
  }
  let icon = if granted { icons.check() } else { icons.cancel() }
  let result-msg = if granted {
    t("berechnung.uebersicht.gewaehrt")
  } else {
    t("berechnung.uebersicht.abgelehnt")
  }

  card(inset: constants.layout.spacing.big, stroke: color)[
    #grid(
      columns: (auto, 1fr),
      column-gutter: constants.layout.spacing.small,
      align: (
        center + horizon,
        left + horizon,
      ),

      icon,

      text(
        size: constants.fonts.size.icons,
        weight: constants.fonts.weight.bold,
        fill: color,
        result-msg,
      ),
    )
  ]

  v(constants.layout.spacing.big)

  heading(level: 2)[#t("berechnung.uebersicht.total.heading")]

  let vorKuerzungUndTeilung = safe-get(
    payload,
    "berechnungVorKuerzungUndTeilung",
  )
  let kuerzung = if hasKuerzung {
    (
      vorKuerzungUndTeilung
        - safe-get(payload, "totalNachKuerzungNachEinreichefrist")
    )
  } else { 0 }
  let unterbruch = (
    vorKuerzungUndTeilung
      - kuerzung
      - safe-get(payload, "totalNachKuerzungUnterbruch", default: 0)
  )

  let totalSubCells = ()

  if hasKuerzung or hasUnterbruch {
    totalSubCells += (
      table.sub-entry(
        t("berechnung.uebersicht.total.ohneKuerzung"),
        format.chf(vorKuerzungUndTeilung),
      ),
    )
  }

  if hasKuerzung {
    totalSubCells += (
      table.sub-entry(
        t("berechnung.uebersicht.total.kuerzung"),
        format.chf(kuerzung, prefix: "negative"),
      ),
    )
  }

  if hasUnterbruch {
    totalSubCells += (
      table.sub-entry(
        "Kürzung wegen Unterbruch",
        format.chf(unterbruch, prefix: "negative"),
      ),
    )
  }

  let totalCells = (
    table.entry(
      t("berechnung.uebersicht.total.betrag"),
      format.chf(safe-get(payload, "berechnungVorTeilungDarlehen")),
      bold: true,
      line: constants.colors.border-dominant,
      sub-table: totalSubCells,
    ),
  )

  if hasDarlehen {
    totalCells += (
      table.entry(
        t("berechnung.uebersicht.total.stipendium"),
        format.chf(safe-get(payload, "berechnungStipendium")),
        bold: true,
      ),
      table.entry(
        t("berechnung.uebersicht.total.darlehen"),
        format.chf(safe-get(payload, "berechnungDarlehen")),
        bold: true,
      ),
    )
  }

  table.einnahmen-kosten(
    inset: constants.layout.spacing.big,
    cell-inset: constants.layout.spacing.semi-big,
    ..totalCells,
  )

  v(constants.layout.spacing.big)

  heading(level: 2)[#t("berechnung.uebersicht.berechnungen.heading")]

  pagebreak(weak: true)
}
