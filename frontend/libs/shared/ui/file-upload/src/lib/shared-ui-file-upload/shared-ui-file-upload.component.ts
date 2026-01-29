import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  input,
  output,
  signal,
  viewChild,
} from '@angular/core';
import {
  ControlValueAccessor,
  FormControl,
  NG_VALUE_ACCESSOR,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatError } from '@angular/material/form-field';

import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiDropFileComponent } from '@dv/shared/ui/drop-file';

@Component({
  selector: 'dv-shared-ui-file-upload',
  imports: [
    ReactiveFormsModule,
    MatError,
    SharedUiAdvTranslocoDirective,
    SharedUiDropFileComponent,
  ],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      multi: true,
      useExisting: SharedUiFileUploadComponent,
    },
  ],
  host: {
    class: 'tw:flex tw:flex-col tw:gap-2',
  },
  templateUrl: './shared-ui-file-upload.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFileUploadComponent implements ControlValueAccessor {
  private touched = false;
  allowedFileTypesSig = input<string[]>();
  selectedFileSig = output<File | null>();

  fileInputSig = viewChild<ElementRef<HTMLInputElement>>('fileInput');
  latestValueSig = signal<File | null>(null);
  fileControl = new FormControl<File | undefined>(
    undefined,
    Validators.required,
  );

  markAsTouched() {
    if (!this.touched) {
      this.onTouched();
      this.touched = true;
      this.fileControl.markAsTouched();
    }
  }

  // ControlValueAccessor implementation
  writeValue(): void {
    // Empty, not writable
  }

  registerOnChange(fn: (value: File | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    if (isDisabled) {
      this.fileControl.disable({ emitEvent: false });
    } else {
      this.fileControl.enable({ emitEvent: false });
    }
  }

  // ControlValueAccessor methods - only deals with string IDs
  private onChange: (value: File | null) => void = () => {
    // Default empty implementation
  };
  private onTouched: () => void = () => {
    // Default empty implementation
  };

  updateFileList(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = input.files;

    const value = files && files.length > 0 ? files[0] : null;
    this.onChange(value);
    this.selectedFileSig.emit(value);
    this.latestValueSig.set(value);
  }

  resetSelectedFile() {
    this.onChange(null);
    this.selectedFileSig.emit(null);
    this.latestValueSig.set(null);
    this.fileControl.patchValue(undefined);
    const input = this.fileInputSig()?.nativeElement;
    if (input) {
      input.value = '';
    }
  }
}
