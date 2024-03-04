import { Pipe, PipeTransform } from '@angular/core';
import { FileSizeOptionsBase, filesize } from 'filesize';

@Pipe({
  standalone: true,
  name: 'filesize',
})
export class FilesizePipe implements PipeTransform {
  transform(value: string | number, format?: FileSizeOptionsBase): string {
    return filesize(+value, format ?? {});
  }
}
