#import "/tenants/bern/constants.typ"

#let header(label, info, value) = (
  label: label,
  info: info,
  value: value,
)

#let footer(label, value) = (
  label: label,
  value: value,
)

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

#let entry(label, amount, info: none, persons: ()) = (
  label: label,
  amount: amount,
  info: info,
  persons: persons,
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
    cells += (
      table.header(
        table.cell(inset: 0pt)[],
        table.cell()[#text(weight: font.bold, header.label)],
        table.cell(align: right)[#text(
          weight: font.bold,
        )[#header.value]],

        if header.info != none {
          table.cell(colspan: 2, inset: (top: 0pt))[
            #text(size: font.small, fill: font.dim)[#text(
              style: font.italic,
              header.info,
            )]
          ]
        },
      ),
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
      cells += (table.hline(stroke: line),)
    }
  }

  if footer != none {
    cells += (
      table.hline(stroke: line-dominant),
      table.footer(
        repeat: false,
        table.cell()[],
        table.cell()[#text(weight: font.bold, footer.label)],
        table.cell(
          align: right,
        )[#text(weight: font.bold)[#footer.value]],
      ),
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
