export type AusbildungsLand = 'CH' | 'AUSLAND';

export const ortToAusbidlungsLand = (ort: string) => {
  return ort === 'AUSLAND' ? 'AUSLAND' : 'CH';
};
