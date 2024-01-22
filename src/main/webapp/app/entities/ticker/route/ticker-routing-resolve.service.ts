import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITicker } from '../ticker.model';
import { TickerService } from '../service/ticker.service';

export const tickerResolve = (route: ActivatedRouteSnapshot): Observable<null | ITicker> => {
  const symbol = route.params['symbol'];
  if (symbol) {
    return inject(TickerService)
      .find(symbol)
      .pipe(
        mergeMap((ticker: HttpResponse<ITicker>) => {
          if (ticker.body) {
            return of(ticker.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);

  /*   const id = route.params['id'];
  if (id) {
    return inject(TickerService)
      .find(id)
      .pipe(
        mergeMap((ticker: HttpResponse<ITicker>) => {
          if (ticker.body) {
            return of(ticker.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null); */
};

export default tickerResolve;
