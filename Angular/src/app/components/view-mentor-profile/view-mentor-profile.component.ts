import { Component } from '@angular/core';
import { MentorProfileService } from '../list-of-mentors/mentor-profile.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-view-mentor-profile',
  templateUrl: './view-mentor-profile.component.html',
  styleUrls: ['./view-mentor-profile.component.css']
})
export class ViewMentorProfileComponent {
  mentor: any;

  constructor(private mentorProfileService: MentorProfileService,
              private router: Router 
    ) {}

ngOnInit() {
  const mentorsData = localStorage.getItem('mentorsData');
  if (mentorsData) {
    this.mentor = JSON.parse(mentorsData);
  }
  const mentorProfileData = this.mentorProfileService.getMentorProfile();
  if (mentorProfileData) {
    this.mentor = mentorProfileData.data;
    localStorage.setItem('mentorsData', JSON.stringify(this.mentor));
    console.log('Mentor Profile Data:', this.mentor);    
  }
}
getFormattedImage(base64Image: string, imageFormat: string): string {
  // Define the default MIME type
  let mimeType = 'image/jpeg';

  if (imageFormat === 'jpeg') {
    mimeType = 'image/jpeg';
  } else if (imageFormat === 'png') {
    mimeType = 'image/png';
  } else if (imageFormat === 'gif') {
    mimeType = 'image/gif';
  } else if (imageFormat === 'jpg') {
  mimeType = 'image/gif';
  } else if (imageFormat === 'webp') {
  mimeType = 'image/webp';
}  
  return `data:${mimeType};base64,${base64Image}`;
}

goBack() {
  this.router.navigate(['/mentor/browse']);
}

logoutUser() {
  sessionStorage.clear();
   this.router.navigate(['/login']);
  }

}
