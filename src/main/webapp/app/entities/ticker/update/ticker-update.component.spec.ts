import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { TickerService } from '../service/ticker.service';
import { ITicker } from '../ticker.model';

import { TickerFormService } from './ticker-form.service';

import { TickerUpdateComponent } from './ticker-update.component';

describe('Ticker Management Update Component', () => {
  let comp: TickerUpdateComponent;
  let fixture: ComponentFixture<TickerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tickerFormService: TickerFormService;
  let tickerService: TickerService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TickerUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TickerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TickerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tickerFormService = TestBed.inject(TickerFormService);
    tickerService = TestBed.inject(TickerService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const ticker: ITicker = { id: 456 };
      const ownedBy: IUser = { id: 29119 };
      ticker.ownedBy = ownedBy;

      const userCollection: IUser[] = [{ id: 4200 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [ownedBy];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ticker });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ticker: ITicker = { id: 456 };
      const ownedBy: IUser = { id: 20189 };
      ticker.ownedBy = ownedBy;

      activatedRoute.data = of({ ticker });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(ownedBy);
      expect(comp.ticker).toEqual(ticker);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITicker>>();
      const ticker = { id: 123 };
      jest.spyOn(tickerFormService, 'getTicker').mockReturnValue(ticker);
      jest.spyOn(tickerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ticker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ticker }));
      saveSubject.complete();

      // THEN
      expect(tickerFormService.getTicker).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tickerService.update).toHaveBeenCalledWith(expect.objectContaining(ticker));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITicker>>();
      const ticker = { id: 123 };
      jest.spyOn(tickerFormService, 'getTicker').mockReturnValue({ id: null });
      jest.spyOn(tickerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ticker: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ticker }));
      saveSubject.complete();

      // THEN
      expect(tickerFormService.getTicker).toHaveBeenCalled();
      expect(tickerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITicker>>();
      const ticker = { id: 123 };
      jest.spyOn(tickerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ticker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tickerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
