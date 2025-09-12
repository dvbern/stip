import { Pipe, PipeTransform } from '@angular/core';
import { FilesizeOptions, filesize } from 'filesize';

@Pipe({
  standalone: true,
  name: 'filesize',
})
export class FilesizePipe implements PipeTransform {
  transform(value: string | number, format?: FilesizeOptions): string {
    return filesize(+value, format ?? {});
  }
}
