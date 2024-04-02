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

    const pdf = new jsPDF();
    results.forEach((result, index) => {
      pdf.addImage(result, 'JPEG', 0, 0, 100, 100);
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
          .map((f) => f.name.replace(/\..+$/, ''))
          .join('-')}.pdf`,
        {
          type: 'application/pdf',
        },
      ),
    ];
  }
}
