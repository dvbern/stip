import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiVersionTextComponent } from './shared-ui-version-text.component';

describe('SharedUiVersionTextComponent', () => {
  let component: SharedUiVersionTextComponent;
  let fixture: ComponentFixture<SharedUiVersionTextComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiVersionTextComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiVersionTextComponent);
    component = fixture.componentInstance;
    // Set version input
    component.version = {
      frontend: '1.0.0',
      backend: '1.0.0',
      sameVersion: true,
    };
    fixture.detectChanges();
  });

  it('should show 1 version if both are same', () => {
    expect(fixture.nativeElement.textContent).toContain('Version: 1.0.0');
    expect(fixture.nativeElement.textContent).not.toContain('Backend');
  });

  it('should show 2 versions if both are different', () => {
    fixture.componentRef.setInput('version', {
      frontend: '1.0.0',
      backend: '1.0.1',
      sameVersion: false,
    });
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('Frontend: 1.0.0');
    expect(fixture.nativeElement.textContent).toContain('Backend: 1.0.1');
  });
});
