import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { saveAs } from 'file-saver';
import * as FileSaver from 'file-saver';
import { Router } from '@angular/router';

@Component({
  selector: 'app-download-page',
  templateUrl: './download-page.component.html',
  styleUrls: ['./download-page.component.scss']
})
export class DownloadPageComponent implements OnInit {

  busy?: Subscription;
  error: boolean = false;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
  }

  download() {
    this.busy = this.userService.downloadApp().subscribe({
      next: (result) => {
        if (result) {
          var binary_string = window.atob(result);
          var len = binary_string.length;
          var bytes = new Uint8Array(len);
          for (var i = 0; i < len; i++) {
              bytes[i] = binary_string.charCodeAt(i);
          }
          var blob = new Blob([bytes], { type: 'application/vnd.android.package-archive'});
          FileSaver.saveAs(blob, 'BrzoDoLokacije.apk');
        }
      },
      error: err => this.error = true
    });
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/']);
  }
}
