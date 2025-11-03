import { Routes } from '@angular/router';

import { trancheRoutes } from '@dv/shared/model/gesuch';
import {
  AUSBILDUNG,
  AUSZAHLUNG,
  DARLEHEN,
  DOKUMENTE,
  EINNAHMEN_KOSTEN,
  EINNAHMEN_KOSTEN_PARTNER,
  ELTERN,
  ELTERN_STEUERERKLAERUNG_STEPS,
  FAMILIENSITUATION,
  GESCHWISTER,
  KINDER,
  LEBENSLAUF,
  PARTNER,
  PERSON,
  TRANCHE,
} from '@dv/shared/model/gesuch-form';

export const baseGesuchFormRoutes: Routes = [
  {
    path: TRANCHE.route,
    resolve: {
      step: () => TRANCHE,
    },
    title: TRANCHE.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-tranche').then(
        (m) => m.sharedFeatureGesuchFormTrancheRoutes,
      ),
  },
  {
    path: KINDER.route,
    resolve: {
      step: () => KINDER,
    },
    title: KINDER.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-kinder').then(
        (m) => m.gesuchAppFeatureGesuchFormKinderRoutes,
      ),
  },
  {
    path: LEBENSLAUF.route,
    resolve: {
      step: () => LEBENSLAUF,
    },
    title: LEBENSLAUF.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-lebenslauf').then(
        (m) => m.gesuchAppFeatureGesuchFormLebenslaufRoutes,
      ),
  },
  {
    path: GESCHWISTER.route,
    resolve: {
      step: () => GESCHWISTER,
    },
    title: GESCHWISTER.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-geschwister').then(
        (m) => m.gesuchAppFeatureGesuchFormGeschwisterRoutes,
      ),
  },
  {
    path: AUSZAHLUNG.route,
    resolve: {
      step: () => AUSZAHLUNG,
    },
    title: AUSZAHLUNG.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-auszahlung').then(
        (m) => m.sharedFeatureGesuchFormAuszahlungRoutes,
      ),
  },
  {
    path: FAMILIENSITUATION.route,
    resolve: {
      step: () => FAMILIENSITUATION,
    },
    title: FAMILIENSITUATION.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-familiensituation').then(
        (m) => m.gesuchAppFeatureGesuchFormFamiliensituationRoutes,
      ),
  },
  {
    path: PARTNER.route,
    resolve: {
      step: () => PARTNER,
    },
    title: PARTNER.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-partner').then(
        (m) => m.gesuchAppFeatureGesuchFormPartnerRoutes,
      ),
  },
  {
    path: ELTERN.route,
    resolve: {
      step: () => ELTERN,
    },
    title: ELTERN.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-eltern').then(
        (m) => m.gesuchAppFeatureGesuchFormElternRoutes,
      ),
  },
  ...Object.values({
    ...ELTERN_STEUERERKLAERUNG_STEPS,
  }).map((step) => ({
    path: step.route,
    resolve: {
      step: () => step,
    },
    title: step.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-eltern-steuererklaerung').then(
        (m) => m.sharedFeatureGesuchFormElternSteuererklaerungRoutes,
      ),
  })),
  {
    path: PERSON.route,
    resolve: {
      step: () => PERSON,
    },
    title: PERSON.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-person').then(
        (m) => m.gesuchAppFeatureGesuchFormPersonRoutes,
      ),
  },
  {
    path: AUSBILDUNG.route,
    resolve: {
      step: () => AUSBILDUNG,
    },
    title: AUSBILDUNG.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/ausbildung').then(
        (m) => m.sharedFeatureAusbildungRoutes,
      ),
  },
  {
    path: EINNAHMEN_KOSTEN.route,
    resolve: {
      step: () => EINNAHMEN_KOSTEN,
    },
    data: {
      einkommenTyp: 'PIA',
    },
    title: EINNAHMEN_KOSTEN.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-einnahmenkosten').then(
        (m) => m.gesuchAppFeatureGesuchFormEinnahmenkostenRoutes,
      ),
  },
  {
    path: EINNAHMEN_KOSTEN_PARTNER.route,
    resolve: {
      step: () => EINNAHMEN_KOSTEN_PARTNER,
    },
    data: {
      einkommenTyp: 'PARTNER',
    },
    title: EINNAHMEN_KOSTEN_PARTNER.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-einnahmenkosten').then(
        (m) => m.gesuchAppFeatureGesuchFormEinnahmenkostenRoutes,
      ),
  },
  {
    path: DARLEHEN.route,
    resolve: {
      step: () => DARLEHEN,
    },
    title: DARLEHEN.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/darlehen').then(
        (m) => m.sharedFeatureDarlehenRoutes,
      ),
  },
  {
    path: DOKUMENTE.route,
    resolve: {
      step: () => DOKUMENTE,
    },
    title: DOKUMENTE.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-dokumente').then(
        (m) => m.sharedFeatureGesuchDokumenteRoutes,
      ),
  },
  ...trancheRoutes.map((route) => ({
    path: `:id/${route}/:trancheId`,
    redirectTo: `${TRANCHE.route}/:id/${route}/:trancheId`,
  })),
];
