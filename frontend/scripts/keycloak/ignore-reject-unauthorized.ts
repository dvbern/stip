const originalEmitWarning = process.emitWarning;

// https://github.com/cypress-io/cypress/commit/740f5ab6a0cebe0070e96373fc791ddc7b48cf9b
export const allowInsecure = () => {
  process.emitWarning = (warning, ...args) => {
    if (
      typeof warning === 'string' &&
      warning.includes('NODE_TLS_REJECT_UNAUTHORIZED')
    ) {
      // node will only emit the warning once
      // https://github.com/nodejs/node/blob/82f89ec8c1554964f5029fab1cf0f4fad1fa55a8/lib/_tls_wrap.js#L1378-L1384
      process.emitWarning = originalEmitWarning;

      return;
    }

    return originalEmitWarning.call(process, warning, ...(args as any));
  };

  process.env['NODE_TLS_REJECT_UNAUTHORIZED'] = '0';
};
