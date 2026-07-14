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

  heading(level: 1, t("berechnung.persoenlich"))

  v(constants.layout.spacing.small)

  row.rows(
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
    (
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
    ),
  )

  v(constants.layout.spacing.small)

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
      let prefix = "berechnung.einnahmen.nettoerwerbseinkommen."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "nettoerwerbseinkommenTotal"),
          prefix: "positive",
        ),
        info: t(
          prefix + "info",
          einkommensfreibetrag: safe-get(stammdaten, "einkommensfreibetrag"),
        ),
        sub-table: (
          safe-get(einnahmen, "nettoerwerbseinkommen", default: ()).map(
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
      let prefix = "berechnung.einnahmen.einnahmenBGSA."

      table.entry(
        table.with-note(
          t(prefix + "label"),
          t("berechnung.notes.einnahmen-kosten.bgsa.identifier"),
        ),
        format.chf(
          safe-get(einnahmen, "einnahmenBGSATotal"),
          prefix: "positive",
        ),
        sub-table: (
          safe-get(einnahmen, "einnahmenBGSA", default: ()).map(
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
      let prefix = "berechnung.einnahmen.kinderAusbildungszulagen."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "kinderAusbildungszulagenTotal"),
          prefix: "positive",
        ),
        sub-table: (
          safe-get(einnahmen, "kinderAusbildungszulagen", default: ()).map(
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
      let prefix = "berechnung.einnahmen.unterhaltsbeitraege."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "unterhaltsbeitraegeTotal"),
          prefix: "positive",
        ),
        sub-table: (
          safe-get(einnahmen, "unterhaltsbeitraege", default: ()).map(
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
      let prefix = "berechnung.einnahmen.eoLeistungen."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "eoLeistungenTotal"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
        sub-table: (
          safe-get(einnahmen, "eoLeistungen", default: ()).map(
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
      let prefix = "berechnung.einnahmen.taggelderAHVIV."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "taggelderAHVIVTotal"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
        sub-table: (
          safe-get(einnahmen, "taggelderAHVIV", default: ()).map(
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
      let prefix = "berechnung.einnahmen.renten."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(einnahmen, "rentenTotal"), prefix: "positive"),
        info: t(prefix + "info"),
        sub-table: (
          safe-get(einnahmen, "renten", default: ()).map(
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
      let prefix = "berechnung.einnahmen.ergaenzungsleistungen."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "ergaenzungsleistungenTotal"),
          prefix: "positive",
        ),
        sub-table: (
          safe-get(einnahmen, "ergaenzungsleistungen", default: ()).map(
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
      let prefix = "berechnung.einnahmen.beitraegeGemeindeInstitutionen."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "beitraegeGemeindeInstitutionen"),
          prefix: "positive",
        ),
      )
    },
    {
      let prefix = "berechnung.einnahmen.andereEinnahmen."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "andereEinnahmenTotal"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
        sub-table: (
          safe-get(einnahmen, "andereEinnahmen", default: ()).map(
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
    {
      let prefix = "berechnung.einnahmen.elterlicheLeistung."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "elterlicheLeistung"),
          prefix: "positive",
        ),
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
      let prefix = "berechnung.kosten.ausbildungskosten."

      table.entry(
        table.with-note(
          t(prefix + "label"),
          t("berechnung.notes.einnahmen-kosten.hoechstwerte.identifier"),
        ),
        format.chf(
          safe-get(kosten, "ausbildungskostenTotal"),
          prefix: "positive",
        ),
        info: t(
          prefix + "info",
          ausbildungskosten: safe-get(kosten, "ausbildungskosten"),
          anzahlPersonenImHaushalt: safe-get(
            budget,
            "anzahlPersonenImHaushalt",
          ),
        ),
      )
    },
    {
      let prefix = "berechnung.kosten.fahrkosten."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(kosten, "fahrkostenTotal"), prefix: "positive"),
        info: t(
          prefix + "info",
          fahrkosten: safe-get(kosten, "fahrkosten"),
          anzahlPersonenImHaushalt: safe-get(
            budget,
            "anzahlPersonenImHaushalt",
          ),
        ),
      )
    },
    {
      let prefix = "berechnung.kosten.verpflegungskosten."

      table.entry(
        table.with-note(
          t(prefix + "label"),
          t("berechnung.notes.einnahmen-kosten.hoechstwerte.identifier"),
        ),
        format.chf(safe-get(kosten, "verpflegungskosten"), prefix: "positive"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.grundbedarf."

      table.entry(
        table.with-note(
          t(prefix + "label", anzahlPersonenImHaushalt: safe-get(
            budget,
            "anzahlPersonenImHaushalt",
          )),
          t("berechnung.notes.einnahmen-kosten.hoechstwerte.identifier"),
        ),
        format.chf(safe-get(kosten, "grundbedarf"), prefix: "positive"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.wohnkosten."

      table.entry(
        table.with-note(
          t(prefix + "label", anzahlPersonenImHaushalt: safe-get(
            budget,
            "anzahlPersonenImHaushalt",
          )),
          t("berechnung.notes.einnahmen-kosten.hoechstwerte.identifier"),
        ),
        format.chf(safe-get(kosten, "wohnkosten"), prefix: "positive"),
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
          t("berechnung.notes.einnahmen-kosten.hoechstwerte.identifier"),
        ),
        format.chf(
          safe-get(kosten, "medizinischeGrundversorgungTotal"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
        sub-table: (
          safe-get(
            einnahmen,
            "medizinischeGrundversorgung",
            default: (),
          ).map(
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
      let prefix = "berechnung.kosten.fahrkostenPartner."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(kosten, "fahrkostenPartner"), prefix: "positive"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.verpflegungPartner."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(kosten, "verpflegungPartner"), prefix: "positive"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.betreuungskostenKinder."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(kosten, "betreuungskostenKinder"),
          prefix: "positive",
        ),
      )
    },
    {
      let prefix = "berechnung.kosten.steuern."

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(kosten, "steuern"), prefix: "positive"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.anteilLebenshaltungskosten."

      table.entry(
        t(prefix + "label"),
        format.chf(
          safe-get(kosten, "anteilLebenshaltungskosten"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
      )
    },
    table.entry(
      t("berechnung.kosten.info"),
      format.chf(safe-get(kosten, "total")),
      bold: true,
    ),
  )

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
      t("berechnung.total.anspruch.label", anzahlMonate: safe-get(
        payload,
        "berechnungsStammdaten.anzahlMonate",
      )),
      format.chf(safe-get(budget, "total")),
      bold: true,
    ),
  )

  table.notes(
    table.note-entry(
      t("berechnung.notes.einnahmen-kosten.bgsa.identifier"),
      t("berechnung.notes.einnahmen-kosten.bgsa.text"),
    ),
    table.note-entry(
      t("berechnung.notes.einnahmen-kosten.hoechstwerte.identifier"),
      t("berechnung.notes.einnahmen-kosten.hoechstwerte.text"),
    ),
  )
}
