#!/usr/bin/env python3

import json
from datetime import date
from pathlib import Path
from typing import Any

ROOT = Path(Path(__file__).parent.resolve(), "../../../../../../resources/testcase/serialized-testcases").resolve()
ANONYMIZED_SOZIALVERSICHERUNGSNUMMER = "756.1111.1111.3"


def anonymize_geburtsdatum(value: Any) -> Any:
    if not isinstance(value, str):
        return value

    try:
        parsed = date.fromisoformat(value)
    except ValueError:
        return value

    rounded_day = 1 if parsed.day <= 15 else 28
    return parsed.replace(day=rounded_day).isoformat()


def anonymize(node: Any, parent_key = None) -> None:
    if isinstance(node, dict):
        for key, value in node.items():
            if key == "vorname" and parent_key is not None:
                node[key] = f"{parent_key}-vorname"
            elif key == "nachname" and parent_key is not None:
                node[key] = f"{parent_key}-nachname"
            elif key == "fullName" and parent_key is not None:
                node[key] = f"{parent_key}-vorname {parent_key}-nachname"
            elif key == "geburtsdatum":
                node[key] = anonymize_geburtsdatum(value)
            elif key == "strasse":
                node[key] = "teststrasse"
            elif key == "sozialversicherungsnummer":
                node[key] = ANONYMIZED_SOZIALVERSICHERUNGSNUMMER
            else:
                anonymize(value, key)

    elif isinstance(node, list):
        for item in node:
            anonymize(item, parent_key)


if __name__ == "__main__":
    files = sorted(ROOT.rglob("*.json"))

    print(ROOT)

    for file in files:
        with file.open("r", encoding="utf-8") as f:
            data = json.load(f)

        anonymize(data)

        with file.open("w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
            f.write("\n")

        print(f"Anonymize {file}")

    print(f"Anonymized: {len(files)} files.")
