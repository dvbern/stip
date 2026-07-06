#import "/tenants/bern/constants.typ"

#let badge(
  key,
  value: none,
  fill: constants.colors.bg,
  size: constants.fonts.size.small,
) = box(
  fill: fill,
  radius: constants.layout.radius.small,
  inset: constants.layout.spacing.small,
)[
  #text(size: size)[
    #text(weight: constants.fonts.weight.bold)[#key #if value != none [:]]
    #if value != none [ #value]
  ]
]
