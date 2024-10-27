import { FormControl } from '@angular/forms';

import { ibanValidator } from './shared-util-validator-iban';

describe('ibanValidator', () => {
  it('should validate a correct IBAN', () => {
    const control = new FormControl('3908704016075473007');
    const validator = ibanValidator();
    const result = validator(control);
    expect(result).toBeNull();
  });

  it('should invalidate an incorrect IBAN', () => {
    const control = new FormControl('9300762011623852958');
    const validator = ibanValidator();
    const result = validator(control);
    expect(result).toEqual({ invalidIBAN: true });
  });

  it('should invalidate a QR-IBAN', () => {
    const control = new FormControl('4431999123000889012');
    const validator = ibanValidator();
    const result = validator(control);
    expect(result).toEqual({ qrIBAN: true });
  });

  it('should pass validation for an empty value', () => {
    const control = new FormControl('');
    const validator = ibanValidator();
    const result = validator(control);
    expect(result).toBeNull();
  });

  it('should pass validation for a null value', () => {
    const control = new FormControl(null);
    const validator = ibanValidator();
    const result = validator(control);
    expect(result).toBeNull();
  });
});
