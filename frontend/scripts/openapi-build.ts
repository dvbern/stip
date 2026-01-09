/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import childProcess from 'node:child_process';
import * as fs from 'node:fs';
import { PathLike } from 'node:fs';
import path from 'node:path';

const contractDir = '../contract';
const yaml = `${contractDir}/openapi.yaml`;
const dependencies = require('../package.json').dependencies;

// Add more APIs as you go
// Keep in mind: there is one file generated per OpenAPI-@Tag,
// Names are CamelCase versions from OpenAPIConst Tags
const generatedApis = [
  'Ausbildung',
  'Ausbildungsgang',
  'Ausbildungsstaette',
  'Auszahlung',
  'Benutzer',
  'Bildungskategorie',
  'Buchhaltung',
  'Configuration',
  'Datenschutzbrief',
  'Delegieren',
  'Dokument',
  'Drucken',
  'Eltern',
  'Darlehen',
  'Fall',
  'Gesuch',
  'GesuchNotiz',
  'Gesuchsjahr',
  'Gesuchsperiode',
  'GesuchTranche',
  'Land',
  'Mail',
  'Massendruck',
  'Notification',
  'Plz',
  'Sozialdienst',
  'SozialdienstAdmin',
  'Stammdaten',
  'Steuerdaten',
  'Steuerdaten',
  'StipDecision',
  'Tenant',
  'Verfuegung',
  'Massendruck',
];

const ngVersion = dependencies['@angular/core'].replace(/[^0-9.]/, '');
console['log']('ngVersion', ngVersion);

function deleteFile(filePath: PathLike) {
  fs.unlinkSync(filePath);
}

function deleteOldFilesSync(directory: string, timestamp: Date) {
  if (!fs.existsSync(directory)) {
    return;
  }

  const files = fs.readdirSync(directory);
  for (const file of files) {
    const filePath = path.join(directory, file);
    const stats = fs.statSync(filePath);
    if (stats.mtime.getTime() < timestamp.getTime()) {
      deleteFile(filePath);
    }
  }
}

function copyFilesForGesuch(directoryFrom: string, directoryTo: string) {
  if (!fs.existsSync(directoryFrom) || !fs.existsSync(directoryTo)) {
    return;
  }

  const files = fs.readdirSync(directoryFrom);
  for (const file of files) {
    const filePath = path.join(directoryFrom, file);
    const filePathTo = path.join(directoryTo, file);
    fs.copyFileSync(filePath, filePathTo);
  }
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
async function executeAndLog(command: string, env: any) {
  return new Promise<void>((resolve, reject) =>
    childProcess.exec(command, env, (error, stdout) => {
      if (error) {
        reject(error);
        return;
      }
      if (stdout) {
        console['log'](stdout.toString());
      }
      resolve();
    }),
  );
}

async function generateOpenApi(directory: string, apis: string[]) {
  const env = {
    ...process.env,
    JAVA_OPTS: ['-Dmodels'].join(' '),
  };

  const apisString = apis.join(':');

  /**
   * Alternatively, use generated types?
   * @example
   * const typesPath = '../../types';
   */
  const typeMap = [
    // [OpenApi-Name/Format, Typescript-Name, Import-Path]
    ['AnyType', 'object', undefined],
    // ['set', 'FakeSet', typesPath],
    ['date', 'string', undefined],
    ['Date', 'string', undefined],
    ['DateTime', 'string', undefined],
    ['iso-date-string', 'IsoDateString', undefined],
    // ['local-time', 'BackendLocalTime', typesPath],
    // ['local-time-hhmm', 'BackendLocalTimeHHMM', typesPath],
    // ['zoned-date-time', 'BackendZonedDateTime', typesPath],
    // ['email', 'BackendEmail', typesPath],
    // ['rest-includes', 'RestIncludes', typesPath],
    //['entity-id', 'EntityID', typesPath],
  ];
  const typeMappingsArg = typeMap.map((e) => `${e[0]}=${e[1]}`).join(',');

  /**
   * In case we need to import some types from other files
   * @example
   * const importMappingsArg = typeMap
   *   .filter((e) => !!e[2])
   *   .map((e) => `${e[1]}=${e[2]}`)
   *   .join(',');
   * // and add ` --import-mappings ${importMappingsArg}` to the args
   */
  const cmd =
    'npx openapi-generator-cli generate' +
    ` -i ${yaml}` +
    ' -g typescript-angular' +
    ' --template-dir scripts/conf/openapi-templates' +
    ` --global-property models,apis=${apisString},supportingFiles` +
    ' --openapi-normalizer REFACTOR_ALLOF_WITH_PROPERTIES_ONLY=true' +
    ` -p ngVersion=${ngVersion}` +
    ' -p basePath=/api/v1' +
    ' -p supportsES6=true' +
    ' -p disallowAdditionalPropertiesIfNotPresent=false' +
    ' -p legacyDiscriminatorBehavior=false' +
    // Sortierung: uebernimmt Originalreihenfolge
    ' -p sortModelPropertiesByRequiredFlag=false' +
    ' -p sortParamsByRequiredFlag=false' +
    ' -p enumPropertyNaming=original' +
    // Eindeutiges Naming fuer Service-Methoden und Parameter-Objekte
    ' -p removeOperationIdPrefix=true' +
    ' -p prefixParameterInterfaces=true' +
    // sonst schneidet es bei einigen Enums den vordersten Teil einfach ab, wenn alle Eintraege das gleiche Prefix haben
    ' -p removeEnumValuePrefix=false' +
    ' -p useSingleRequestParameter=true' +
    ` --type-mappings ${typeMappingsArg}` +
    ' -o ' +
    directory;

  await executeAndLog(cmd, env);
}

async function sleep(msec: number) {
  return new Promise((resolve) => setTimeout(resolve, msec));
}

// async main
(async () => {
  const timestamp = new Date();
  // noinspection MagicNumberJS
  await sleep(100); // make sure timestamp ticks

  const generatorPath = 'tmp/generated';
  const apiPath = path.join(generatorPath, 'api');
  const modelsPath = path.join(generatorPath, 'model');

  await executeAndLog('npm run build', { cwd: contractDir });
  await generateOpenApi(generatorPath, generatedApis);

  deleteOldFilesSync(apiPath, timestamp);
  deleteOldFilesSync(modelsPath, timestamp);
  const gesuchModelPath = 'libs/shared/model/gesuch/src/lib/openapi/model';
  const gesuchServicePath = 'libs/shared/model/gesuch/src/lib/openapi/services';
  copyFilesForGesuch(apiPath, gesuchServicePath);
  copyFilesForGesuch(modelsPath, gesuchModelPath);
  // we can generate only a selection of model entities according to namespace for example.
  // That would allow us to split the model directly in the right packages. Lets see if we need it.
})();
