import {
  CustomDokumentTyp,
  Dokument,
  DokumentArt,
  DokumentTyp,
  GesuchDokument,
  UnterschriftenblattDokument,
  UnterschriftenblattDokumentTyp,
} from '@dv/shared/model/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';
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

export type SharedModelGesuchDokument =
  | SharedModelStandardGesuchDokument
  | SharedModelAdditionalGesuchDokument
  | SharedModelCustomGesuchDokument;

export type SharedModelTableDokument =
  | SharedModelTableRequiredDokument
  | SharedModelTableCustomDokument;

export interface DokumentOptions {
  permissions: PermissionMap;
  titleKey: string;
  descriptionKey?: string;
  allowTypes: string;
  dokument: SharedModelGesuchDokument;
  initialDokumente?: Dokument[];
}

export interface SharedModelTableRequiredDokument {
  formStep: SharedModelGesuchFormStep;
  dokumentTyp: DokumentTyp;
  gesuchDokument?: GesuchDokument;
  dokumentOptions: DokumentOptions;
}

export interface SharedModelTableCustomDokument {
  dokumentTyp: CustomDokumentTyp;
  canDelete: boolean;
  showUpload: boolean;
  gesuchDokument?: GesuchDokument;
  dokumentOptions: DokumentOptions;
}

export type SharedModelTableAdditionalDokument = {
  dokumentTyp: UnterschriftenblattDokumentTyp;
  gesuchDokument?: UnterschriftenblattDokument;
  dokumentOptions: DokumentOptions;
};

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
) => {
  switch (dokumentModel.art) {
    case 'GESUCH_DOKUMENT':
    case 'CUSTOM_DOKUMENT': {
      return (
        permission.canUploadDocuments &&
        dokumentModel.gesuchDokument?.status !== 'AKZEPTIERT'
      );
    }
    case 'UNTERSCHRIFTENBLATT': {
      return permission.canUploadUnterschriftenblatt;
    }
  }
};
