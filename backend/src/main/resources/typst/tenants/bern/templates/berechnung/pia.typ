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

  heading(level: 1, text(
    size: constants.fonts.size.big,
    weight: constants.fonts.weight.bold,
    t("berechnung.persoenlich"),
  ))

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
      badge.badge(t("common.birthday"), value: safe-get(
        budget,
        "geburtsdatum",
      )),
    ),
    (
      badge.badge(t("common.education-year"), value: safe-get(
        payload,
        "yearRange",
      )),

      badge.badge(t("common.from"), value: display-date(safe-get(
        payload,
        "gueltigAb",
      ))),

      badge.badge(t("common.till"), value: display-date(safe-get(
        payload,
        "gueltigBis",
      ))),

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

  table.rounded-bg(
    header: table.header(
      t("berechnung.einnahmen.label"),
      t("berechnung.einnahmen.info"),
      format.chf(safe-get(einnahmen, "total")),
    ),
    footer: table.footer(
      t("berechnung.einnahmen.info"),
      format.chf(safe-get(einnahmen, "total")),
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
        persons: (
          safe-get(einnahmen, "nettoerwerbseinkommen", default: ()).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
        t(prefix + "label"),
        format.chf(
          safe-get(einnahmen, "einnahmenBGSATotal"),
          prefix: "positive",
        ),
        info: t(
          prefix + "info",
        ),
        persons: (
          safe-get(einnahmen, "einnahmenBGSA", default: ()).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
        persons: (
          safe-get(einnahmen, "kinderAusbildungszulagen", default: ()).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
        persons: (
          safe-get(einnahmen, "unterhaltsbeitraege", default: ()).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
        persons: (
          safe-get(einnahmen, "eoLeistungen", default: ()).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
        persons: (
          safe-get(einnahmen, "taggelderAHVIV", default: ()).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
        persons: (
          safe-get(einnahmen, "renten", default: ()).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
        persons: (
          safe-get(einnahmen, "ergaenzungsleistungen", default: ()).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
        persons: (
          safe-get(einnahmen, "andereEinnahmen", default: ()).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
  )

  let kosten = safe-get(
    budget,
    "kosten",
  )

  table.rounded-bg(
    header: table.header(
      t("berechnung.kosten.label"),
      t("berechnung.kosten.info"),
      format.chf(safe-get(kosten, "total")),
    ),
    footer: table.footer(
      t("berechnung.kosten.info"),
      format.chf(safe-get(kosten, "total")),
    ),
    {
      let prefix = "berechnung.kosten.ausbildungskosten."

      table.entry(
        t(prefix + "label"),
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
        t(prefix + "label"),
        format.chf(safe-get(kosten, "verpflegungskosten"), prefix: "positive"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.grundbedarf."

      table.entry(
        t(prefix + "label", anzahlPersonenImHaushalt: safe-get(
          budget,
          "anzahlPersonenImHaushalt",
        )),
        format.chf(safe-get(kosten, "grundbedarf"), prefix: "positive"),
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
        format.chf(safe-get(kosten, "wohnkosten"), prefix: "positive"),
        info: t(prefix + "info"),
      )
    },
    {
      let prefix = "berechnung.kosten.medizinischeGrundversorgung."

      table.entry(
        t(prefix + "label", anzahlPersonenImHaushalt: safe-get(
          budget,
          "anzahlPersonenImHaushalt",
        )),
        format.chf(
          safe-get(kosten, "medizinischeGrundversorgungTotal"),
          prefix: "positive",
        ),
        info: t(prefix + "info"),
        persons: (
          safe-get(
            einnahmen,
            "medizinischeGrundversorgung",
            default: (),
          ).map(
            person => table.person(safe-get(person, "vorname"), format.chf(
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
  )

  table.rounded-bg(
    footer: table.footer(
      t("berechnung.total.label", anzahlMonate: safe-get(
        payload,
        "berechnungsStammdaten.anzahlMonate",
      )),
      format.chf(safe-get(budget, "total")),
    ),
    {
      let prefix = "berechnung.total.fehlbetrag."
      let proKopfTeilung = safe-get(budget, "proKopfTeilung")

      table.entry(
        t(prefix + "label"),
        format.chf(safe-get(budget, "fehlbetrag"), prefix: "negative"),
        info: t(prefix + "info"),
        persons: if proKopfTeilung != none {
          (
            table.person(
              t(prefix + "proKopfTeilung"),
              proKopfTeilung,
            ),
          )
        } else {
          ()
        },
      )
    },
  )
}
