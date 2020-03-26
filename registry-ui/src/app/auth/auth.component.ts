import {Component, OnInit} from '@angular/core';
import {AmplifyService} from 'aws-amplify-angular';
import {Router} from '@angular/router';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {

  public signUpConfig = {
    header: 'Create Account',
    signUpFields: [
      {label: 'First Name', name: 'name', required: true, displayOrder: 1},
      {label: 'Last Name', name: 'family_name', required: true, displayOrder: 2},
      {label: 'Email', name: 'email', required: true, displayOrder: 3},
      {label: 'Password', name: 'password', required: true, displayOrder: 4, type: 'password'},
      {label: 'Confirm Password', name: 'password2', required: true, displayOrder: 5, type: 'password'}
    ],
    hiddenDefaults: ['password', 'email']
  };

  constructor(public amplifyService: AmplifyService, public router: Router) {
    this.amplifyService = amplifyService;

    this.amplifyService.authStateChange$
      .subscribe(authState => {
        if (authState.state === 'signedIn') {
          this.router.navigate(['/profile']);
        }
      });
  }

  ngOnInit() {}
}
