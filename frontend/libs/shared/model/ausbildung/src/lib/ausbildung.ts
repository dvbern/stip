export type AusbildungsLand = 'CH' | 'AUSLAND';
// TODO: Remove this type and use the one from the shared model
export const ortToAusbidlungsLand = (ort: string) => {
  return ort === 'AUSLAND' ? 'AUSLAND' : 'CH';
};
