#let safe-get(obj, path, default: none) = {
  if type(obj) != dictionary and type(obj) != array {
    return default
  }

  let current = obj

  for part in path.split(".") {
    if current == none {
      return default
    }

    if type(current) == dictionary and part in current {
      current = current.at(part)
    } else if type(current) == array and part.match(regex("^\d+$")) != none {
      let idx = int(part)
      // Check that the index is within the array bounds
      if idx < current.len() {
        current = current.at(idx)
      } else {
        return default
      }
    } else {
      return default
    }
  }

  if current == none {
    return default
  } else {
    return current
  }
}

#let string-to-datetime(date-str) = {
  if date-str == none or date-str == "-" {
    return none
  }

  let parts = date-str.split("-")

  if parts.len() != 3 {
    return none
  }

  return datetime(
    year: int(parts.at(0)),
    month: int(parts.at(1)),
    day: int(parts.at(2)),
  )
}

#let display-date(date-str, format: "[day].[month].[year]") = {
  let date = string-to-datetime(date-str)

  if date == none {
    "-"
  } else {
    date.display(format)
  }
}
