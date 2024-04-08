import { Injectable } from '@angular/core';
import type { jsPDF } from 'jspdf';

@Injectable({ providedIn: 'root' })
export class SharedUtilDocumentMergerService {
  jspdf?: typeof jsPDF;

  private async getJsPDF() {
    if (!this.jspdf) {
      // Lazy import to reduce initial bundle size
      this.jspdf = (await import('jspdf')).jsPDF;
    }

    return this.jspdf;
  }

  async mergeImageDocuments(context: string, files: File[]): Promise<File[]> {
    const jsPDF = await this.getJsPDF();
    const imageFiles = files.filter((file) => file.type.startsWith('image/'));
    const allFiles = files.filter((file) => !file.type.startsWith('image/'));

    if (imageFiles.length <= 1) {
      return files;
    }

    const results = await new Promise<string[]>((resolve) => {
      const results: string[] = [];
      imageFiles.forEach((file) => {
        const reader = new FileReader();
        reader.onload = (e) => {
          const result = e.target?.result;
          if (typeof result !== 'string') {
            return;
          }
          results.push(result);

          if (results.length === imageFiles.length) {
            resolve(results);
          }
        };
        reader.readAsDataURL(file);
      });
    });

    const pdf = new jsPDF('p', 'pt', 'a4', true);
    const margin = 10;
    const pdfWidth = pdf.internal.pageSize.getWidth() - margin * 2;
    const pdfHeight = pdf.internal.pageSize.getHeight() - margin * 2;

    results.forEach((result, index) => {
      const imgProps = pdf.getImageProperties(result);

      let imgHeight = imgProps.height;
      let imgWidth = imgProps.width;

      // Contain image within pdf or scale down if necessary
      if (imgWidth > pdfWidth) {
        const scale = pdfWidth / imgWidth;
        imgWidth *= scale;
        imgHeight *= scale;
      }
      if (imgHeight > pdfHeight) {
        const scale = pdfHeight / imgHeight;
        imgWidth *= scale;
        imgHeight *= scale;
      }

      pdf.addImage(
        result,
        'JPEG',
        margin,
        margin,
        imgWidth,
        imgHeight,
        `image-${index}`,
        'FAST',
      );
      if (index !== results.length - 1) {
        pdf.addPage();
      }
    });
    const blob = pdf.output('blob');

    return [
      ...allFiles,
      new File(
        [blob],
        `${context}_${imageFiles
          .map((f) => f.name.replace(/\.[^.]+$/, ''))
          .join('-')}.pdf`,
        {
          type: 'application/pdf',
        },
      ),
    ];
  }
}
