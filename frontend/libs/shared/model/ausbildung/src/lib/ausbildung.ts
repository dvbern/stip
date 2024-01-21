import { Ausbildungsort } from '@dv/shared/model/gesuch';

export type AusbildungsLand = 'CH' | 'AUSLAND';

export const ortToAusbidlungsLand = (ort: Ausbildungsort) => {
  return ort === 'AUSLAND' ? 'AUSLAND' : 'CH';
};
