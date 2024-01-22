import task from '@cypress/code-coverage/task';
import * as os from 'os';
import * as path from 'path';
import * as fs from 'fs';

const lastPortFile = path.join(
  os.tmpdir(),
  '__last-cypress-component-test-port.lock',
);

export const resetPortCounter = () => {
  fs.unlinkSync(lastPortFile);
};

export const getNewComponentTestPort = () => {
  let newPort = 52080;
  if (fs.existsSync(lastPortFile)) {
    newPort = +fs.readFileSync(lastPortFile).toString('utf-8');
    if (newPort < 52080 || newPort >= 53000) {
      newPort = 52080;
    }
  }
  fs.writeFileSync(lastPortFile, (newPort + 2).toString());
  return newPort;
};

const createCypressConfig = <T extends Partial<Cypress.ResolvedConfigOptions>>(
  config: T,
) => config;

export const dvCypressConfigs = {
  component: createCypressConfig({
    port: getNewComponentTestPort(),
    setupNodeEvents(on, config) {
      task(on, config);
      return config;
    },
  }),
  e2e: createCypressConfig({
    video: true,
    setupNodeEvents(on, config) {
      task(on, config);

      // https://docs.cypress.io/guides/guides/screenshots-and-videos#Delete-videos-for-specs-without-failing-or-retried-tests
      on('after:spec', (_, results: CypressCommandLine.RunResult) => {
        if (results && results.video) {
          // Do we have failures for any retry attempts?
          const failures = results.tests.some((test) =>
            test.attempts.some((attempt) => attempt.state === 'failed'),
          );
          if (!failures) {
            // delete the video if the spec passed and no tests retried
            fs.unlinkSync(results.video);
          }
        }
      });
      return config;
    },
    watchForFileChanges: false,
  }),
};
