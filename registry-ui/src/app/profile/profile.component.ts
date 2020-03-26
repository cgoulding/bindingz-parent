import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Auth} from 'aws-amplify';
import {BackendService} from "../backend.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  userId: string;
  userName: string;

  constructor(private router: Router, private backendService: BackendService) {
  }

  ngOnInit() {
    Auth.currentAuthenticatedUser({
      bypassCache: false
    }).then(async user => {
      this.userName = user.username;
      this.userId = user.attributes.sub;
      console.log(`Current user: ${this.userName} ${this.userId}`);
    }).catch(err => console.log(err));

    Auth.currentSession().then(res=>{
      let accessToken = res.getAccessToken()
      let jwt = accessToken.getJwtToken()
      console.log(`Current jwt: ${jwt}`)
      this.backendService.setLoggedIn(true, jwt);
    })
  }

  logOut() {
    Auth.signOut({global: true})
      .then(data => {
        this.router.navigate(['/auth']);
      })
      .catch(err => console.log(err));
  }
}
