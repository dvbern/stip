export function removeDuplicates(value: string) {
  return value.replace(/,,/g, ',').replace(/--/g, '-');
}

export function cleanupZuweisung(value: string) {
  return value
    .replace(/ /g, '')
    .replace(/-$/, '')
    .replace(/^-/, '')
    .replace(/(,-|-,)/, ',');
}

export function sortZuweisung(value: string) {
  return value
    .split(',')
    .map((group) =>
      group
        .split('-')
        .sort((a, b) => a.localeCompare(b))
        .join('-'),
    )
    .sort((a, b) => a.localeCompare(b))
    .join(',');
}
