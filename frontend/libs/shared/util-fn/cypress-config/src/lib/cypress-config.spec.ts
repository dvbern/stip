import { getNewComponentTestPort, resetPortCounter } from './cypress-config';

// The jest type definition for expect is being overwritten by cypress' Chai expect definition
const jestExpect: jest.Expect = expect as any;

describe('automatic port numer increment', () => {
  it.only('should increment the port number each time "getNewComponentTestPort" is called', () => {
    resetPortCounter();
    jestExpect(getNewComponentTestPort()).toBe(52080);
    jestExpect(getNewComponentTestPort()).toBe(52082);
  });
});
