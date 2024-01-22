import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITicker } from '../ticker.model';
import { TickerService } from '../service/ticker.service';

@Component({
  standalone: true,
  templateUrl: './ticker-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TickerDeleteDialogComponent {
  ticker?: ITicker;

  constructor(
    protected tickerService: TickerService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tickerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
