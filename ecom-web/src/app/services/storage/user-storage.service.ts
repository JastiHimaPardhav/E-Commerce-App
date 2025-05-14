// import { Injectable } from '@angular/core';

// const TOKEN = 'ecom-token';
// const USER = 'ecom-user';

// @Injectable({
//   providedIn: 'root'
// })

// export class UserStorageService {
//   constructor() { }
//   public saveToken(token: string): void {
//     window.localStorage.removeItem(TOKEN);
//     window.localStorage.setItem(TOKEN, token);
//   }

//   public saveUser(user): void {
//     window.localStorage.removeItem(USER);
//     window.localStorage.setItem(USER, JSON.stringify(user));
//   }

  
//   static getToken(): string {
//     return localStorage.getItem(TOKEN);
//   }

//   static getUser(): any {
//     return JSON.parse(localStorage.getItem(USER));
//   }

//   static getUserId(): string {
//     const user = this.getUser();
//     if (user == null) {
//       return '';
//     }
//     return user.userId;
//   }

//   static getUserRole(): string {
//     const user = this.getUser();
//     if (user == null) {
//       return '';
//     }
//     return user.role
//   }

  
// static isAdminLoggedIn(): boolean { 
//   if(this.getToken === null){ 
//     return false;
//   }
//   const role: string = this.getUserRole(); 
//   return role == 'ADMIN';
// }

// static isCustomerLoggedIn(): boolean{
//   if(this.getToken === null){ 
//     return false;
// }
// const role: string = this.getUserRole(); 
// return role == 'CUSTOMER';
// }
// static signout(): void {
// window.localStorage.removeItem(TOKEN);
// window.localStorage.removeItem(USER);
// }
// }

import { Injectable } from '@angular/core';

const TOKEN = 'ecom-token';
const USER = 'ecom-user';

@Injectable({
  providedIn: 'root'
})
export class UserStorageService {
  constructor() {}

  public saveToken(token: string): void {
    if (typeof window !== 'undefined') {
      window.localStorage.removeItem(TOKEN);
      window.localStorage.setItem(TOKEN, token);
    }
  }

  public saveUser(user: any): void {
    if (typeof window !== 'undefined') {
      window.localStorage.removeItem(USER);
      window.localStorage.setItem(USER, JSON.stringify(user));
    }
  }

  static getToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem(TOKEN);
    }
    return null;
  }

  static getUser(): any {
    if (typeof window !== 'undefined') {
      const userStr = localStorage.getItem(USER);
      return userStr ? JSON.parse(userStr) : null;
    }
    return null;
  }

  static getUserId(): string {
    const user = this.getUser();
    return user?.userId ?? '';
  }

  static getUserRole(): string {
    const user = this.getUser();
    return user?.role ?? '';
  }

  static isAdminLoggedIn(): boolean {
    return this.getToken() !== null && this.getUserRole() === 'ADMIN';
  }

  static isCustomerLoggedIn(): boolean {
    return this.getToken() !== null && this.getUserRole() === 'CUSTOMER';
  }

  static signout(): void {
    if (typeof window !== 'undefined') {
      window.localStorage.removeItem(TOKEN);
      window.localStorage.removeItem(USER);
    }
  }
}
