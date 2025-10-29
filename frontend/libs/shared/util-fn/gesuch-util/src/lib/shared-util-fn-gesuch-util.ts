import { IChange, diff } from 'json-diff-ts';

import { RolesMap } from '@dv/shared/model/benutzer';
import { CompileTimeConfig } from '@dv/shared/model/config';
import {
  AppTrancheChange,
  ElternTyp,
  ElternUpdate,
  FamiliensituationUpdate,
  FormPropsExcluded,
  GSFormStepProps,
  GesuchFormular,
  GesuchFormularType,
  GesuchTranche,
  GesuchUrlType,
  SBFormStepProps,
  SharedModelGesuch,
  SteuerdatenTyp,
  TrancheSetting,
} from '@dv/shared/model/gesuch';
import {
  ABSCHLUSS,
  ELTERN_STEUERDATEN_STEPS,
  ELTERN_STEUERERKLAERUNG_STEPS,
  FormRoutesToPropsMap,
  GesuchFormStep,
  GesuchFormStepView,
  isSteuererklaerungStep,
} from '@dv/shared/model/gesuch-form';
import { capitalized, lowercased } from '@dv/shared/model/type-util';

export interface ElternSituation {
  expectVater: boolean;
  expectMutter: boolean;
  vater?: ElternUpdate;
  mutter?: ElternUpdate;
}

export function calculateElternSituationGesuch(
  gesuch: GesuchFormularType | null,
): ElternSituation {
  return calculateElternSituation(gesuch?.familiensituation, gesuch?.elterns);
}

function calculateElternSituation(
  familienSituation: FamiliensituationUpdate | undefined,
  elterns: ElternUpdate[] | undefined,
): ElternSituation {
  return {
    expectVater: calculateExpectElternteil(ElternTyp.VATER, familienSituation),
    expectMutter: calculateExpectElternteil(
      ElternTyp.MUTTER,
      familienSituation,
    ),
    vater: findElternteil(ElternTyp.VATER, elterns),
    mutter: findElternteil(ElternTyp.MUTTER, elterns),
  };
}

export function calculateExpectElternteil(
  type: ElternTyp,
  familienSituation: FamiliensituationUpdate | undefined,
): boolean {
  if (familienSituation) {
    const elternteilLebt = familienSituation.elternteilUnbekanntVerstorben
      ? familienSituation?.[`${lowercased(type)}UnbekanntVerstorben`] ===
        'WEDER_NOCH'
      : true;
    const elternteilZahltKeineAlimente =
      familienSituation.werZahltAlimente !== type &&
      familienSituation.werZahltAlimente !== 'GEMEINSAM';
    return elternteilLebt && elternteilZahltKeineAlimente;
  }
  return false;
}

export function findElternteil(
  elternTyp: ElternTyp,
  elterns: ElternUpdate[] | undefined,
): ElternUpdate | undefined {
  return elterns?.find((eltern) => eltern.elternTyp === elternTyp);
}

/**
 * Flatten the given type to a non-array type.
 */
type Unpack<T> = T extends Array<infer U> ? U : T;
/**
 * Extract the changeable properties of a GesuchFormular.
 */
type ChangeableProperties = Exclude<
  keyof GesuchFormularType,
  'steuerdatenTabs'
>;
/**
 * Defines the values of a GesuchFormular which are not arrays.
 */
type NonArrayForm = Exclude<
  Unpack<GesuchFormularType[ChangeableProperties]>,
  unknown[] | undefined
>;
/**
 * Defines the values of a GesuchFormular which are arrays.
 */
type ArrayForms = Extract<GesuchFormularType[ChangeableProperties], unknown[]>;

/**
 * Keys to skip when calculating the changes between two versions of a form.
 */
const keysToSkip = ['id'];

/**
 * This function checks which previous changes should be displayed in the view.
 * If the sachbearbeiter has made changes on the current formular view, the those changes will be displayed and not the previous changes from the gesuchsteller.
 * If the sachbearbeiter has not made changes on the current formular view, the previous changes from the gesuchsteller will be displayed.
 * @param view The view containing the GesuchFormular and the changes.
 * @param key The key of the GesuchFormular property.
 */
export const selectChangeForView = <
  K extends FormPropsExcluded | 'steuererklaerung',
>(
  view: {
    gesuchFormular: GesuchFormular | null;
    tranchenChanges: AppTrancheChange | null;
  },
  key: K,
): {
  current: GesuchFormularType[K] | undefined;
  previous: GesuchFormularType[K];
} => {
  const changes = view.tranchenChanges;
  const currentFormular = view.gesuchFormular?.[key];

  const sachbearbeiterHasChangesOnView =
    changes?.sb?.affectedSteps.includes(key) ?? false;

  const current = currentFormular;

  const previous = (
    sachbearbeiterHasChangesOnView
      ? changes?.sb?.tranche?.gesuchFormular?.[key]
      : changes?.gs?.tranche?.gesuchFormular?.[key]
  )!;

  return { current, previous };
};

export const stepHasChanges = (
  tranchenChanges: AppTrancheChange | null,
  step: GesuchFormStepView,
) => {
  return (
    tranchenChanges?.gs?.affectedSteps.includes(
      FormRoutesToPropsMap[step.route] ?? -1,
    ) ||
    tranchenChanges?.sb?.affectedSteps.includes(
      FormRoutesToPropsMap[step.route] ?? -1,
    )
  );
};

/**
 * Calculate the changes between two versions of a GesuchFormular property.
 */
export function getChangesForForm<T extends NonArrayForm, K extends keyof T>(
  changed?: T,
  original?: T,
): { [key in K]?: T[key] | undefined } {
  if (!original || !changed) {
    return {};
  }
  const rawDiff = diff(original, changed, { keysToSkip });
  const addChange = <T>(record: T, change: IChange) => {
    // Skip if the record is not an object or the key is already present
    if (!record || typeof record !== 'object' || change.key in record) {
      return record;
    }
    record[change.key as keyof T] =
      // Changes to undefined are marked with 2 entries, one REMOVE and one ADD
      // ADD/REMOVE operations don't have an oldValue
      change.type !== 'UPDATE' ? change.value : change.oldValue;
    return record;
  };

  const difference = rawDiff.reduce(
    (acc, c) => {
      if (c.changes) {
        acc[c.key as K] = c.changes.reduce(
          (sub, s) => addChange(sub, s),
          {} as T[K],
        );
      } else {
        addChange(acc, c);
      }
      return acc;
    },
    {} as {
      [key in K]?: T[key];
    },
  );

  if (Object.keys(difference).length === 0) {
    return {};
  }
  return difference;
}

/**
 * Calculate the changes between two lists of GesuchFormular properties using a custom identifier.
 */
export function getChangesForList<
  const T extends ArrayForms[number],
  const R extends string,
>(
  changed: T[] | undefined,
  original: T[] | undefined,
  getIdentifier?: (value: T) => R | undefined,
) {
  if (!original || !changed) {
    return null;
  }
  const _changes = diff(original, changed, {
    keysToSkip,
  }); // We only care about the first change because the dataset is just a list
  const changes = _changes[0];

  if (!changes) {
    return null;
  }

  // Identify new entries
  let newEntriesByIdentifier: Partial<Record<R, boolean>> = {};
  if (changes.type === 'UPDATE') {
    newEntriesByIdentifier =
      changes.changes
        ?.filter((c) => c.type === 'ADD')
        .reduce<Partial<Record<R, boolean>>>((entries, c) => {
          const identifier = getIdentifier?.(c.value);
          return { ...entries, [identifier ?? +c.key]: true };
        }, {}) ?? {};
  }

  // Collect all changes and associate them with the identifier
  const collectedChanges = original.map((c, i) => ({
    identifier: getIdentifier?.(c),
    values: getChangesForForm(
      // Compare using the identifier
      getIdentifier
        ? changed.find((cc) => getIdentifier(cc) === getIdentifier(c))
        : // Otherwise fallback to index comparison
          changed[i],
      c,
    ),
  }));

  if (
    collectedChanges.length === 0 &&
    Object.keys(newEntriesByIdentifier).length === 0
  ) {
    return null;
  }

  return {
    // Group changes by identifier
    changesByIndex: collectedChanges.map((c) => c.values),
    changesByIdentifier: collectedChanges.reduce(
      (acc, c) => ({
        ...acc,
        ...(c.identifier ? { [c.identifier]: c.values } : {}),
      }),
      {} as Record<R, T>,
    ),
    newEntriesByIdentifier,
    hasChangesByIdentifier: collectedChanges.reduce(
      (acc, c) => ({
        ...acc,
        ...(c.identifier
          ? { [c.identifier]: Object.keys(c.values ?? {}).length > 0 }
          : {}),
      }),
      {} as Record<R, boolean>,
    ),
  };
}

/**
 * Returns true if the gesuchFormular has the given property
 */
export const isGesuchFormularProp =
  (formKeys: string[]) =>
  (prop?: string): prop is GSFormStepProps => {
    if (!prop) return false;
    return formKeys.includes(prop);
  };

export const createTrancheSetting = (
  gesuchUrlTyp: GesuchUrlType | null,
  gesuchTranche: GesuchTranche | undefined,
): TrancheSetting | null => {
  return gesuchTranche && gesuchUrlTyp
    ? ({
        type: gesuchTranche.typ,
        gesuchUrlTyp,
        routesSuffix: [lowercased(gesuchUrlTyp), gesuchTranche.id],
      } as const)
    : null;
};

type AdditionalSteps = {
  after: GesuchFormStep;
  steps: GesuchFormStep[];
};

/**
 * Append additional steps after the given step
 */
export const appendSteps = (
  steps: GesuchFormStep[],
  additionalSteps: AdditionalSteps[],
) => {
  const afterMap = additionalSteps.reduce(
    (acc, { after, steps }) => {
      if (steps.length > 0) {
        acc[after.route] = steps;
      }
      return acc;
    },
    {} as Record<string, GesuchFormStep[]>,
  );
  return steps.reduce((acc, step) => {
    if (afterMap[step.route]) {
      return [...acc, step, ...afterMap[step.route]];
    }
    return [...acc, step];
  }, [] as GesuchFormStep[]);
};

export function addStepsByAppType(
  sharedSteps: GesuchFormStep[],
  rolesMap: RolesMap,
  steuerdatenTabs: SteuerdatenTyp[] | undefined,
  compileTimeConfig?: CompileTimeConfig,
) {
  switch (compileTimeConfig?.appType) {
    case 'gesuch-app':
      return [...sharedSteps, ABSCHLUSS];
    case 'sachbearbeitung-app': {
      const steuerdatenSteps =
        rolesMap.V0_Sachbearbeiter || rolesMap.V0_Freigabestelle
          ? steuerdatenTabs?.map((typ) => ({
              step: ELTERN_STEUERDATEN_STEPS[typ],
              type: typ,
            }))
          : null;
      return steuerdatenSteps
        ? appendSteps(
            sharedSteps,
            steuerdatenSteps.map((s) => {
              return {
                after: ELTERN_STEUERERKLAERUNG_STEPS[s.type],
                steps: [s.step],
              };
            }),
          )
        : sharedSteps;
    }

    default:
      return [];
  }
}

/**
 * Calculates the changes between the gesuchTrancheToWorkWith and previous tranches
 *
 * Returns an object of changes for GS and SB:
 * - tranche: the tranche containing the previous gesuchFormular
 * - affectedSteps: the steps that have changed
 */
export function prepareTranchenChanges(
  gesuch: SharedModelGesuch | null,
): AppTrancheChange | null {
  if (!gesuch) {
    return null;
  }
  const adresseIdFields = formularPropsContaining<{ adresse: { id?: string } }>(
    {
      personInAusbildung: null,
      elterns: null,
      partner: null,
    },
  ).map((step) => `${step}.adresse.id`);

  const idFields = formularPropsContaining<{ id?: string }>({
    steuererklaerung: null,
    lebenslaufItems: null,
    elterns: null,
    geschwisters: null,
    kinds: null,
  }).map((step) => `${step}.id`);

  /**
   * Changes have Zero, one or max two tranches
   * - Zero: No changes
   * - One: GS erstellt einen Antrag. Changes should be calculated between gesuchTrancheToWorkWith and changes[0]
   * - Two: SB bearbeitet einen Antrag (As soon as he changes status). Changes should be calculated between changes[1] and gesuchTrancheToWorkWith
   */
  const allChanges = gesuch.changes?.map((tranche) => {
    const changes = diff(
      tranche.gesuchFormular,
      gesuch.gesuchTrancheToWorkWith.gesuchFormular,
      {
        // The json-diff-ts library updated their handling of nested keys
        // https://github.com/ltwlf/json-diff-ts/pull/243/files#diff-b335630551682c19a781afebcf4d07bf978fb1f8ac04c6bf87428ed5106870f5
        keysToSkip: [...adresseIdFields, ...idFields],
        embeddedObjKeys: {
          /** Used to have a more accurate diff for steuerdaten in {@link hasSteuererklaerungChanges} */
          ['steuererklaerung']: 'steuerdatenTyp',
        },
      },
    );
    return {
      tranche,
      affectedSteps: [
        ...changes
          .filter(
            (c) =>
              // Ignore steuerdaten changes, they are handled separately
              !isSteuererklaerungStep(
                c.key as GSFormStepProps | SBFormStepProps,
              ) &&
              ((c.changes?.length ?? 0) > 0 ||
                // Also mark the step as affected if a new entry has been added or removed
                c.type !== 'UPDATE'),
          )
          .map((c) => c.key),
        ...hasSteuererklaerungChanges(changes),
      ],
    };
  });

  if (!allChanges || allChanges.length <= 0) {
    return null;
  }

  return {
    gs: allChanges[0],
    sb: allChanges[1],
  };
}

/**
 * Used to mark steuerdatenVater/Mutter Tabs as affected if steuerdatenTyp has changed to FAMILIE
 * or back to individual
 */
export const hasSteuererklaerungChanges = (
  changes: IChange[],
): GSFormStepProps[] => {
  const steuererklaerungChange = changes.find(
    (c) =>
      isSteuererklaerungStep(c.key as GSFormStepProps | SBFormStepProps) &&
      c.type === 'UPDATE',
  );
  const affectedSteps = new Set<GSFormStepProps>();

  // Check if steuerdaten have changed
  (['MUTTER', 'VATER', 'FAMILIE'] satisfies SteuerdatenTyp[]).forEach(
    (steuerdatenTyp) => {
      const steuerdatenTypChange = steuererklaerungChange?.changes?.find(
        (c) => c.key === steuerdatenTyp,
      );
      if (
        steuerdatenTypChange &&
        ((steuerdatenTypChange.changes ?? []).length > 0 ||
          steuerdatenTypChange.type !== 'UPDATE')
      ) {
        affectedSteps.add(
          `steuererklaerung${capitalized(lowercased(steuerdatenTyp))}`,
        );
      }
    },
  );

  return Array.from(affectedSteps);
};

type RelevantFields = Exclude<keyof GesuchFormular, 'ausbildung'>;

type FieldsThatContain<
  T extends Record<string, unknown>,
  U = RelevantFields,
> = U extends RelevantFields
  ? Exclude<GesuchFormular[U], undefined> extends T | T[]
    ? U
    : never
  : never;

/**
 * Returns Formular properties which types are containing the given sub-type
 *
 * @example
 * ```ts
 * formularPropsContaining<{ heimatort?: string }>({
 *     personInAusbildung: null,
 * });
 * ```
 */
const formularPropsContaining = <T extends Record<string, unknown>>(
  obj: Record<FieldsThatContain<T>, null>,
) => Object.keys(obj) as FieldsThatContain<T>[];
