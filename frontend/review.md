# Review Feedback (KSTIP-3337)

## Ziel

Die folgenden Punkte fassen das Code-Review zusammen, bereinigt und nach Datei gruppiert.

## 1) Tranche-Logik vereinheitlichen ✅

Datei: `frontend/libs/sachbearbeitung-app/feature/gesuch-form/src/lib/gesuch-form/sachbearbeitung-app-feature-gesuch-form.component.html`

- Es gibt zwei unterschiedliche Implementationen fuer sehr aehnliche Tranche-Bedingungen.
- Vorschlag: Logik vereinheitlichen oder in ein `@let` auslagern.

## 2) Debug-Code entfernen

Datei: `frontend/libs/sachbearbeitung-app/feature/gesuch-form/src/lib/gesuch-form/sachbearbeitung-app-feature-gesuch-form.component.html`

- Dieser Teil wirkt wie vergessener Debug-Code und sollte entfernt werden:

```html
<span>{{ stepSig()?.route }}</span>
```

## 3) Versionen-Menu Styling

Datei: `frontend/libs/sachbearbeitung-app/feature/gesuch-layout/src/lib/sachbearbeitung-app-feature-gesuch-layout/sachbearbeitung-app-feature-gesuch-layout.component.html`

- Das Versionen-Menu hat keinen `tw:dv-nav-button-outline` Link/Button.
- Erwartung: visuell und funktional an die uebrigen Nav-Buttons angleichen.

## 4) Zu viele Einzelwerte im Template ✅

Datei: `frontend/libs/sachbearbeitung-app/feature/gesuch-layout/src/lib/sachbearbeitung-app-feature-gesuch-layout/sachbearbeitung-app-feature-gesuch-layout.component.html`

- Die Anzahl `@let`-Werte und Bedingungen waechst stark.
- Vorschlag: in eine gebuendelte Struktur zusammenfassen, z. B. `optionsSig`, `permissions` oder `capabilities`.

## 5) Komplexe Disabled-Logik auslagern ✅

Datei: `frontend/libs/sachbearbeitung-app/feature/gesuch-layout/src/lib/sachbearbeitung-app-feature-gesuch-layout/sachbearbeitung-app-feature-gesuch-layout.component.html`

- Die verknuepfte `||`-Logik fuer `disabled` ist schwer lesbar.
- Vorschlag: aus den Bedingungen einen abgeleiteten Wert erzeugen und nur diesen im Template verwenden.

## 6) Overflow verursacht Ueberlappung

Datei: `frontend/libs/shared/styles/theme/src/material-overrides.scss`

- `overflow: visible` fuehrt zu ueberlappenden Inhalten.
- Hinweis aus Implementierung: wurde fuer Change-Bullets gesetzt.
- To-do: Alternative finden, die Bullets erlaubt ohne Layout-Ueberlappung.

## 7) Query-Param Mapping mehrfach vorhanden

Datei: `frontend/libs/sachbearbeitung-app/feature/gesuch-layout/src/lib/sachbearbeitung-app-feature-gesuch-layout/sachbearbeitung-app-feature-gesuch-layout.component.ts`

- Aehnliche Query-Param-Logik kommt mehrfach vor (nitpick: 3x).
- Vorschlag: in eine Helper-Funktion auslagern.

## 8) Tooltip + ARIA Redundanz

Datei: `frontend/libs/sachbearbeitung-app/feature/infos/src/lib/sachbearbeitung-app-feature-infos/sachbearbeitung-app-feature-infos.component.html`

- `matTooltip` fuegt bereits `aria-describedby` hinzu.
- Pruefen, ob das zusaetzliche `aria-label` hier redundant ist.

## 9) Beschwerde-Badge im Header ✅

Datei: `frontend/libs/sachbearbeitung-app/feature/gesuch-layout/src/lib/sachbearbeitung-app-feature-gesuch-layout/sachbearbeitung-app-feature-gesuch-layout.component.html`

- Das Icon sollte innerhalb des `<a>`-Tags liegen, damit Cursor und Klickflaeche konsistent bleiben.
- Design-Hinweis: eher Ausrufezeichen-Stil statt aktueller Darstellung.
- Vorschlag aus Review:

```html
<span class="tw:absolute tw:font-dv-icon tw:-top-3 tw:-right-3 tw:flex tw:items-center tw:justify-center tw:text-xl tw:h-4 tw:w-4 tw:rounded-full tw:text-white tw:bg-dv-warning tw:p-3.25" [matTooltip]="t('sachbearbeitung-app.infos.beschwerde.haengig.title')">priority_high</span>
```

## 10) Beschwerde-Badge in Infos

Datei: `frontend/libs/sachbearbeitung-app/feature/infos/src/lib/sachbearbeitung-app-feature-infos/sachbearbeitung-app-feature-infos.component.ts`

- Gleiche Design-Anpassung wie im Header anstreben.
- Alternativ laut Review: in diesem Kontext ganz ohne Icon arbeiten.

## 11) Kommentar-Texte in gesuch-form-steps.ts

Datei: `frontend/libs/shared/model/gesuch-form/src/lib/gesuch-form-steps.ts`

- Nitpick zur Benennung/Kommentierung:
- `Ungrouped Steps`
- `Dynamic in Group "Familienangaben"`
- To-do: Kommentare konsistent und sprachlich einheitlich halten.

## 12) Benennung in mergeGroupStatus

Datei: `frontend/libs/shared/pattern/gesuch-step-nav/src/shared-pattern-gesuch-step-nav/shared-pattern-gesuch-step-nav.component.ts`

- Nitpick: Parameter-/Variablennamen rund um Group/Step klarer benennen.

## 13) Vereinfachung mergeGroupStatus

Datei: `frontend/libs/shared/pattern/gesuch-step-nav/src/shared-pattern-gesuch-step-nav/shared-pattern-gesuch-step-nav.component.ts`

- Vorgeschlagene vereinfachte Variante:

```ts
return (['INVALID', 'WARNING', 'VALID'] satisfies StepState[]).find((s) => [current, next].includes(s));
```
