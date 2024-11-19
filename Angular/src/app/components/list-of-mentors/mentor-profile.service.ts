import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MentorProfileService {

  private mentorProfileData: any; // Store mentor's profile data

  setMentorProfile(profileData: any) {
    this.mentorProfileData = profileData;
  }

  getMentorProfile() {
    return this.mentorProfileData;
  }
}
