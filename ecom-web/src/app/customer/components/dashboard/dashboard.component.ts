import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
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

  constructor(private customerService : CustomerService,
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
    this.customerService.getAllProducts().subscribe(res => {
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
    this.customerService.getAllProductsByName(title).subscribe(res => {
      res.forEach(element => {
        element.processedImg = 'data:image/jpeg;base64,' + element.byteImage;
        this.products.push(element);
      });
      console.log("Products - "+ this.products);
    });
  }

  addToCart(id: any){
    this.customerService.addToCart(id).subscribe(res =>{
      this.snackBar.open("Product added to cart successfully", "Close",
        { duration : 5000 }
      )
    })
  }

}
