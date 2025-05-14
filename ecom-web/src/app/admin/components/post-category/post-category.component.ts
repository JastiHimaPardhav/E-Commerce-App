import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-category',
  standalone: false,
  templateUrl: './post-category.component.html',
  styleUrl: './post-category.component.scss'
})
export class PostCategoryComponent {

  categoryForm! : FormGroup;

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private adminService: AdminService,
    private router: Router
  ){}

  ngOnInit():void{
    this.categoryForm = this.fb.group({
      name:[null, [Validators.required]],
      description:[null,[Validators.required]]
    })
  }
  
  
  addCategory(): void {
    if (this.categoryForm.valid) {
      this.adminService.addCategory(this.categoryForm.value).subscribe((res) => {
        if (res.id != null) {
          this.snackBar.open('Category Posted Successfully!', 'Close', {
            duration: 5000
          });

          this.router.navigateByUrl('/admin/dashboard');
        } else {
          this.snackBar.open(res.message, 'close', {
            duration: 5000,
            panelClass: 'error-snackbar'
          });
        }
      })
    } else {
      this.categoryForm.markAllAsTouched();
    }
  }

}
