import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TickerDetailComponent } from './ticker-detail.component';

describe('Ticker Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TickerDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TickerDetailComponent,
              resolve: { ticker: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TickerDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ticker on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TickerDetailComponent);

      // THEN
      expect(instance.ticker).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
