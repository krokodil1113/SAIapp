import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ticker.test-samples';

import { TickerFormService } from './ticker-form.service';

describe('Ticker Form Service', () => {
  let service: TickerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TickerFormService);
  });

  describe('Service methods', () => {
    describe('createTickerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTickerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            currency: expect.any(Object),
            description: expect.any(Object),
            displaySymbol: expect.any(Object),
            figi: expect.any(Object),
            mic: expect.any(Object),
            shareClassFIGI: expect.any(Object),
            symbol: expect.any(Object),
            symbol2: expect.any(Object),
            type: expect.any(Object),
            ownedBy: expect.any(Object),
          }),
        );
      });

      it('passing ITicker should create a new form with FormGroup', () => {
        const formGroup = service.createTickerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            currency: expect.any(Object),
            description: expect.any(Object),
            displaySymbol: expect.any(Object),
            figi: expect.any(Object),
            mic: expect.any(Object),
            shareClassFIGI: expect.any(Object),
            symbol: expect.any(Object),
            symbol2: expect.any(Object),
            type: expect.any(Object),
            ownedBy: expect.any(Object),
          }),
        );
      });
    });

    describe('getTicker', () => {
      it('should return NewTicker for default Ticker initial value', () => {
        const formGroup = service.createTickerFormGroup(sampleWithNewData);

        const ticker = service.getTicker(formGroup) as any;

        expect(ticker).toMatchObject(sampleWithNewData);
      });

      it('should return NewTicker for empty Ticker initial value', () => {
        const formGroup = service.createTickerFormGroup();

        const ticker = service.getTicker(formGroup) as any;

        expect(ticker).toMatchObject({});
      });

      it('should return ITicker', () => {
        const formGroup = service.createTickerFormGroup(sampleWithRequiredData);

        const ticker = service.getTicker(formGroup) as any;

        expect(ticker).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITicker should not enable id FormControl', () => {
        const formGroup = service.createTickerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTicker should disable id FormControl', () => {
        const formGroup = service.createTickerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
