#let chf(value, prefix: "none") = {
  assert(
    prefix in ("positive", "negative", "none"),
    message: "prefix must be one of: \"positive\", \"negative\", or \"none\"",
  )

  if value == none or type(value) != int {
    return "-"
  }

  let s = str(calc.abs(value))
  let out = ""

  let sign = if value == 0 {
    ""
  } else if prefix == "positive" {
    "+"
  } else if prefix == "negative" {
    "-"
  } else {
    ""
  }

  for (i, c) in s.clusters().rev().enumerate() {
    if i > 0 and calc.rem(i, 3) == 0 {
      out = "’" + out
    }
    out = c + out
  }

  [#sign #out CHF]
}
