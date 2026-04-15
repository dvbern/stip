import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Injector,
  OnInit,
  effect,
  inject,
  input,
  output,
  runInInjectionContext,
  signal,
  viewChild,
} from '@angular/core';
import {
  ControlValueAccessor,
  FormControl,
  NgControl,
  ReactiveFormsModule,
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
    class: 'tw:flex tw:flex-col tw:gap-2',
  },
  templateUrl: './shared-ui-file-upload.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFileUploadComponent
  implements ControlValueAccessor, OnInit
{
  private touched = false;
  private injector = inject(Injector);
  ngControl = inject(NgControl, { optional: true });
  allowedFileTypesSig = input<string[]>();
  selectedFileSig = output<File | null>();

  fileInputSig = viewChild<ElementRef<HTMLInputElement>>('fileInput');
  latestValueSig = signal<File | null>(null);
  fileControl = new FormControl<File | undefined>(undefined);

  constructor() {
    // this is a workaround to get access to the NgControl instance and not run into circular dependency issues
    // https://stackoverflow.com/questions/45755958/how-to-get-formcontrol-instance-from-controlvalueaccessor
    if (this.ngControl) {
      this.ngControl.valueAccessor = this;
    }
  }

  ngOnInit() {
    runInInjectionContext(this.injector, () => {
      effect(() => {
        const touched = this.ngControl?.control?.['touchedReactive']();
        if (touched) {
          this.markAsTouched();
        }
      });
    });
  }

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
