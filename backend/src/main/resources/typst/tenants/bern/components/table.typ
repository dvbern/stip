#import "/tenants/bern/constants.typ"

#let section(label, info, value) = (
  label: label,
  info: info,
  value: value,
)

#let header = section
#let footer = section

#let font(small, base, big, bold, italic, dim) = (
  small: small,
  base: base,
  big: big,
  bold: bold,
  italic: italic,
  dim: dim,
)

#let person(name, amount) = (
  name: name,
  amount: amount,
)

#let entry(label, amount, info: none, persons: (), line: auto) = (
  label: label,
  amount: amount,
  info: info,
  persons: persons,
  line: line,
)

#let entry-rows(item) = (
  1 + if item.info != none { 1 } else { 0 } + item.persons.len()
)

#let rounded-bg(
  header: none,
  footer: none,
  fill: constants.colors.bg,
  line: constants.colors.border,
  line-dominant: constants.colors.border-dominant,
  radius: constants.layout.radius.big,
  inset: constants.layout.spacing.base,
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

  if header != none {
    let header-cells = (
      table.cell(inset: 0pt)[],
      table.cell()[#text(weight: font.bold, header.label)],
      table.cell(align: right)[
        #text(weight: font.bold)[#header.value]
      ],
    )

    if header.info != none {
      header-cells += (
        table.cell(inset: 0pt)[],
        table.cell(inset: (top: 0pt))[
          #text(size: font.small, fill: font.dim)[
            #text(style: font.italic, header.info)
          ]
        ],
        table.cell(inset: 0pt)[],
      )
    }

    cells += (
      table.header(..header-cells),
      table.hline(stroke: line-dominant),
    )
  }

  for (i, item) in entries.enumerate() {
    cells += (
      table.cell(
        rowspan: entry-rows(item),
        breakable: false,
        inset: 0pt,
      )[],
      table.cell()[
        #item.label
      ],
      table.cell(align: right)[
        #item.amount
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

    for p in item.persons {
      cells += (
        table.cell(inset: (top: 0pt))[
          #text(size: font.small, fill: font.dim)[#p.name]
        ],
        table.cell(inset: (top: 0pt), align: right)[
          #text(size: font.small, fill: font.dim)[#p.amount]
        ],
      )
    }

    if i < entries.len() - 1 {
      let item-line = if item.line == auto { line } else { item.line }
      cells += (table.hline(stroke: item-line),)
    }
  }

  if footer != none {
    let footer-cells = (
      table.cell(inset: 0pt)[],
      table.cell()[#text(weight: font.bold, footer.label)],
      table.cell(align: right)[
        #text(weight: font.bold)[#footer.value]
      ],
    )

    if footer.info != none {
      footer-cells += (
        table.cell(inset: 0pt)[],
        table.cell(inset: (top: 0pt))[
          #text(size: font.small, fill: font.dim)[
            #text(style: font.italic, footer.info)
          ]
        ],
        table.cell(inset: 0pt)[],
      )
    }

    cells += (
      table.hline(stroke: line-dominant),
      table.footer(..footer-cells),
    )
  }

  block(fill: fill, radius: radius, width: 100%, inset: (x: inset, y: 0pt))[
    #table(
      columns: (auto, 1fr, auto),
      stroke: none,
      inset: (x: 0pt, y: inset),
      ..cells,
    )
  ]
}
