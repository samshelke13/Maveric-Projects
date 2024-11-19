import { CanActivate, Router } from '@angular/router';
import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';
import { AuthService } from '../auth/auth.service';

 

@Injectable({
  providedIn: 'root',
})
export class LoginGuard implements CanActivate {
  constructor(private Auth:AuthService, private router:Router){
    // //console.log(this.Auth.ISloggedIn());
  }
  canActivate(){
  if (this.Auth.MentorIsLoggedIn()){
    Swal.fire({
      icon: 'warning',
      title: '',
      text: 'Already logged In',
    });
    this.router.navigate([`mentor-view/${sessionStorage.getItem('mentorId')}`]);
  return false;
  }
  else if (this.Auth.MenteeIsLoggedIn()){
    Swal.fire({
      icon: 'warning',
      title: '',
      text: 'Already logged in..!!',
    });
    this.router.navigate([`mentor/search`]);
  return false;
  }
  return true;
}

}
