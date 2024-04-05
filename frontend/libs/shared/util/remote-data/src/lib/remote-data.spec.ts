import { map, of } from 'rxjs';

import { handleApiResponse } from './remote-data';

describe('handleApiResponse', () => {
  it('should handle success', () => {
    const handler = jest.fn();
    const data = 'data';
    of(data).pipe(handleApiResponse(handler)).subscribe();
    expect(handler).toHaveBeenCalledWith({
      type: 'success',
      data,
      error: undefined,
    });
  });
  it('should handle error', () => {
    const handler = jest.fn();
    const error = new Error();
    of(null)
      .pipe(
        map(() => {
          throw error;
        }),
        handleApiResponse(handler),
      )
      .subscribe();
    expect(handler).toHaveBeenCalledWith({
      type: 'failure',
      data: undefined,
      error,
    });
  });
});
