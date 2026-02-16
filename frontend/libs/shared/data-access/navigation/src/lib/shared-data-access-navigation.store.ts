import { Injectable, computed } from '@angular/core';
import { patchState, signalStore, withState } from '@ngrx/signals';

type NavigationItem = {
  label: string;
  route: string[];
};

type NavigationState = {
  navigationitems: NavigationItem[];
};

const initialState: NavigationState = {
  navigationitems: [],
};

@Injectable({ providedIn: 'root' })
export class NavigationStore extends signalStore(
  { protectedState: false },
  withState(initialState),
) {
  setNavigationItems = (navigationitems: NavigationItem[]) =>
    patchState(this, { navigationitems });

  navigationViewSig = computed(() => this.navigationitems());
}
