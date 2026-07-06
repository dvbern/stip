#import "/shared/utils/helper.typ": *
#import "/shared/utils/format.typ"
#import "/shared/components/row.typ"
#import "/tenants/bern/components/badge.typ"
#import "/tenants/bern/components/table.typ"
#import "/tenants/bern/constants.typ"

#let render(data, t) = {
  let payload = safe-get(
    data,
    "payload",
  )
  let stammdaten = safe-get(
    payload,
    "berechnungsStammdaten",
  )
  let budget = safe-get(
    payload,
    "budget",
  )

  let steuerdaten-typ = safe-get(budget, "steuerdatenTyp")

  heading(level: 1, t("berechnung." + lower(steuerdaten-typ)))

  v(constants.layout.spacing.base)

  let badge-rows = (
    (
      badge.badge([#safe-get(budget, "vorname") #safe-get(
          budget,
          "nachname",
        )]),
      badge.badge(t("common.svNr"), value: safe-get(
        budget,
        "sozialversicherungsnummer",
      )),
      badge.badge(t("common.birthday"), value: display-date(safe-get(
        budget,
        "geburtsdatum",
      ))),
    ),
  )

  if safe-get(budget, "vornamePartner") != none {
    badge-rows.push((
      badge.badge([#safe-get(budget, "vornamePartner") #safe-get(
          budget,
          "nachnamePartner",
        )]),
      badge.badge(t("common.svNr"), value: safe-get(
        budget,
        "sozialversicherungsnummerPartner",
      )),
      badge.badge(t("common.birthday"), value: display-date(safe-get(
        budget,
        "geburtsdatumPartner",
      ))),
    ))
  }

  badge-rows.push((
    badge.badge(t("common.education-year"), value: safe-get(
      payload,
      "yearRange",
    )),
    badge.badge(t("common.from"), value: display-date(
      safe-get(
        payload,
        "gueltigAb",
      ),
      format: "[month].[year]",
    )),
    badge.badge(t("common.till"), value: display-date(
      safe-get(
        payload,
        "gueltigBis",
      ),
      format: "[month].[year]",
    )),
    badge.badge(t("common.months"), value: safe-get(
      payload,
      "berechnungsStammdaten.anzahlMonate",
    )),
  ))

  row.rows(..badge-rows)

  v(constants.layout.spacing.base)

  let einnahmen = safe-get(
    budget,
    "einnahmen",
  )

  table.einnahmen-kosten(
    table.entry(
      t("berechnung.einnahmen.label"),
      format.chf(safe-get(einnahmen, "total")),
      info: t("berechnung.einnahmen.info"),
      bold: true,
      line: constants.colors.border-dominant,
    ),
    {
      let prefix = "berechnung.einnahmen.totalEinkuenfte."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(einnahmen, "totalEinkuenfte"), prefix: "positive"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.einnahmen.einnahmenBGSA."

      table.entry(
        table.with-note(
          t(prefix + "label"),
          t("berechnung.notes.bgsa.identifier"),
        ),
        format.chf(safe-get(einnahmen, "einnahmenBGSA"), prefix: "positive"),
      )
    },
    {
      let prefix = "berechnung.einnahmen.ergaenzungsleistungen."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "ergaenzungsleistungen"),
          prefix: "positive",
        ),
      )
    },
    {
      let prefix = "berechnung.einnahmen.andereEinnahmen."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(einnahmen, "andereEinnahmen"), prefix: "positive"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.einnahmen.eigenmietwert."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(einnahmen, "eigenmietwert"), prefix: "negative"),
      )
    },
    {
      let prefix = "berechnung.einnahmen.unterhaltsbeitraegeAbzug."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "unterhaltsbeitraege"),
          prefix: "negative",
        ),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.einnahmen.sauele3."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(einnahmen, "sauele3"), prefix: "negative"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.einnahmen.sauele2."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(einnahmen, "sauele2"), prefix: "negative"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.einnahmen.rentenAbzug."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(einnahmen, "renten"), prefix: "negative"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.einnahmen.einkommensfreibetragAbzug."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "einkommensfreibetrag"),
          prefix: "negative",
        ),
        info: t(prefix + "info", einkommensfreibetrag: safe-get(
          stammdaten,
          "einkommensfreibetrag",
        )),
        line: constants.colors.border-dominant,
      )
    },
    {
      let prefix = "berechnung.einnahmen.zwischentotal."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(einnahmen, "zwischentotal")),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.einnahmen.anrechenbaresVermoegen."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "anrechenbaresVermoegen"),
          prefix: "positive",
        ),
        info: t(prefix + "info", steuerbaresVermoegen: safe-get(
          einnahmen,
          "steuerbaresVermoegen",
        )),
      )
    },
    table.entry(
      t("berechnung.einnahmen.info"),
      format.chf(safe-get(einnahmen, "total")),
      bold: true,
    ),
  )

  pagebreak(weak: true)

  let kosten = safe-get(
    budget,
    "kosten",
  )

  table.einnahmen-kosten(
    table.entry(
      t("berechnung.kosten.label"),
      format.chf(safe-get(kosten, "total")),
      info: t("berechnung.kosten.info"),
      bold: true,
      line: constants.colors.border-dominant,
    ),
    {
      let prefix = "berechnung.kosten.grundbedarf."

      table.entry(
        table.with-note(
          t(prefix + "label", anzahlPersonenImHaushalt: safe-get(
            budget,
            "anzahlPersonenImHaushalt",
          )),
          t("berechnung.notes.hoechstwerte.identifier"),
        ),
        format.chf(
          safe-get(kosten, "grundbedarf"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.wohnkosten."

      table.entry(
        t(prefix + "label", anzahlPersonenImHaushalt: safe-get(
          budget,
          "anzahlPersonenImHaushalt",
        )),
        format.chf(
          safe-get(kosten, "wohnkosten"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.medizinischeGrundversorgung."

      table.entry(
        table.with-note(
          t(prefix + "label", anzahlPersonenImHaushalt: safe-get(
            budget,
            "anzahlPersonenImHaushalt",
          )),
          t("berechnung.notes.hoechstwerte.identifier"),
        ),
        format.chf(
          safe-get(kosten, "medizinischeGrundversorgung"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.integrationszulage."

      table.entry(
        table.with-note(
          t(prefix + "label", inAusbildungStehendeKinder: safe-get(
            kosten,
            "integrationszulageAnzahl",
          )),
          t("berechnung.notes.hoechstwerte.identifier"),
        ),
        format.chf(
          safe-get(kosten, "integrationszulageTotal"),
          prefix: "positive",
        ),
        info: t(prefix + "info", abzugsLimite: format.chf(safe-get(
          stammdaten,
          "abzugslimite",
        ))),
      )
    },
    {
      let prefix = "berechnung.kosten.kantonsGemeindesteuern."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(kosten, "kantonsGemeindesteuern"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.bundessteuern."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(kosten, "bundessteuern"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.fahrkostenFam."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(kosten, "fahrkostenTotal"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
        sub-table: (
          safe-get(kosten, "fahrkosten", default: ()).map(
            person => table.sub-entry(safe-get(person, "vorname"), format.chf(
              safe-get(
                person,
                "value",
              ),
            )),
          )
        ),
      )
    },
    {
      let prefix = "berechnung.kosten.verpflegung."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(kosten, "verpflegungTotal"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
        sub-table: (
          safe-get(kosten, "verpflegung", default: ()).map(
            person => table.sub-entry(safe-get(person, "vorname"), format.chf(
              safe-get(
                person,
                "value",
              ),
            )),
          )
        ),
      )
    },
    table.entry(
      t("berechnung.kosten.info"),
      format.chf(safe-get(kosten, "total")),
      bold: true,
    ),
  )

  if safe-get(budget, "ungedeckterAnteilLebenshaltungskosten") != 0 {
    table.einnahmen-kosten(
      {
        let prefix = "berechnung.total.fehlbetrag."
        let proKopfTeilung = safe-get(budget, "proKopfTeilung")

        table.entry(
          t(prefix + "label"),
          format.chf(safe-get(budget, "fehlbetrag"), prefix: "negative"),
          info: t(prefix + "info"),
          sub-table: if proKopfTeilung != none {
            (
              table.sub-entry(
                t(prefix + "proKopfTeilung"),
                proKopfTeilung,
              ),
            )
          } else {
            ()
          },
        )
      },
      table.entry(
        t("berechnung.total.ungedeckterAnteilLebenshaltungskosten.label"),
        format.chf(safe-get(budget, "ungedeckterAnteilLebenshaltungskosten")),
        info: t("berechnung.total.ungedeckterAnteilLebenshaltungskosten.info"),
      ),
    )
  } else {
    table.einnahmen-kosten(
      {
        let prefix = "berechnung.total.einnahmeUeberschuss."
        let proKopfTeilung = safe-get(budget, "proKopfTeilung")

        table.entry(
          t(prefix + "label"),
          format.chf(safe-get(budget, "fehlbetrag"), prefix: "negative"),
          info: t(prefix + "info"),
          sub-table: if proKopfTeilung != none {
            (
              table.sub-entry(
                t(prefix + "proKopfTeilung"),
                proKopfTeilung,
              ),
            )
          } else {
            ()
          },
        )
      },
      table.entry(
        t("berechnung.total.anrechenbareElterlicheLeistung.label"),
        format.chf(safe-get(budget, "anrechenbareElterlicheLeistung")),
        info: t("berechnung.total.anrechenbareElterlicheLeistung.info"),
      ),
    )
  }

  table.notes(
    table.note-entry(
      t("berechnung.notes.bgsa.identifier"),
      t("berechnung.notes.bgsa.text"),
    ),
    table.note-entry(
      t("berechnung.notes.hoechstwerte.identifier"),
      t("berechnung.notes.hoechstwerte.text"),
    ),
  )
}
