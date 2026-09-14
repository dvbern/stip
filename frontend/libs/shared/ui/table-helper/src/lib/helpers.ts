import { WritableSignal } from 'node_modules/@angular/core/types/_chrome_dev_tools_performance-chunk';

import { GesuchDokument } from '@dv/shared/model/gesuch';

export type RowExpansionOverrides = Record<string, boolean>;

export const isExpanded = (
  gesuchDokument: GesuchDokument | undefined,
  overrides: RowExpansionOverrides,
): boolean => {
  if (!gesuchDokument) return false;
  const hasComments = (gesuchDokument.kommentars?.length ?? 0) > 0;
  if (!hasComments) return false;

  const override = overrides[gesuchDokument.id];
  if (override !== undefined) return override;

  const isAccepted = gesuchDokument.status === 'AKZEPTIERT';
  return !isAccepted;
};

export const toggleRowFn = (
  dokument: GesuchDokument | undefined,
  rowExpansionOverrides: WritableSignal<RowExpansionOverrides>,
) => {
  const documentId = dokument?.id;
  const hasKommentare = (dokument?.kommentars?.length ?? 0) > 0;
  if (!documentId || !hasKommentare) return;

  const nextExpanded = !isExpanded(dokument, rowExpansionOverrides());
  rowExpansionOverrides.update((overrides) => ({
    ...overrides,
    [documentId]: nextExpanded,
  }));
};
