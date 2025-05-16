import { UpperCasePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

import {
  DEFAULT_LANGUAGE,
  Language,
  SUPPORTED_LANGUAGES,
} from '@dv/shared/model/language';

@Component({
    selector: 'dv-shared-ui-language-selector',
    imports: [UpperCasePipe, TranslatePipe],
    templateUrl: './shared-ui-language-selector.component.html',
    styleUrls: ['./shared-ui-language-selector.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class SharedUiLanguageSelectorComponent {
  @Input() language: Language = DEFAULT_LANGUAGE;
  @Input() languages: Language[] = SUPPORTED_LANGUAGES;
  @Output() languageChange = new EventEmitter<Language>();

  trackByIndex(index: number): number {
    return index;
  }

  handleLanguageChange(lang: Language) {
    this.languageChange.emit(lang);
  }
}
