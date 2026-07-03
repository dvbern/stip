#import "/shared/translation/engine.typ": make-i18n

#let data = json(bytes(sys.inputs.at("data", default: "{}")))

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

#set page(margin: (x: 2.5cm))

#import data.template: render

#render(data, i18n.t)
