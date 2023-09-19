import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  EventEmitter,
  HostBinding,
  HostListener,
  Output,
  Renderer2,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'dv-shared-ui-drop-file',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './shared-ui-drop-file.component.html',
  styleUrls: ['./shared-ui-drop-file.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiDropFileComponent {
  @Output() filesDropped = new EventEmitter<File[]>();

  private elementRef: ElementRef<HTMLElement> = inject(ElementRef);
  private renderer = inject(Renderer2);

  @HostBinding('class') classes = 'p-5 border border-2 rounded';

  @HostListener('dragover', ['$event']) public onDragOver(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    this.renderer.addClass(this.elementRef.nativeElement, 'dragging');
  }

  @HostListener('dragleave', ['$event']) public onDragLeave(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    this.renderer.removeClass(this.elementRef.nativeElement, 'dragging');
  }

  @HostListener('drop', ['$event']) public onDrop(evt: DragEvent) {
    evt.preventDefault();
    evt.stopPropagation();
    this.renderer.removeClass(this.elementRef.nativeElement, 'dragging');
    if (!evt.dataTransfer) {
      return;
    }

    const files = Array.from(evt.dataTransfer.files);

    if (files.length > 0) {
      this.filesDropped.emit(files);
    }
  }
}
