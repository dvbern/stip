import { PATTERN_EMAIL } from '@dv/shared/model/gesuch';

describe('email pattern', () => {
  // Format
  it('empty', () => testInput('', false));
  it('no domain', () => testInput('hallo@welt', false));
  it('no domain', () => testInput('hallo@welt.', false));
  it('no @', () => testInput('welt.ch', false));
  it('no name', () => testInput('@welt.ch', false));
  it('too short domain', () => testInput('hallo@welt.c', true));
  it('starts with _', () => testInput('_hallo@welt.ch', true));
  it('ends with _', () => testInput('hallo_@welt.ch', true));
  it('starts with .', () => testInput('.hallo@welt.ch', false));
  it('ends with .', () => testInput('hallo.@welt.ch', false));
  it('consecutive .', () => testInput('hans..muster@welt.ch', false));

  // valid
  it('good .', () => testInput('heinz.mueller@dvbern.ch', true));
  it('good -', () => testInput('heinz-mueller@dvbern.ch', true));
  it('good _', () => testInput('heinz_mueller@dvbern.ch', true));
  it('good |', () => testInput('aaa|asdsads@sdsdsd.com', true));
  it('good +', () => testInput('heinz-mueller+001@dvbern.ch', true));
  it('good %', () => testInput('aaa%asdsads@sdsdsd.com', true));
  it('good &', () => testInput('aaa&asdsads@sdsdsd.com', true));
  it('good short', () => testInput('h@dvbern.ch', true));
  it('good short', () => testInput('h@b.ch', true));
  it('good big letter', () => testInput('Heinz.mueller@dvbern.ch', true));

  // ungueltige Zeichen
  it('special characters mix', () =>
    testInput('()<>,;:"[]|ç%&@adsad.com', false));

  it('(', () => testInput('aaa(asdsads@sdsdsd.com', false));
  it(')', () => testInput('aaa)asdsads@sdsdsd.com', false));
  it('<', () => testInput('aaa<asdsads@sdsdsd.com', false));
  it('>', () => testInput('aaa>asdsads@sdsdsd.com', false));
  it('@', () => testInput('aaa@asdsads@sdsdsd.com', false));
  it(',', () => testInput('aaa,asdsads@sdsdsd.com', false));
  it(';', () => testInput('aaa;asdsads@sdsdsd.com', false));
  it(':', () => testInput('aaa:asdsads@sdsdsd.com', false));
  it('"', () => testInput('aaa"asdsads@sdsdsd.com', false));
  it('[', () => testInput('aaa[asdsads@sdsdsd.com', false));
  it(']', () => testInput('aaa]asdsads@sdsdsd.com', false));
  it('ç', () => testInput('aaaçasdsads@sdsdsd.com', false));

  it('space', () => testInput('aaa asdsads@sdsdsd.com', false));

  it('ä', () => testInput('aaaäasdsads@sdsdsd.com', false));
  it('é', () => testInput('aaaéasdsads@sdsdsd.com', false));
  it('ü', () => testInput('aaaüasdsads@sdsdsd.com', false));
  it('Ä', () => testInput('aaaÄasdsads@sdsdsd.com', false));
  it('ô', () => testInput('aaaôasdsads@sdsdsd.com', false));

  function testInput(input: string, expectedValid: boolean) {
    const regexp = new RegExp(PATTERN_EMAIL);
    const valid = regexp.test(input);
    expect(valid).toBe(expectedValid);
  }
});
