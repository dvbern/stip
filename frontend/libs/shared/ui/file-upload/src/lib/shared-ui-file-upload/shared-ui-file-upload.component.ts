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
  private injector = inject(Injector);
  ngControl = inject(NgControl, { optional: true });
  allowedFileTypesSig = input<string[]>();
  multipleSig = input<boolean>();
  selectedFileSig = output<File[] | undefined>();

  fileInputSig = viewChild<ElementRef<HTMLInputElement>>('fileInput');
  latestValueSig = signal<File[] | undefined>(undefined);
  fileControl = new FormControl<File[] | undefined>(undefined);

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
          this.fileControl.markAsTouched();
        } else {
          this.fileControl.markAsUntouched();
        }
      });
    });
  }

  // ControlValueAccessor implementation
  writeValue(): void {
    // Empty, not writable
  }

  registerOnChange(fn: (value: File[] | undefined) => void): void {
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
  private onChange: (value: File[] | undefined) => void = () => {
    // Default empty implementation
  };
  private onTouched: () => void = () => {
    // Default empty implementation
  };

  updateFileList(event: Event) {
    const input = event.target as HTMLInputElement;
    const files = input.files;

    const values = files && files.length > 0 ? Array.from(files) : undefined;
    this.onChange(values);
    this.onTouched();
    this.selectedFileSig.emit(values);
    this.latestValueSig.set(values);
  }

  resetSelectedFile() {
    this.onChange(undefined);
    this.selectedFileSig.emit(undefined);
    this.latestValueSig.set(undefined);
    this.fileControl.patchValue(undefined);
    const input = this.fileInputSig()?.nativeElement;
    if (input) {
      input.value = '';
    }
  }
}
