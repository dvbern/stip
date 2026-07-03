#let render(data, t) = {
  text("familie berechnung: ")
  text(t("amount.label", amount: data.at("amount", default: "-")))
}
