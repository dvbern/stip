import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
  input,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormBuilder,
  FormControl,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { TranslatePipe } from '@ngx-translate/core';

import { Land } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiZuvorHintDirective,
} from '@dv/shared/ui/form';
import { SharedUiTranslateChangePipe } from '@dv/shared/ui/translate-change';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

@Component({
  selector: 'dv-shared-ui-land-autocomplete',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiZuvorHintDirective,
    SharedUiTranslateChangePipe,
  ],
  templateUrl: './shared-ui-land-autocomplete.component.html',
  styleUrl: './shared-ui-land-autocomplete.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiLandAutocompleteComponent {
  private landLookupService = inject(LandLookupService);
  laenderSig = this.landLookupService.getCachedLandLookup();
  private fb = inject(FormBuilder);

  landIdControlSig = input.required<FormControl<string | undefined>>();

  control = this.fb.nonNullable.control<Land | string | undefined>(undefined);
  controlChangeSig = toSignal(this.control.valueChanges);

  laenderValuesSig = computed(() => {
    const searchTerm = this.controlChangeSig();
    const laender = this.laenderSig()?.filter((l) => l.eintragGueltig);

    if (laender && typeof searchTerm === 'string') {
      return laender.filter((land) =>
        land.deKurzform.toLowerCase().includes(searchTerm.toLowerCase()),
      );
    }

    return [];
  });

  displayWithLand(land: Land | undefined): string {
    return land ? land.deKurzform : '';
  }
}
