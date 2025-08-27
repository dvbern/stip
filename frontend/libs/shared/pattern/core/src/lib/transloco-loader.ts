import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Translation, TranslocoLoader } from '@jsverse/transloco';
import { forkJoin, map } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TranslocoHttpLoader implements TranslocoLoader {
  private http = inject(HttpClient);

  getTranslation(lang: string) {
    return forkJoin({
      base: this.http.get<Translation>(`/assets/i18n/${lang}.json`),
      shared: this.http.get<Translation>(`/assets/i18n/shared.${lang}.json`),
    }).pipe(map(({ base, shared }) => ({ ...base, ...shared })));
  }
}
