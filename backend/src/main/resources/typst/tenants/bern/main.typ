#import "/shared/translation/engine.typ": make-i18n
#import "/tenants/bern/constants.typ"

#let data = json(bytes(sys.inputs.at("data")))

#let de = yaml("translations/de.yaml")
#let fr = yaml("translations/fr.yaml")

#let catalog = (
  de: de,
  fr: fr,
)

#let i18n = make-i18n(
  catalog,
  locale: data.lang,
)

#set page(paper: "a4", margin: (x: 2.5cm))

#set text(
  font: constants.fonts.family,
  size: constants.fonts.size.base,
  weight: constants.fonts.weight.regular,
  style: constants.fonts.style.normal,
  lang: data.lang,
  region: "CH",
)

#show link: underline

#import data.template: render

#render(data, i18n.t)
