import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Contact } from '../models/contact';
import { ContactServiceService } from '../service/contact-service.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-delete-contact',
  standalone: false,
  templateUrl: './delete-contact.component.html',
  styleUrls: ['./delete-contact.component.css'] // Corrected property name
})
export class DeleteContactComponent implements OnInit {

  @Input() contactToDelete!: Contact;

  isDeleting: boolean = false;
  error: string | null = null;

  constructor(
    public activeModal: NgbActiveModal,
    private contactService: ContactServiceService
  ) { }

  ngOnInit(): void {
    if (!this.contactToDelete) {
      console.error("DeleteContactComponent did not receive contact data.");
      this.error = "Cannot identify contact to delete.";
    }
  }

  confirmDelete(): void {
     // Ensure we have an ID before attempting delete
    if (!this.contactToDelete || typeof this.contactToDelete.id === 'undefined') {
        this.error = "Cannot delete contact: Missing ID.";
        this.isDeleting = false;
        return;
    }

    this.isDeleting = true;
    this.error = null;

    this.contactService.deleteData(this.contactToDelete.id).subscribe({
      next: (response) => {
        console.log('Delete successful:', response);
        this.isDeleting = false;
        this.activeModal.close('deleted'); // Close modal and pass 'deleted' result
      },
      error: (err: HttpErrorResponse) => {
        console.error('Error deleting contact:', err);
        this.error = `Failed to delete contact. ${err.error instanceof ProgressEvent ? 'Network error or server unavailable.' : err.error || err.message }`;
        this.isDeleting = false;
      }
    });
  }

  dismiss(): void {
    this.activeModal.dismiss('cancel');
  }
}