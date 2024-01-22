import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TickerComponent } from './list/ticker.component';
import { TickerDetailComponent } from './detail/ticker-detail.component';
import { TickerUpdateComponent } from './update/ticker-update.component';
import TickerResolve from './route/ticker-routing-resolve.service';

const tickerRoute: Routes = [
  {
    path: '',
    component: TickerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  /*  {
    path: ':id/view',
    component: TickerDetailComponent,
    resolve: {
      ticker: TickerResolve,
    }, */
  {
    path: ':symbol/view',
    component: TickerDetailComponent,
    resolve: {
      ticker: TickerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TickerUpdateComponent,
    resolve: {
      ticker: TickerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TickerUpdateComponent,
    resolve: {
      ticker: TickerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tickerRoute;
