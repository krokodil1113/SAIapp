import { ITicker, NewTicker } from './ticker.model';

export const sampleWithRequiredData: ITicker = {
  id: 20547,
};

export const sampleWithPartialData: ITicker = {
  id: 10154,
  currency: 'lest new',
  description: 'while',
  figi: 'in',
  mic: 'acorn supposing ringed',
  symbol: 'powerfully',
};

export const sampleWithFullData: ITicker = {
  id: 11944,
  currency: 'down',
  description: 'transcend overgeneralise eek',
  displaySymbol: 'bah',
  figi: 'aw',
  mic: 'limply',
  shareClassFIGI: 'till yawningly',
  symbol: 'plump cherish at',
  symbol2: 'until hm',
  type: 'to shakily',
};

export const sampleWithNewData: NewTicker = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
