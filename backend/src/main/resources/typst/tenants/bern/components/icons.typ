#import "/tenants/bern/constants.typ"

#let material-icon(
  name,
  size: 24pt,
  color: black,
  filled: false,
) = text(
  font: "Material Symbols Rounded",
  size: size,
  fill: color,
  weight: 400,
  features: ("liga",),
  variations: (
    FILL: if filled { 100 } else { 0 },
    opsz: 24,
  ),
)[#name]

#let cancel(
  size: constants.fonts.size.icons,
  color: constants.colors.error,
) = material-icon(
  "cancel",
  size: size,
  color: color,
  filled: true,
)

#let check(
  size: constants.fonts.size.icons,
  color: constants.colors.success,
) = material-icon(
  "check_circle",
  size: size,
  color: color,
  filled: true,
)
