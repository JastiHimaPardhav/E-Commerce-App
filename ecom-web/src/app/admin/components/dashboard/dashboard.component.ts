import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

  products: any[] = [];
  searchProductForm!: FormGroup;

  constructor(private adminService : AdminService,
    private fb : FormBuilder,
    private snackBar : MatSnackBar
  ){}

  ngOnInit(){
    this.getAllProducts();
    this.searchProductForm = this.fb.group({
      title: [null, [Validators.required]]
    });
    
  } 
 
  getAllProducts() {
    this.products = [];
    this.adminService.getAllProducts().subscribe(res => {
      res.forEach(element => {
        element.processedImg = 'data:image/jpeg;base64,' + element.byteImage;
        this.products.push(element);
      });
      console.log("Products - "+ this.products);
    });
  }

  submitForm(){
    this.products = [];
    const title = this.searchProductForm.get('title')!.value;
    this.adminService.getAllProductsByName(title).subscribe(res => {
      res.forEach(element => {
        element.processedImg = 'data:image/jpeg;base64,' + element.byteImage;
        this.products.push(element);
      });
      console.log("Products - "+ this.products);
    });
  }

  
  // deleteProduct(productId: any) {
  //   this.adminService.deleteProduct(productId).subscribe(res => {
  //     if (res.body == null) {
  //       this.snackBar.open('Product Deleted Successfully!', 'Close', {
  //         duration: 5000
  //       });
  //       this.getAllProducts();
  //     } else {
  //       this.snackBar.open(res.message, 'Close', {
  //         duration: 5000,
  //         panelClass: 'error-snackbar'
  //       });
  //     }
  //   })
  // }
  deleteProduct(productId: any) {
    this.adminService.deleteProduct(productId).subscribe({
      next: () => {
        this.snackBar.open('Product Deleted Successfully!', 'Close', {
          duration: 5000
        });
        this.getAllProducts();
      },
      error: (err) => {
        this.snackBar.open('Error deleting product!', 'Close', {
          duration: 5000,
          panelClass: 'error-snackbar'
        });
        console.error(err);
      }
    });
  }
  

}
