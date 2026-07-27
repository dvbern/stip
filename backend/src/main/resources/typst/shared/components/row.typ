#let row(spacing: 1%, ..items) = stack(
  dir: ltr,
  spacing: spacing,
  ..items.pos(),
)

#let rows(spacing: 1%, ..lines) = stack(
  dir: ttb,
  spacing: spacing,
  ..lines.pos().map(line => row(..line)),
)
