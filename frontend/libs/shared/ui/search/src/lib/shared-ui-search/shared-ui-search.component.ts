import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';

@Component({
  selector: 'dv-shared-ui-search',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    TranslocoPipe,
    SharedUiMaxLengthDirective,
  ],
  templateUrl: './shared-ui-search.component.html',
  styleUrls: ['./shared-ui-search.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiSearchComponent {}
