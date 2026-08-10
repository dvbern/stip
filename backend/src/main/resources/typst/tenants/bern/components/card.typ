#import "/tenants/bern/constants.typ"

#let card(
  fill: constants.colors.bg,
  radius: constants.layout.radius.small,
  inset: constants.layout.spacing.base,
  width: 100%,
  stroke: none,
  body,
) = {
  block(
    fill: fill,
    radius: radius,
    width: width,
    inset: inset,
    stroke: stroke,
    body,
  )
}
