import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITicker } from '../ticker.model';
import { TickerService } from '../service/ticker.service';
import { TickerFormService, TickerFormGroup } from './ticker-form.service';

@Component({
  standalone: true,
  selector: 'jhi-ticker-update',
  templateUrl: './ticker-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TickerUpdateComponent implements OnInit {
  isSaving = false;
  ticker: ITicker | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: TickerFormGroup = this.tickerFormService.createTickerFormGroup();

  constructor(
    protected tickerService: TickerService,
    protected tickerFormService: TickerFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ticker }) => {
      this.ticker = ticker;
      if (ticker) {
        this.updateForm(ticker);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ticker = this.tickerFormService.getTicker(this.editForm);
    if (ticker.id !== null) {
      this.subscribeToSaveResponse(this.tickerService.update(ticker));
    } else {
      this.subscribeToSaveResponse(this.tickerService.create(ticker));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITicker>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ticker: ITicker): void {
    this.ticker = ticker;
    this.tickerFormService.resetForm(this.editForm, ticker);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, ticker.ownedBy);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.ticker?.ownedBy)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
