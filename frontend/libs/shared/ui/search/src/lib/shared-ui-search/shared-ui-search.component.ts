import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'dv-shared-ui-search',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, TranslateModule],
  templateUrl: './shared-ui-search.component.html',
  styleUrls: ['./shared-ui-search.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiSearchComponent {}
