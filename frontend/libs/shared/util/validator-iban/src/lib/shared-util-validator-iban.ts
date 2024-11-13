import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { extractIBAN } from 'ibantools';

export function ibanValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (control.value === null || control.value === '') {
      return null;
    }
    const extractIBANResult = extractIBAN('CH' + control.value);
    if (!extractIBANResult.valid || extractIBANResult.countryCode !== 'CH') {
      return { invalidIBAN: true };
    }
    // Check IBAN is not a QR-IBAN, a QR based one, has in the first 5 digits a value between 30000 and 31999
    if (
      +extractIBANResult.iban.substring(4, 9) >= 30000 &&
      +extractIBANResult.iban.substring(4, 9) <= 31999
    ) {
      return { qrIBAN: true };
    }
    return null;
  };
}
