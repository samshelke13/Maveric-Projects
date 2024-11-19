import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http'
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})

export class TalentSharePortalMentorService {
  private baseURL = `${environment.mentorUrl}:8070/api/v1/mentor`;
  private skillbaseURL = `${environment.mentorUrl}:8070/api/v1/skill`;
  constructor(private httpClient: HttpClient) { }
  
    getallMentorsList(): Observable<any> {
      console.log("getallMentorsList method called..!")
      const additionalPath = "/getAllMentors";
      const url = `${this.baseURL}${additionalPath}`;
      return this.httpClient.get<any>(url);
  }

    getMentorsListBySkill(skills: string[]): Observable<any> {
      console.log("getMentorsListBySkill method called..!")
      const additionalPath = "/search";
      const url = `${this.baseURL}${additionalPath}`;
      const params = new HttpParams().set('skills', skills.join(','));
      return this.httpClient.get<any>(url, { params });
  }

    viewMentor(empid: number): Observable<any> {
    console.log("viewMentor method called..!")
    const additionalPath = "/get/";
    const url = `${this.baseURL}${additionalPath}${empid}`;
    // const params = new HttpParams().set('skills', skills.join(','));
    return this.httpClient.get<any>(url);
}
}