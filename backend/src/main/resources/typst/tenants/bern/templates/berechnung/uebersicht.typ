#import "/tenants/bern/constants.typ"
#import "/tenants/bern/components/icons.typ"
#import "/shared/utils/helper.typ": *
#import "/shared/utils/format.typ"
#import "/tenants/bern/components/card.typ": card
#import "/tenants/bern/components/table.typ"

#let group-tranchen(tranchen) = {
  let grouped = (:)

  for tranche in tranchen {
    let t-id = tranche.at("gesuchTrancheId")

    if t-id not in grouped {
      grouped.insert(t-id, (
        gesuchTrancheId: t-id,
        startDate: safe-get(tranche, "gueltigAb"),
        endDate: safe-get(tranche, "gueltigBis"),
        anzahlMonate: safe-get(tranche, "berechnungsStammdaten.anzahlMonate"),
        total: 0,
        berechnungen: (),
      ))
    }

    let current-group = grouped.at(t-id)

    current-group.total += safe-get(tranche, "total", default: 0)

    let anteil-kinder = safe-get(
      tranche,
      "berechnungsanteilKinder",
      default: 100,
    )
    let anteil-pia = safe-get(
      tranche,
      "berechnungsanteilKinderPia",
      default: 100,
    )
    let anteil-total = calc.round((anteil-kinder * anteil-pia) / 100, digits: 2)

    let tranche-copy = tranche
    tranche-copy.insert("berechnungsanteilTotal", anteil-total)
    current-group.berechnungen.push(tranche-copy)

    grouped.insert(t-id, current-group)
  }

  return grouped
}

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
  let noteCells = ()

  if hasKuerzung or hasUnterbruch {
    totalSubCells += (
      table.sub-entry(
        t("berechnung.uebersicht.total.ohneKuerzung"),
        format.chf(vorKuerzungUndTeilung),
        font-size: "normal",
      ),
    )
  }

  if hasKuerzung {
    let identifier = t("berechnung.notes.uebersicht.kuerzung.identifier")

    totalSubCells += (
      table.sub-entry(
        table.with-note(t("berechnung.uebersicht.total.kuerzung"), identifier),
        format.chf(kuerzung, prefix: "negative"),
        font-size: "normal",
      ),
    )
    noteCells += (
      table.note-entry(
        identifier,
        t(
          "berechnung.notes.uebersicht.kuerzung.text",
          anzahlMonate: safe-get(payload, "anzahlMonateEinreichefrist"),
          betrag: format.chf(kuerzung),
        ),
      ),
    )
  }

  if hasUnterbruch {
    let identifier = t("berechnung.notes.uebersicht.unterbruch.identifier")

    totalSubCells += (
      table.sub-entry(
        table.with-note(
          t("berechnung.uebersicht.total.unterbruch"),
          identifier,
        ),
        format.chf(unterbruch, prefix: "negative"),
        font-size: "normal",
      ),
    )
    noteCells += (
      table.note-entry(
        identifier,
        t(
          "berechnung.notes.uebersicht.unterbruch.text",
          anzahlMonate: safe-get(payload, "anzahlMonateUnterbruch"),
          betrag: format.chf(unterbruch),
        ),
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

  if hasKuerzung or hasUnterbruch {
    table.notes(
      ..noteCells,
    )
  }

  v(constants.layout.spacing.big)

  heading(level: 2)[#t("berechnung.uebersicht.berechnungen.heading")]

  let tranchen = safe-get(payload, "tranchenBerechnungsresultate", default: ())

  let grouped-tranchen = group-tranchen(tranchen)

  for (_, group) in grouped-tranchen.pairs() {
    let anzahlMonate = str(group.anzahlMonate)
    let title = t(
      "berechnung.uebersicht.berechnungen.tranche.heading",
      anzahlMonate: anzahlMonate,
    )

    let start = display-date(group.startDate, format: "[month].[year]")
    let end = display-date(group.endDate, format: "[month].[year]")
    let date-range = "(" + start + " - " + end + ")"

    let mapped-berechnungen = group
      .berechnungen
      .enumerate()
      .map(((i, b)) => (
        label: t(
          "berechnung.uebersicht.berechnungen.tranche.berechnung-index",
          index: i + 1,
        ),
        percentage-text: t(
          "berechnung.uebersicht.berechnungen.tranche.percentage-von",
          percentage: str(b.berechnungsanteilTotal),
          base: format.chf(b.ungekuerztTotal),
        ),
        amount: format.chf(b.total, prefix: "positive"),
      ))

    table.tranche-group(
      title,
      date-range,
      format.chf(group.total),
      mapped-berechnungen,
    )
    v(constants.layout.spacing.base)
  }

  pagebreak(weak: true)
}
