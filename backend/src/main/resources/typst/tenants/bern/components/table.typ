#import "/tenants/bern/constants.typ"
#import "/tenants/bern/components/card.typ": card

#let font(small, base, big, bold, italic, dim) = (
  small: small,
  base: base,
  big: big,
  bold: bold,
  italic: italic,
  dim: dim,
)

#let sub-entry(label, amount) = (
  label: label,
  amount: amount,
)

#let entry(
  label,
  amount,
  info: none,
  sub-table: (),
  line: auto,
  bold: false,
) = (
  label: label,
  amount: amount,
  info: info,
  sub-table: sub-table,
  line: line,
  bold: bold,
)

#let entry-rows(item) = (
  1 + if item.info != none { 1 } else { 0 } + item.sub-table.len()
)

#let einnahmen-kosten(
  fill: constants.colors.bg,
  line: constants.colors.border,
  line-dominant: constants.colors.border-dominant,
  radius: constants.layout.radius.small,
  inset: constants.layout.spacing.base,
  cell-inset: constants.layout.spacing.base,
  font: font(
    constants.fonts.size.small,
    constants.fonts.size.base,
    constants.fonts.size.big,
    constants.fonts.weight.bold,
    constants.fonts.style.italic,
    constants.colors.text-dim,
  ),
  ..children,
) = {
  let entries = children.pos()
  let cells = ()

  for (i, item) in entries.enumerate() {
    cells += (
      table.cell(
        rowspan: entry-rows(item),
        breakable: false,
        inset: 0pt,
      )[],
      table.cell()[
        #if item.bold {
          text(weight: font.bold)[#eval(item.label, mode: "markup")]
        } else {
          eval(item.label, mode: "markup")
        }
      ],
      table.cell(align: right)[
        #if item.bold {
          text(weight: font.bold)[#item.amount]
        } else {
          item.amount
        }
      ],
    )

    if item.info != none {
      cells += (
        table.cell(inset: (top: 0pt))[
          #text(size: font.small, fill: font.dim)[#text(
            style: font.italic,
            eval(
              item.info,
              mode: "markup",
            ),
          )]
        ],
        table.cell()[],
      )
    }

    for (idx, se) in item.sub-table.enumerate() {
      let is-last = idx == item.sub-table.len() - 1
      let bottom-inset = if is-last { cell-inset } else { cell-inset / 2 }

      cells += (
        table.cell(inset: (top: 0pt, bottom: bottom-inset))[
          #text(size: font.small, fill: font.dim)[#eval(
            se.label,
            mode: "markup",
          )]
        ],
        table.cell(inset: (top: 0pt, bottom: bottom-inset), align: right)[
          #text(size: font.small, fill: font.dim)[#se.amount]
        ],
      )
    }

    if i < entries.len() - 1 {
      let item-line = if item.line == auto { line } else { item.line }
      cells += (table.hline(stroke: item-line),)
    }
  }

  card(fill: fill, radius: radius, inset: (x: inset, y: inset - cell-inset))[
    #table(
      columns: (auto, 1fr, auto),
      stroke: none,
      inset: (x: 0pt, y: cell-inset),
      ..cells,
    )
  ]
}

#let with-note(text, note-id) = {
  if note-id == none or note-id == "" {
    return text
  }
  if text == none or text == "" {
    return ""
  }

  let note-markup = "#super[" + note-id + "]"

  return text + " " + note-markup
}

#let note-entry(identifier, content) = (
  table.cell(align: center + horizon)[#text(
    weight: constants.fonts.weight.bold,
    identifier,
  )],
  table.cell()[#content],
)

#let notes(
  fill: constants.colors.bg,
  radius: constants.layout.radius.small,
  inset: constants.layout.spacing.small,
  ..children,
) = {
  card(fill: fill, radius: radius, inset: inset)[
    #text(size: constants.fonts.size.small, fill: constants.colors.text-dim)[
      #table(
        columns: (auto, 1fr),
        stroke: none,
        ..children.pos().flatten()
      )
    ]
  ]
}
