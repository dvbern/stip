import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import {
  CustomDokumentTyp,
  DarlehenDokument,
  DarlehenDokumentType,
  Dokument,
  DokumentArt,
  DokumentTyp,
  GesuchDokument,
  GesuchDokumentKommentar,
  UnterschriftenblattDokument,
  UnterschriftenblattDokumentTyp,
} from '@dv/shared/model/gesuch';
import { GesuchFormStep } from '@dv/shared/model/gesuch-form';
import { PermissionMap } from '@dv/shared/model/permission-state';
import { Extends } from '@dv/shared/model/type-util';

export type SharedModelStandardGesuchDokument = {
  art: Extends<DokumentArt, 'GESUCH_DOKUMENT'>;
  dokumentTyp: DokumentTyp;
  trancheId: string;
  gesuchDokument?: GesuchDokument;
};

export type SharedModelAdditionalGesuchDokument = {
  art: Extends<DokumentArt, 'UNTERSCHRIFTENBLATT'>;
  dokumentTyp: UnterschriftenblattDokumentTyp;
  gesuchId: string;
  trancheId: string;
  gesuchDokument?: UnterschriftenblattDokument;
};

export type SharedModelCustomGesuchDokument = {
  art: Extends<DokumentArt, 'CUSTOM_DOKUMENT'>;
  dokumentTyp: CustomDokumentTyp;
  gesuchId: string;
  trancheId: string;
  gesuchDokument?: GesuchDokument;
};

export type SharedModelDarlehenDokument = {
  art: Extends<DokumentArt, 'DARLEHEN_DOKUMENT'>;
  dokumentTyp: DarlehenDokumentType;
  darlehenId: string;
  gesuchDokument?: DarlehenDokument;
};

export interface SharedModelTableRequiredDokument {
  formStep: GesuchFormStep;
  dokumentTyp: DokumentTyp;
  gesuchDokument?: GesuchDokument;
  dokumentOptions: StandardDokumentOptions;
  kommentare: GesuchDokumentKommentar[];
  kommentarePending: boolean;
}

export interface SharedModelTableCustomDokument {
  dokumentTyp: CustomDokumentTyp;
  canDelete: boolean;
  gesuchDokument?: GesuchDokument;
  dokumentOptions: CustomDokumentOptions;
  kommentare: GesuchDokumentKommentar[];
  kommentarePending: boolean;
}

export type SharedModelTableAdditionalDokument = {
  dokumentTyp: UnterschriftenblattDokumentTyp;
  gesuchDokument?: UnterschriftenblattDokument;
  dokumentOptions: StandardDokumentOptions;
};

export type SharedModelGesuchDokument =
  | SharedModelStandardGesuchDokument
  | SharedModelAdditionalGesuchDokument
  | SharedModelCustomGesuchDokument
  | SharedModelDarlehenDokument;

export type SharedModelTableDokument =
  | SharedModelTableRequiredDokument
  | SharedModelTableCustomDokument;

type DokumentInfoTranslatable = {
  type: 'TRANSLATABLE';
  title: SharedTranslationKey;
  description?: SharedTranslationKey;
};
type DokumentInfoText = {
  type: 'TEXT';
  title: string;
  description?: string;
};
export type DokumentInfo = DokumentInfoTranslatable | DokumentInfoText;

interface BaseDocumentOptions {
  permissions: PermissionMap;
  allowTypes: string;
  dokument: SharedModelGesuchDokument;
  initialDokumente?: Dokument[];
}

export interface StandardDokumentOptions extends BaseDocumentOptions {
  info: DokumentInfoTranslatable;
}

export interface DarlehenDokumentOptions extends BaseDocumentOptions {
  info: DokumentInfoTranslatable;
}

export interface CustomDokumentOptions extends BaseDocumentOptions {
  info: DokumentInfoText;
}

export type DokumentOptions =
  | StandardDokumentOptions
  | CustomDokumentOptions
  | DarlehenDokumentOptions;

// Darlehen Dokument

export interface DokumentUpload {
  file: Dokument;
  isTemporary?: boolean;
  progress?: number;
  error?: { translationKey: string; values?: unknown };
}

export interface DokumentView extends DokumentUpload {
  state: 'uploading' | 'done' | 'error';
  theme:
    | { icon: 'warning'; type: 'danger'; color: 'warn' }
    | { icon: 'sync'; type: 'info'; color: 'info' }
    | { icon: 'check'; type: 'success'; color: 'success' };
}

export interface DokumentListView {
  gesuchDokument?: SharedModelGesuchDokument;
  dokuments: DokumentView[];
}

export interface DokumentState {
  dokumentModel?: SharedModelGesuchDokument;
  dokuments: DokumentUpload[];
  errorKey?: string;
}

export interface UploadView {
  permissions: PermissionMap;
  dokumentModel: SharedModelGesuchDokument;
  initialDokuments?: Dokument[];
  hasEntries: boolean;
  isSachbearbeitungApp: boolean;
}

export const isUploadable = (
  dokumentModel: SharedModelGesuchDokument,
  permission: PermissionMap,
  isSachbearbeitungApp: boolean,
) => {
  switch (dokumentModel.art) {
    case 'GESUCH_DOKUMENT':
    case 'CUSTOM_DOKUMENT': {
      if (!isSachbearbeitungApp) {
        const status = dokumentModel.gesuchDokument?.status;
        return status !== 'AKZEPTIERT' && permission.canUploadDocuments;
      }
      return permission.canUploadDocuments;
    }
    case 'UNTERSCHRIFTENBLATT': {
      return permission.canUploadUnterschriftenblatt;
    }
    // todo: add logic
    case 'DARLEHEN_DOKUMENT': {
      return true;
    }
  }
};
