import 'dotenv/config';
import { writeFileSync } from 'node:fs';
import { resolve } from 'node:path';

const version = process.env.VERSION;
if (!version) {
  throw Error("'VERSION' environment variable is not set! Aborting build.");
}

const file = resolve(
  __dirname,
  '..',
  'libs',
  'shared',
  'model',
  'version',
  'src',
  'lib',
  'version.ts',
);
writeFileSync(
  file,
  `// IMPORTANT: THIS FILE IS AUTO GENERATED! DO NOT MANUALLY EDIT OR CHECK INTO GIT
export const VERSION = '${version}';
`,
  {
    flag: 'w',
  },
);

console.info(`Setting version '${version}' in version.ts`);
