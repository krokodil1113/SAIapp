import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITicker } from '../ticker.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ticker.test-samples';

import { TickerService } from './ticker.service';

const requireRestSample: ITicker = {
  ...sampleWithRequiredData,
};

describe('Ticker Service', () => {
  let service: TickerService;
  let httpMock: HttpTestingController;
  let expectedResult: ITicker | ITicker[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TickerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Ticker', () => {
      const ticker = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ticker).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ticker', () => {
      const ticker = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ticker).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ticker', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ticker', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ticker', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTickerToCollectionIfMissing', () => {
      it('should add a Ticker to an empty array', () => {
        const ticker: ITicker = sampleWithRequiredData;
        expectedResult = service.addTickerToCollectionIfMissing([], ticker);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ticker);
      });

      it('should not add a Ticker to an array that contains it', () => {
        const ticker: ITicker = sampleWithRequiredData;
        const tickerCollection: ITicker[] = [
          {
            ...ticker,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTickerToCollectionIfMissing(tickerCollection, ticker);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ticker to an array that doesn't contain it", () => {
        const ticker: ITicker = sampleWithRequiredData;
        const tickerCollection: ITicker[] = [sampleWithPartialData];
        expectedResult = service.addTickerToCollectionIfMissing(tickerCollection, ticker);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ticker);
      });

      it('should add only unique Ticker to an array', () => {
        const tickerArray: ITicker[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tickerCollection: ITicker[] = [sampleWithRequiredData];
        expectedResult = service.addTickerToCollectionIfMissing(tickerCollection, ...tickerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ticker: ITicker = sampleWithRequiredData;
        const ticker2: ITicker = sampleWithPartialData;
        expectedResult = service.addTickerToCollectionIfMissing([], ticker, ticker2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ticker);
        expect(expectedResult).toContain(ticker2);
      });

      it('should accept null and undefined values', () => {
        const ticker: ITicker = sampleWithRequiredData;
        expectedResult = service.addTickerToCollectionIfMissing([], null, ticker, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ticker);
      });

      it('should return initial array if no Ticker is added', () => {
        const tickerCollection: ITicker[] = [sampleWithRequiredData];
        expectedResult = service.addTickerToCollectionIfMissing(tickerCollection, undefined, null);
        expect(expectedResult).toEqual(tickerCollection);
      });
    });

    describe('compareTicker', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTicker(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTicker(entity1, entity2);
        const compareResult2 = service.compareTicker(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTicker(entity1, entity2);
        const compareResult2 = service.compareTicker(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTicker(entity1, entity2);
        const compareResult2 = service.compareTicker(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
