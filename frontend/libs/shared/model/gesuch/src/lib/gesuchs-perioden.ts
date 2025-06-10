import { GueltigkeitStatus } from './openapi/model/gueltigkeitStatus';

export type GueltigkeitStatusFrontend =
  | GueltigkeitStatus
  | 'PUBLIZIERT_INAKTIV';

export const GueltigkeitStatusFrontend: Record<
  GueltigkeitStatusFrontend,
  string
> = {
  PUBLIZIERT: 'PUBLIZIERT',
  //  when today < aufschaltterminStart
  PUBLIZIERT_INAKTIV: 'PUBLIZIERT_INAKTIV',
  ENTWURF: 'ENTWURF',
  ARCHIVIERT: 'ARCHIVIERT',
};
