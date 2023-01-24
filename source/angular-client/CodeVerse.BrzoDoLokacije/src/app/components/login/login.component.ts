import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ActionResultResponse } from 'src/app/models/action-result-response.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  error: boolean = false;
  errorMsg: string = '';
  busy?: Subscription;

  loginForm: FormGroup = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  })

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const username = this.loginForm.get('username')?.value.trim();
      const password = this.loginForm.get('password')?.value.trim();

      this.busy = this.userService.login(username, password).subscribe({
        next: (result: ActionResultResponse) => {
          if (result) {
            if (result.success) {
              localStorage.setItem("user", JSON.stringify(result.actionData.user));
              localStorage.setItem("token", result.actionData.token);
              this.router.navigate(['/download']);
            } else {
              if (result.hasErrors) {
                result.errors.forEach(err => this.errorMsg = `${err}`);
                this.error = true;
              }
            }
          }
        },
        error: err => {
          this.error = true;
          this.errorMsg = 'Greska na serveru!';
        }
      });
    }
  }

}
