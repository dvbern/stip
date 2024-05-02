import { Injectable, TemplateRef, computed, signal } from '@angular/core';

@Injectable()
export class SharedUtilHeaderService {
  suffixSig = computed(() => this.setSuffixSig());
  private setSuffixSig = signal<TemplateRef<unknown> | null>(null);

  updateSuffix(suffix?: TemplateRef<unknown> | null) {
    this.setSuffixSig.set(suffix ?? null);
  }

  removeSuffix() {
    this.setSuffixSig.set(null);
  }
}
