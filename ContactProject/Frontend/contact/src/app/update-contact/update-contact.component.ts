import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Contact } from '../models/contact';
import { ContactServiceService } from '../service/contact-service.service';
// HttpErrorResponse might not be strictly needed here if using the service's error handler
// But it's fine to keep if you want specific checks
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-update-contact',
  standalone: false, // Usually not needed
  templateUrl: './update-contact.component.html',
  styleUrls: ['./update-contact.component.css']
})
export class UpdateContactComponent implements OnInit {

  @Input() contactToUpdate!: Contact;
  currentContact: Contact = { id: 0, name: '', mobileNo: '', landLineNo: '', address: '', pincode: '' };
  isSaving: boolean = false;
  error: string | null = null;

  constructor(
    public activeModal: NgbActiveModal,
    private contactService: ContactServiceService
  ) { }

  ngOnInit(): void {
    if (this.contactToUpdate) {
      this.currentContact = { ...this.contactToUpdate };
    } else {
      console.error("UpdateContactComponent did not receive contact data.");
      this.error = "Could not load contact data for editing.";
    }
  }

  onSubmit(updateForm: NgForm): void {
    if (updateForm.invalid) {
       Object.values(updateForm.controls).forEach(control => {
        control.markAsTouched();
      });
      this.error = "Please fix the errors in the form.";
      return;
    }

    this.isSaving = true;
    this.error = null;

    if (!this.currentContact || typeof this.currentContact.id === 'undefined') {
        this.error = "Cannot update contact: Missing ID.";
        this.isSaving = false;
        return;
    }

    this.contactService.updateData(this.contactToUpdate.id, this.currentContact).subscribe({
      next: (response: string) => {
        console.log('Update successful:', response); // response is now known to be a string
        this.isSaving = false;
        this.activeModal.close('saved');
      },
      error: (err: Error) => {
          console.error('Error updating contact:', err);
          this.error = `Failed to update contact: ${err.message || 'Unknown error'}`;
          this.isSaving = false;
      }
     
    });
  }

  dismiss(): void {
    this.activeModal.dismiss('cancel');
  }
}