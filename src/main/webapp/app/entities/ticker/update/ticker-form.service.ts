import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITicker, NewTicker } from '../ticker.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITicker for edit and NewTickerFormGroupInput for create.
 */
type TickerFormGroupInput = ITicker | PartialWithRequiredKeyOf<NewTicker>;

type TickerFormDefaults = Pick<NewTicker, 'id'>;

type TickerFormGroupContent = {
  id: FormControl<ITicker['id'] | NewTicker['id']>;
  currency: FormControl<ITicker['currency']>;
  description: FormControl<ITicker['description']>;
  displaySymbol: FormControl<ITicker['displaySymbol']>;
  figi: FormControl<ITicker['figi']>;
  mic: FormControl<ITicker['mic']>;
  shareClassFIGI: FormControl<ITicker['shareClassFIGI']>;
  symbol: FormControl<ITicker['symbol']>;
  symbol2: FormControl<ITicker['symbol2']>;
  type: FormControl<ITicker['type']>;
  ownedBy: FormControl<ITicker['ownedBy']>;
};

export type TickerFormGroup = FormGroup<TickerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TickerFormService {
  createTickerFormGroup(ticker: TickerFormGroupInput = { id: null }): TickerFormGroup {
    const tickerRawValue = {
      ...this.getFormDefaults(),
      ...ticker,
    };
    return new FormGroup<TickerFormGroupContent>({
      id: new FormControl(
        { value: tickerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      currency: new FormControl(tickerRawValue.currency),
      description: new FormControl(tickerRawValue.description),
      displaySymbol: new FormControl(tickerRawValue.displaySymbol),
      figi: new FormControl(tickerRawValue.figi),
      mic: new FormControl(tickerRawValue.mic),
      shareClassFIGI: new FormControl(tickerRawValue.shareClassFIGI),
      symbol: new FormControl(tickerRawValue.symbol),
      symbol2: new FormControl(tickerRawValue.symbol2),
      type: new FormControl(tickerRawValue.type),
      ownedBy: new FormControl(tickerRawValue.ownedBy),
    });
  }

  getTicker(form: TickerFormGroup): ITicker | NewTicker {
    return form.getRawValue() as ITicker | NewTicker;
  }

  resetForm(form: TickerFormGroup, ticker: TickerFormGroupInput): void {
    const tickerRawValue = { ...this.getFormDefaults(), ...ticker };
    form.reset(
      {
        ...tickerRawValue,
        id: { value: tickerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TickerFormDefaults {
    return {
      id: null,
    };
  }
}
