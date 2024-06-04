import { Injectable, inject } from '@angular/core';

import { <%= classify(name) %>Store } from '@dv/<%= scope %>/data-access/<%= dasherize(name) %>';

@Injectable({
  providedIn: 'root',
})
export class <%= classify(name) %>Service {
  private <%- camelize(name) %>Store = inject(<%= classify(name) %>Store);
}
