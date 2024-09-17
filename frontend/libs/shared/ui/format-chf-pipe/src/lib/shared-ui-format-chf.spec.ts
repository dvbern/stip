import {
  SharedUiFormatChfNegativePipe,
  SharedUiFormatChfPipe,
  SharedUiFormatChfPositivePipe,
} from './shared-ui-format-chf.pipe';

describe('SharedUiFormatChfPipe', () => {
  let pipe: SharedUiFormatChfPipe;

  beforeEach(() => {
    pipe = new SharedUiFormatChfPipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should return formatted string for positive number', () => {
    expect(pipe.transform(10000)).toBe("10'000");
  });

  it('should return formatted string for negative number with default addNegativeSign flag', () => {
    expect(pipe.transform(-10000)).toBe("- 10'000");
  });

  it('should return formatted string for negative number without addNegativeSign flag', () => {
    expect(pipe.transform(-10000, false)).toBe("10'000");
  });

  it('should return "0" for zero value', () => {
    expect(pipe.transform(0)).toBe('0');
  });

  it('should return empty string for undefined value', () => {
    expect(pipe.transform(undefined)).toBe('');
  });
});

describe('SharedUiFormatChfPositivePipe', () => {
  let pipe: SharedUiFormatChfPositivePipe;

  beforeEach(() => {
    pipe = new SharedUiFormatChfPositivePipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should return formatted string for positive number without alwaysPositive flag', () => {
    expect(pipe.transform(1234)).toBe("+ 1'234");
  });

  it('should return formatted string for positive number with alwaysPositive flag', () => {
    expect(pipe.transform(1234, true)).toBe("+ 1'234");
  });

  it('should return formatted string for negative number without alwaysPositive flag', () => {
    expect(pipe.transform(-1234)).toBe("- 1'234");
  });

  it('should return formatted string for negative number with alwaysPositive flag', () => {
    expect(pipe.transform(-1234, true)).toBe("+ 1'234");
  });

  it('should return "0" for zero value', () => {
    expect(pipe.transform(0)).toBe('0');
  });

  it('should return empty string for undefined value', () => {
    expect(pipe.transform(undefined)).toBe('');
  });
});

describe('SharedUiFormatChfNegativePipe', () => {
  let pipe: SharedUiFormatChfNegativePipe;

  beforeEach(() => {
    pipe = new SharedUiFormatChfNegativePipe();
  });

  it('create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should return formatted string for positive number without alwaysNegative flag', () => {
    expect(pipe.transform(1234)).toBe("1'234");
  });

  it('should return formatted string for positive number with alwaysNegative flag', () => {
    expect(pipe.transform(1234, true)).toBe("- 1'234");
  });

  it('should return formatted string for negative number without alwaysNegative flag', () => {
    expect(pipe.transform(-1234)).toBe("- 1'234");
  });

  it('should return formatted string for negative number with alwaysNegative flag', () => {
    expect(pipe.transform(-1234, true)).toBe("- 1'234");
  });

  it('should return "0" for zero value', () => {
    expect(pipe.transform(0)).toBe('0');
  });

  it('should return empty string for undefined value', () => {
    expect(pipe.transform(undefined)).toBe('');
  });
});
