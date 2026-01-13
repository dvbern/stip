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
  host: {
    class: 'tw:flex',
  },
  templateUrl: './shared-ui-file-upload.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFileUploadComponent implements ControlValueAccessor {
  private touched = false;
  allowedFileTypesSig = input.required<string[]>();
  selectedFileSig = output<File | null>();

  fileInputSig = viewChild<ElementRef<HTMLInputElement>>('fileInput');
  latestValueSig = signal<File | undefined>(undefined);
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
  writeValue(file: File | undefined): void {
    this.latestValueSig.set(file);
  }

  registerOnChange(fn: (value: string | undefined) => void): void {
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
  private onChange: (value: string | undefined) => void = () => {
    // Default empty implementation
  };
  private onTouched: () => void = () => {
    // Default empty implementation
  };

  updateFileList(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = input.files;

    if (files && files.length > 0) {
      this.selectedFileSig.emit(files[0]);
    } else {
      this.selectedFileSig.emit(null);
    }
  }

  resetSelectedFile() {
    this.onChange(undefined);
    this.selectedFileSig.emit(null);
    this.fileControl.patchValue(undefined);
    const input = this.fileInputSig()?.nativeElement;
    if (input) {
      input.value = '';
    }
  }
}
