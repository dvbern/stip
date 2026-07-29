#let _missing = "__missing__"

#let _get-path(tree, path, default: _missing) = {
  let current = tree

  for part in path.split(".") {
    if type(current) != dictionary or part not in current {
      return default
    }

    current = current.at(part)
  }

  current
}

#let _interpolate(message, values) = {
  message.replace(regex("\\{([A-Za-z_][A-Za-z0-9_]*)\\}"), m => {
    let name = m.captures.at(0)

    if name in values {
      let val = values.at(name)
      if val == none { "" } else { str(val) }
    } else {
      panic("Missing translation placeholder: `" + name + "`")
    }
  })
}

#let _resolve-entry(entry, values) = {
  if type(entry) == function {
    return entry(..values)
  }

  if type(entry) == str {
    return _interpolate(entry, values)
  }

  entry
}

#let make-i18n(
  catalog,
  locale: "de",
) = {
  let root = catalog.at(locale, default: _missing)

  if root == _missing {
    panic("Missing translation locale: `" + locale + "`")
  }

  let t(key, ..args) = {
    let entry = _get-path(root, key, default: _missing)

    if entry == _missing {
      panic(
        "Missing translation key: `" + key + "` for locale `" + locale + "`",
      )
    }

    _resolve-entry(entry, args.named())
  }

  (
    t: t,
    locale: locale,
  )
}
