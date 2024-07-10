import { Route } from '@angular/router';

import {
  AUSBILDUNG,
  AUSZAHLUNG,
  DOKUMENTE,
  EINNAHMEN_KOSTEN,
  ELTERN,
  ELTERN_STEUER_STEPS,
  FAMILIENSITUATION,
  GESCHWISTER,
  KINDER,
  LEBENSLAUF,
  PARTNER,
  PERSON,
  PROTOKOLL,
} from '@dv/shared/model/gesuch-form';

export const sachbearbeitungAppFeatureGesuchFormRoutes: Route[] = [
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
      import('@dv/shared/feature/gesuch-form-auszahlungen').then(
        (m) => m.gesuchAppFeatureGesuchFormAuszahlungenRoutes,
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
  ...Object.values(ELTERN_STEUER_STEPS).map((step) => ({
    path: step.route,
    resolve: {
      step: () => step,
    },
    title: step.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-eltern-steuerdaten').then(
        (m) => m.sharedFeatureGesuchFormElternSteuerdatenRoutes,
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
      import('@dv/shared/feature/gesuch-form-education').then(
        (m) => m.gesuchAppFeatureGesuchFormEducationRoutes,
      ),
  },
  {
    path: EINNAHMEN_KOSTEN.route,
    resolve: {
      step: () => EINNAHMEN_KOSTEN,
    },
    title: EINNAHMEN_KOSTEN.translationKey,
    loadChildren: () =>
      import('@dv/shared/feature/gesuch-form-einnahmenkosten').then(
        (m) => m.gesuchAppFeatureGesuchFormEinnahmenkostenRoutes,
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
  {
    path: PROTOKOLL.route,
    resolve: {
      step: () => PROTOKOLL,
    },
    title: 'shared.protokoll.title',
    loadChildren: () =>
      import(
        '@dv/sachbearbeitung-app/feature/gesuch-form-statusprotokoll'
      ).then((m) => m.sachbearbeitungAppFeatureGesuchFormStatusprotokollRoutes),
  },
];
